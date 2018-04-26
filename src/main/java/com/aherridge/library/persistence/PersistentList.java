package com.aherridge.library.persistence;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class PersistentList<E> extends LinkedList<E>
{
	private final File file;
	private final Type type;

	public PersistentList(File file, Type type)
	{
		this.file = file;
		this.type = type;

		if (file.exists())
		{
			read();
		}
		else if (file.getParentFile().exists() || file.getParentFile().mkdirs())
		{
			write();
		}
		else
		{
			System.out.println("Could not create persistent file");
		}
	}

	@Override
	public boolean add(E e)
	{
		boolean result = super.add(e);
		write();
		return result;
	}

	@Override
	public boolean remove(Object e)
	{
		boolean result = super.remove(e);
		write();
		return result;
	}

	@Override
	public E remove()
	{
		E result = super.remove();
		write();
		return result;
	}

	private void read()
	{
		addAll(GsonUtil.readFromFile(type, file));
	}

	private void write()
	{
		GsonUtil.writeToFile(this, file);
	}
}
