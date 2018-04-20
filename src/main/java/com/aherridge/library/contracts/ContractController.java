package com.aherridge.library.contracts;

import com.aherridge.library.util.Path;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.aherridge.library.user.UserUtil.getCurrentUser;

public class ContractController
{
	public static Route REMOVE_CONTRACT = (Request request, Response response) ->
	{
		ContractService.remove(getUserId(request), getBookId(request));
		response.redirect(Path.Web.DASHBOARD);
		return null;
	};

	private static String getUserId(Request request)
	{
		return request.queryParams("userId");
	}

	private static String getBookId(Request request)
	{
		return request.queryParams("bookId");
	}
}
