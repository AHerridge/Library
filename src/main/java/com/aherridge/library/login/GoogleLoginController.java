package com.aherridge.library.login;

import com.aherridge.library.provider.CashedProvider;
import com.aherridge.library.provider.Provider;
import com.aherridge.library.user.User;
import com.aherridge.library.util.Path;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static com.aherridge.library.user.UserUtil.getCurrentUser;
import static com.aherridge.library.user.UserUtil.setCurrentUser;
import static com.aherridge.library.util.GoogleConsts.*;

public class GoogleLoginController extends LoginController
{
	private static final Provider<User> USER_PROVIDER = new CashedProvider<>(new GoogleUserProvider(), 10);

	private static final GoogleAuthorizationCodeFlow FLOW = new GoogleAuthorizationCodeFlow.Builder(
			HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES).setAccessType("offline").build();

	public Object serveLoginPage(Request request, Response response)
	{
		if (getLoginDest(request) == null)
		{
			setLoginDest(request, Path.Web.DASHBOARD);
		}

		response.redirect(
				FLOW.newAuthorizationUrl()
						.set("device_id", UUID.randomUUID().toString())// Math.abs(new Random().nextLong()))
						.set("device_name", "device")
						.setRedirectUri(request.url().replace("/login/", REDIRECT_URI)).build()
		);

		return null;
	}

	public Object authenticate(Request request, Response response)
	{
		try
		{
			GoogleTokenResponse tokenResponse =
					new GoogleAuthorizationCodeTokenRequest(HTTP_TRANSPORT, JSON_FACTORY,
							CLIENT_ID, CLIENT_SECRET, request.queryParams("code"), request.url().replace("/login/", REDIRECT_URI)).execute();

			setCurrentUser(request, USER_PROVIDER.get(tokenResponse.getAccessToken()));
			System.out.println(getCurrentUser(request));

			String loginDest = getLoginDest(request);
			removeLoginDest(request);

			response.redirect(loginDest);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println(getCurrentUser(request));

		return null;
	}
}
