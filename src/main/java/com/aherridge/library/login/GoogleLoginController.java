package com.aherridge.library.login;

import com.aherridge.library.provider.CashedProvider;
import com.aherridge.library.provider.Provider;
import com.aherridge.library.user.User;
import com.aherridge.library.util.Path;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Random;

import static com.aherridge.library.user.UserUtil.setCurrentUser;
import static com.aherridge.library.util.GoogleConsts.*;

public class GoogleLoginController extends LoginController
{
	private static final Provider<User> USER_PROVIDER = new CashedProvider<>(new GoogleUserProvider(), 10);

	private static final GoogleAuthorizationCodeFlow FLOW = new GoogleAuthorizationCodeFlow.Builder(
			HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES).setAccessType("offline").build();

	private static final String CREDENTIAL_ATTRIB = "google-credential";

	public Object serveLoginPage(Request request, Response response)
	{
		if (getLoginDest(request) == null)
		{
			setLoginDest(request, Path.Web.DASHBOARD);
		}

		response.redirect(
				FLOW.newAuthorizationUrl()
						.set("device_id", Math.abs(new Random().nextLong()))
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
			createCredentials(request, tokenResponse.getAccessToken());

			String loginDest = getLoginDest(request);
			removeLoginDest(request);

			response.redirect(loginDest);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private void createCredentials(Request request, String accessToken)
	{
		GoogleCredential credential = new GoogleCredential.Builder()
				.setJsonFactory(JSON_FACTORY)
				.setTransport(HTTP_TRANSPORT)
				.setClientSecrets(CLIENT_ID, CLIENT_SECRET)
				.build()
				.setAccessToken(accessToken);

		request.session().attribute(CREDENTIAL_ATTRIB, credential);
	}

	public Credential getCredential(Request request)
	{
		return request.session().attribute(CREDENTIAL_ATTRIB);
	}
}
