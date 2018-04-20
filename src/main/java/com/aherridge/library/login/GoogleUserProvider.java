package com.aherridge.library.login;

import com.aherridge.library.provider.Provider;
import com.aherridge.library.user.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;

import java.io.IOException;

import static com.aherridge.library.util.GoogleConsts.*;

public class GoogleUserProvider implements Provider<User>
{
	@Override
	public User get(String accessToken)
	{
		try
		{
			GoogleCredential credential = new GoogleCredential.Builder()
					.setJsonFactory(JSON_FACTORY)
					.setTransport(HTTP_TRANSPORT)
					.setClientSecrets(CLIENT_ID, CLIENT_SECRET)
					.build()
					.setAccessToken(accessToken);

			Oauth2 oauth2 = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
					.setApplicationName(APP_NAME).build();

			return convertToUser(oauth2.userinfo().get().execute());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private User convertToUser(Userinfoplus userinfoplus)
	{
		return new User(userinfoplus.getId(), userinfoplus.getGivenName(), userinfoplus.getFamilyName(), userinfoplus.getEmail(), userinfoplus.getLink());
	}
}
