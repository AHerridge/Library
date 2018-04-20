package com.aherridge.library.book;

import com.aherridge.library.provider.Provider;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.util.List;

import static com.aherridge.library.util.GoogleConsts.*;

public class GoogleBookProvider implements Provider<Book>
{
	private static Books books;

	static
	{
		try
		{
			books = new Books.Builder(HTTP_TRANSPORT, JSON_FACTORY, null)
					.setApplicationName(APP_NAME)
					.setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
					.build();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Book get(String id)
	{
		try
		{
			Books.Volumes.List volumesList = books.volumes().list("isbn:" + id);
			Volumes volumes = volumesList.execute();
			if (volumes.getItems() != null && volumes.getItems().size() > 0)
			{
				return convertToBook(volumes.getItems().get(0));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private Book convertToBook(Volume volume)
	{
		if (volume == null)
		{
			return null;
		}
		else
		{
			String id = volume.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier();
			String title = volume.getVolumeInfo().getTitle();
			List<String> authors = volume.getVolumeInfo().getAuthors();
			String description = volume.getVolumeInfo().getDescription();
			String imageLink = null;

			if (volume.getVolumeInfo().getImageLinks() != null)
			{
				imageLink = (String) volume.getVolumeInfo().getImageLinks().getOrDefault("thumbnail", "");
			}

			return new Book(id, title, authors, description, imageLink);
		}
	}
}
