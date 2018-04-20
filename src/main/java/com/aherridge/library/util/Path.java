package com.aherridge.library.util;

import lombok.Getter;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Path
{
	public static class Web
	{
		@Getter
		public static final String DASHBOARD = "/";

		@Getter
		public static final String LOGIN = "/login/";
		@Getter
		public static final String LOGOUT = "/logout/";
		@Getter
		public static final String AUTH = "/auth/";

		@Getter
		public static final String ADMIN = "/admin/";

		@Getter
		public static final String SCAN = "/scan/";

		@Getter
		public static final String BOOKS = "/books/";
		@Getter
		public static final String ONE_BOOK = BOOKS + ":id/";
		@Getter
		public static final String RETURN_BOOK = "return/";
		@Getter
		public static final String CHECKOUT_BOOK = "checkout/";
		@Getter
		public static final String ADD_BOOK = "add/";

		@Getter
		public static final String REMOVE_CONTRACT = "/contracts/remove/";

		@Getter
		public static final String URL = "http://" + getHostAddress() + ":8080";

		private static String getHostAddress()
		{
			try
			{
				return Inet4Address.getLocalHost().getHostAddress();
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
				return "";
			}
		}
	}

	public static class Template
	{
		public final static String DASHBOARD = "/velocity/dashboard.vm";
		public final static String ADMIN_DASHBOARD = "/velocity/admin-dashboard.vm";
		public final static String SCAN = "/velocity/scan.vm";
		public static final String BOOKS_ONE = "/velocity/one.vm";
		public static final String ADD_BOOK = "/velocity/add.vm";
		public static final String NOT_FOUND = "/velocity/notFound.vm";
	}
}
