package com.rsrj.devdojo.springboot2.util;

import com.rsrj.devdojo.springboot2.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

	public static AnimePostRequestBody createAnimePostRequestBody() {
		return AnimePostRequestBody.builder()
				/*Faz com que os nomes estejam vinculados para facilitar os testes*/
				.name(AnimeCreator.createAnimeToBeSaved().getName())
				.build();
	}
}
