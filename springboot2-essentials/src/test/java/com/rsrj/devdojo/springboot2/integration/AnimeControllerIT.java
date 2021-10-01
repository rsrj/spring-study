package com.rsrj.devdojo.springboot2.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;

import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.repository.AnimeRepository;
import com.rsrj.devdojo.springboot2.util.AnimeCreator;
import com.rsrj.devdojo.springboot2.wrapper.PageableResponse;

import lombok.extern.log4j.Log4j2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Log4j2
public class AnimeControllerIT {
	@Autowired
	private TestRestTemplate testRestTemplate;
	@LocalServerPort
	private int port;
	@Autowired
	private AnimeRepository animeRepository;
	
	@Test
	@DisplayName("listAll returns list of anime when successful")
	void listAll_ReturnsListOfAnimes_WhenSuccessful() {
	
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		String expectedName = savedAnime.getName();	
		
		List<Anime> animeList = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {
				}).getBody();
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
	
	}
	
	
	@Test
	@DisplayName("list returns list of anime inside page object when successful")
	void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
		
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String expectedName = savedAnime.getName();
		log.info("Anime salvo: "+ savedAnime.toString());
		
		/*Page eh um objeto que nao se pode criar diretamente por isso foi utilizado um wrapper
		 * que pega a resposta e a encapsula*/
		PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Anime>>() {
				}).getBody();
		
		Assertions.assertThat(animePage).isNotNull();
		
		Assertions.assertThat(animePage.toList())
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
			
	}
	
	
}
