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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.repository.AnimeRepository;
import com.rsrj.devdojo.springboot2.requests.AnimePostRequestBody;
import com.rsrj.devdojo.springboot2.util.AnimeCreator;
import com.rsrj.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import com.rsrj.devdojo.springboot2.wrapper.PageableResponse;

import lombok.extern.log4j.Log4j2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/* Nao remove os objetos gerados no banco de dados apos a execucao de cada teste*/
@AutoConfigureTestDatabase
/* Encerra o banco de dados no fim de cada teste, em detrimento do tempo de execucao
 * de todos os testes*/
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
	

	
	@Test
	@DisplayName("findById returns anime when successful")
	void findById_ReturnsAnime_WhenSuccessful() {
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		Long expectedId = savedAnime.getId();	
		
//		Anime animeResponse = testRestTemplate.exchange("/animes/{id}", HttpMethod.GET, null,
//				new ParameterizedTypeReference<Anime>() {},
//				expectedId).getBody();
		
		/*Forma mais simples de fazer a requisicao*/
		Anime animeResponse = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);
		
		Assertions.assertThat(animeResponse).isNotNull();
		
		Assertions.assertThat(animeResponse.getId()).isNotNull().isEqualTo(expectedId);
		Assertions.assertThat(animeResponse.getName()).isNotNull().isEqualTo(savedAnime.getName());
		
	}
	
	@Test
	@DisplayName("findByName returns list of animes when successful")
	void findByName_ReturnsListOfAnimes_WhenSuccessful() {
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String expectedName = savedAnime.getName();
		
		String url = String.format("/animes/find?name=%s", expectedName);
		
		List<Anime> animeList = testRestTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {
				}).getBody();
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animeList.get(0).getName()).isNotNull().isEqualTo(expectedName);
		
	}

	
	@Test
	@DisplayName("findByName returns an empty list of anime when anime is not found")
	void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
		
		List<Anime> animeList = testRestTemplate.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {
				}).getBody();
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isEmpty();
	}
	
	
	@Test
	@DisplayName("save returns anime when successful")
	void save_ReturnsAnime_WhenSuccessful() {	
		AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
		
		ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);
				
		Assertions.assertThat(animeResponseEntity).isNotNull();
		Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
		Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

	}
	
	@Test
	@DisplayName("replace updates anime when Successful")
	void replace_UpdatesAnime_WhenSuccessful() {	
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		savedAnime.setName("new name");
		
		ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes", 
				HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);
				
		Assertions.assertThat(animeResponseEntity).isNotNull();
		Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	
	}
	
	@Test
	@DisplayName("delete removes anime when successful")
	void delete_RemovesAnime_WhenSuccessful() {	
		
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}", 
				HttpMethod.DELETE, null, Void.class, savedAnime.getId());
				
		Assertions.assertThat(animeResponseEntity).isNotNull();
		
		Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	
		
	}
	
	
	
	
}
