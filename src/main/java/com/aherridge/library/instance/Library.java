package com.aherridge.library.instance;

import com.aherridge.library.book.Book;
import com.aherridge.library.contracts.Contract;
import com.aherridge.library.persistence.GsonUtil;
import com.google.gson.reflect.TypeToken;
import lombok.Value;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

@Value
public class Library
{
	Set<String> adminIds;
	Set<Contract> contracts;
	Set<Book> manuallyAddedBooks;

	public Library()
	{
		adminIds = new LinkedHashSet<>();
		contracts = new LinkedHashSet<>();
		manuallyAddedBooks = new LinkedHashSet<>();
	}

	public static void main(String[] args)
	{
		Library library1 = GsonUtil.readFromFile(new TypeToken<Library>()
		{
		}.getType(), new File("./src/main/resources/instances/library1.json"));

		if (library1 != null)
		{
			library1.getAdminIds().add("123");

			GsonUtil.writeToFile(library1, new File("./src/main/resources/instances/library1.json"));
		}
	}
}
