package com.aherridge.library.book;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class Book implements Serializable
{
	String id, title;
	List<String> authors;
	String description, imageLink;
}
