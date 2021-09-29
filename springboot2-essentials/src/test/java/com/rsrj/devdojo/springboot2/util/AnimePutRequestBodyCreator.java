package com.rsrj.devdojo.springboot2.util;

import com.rsrj.devdojo.springboot2.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
	public static AnimePutRequestBody createAnimePutRequestBody() {
		return AnimePutRequestBody.builder()
				.name(AnimeCreator.createValidUpdatedAnime().getName())
				.id(AnimeCreator.createValidUpdatedAnime().getId())
				.build();
	}
}
