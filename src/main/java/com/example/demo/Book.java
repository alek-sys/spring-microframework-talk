package com.example.demo;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public class Book {

	@Id
    private final Long id;
 	private final String title;

	public Book(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public java.lang.Long getId() {
		return id;
	}

	public java.lang.String getTitle() {
		return title;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book book = (Book) o;
		return Objects.equals(id, book.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", title='" + title + '\'' +
				'}';
	}
}
