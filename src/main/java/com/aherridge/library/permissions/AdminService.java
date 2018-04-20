package com.aherridge.library.permissions;

import com.aherridge.library.persistence.PersistentList;
import com.aherridge.library.user.User;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

public class AdminService
{
	private static final Collection<String> adminIds = new PersistentList<>(new File("./src/main/resources/persist/admins.json"), new TypeToken<LinkedList<String>>()
	{
	}.getType());

	public static boolean isAdmin(User user)
	{
		return adminIds.contains(user.getId());
	}

	public static void addAdmin(String userId)
	{
		adminIds.add(userId);
	}

	public static void removeAdmin(String userId)
	{
		adminIds.remove(userId);
	}
}
