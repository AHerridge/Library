package com.aherridge.library.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GsonUtil
{
	private static final Gson gson;

	static
	{
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	public static <T> T readFromFile(Type type, File file)
	{
		try
		{
			Reader reader = new InputStreamReader(new FileInputStream(file));
			return gson.fromJson(reader, type);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void writeToFile(Object object, File file)
	{
		try
		{
			Writer writer = new FileWriter(file);
			gson.toJson(object, writer);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
