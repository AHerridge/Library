package com.aherridge.library.provider;

public interface Provider<T>
{
	T get(String id);
}
