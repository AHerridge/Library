package com.aherridge.library.user;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.DirectoryRequestInitializer;
import com.google.api.services.admin.directory.model.Users;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import static com.aherridge.library.util.GoogleConsts.*;


public class GoogleDirectoryUtil
{
	public static Collection<User> getUserWithNameContaining(Credential credential, String searchTerm)
	{
		Directory directory = new Directory.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setDirectoryRequestInitializer(new DirectoryRequestInitializer(API_KEY))
				.setApplicationName(APP_NAME)
				.build();

		Collection<User> users = new LinkedList<>();

		try
		{
			Users result = directory.users().list()
					.setDomain("dublinschools.net")
					.setQuery("name:" + searchTerm)
					.setMaxResults(10)
					.setViewType("domain_public")
					.execute();

			for (com.google.api.services.admin.directory.model.User user : result.getUsers())
			{
				users.add(convertToUser(user));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return users;
	}

	private static User convertToUser(com.google.api.services.admin.directory.model.User user)
	{
		return new User(user.getId(), user.getName().getGivenName(), user.getName().getFamilyName(), user.getPrimaryEmail(), user.getThumbnailPhotoUrl());
	}
}
