package com.aherridge.library.book;

import com.aherridge.library.persistence.PersistentList;
import com.aherridge.library.provider.Provider;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.LinkedList;

public class ManualBookProvider extends PersistentList<Book> implements Provider<Book>
{
	public ManualBookProvider(File file)
	{
		super(file, new TypeToken<LinkedList<Book>>()
		{
		}.getType());
	}

	public Book get(String id)
	{
		return stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
	}
}
