package com.aherridge.library.provider;

public class ComboProvider<T> implements Provider<T>
{
	private final Provider<T>[] sources;

	public ComboProvider(Provider<T>... sources)
	{
		this.sources = sources;
	}

	@Override
	public T get(String id)
	{
		for (Provider<T> source : sources)
		{
			T t = source.get(id);
			if (t != null)
			{
				return t;
			}
		}

		return null;
	}
}
