package com.aherridge.library.provider;

import java.util.LinkedHashMap;
import java.util.Map;

public class CashedProvider<T> implements Provider<T>
{
	private final Provider<T> source;
	private final Map<String, T> cache;

	public CashedProvider(Provider<T> source, int cacheSize)
	{
		this.source = source;

		cache = new LinkedHashMap<String, T>(cacheSize)
		{
			protected boolean removeEldestEntry(Map.Entry eldest)
			{
				return size() > cacheSize;
			}
		};
	}

	@Override
	public T get(String id)
	{
		return cache.getOrDefault(id, cache.put(id, source.get(id)));
	}
}
