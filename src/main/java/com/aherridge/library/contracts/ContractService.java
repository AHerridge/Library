package com.aherridge.library.contracts;

import com.aherridge.library.book.Book;
import com.aherridge.library.persistence.PersistentList;
import com.aherridge.library.user.User;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ContractService
{
	private static Collection<Contract> contracts;

	static
	{
		contracts = new PersistentList<>(new File("./persist/contracts.json"), new TypeToken<LinkedList<Contract>>()
		{
		}.getType());
	}

	public static void create(User user, Book book)
	{
		contracts.add(new Contract(user.getId(), user.getLastName() + " " + user.getFirstName(), book.getId(), book.getTitle(), LocalDate.now().plusDays(7)));
	}

	public static void remove(User user, Book book)
	{
		remove(user.getId(), book.getId());
	}

	public static void remove(String userId, String bookId)
	{
//		contracts.removeIf(contract -> contract.getUserId().equals(userId) && contract.getBookId().equals(bookId));

		for (Contract contract : contracts)
		{
			if (contract.getUserId().equals(userId) && contract.getBookId().equals(bookId))
			{
				contracts.remove(contract);
				return;
			}
		}
	}

	public static boolean isBorrowing(User user, Book book)
	{
		return !filterBy(contract -> contract.getUserId().equals(user.getId()) && contract.getBookId().equals(book.getId())).isEmpty();
	}

	public static Collection<Contract> get(User user)
	{
		return filterBy(contract -> contract.getUserId().equals(user.getId()));
	}

	public static Collection<Contract> get(Book book)
	{
		return filterBy(contract -> contract.getBookId().equals(book.getId()));
	}

	public static Collection<Contract> filterBy(Predicate<Contract> filter)
	{
		return contracts.stream().filter(filter).collect(Collectors.toList());
	}

	public static Collection<Contract> getAll()
	{
		return new LinkedList<>(contracts);
	}

	public static Collection<Contract> getAllOverDue()
	{
		return filterBy(contract -> contract.getDueDate().isBefore(LocalDate.now()));
	}
}

