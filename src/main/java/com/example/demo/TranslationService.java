package com.example.demo;

public class TranslationService {

	String translateTitle(String lang, String title) {
		if ("fr".equals(lang) && "A year in Provence".equals(title)) {
			return "Une année en Provence";
		}

		return title;
	}

}
