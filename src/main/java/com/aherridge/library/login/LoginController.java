package com.aherridge.library.login;

import com.aherridge.library.user.UserUtil;
import com.aherridge.library.util.Path;
import spark.Request;
import spark.Response;

import static com.aherridge.library.user.UserUtil.getCurrentUser;
import static spark.Spark.halt;

public abstract class LoginController
{
	private static final String LOGIN_DEST = "login-destination";

	protected static String getLoginDest(Request request)
	{
		return request.session().attribute(LOGIN_DEST);
	}

	protected static void setLoginDest(Request request, String path)
	{
		request.session().attribute(LOGIN_DEST, path);
	}

	protected static void removeLoginDest(Request request)
	{
		request.session().removeAttribute(LOGIN_DEST);
	}

	public abstract Object serveLoginPage(Request request, Response response);

	public abstract Object authenticate(Request request, Response response);

	public void redirectToLogin(Request request, Response response)
	{
		if (request.url().endsWith(Path.Web.LOGIN))
		{
			setLoginDest(request, Path.Web.DASHBOARD);
		}
		else
		{
			setLoginDest(request, request.pathInfo());
		}

		response.redirect(Path.Web.LOGIN);
	}

	public void ensureUserIsLoggedIn(Request request, Response response)
	{
		if (getCurrentUser(request) == null && !(request.pathInfo().equals(Path.Web.LOGIN) || request.pathInfo().equals(Path.Web.AUTH)))
		{
			redirectToLogin(request, response);
			throw halt("Not authenticated!!");
		}
	}

	public Object logout(Request request, Response response)
	{
		UserUtil.setCurrentUser(request, null);
		request.session().invalidate();
		response.redirect(Path.Web.LOGIN);
		return null;
	}
}
