package com.aherridge.library.book;

import com.aherridge.library.contracts.ContractService;
import com.aherridge.library.provider.CashedProvider;
import com.aherridge.library.provider.ComboProvider;
import com.aherridge.library.provider.Provider;
import com.aherridge.library.util.Path;
import com.aherridge.library.view.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import static com.aherridge.library.user.UserUtil.getCurrentUser;

public class BookController
{
	private static final Provider<Book> bookProvider;
	private static final ManualBookProvider manualBookProvider;

	static
	{
		manualBookProvider = new ManualBookProvider(new File("./src/main/resources/persist/manual_library.json"));
		bookProvider = new ComboProvider<>(manualBookProvider, new CashedProvider<>(new GoogleBookProvider(), 2));
	}

	public static Route FETCH_ONE_BOOK = (Request request, Response response) ->
	{
		HashMap<String, Object> model = new HashMap<>();
		Book book = bookProvider.get(getParamID(request));
		model.put("book", book);
		return ViewUtil.render(request, model, Path.Template.BOOKS_ONE);
	};
	public static Route SERVE_ADD_FORM = (Request request, Response response) ->
			ViewUtil.render(request, new HashMap<>(), Path.Template.ADD_BOOK);
	public static Route RETURN_BOOK = (Request request, Response response) ->
	{
		ContractService.remove(getCurrentUser(request), bookProvider.get(getParamID(request)));
		response.redirect(request.url().replace(Path.Web.RETURN_BOOK, ""));
		return null;
	};
	public static Route CHECKOUT_BOOK = (Request request, Response response) ->
	{
		ContractService.create(getCurrentUser(request), bookProvider.get(getParamID(request)));
		response.redirect(request.url().replace(Path.Web.CHECKOUT_BOOK, ""));
		return null;
	};

	public static Route ADD_BOOK = (Request request, Response response) ->
	{
		Book book = parseFromParams(request);
		manualBookProvider.add(book);
		response.redirect("");
		return null;
	};

	private static String getParamID(Request request)
	{
		return request.params("id");
	}

	private static Book parseFromParams(Request request)
	{
		return new Book(request.queryParams("id"), request.queryParams("title"), Arrays.asList(request.queryParams("authors").split(", ")),
				request.queryParams("description"), request.queryParams("image-link"));
	}
}
