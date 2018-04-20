package com.aherridge.library.user;

import spark.Request;

public abstract class UserUtil
{
	private static final String CURRENT_USER = "current-user";

	public static User getCurrentUser(Request request)
	{
		return request.session().attribute(CURRENT_USER);
	}

	public static void setCurrentUser(Request request, User user)
	{
		request.session().attribute(CURRENT_USER, user);
	}
}
