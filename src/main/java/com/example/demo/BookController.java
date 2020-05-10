package com.example.demo;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/book")
public class BookController {

	private final BookRepository bookRepository;
	private final TranslationService translationService;

	BookController(BookRepository bookRepository, TranslationService translationService) {
		this.bookRepository = bookRepository;
		this.translationService = translationService;
	}

	@GetMapping
	public Flux<Book> catalog(@RequestParam(required = false) String lang) {
		Flux<Book> books = bookRepository.findAll();
		if (StringUtils.isEmpty(lang)) {
			return books;
		}

		return books.map(book -> new Book(book.getId(), translationService.translateTitle(lang, book.getTitle())));
	}

}
