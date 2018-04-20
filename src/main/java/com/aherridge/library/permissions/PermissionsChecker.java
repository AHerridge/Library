package com.aherridge.library.permissions;

import com.aherridge.library.user.UserUtil;
import org.eclipse.jetty.http.HttpStatus;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import static spark.Spark.halt;

public class PermissionsChecker implements Filter
{
	private final Collection<String> adminOnlyPaths;

	public PermissionsChecker(String... adminOnlyPaths)
	{
		this.adminOnlyPaths = new LinkedList<String>(Arrays.asList(adminOnlyPaths))
		{
			@Override
			public boolean contains(Object o)
			{
				for (String string : this)
				{
					if (string.equals(o))
					{
						return true;
					}
				}

				return false;
			}
		};
	}

	@Override
	public void handle(Request request, Response response)
	{
		if (adminOnlyPaths.contains(request.pathInfo()) && !AdminService.isAdmin(UserUtil.getCurrentUser(request)))
		{
			halt(HttpStatus.FORBIDDEN_403, "FORBIDDEN");
		}
	}
}
