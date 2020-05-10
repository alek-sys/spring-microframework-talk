package com.example.demo;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Book {

	@Id
    private final Long id;
 	private final String title;

}
