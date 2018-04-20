package com.aherridge.library.contracts;

import lombok.Value;

import java.time.LocalDate;

@Value
public class Contract
{
	String userId, userName, bookId, bookTitle;
	LocalDate dueDate;
}
