package com.aherridge.library.user;

import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceRequestInitializer;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;

import static com.aherridge.library.util.GoogleConsts.*;

public class GooglePeopleUtil
{
	private static final PeopleService.People people;

	static
	{
		people = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, null)
				.setPeopleServiceRequestInitializer(new PeopleServiceRequestInitializer(API_KEY))
				.setApplicationName(APP_NAME)
				.build().people();
	}

	public static Person getUserById(String id)
	{
		try
		{
			return people.get("people/" + id).setPersonFields("names").execute();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
