package com.aherridge.library;

import com.aherridge.library.book.BookController;
import com.aherridge.library.contracts.ContractController;
import com.aherridge.library.login.GoogleLoginController;
import com.aherridge.library.login.LoginController;
import com.aherridge.library.permissions.PermissionsChecker;
import com.aherridge.library.user.GoogleDirectoryUtil;
import com.aherridge.library.util.Path;
import com.aherridge.library.view.ViewUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Server
{
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args)
	{
		//Setup https
		secure("./src/main/resources/keystore.jks", "openlibrary", null, "openlibrary");

		port(8080);

		staticFiles.location("/public/");
		staticFiles.expireTime(600L);

		before("*", Server::simpleDebug);

		//Fix urls that don't end with a slash
		before("*", (request, response) ->
		{
			if (!request.pathInfo().endsWith("/"))
			{
				response.redirect(request.url() + "/");
			}
		});

		//Handle exception caused by domain name differences
		exception(IllegalArgumentException.class, (e, request, response) -> response.redirect(Path.Web.DASHBOARD));

		//User log in/out
		LoginController loginController = new GoogleLoginController();
		before("*", loginController::ensureUserIsLoggedIn);
		get(Path.Web.LOGIN, loginController::serveLoginPage);
		get(Path.Web.AUTH, loginController::authenticate);
		get(Path.Web.LOGOUT, loginController::logout);

		//Admin portion
		before("*", new PermissionsChecker(Path.Web.BOOKS + Path.Web.ADD_BOOK, Path.Web.REMOVE_CONTRACT));
		get(Path.Web.BOOKS + Path.Web.ADD_BOOK, BookController.SERVE_ADD_FORM);
		post(Path.Web.BOOKS + Path.Web.ADD_BOOK, BookController.ADD_BOOK);
		post(Path.Web.REMOVE_CONTRACT, ContractController.REMOVE_CONTRACT);
		get("/admin/users/", ((request, response) -> GoogleDirectoryUtil.getUserWithNameContaining(request.session().attribute("google-credential"), "dye")));

		//User portion
		get(Path.Web.DASHBOARD, (request, response) -> ViewUtil.render(request, new HashMap<>(), Path.Template.DASHBOARD));
		get(Path.Web.SCAN, (request, response) -> ViewUtil.render(request, new HashMap<>(), Path.Template.SCAN));
		get(Path.Web.ONE_BOOK, BookController.FETCH_ONE_BOOK);

		//Book commands
		post(Path.Web.ONE_BOOK + Path.Web.RETURN_BOOK, BookController.RETURN_BOOK);
		post(Path.Web.ONE_BOOK + Path.Web.CHECKOUT_BOOK, BookController.CHECKOUT_BOOK);

		//Add GZip header
		after("*", (Request request, Response response) -> response.header("Content-Encoding", "gzip"));
	}

	private static void simpleDebug(Request request, Response response)
	{
		logger.info(request.requestMethod() + " " + request.url());
	}

	private static void debug(Request request, Response response)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(request.url()).append("\n");
		sb.append("\trequest-method = ").append(request.requestMethod()).append("\n");
		sb.append("\tpath-info = ").append(request.pathInfo()).append("\n");
		sb.append("\tsession-id = ").append(request.session().id()).append("\n");

		sb.append("\tparams:").append("\n");
		for (Map.Entry param : request.params().entrySet())
		{
			sb.append("\t\t").append(param.getKey()).append(" = ").append(param.getValue()).append("\n");
		}

		sb.append("\tquery-params:").append("\n");
		for (String queryParam : request.queryParams())
		{
			sb.append("\t\t").append(queryParam).append(" = ").append(request.queryParams(queryParam)).append("\n");
		}

		sb.append("\tattributes:").append("\n");
		for (String attribute : request.attributes())
		{
			sb.append("\t\t").append(attribute).append(" = ").append(request.attribute(attribute).toString()).append("\n");
		}

		sb.append("\tsession-attributes:").append("\n");
		for (String sessionAttribute : request.session().attributes())
		{
			sb.append("\t\t").append(sessionAttribute).append(" = ").append(request.session().attribute(sessionAttribute).toString()).append("\n");
		}

		sb.append("\tcookies:").append("\n");
		for (Map.Entry cookie : request.cookies().entrySet())
		{
			sb.append("\t\t").append(cookie.getKey()).append(" = ").append(cookie.getValue()).append("\n");
		}

		logger.info(sb.toString());
	}
}