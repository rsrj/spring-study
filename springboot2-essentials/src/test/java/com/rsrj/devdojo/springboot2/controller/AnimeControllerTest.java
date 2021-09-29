package com.rsrj.devdojo.springboot2.controller;

import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.requests.AnimePostRequestBody;
import com.rsrj.devdojo.springboot2.requests.AnimePutRequestBody;
import com.rsrj.devdojo.springboot2.service.AnimeService;
import com.rsrj.devdojo.springboot2.util.AnimeCreator;
import com.rsrj.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import com.rsrj.devdojo.springboot2.util.AnimePutRequestBodyCreator;

/*Sinaliza que quer utilizar o JUnit com Spring*/
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

	/*Quase se deseja testar a classe em si*/
	@InjectMocks
	private AnimeController animeController;
	/*Utilizado para todas as classes internas a classe testada*/
	@Mock
	private AnimeService animeServiceMock;
	
	
	@BeforeEach
	void setUp() {
		PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
				.thenReturn(animePage);

		BDDMockito.when(animeServiceMock.listAllNonPageable())
				.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		/*O Mock retorna sempre o anime esperado quando o metodo findByIdOrThrowBadRequestException eh usado*/
		BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
			.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.when(animeServiceMock.findByNameContaining(ArgumentMatchers.anyString()))
			.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
			.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));
	
		BDDMockito.doNothing().when(animeServiceMock).delete(anyLong());
	
	
	}
	
	@Test
	@DisplayName("list returns list of anime inside page object when successful")
	void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();	
		/*Embora o animePage seja iniciado com nulo, o mock cria o retorno
		 * esperado, ou seja, um Page*/
		Page<Anime> animePage = animeController.list(null).getBody();
		
		Assertions.assertThat(animePage).isNotNull();
		
		Assertions.assertThat(animePage.toList())
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
			
	}

	@Test
	@DisplayName("listAll returns list of anime when successful")
	void listAll_ReturnsListOfAnimes_WhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();	
		
		List<Anime> animeList = animeController.listAll().getBody();
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
			
	}
	
	@Test
	@DisplayName("findById returns anime when successful")
	void findById_ReturnsAnime_WhenSuccessful() {
		Long expectedId = AnimeCreator.createValidAnime().getId();	
		
		Anime anime = animeController.findById(1).getBody();
		
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
		
	}
	
	@Test
	@DisplayName("findByName returns list of animes when successful")
	void findByName_ReturnsListOfAnimes_WhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();	
		
		List<Anime> animeList = animeController.findByName("").getBody();
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animeList.get(0).getName()).isNotNull().isEqualTo(expectedName);
		
	}

	
	@Test
	@DisplayName("findByName returns an empty list of anime when anime is not found")
	void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
		
		/*Sobrescrevendo a instrucao determinada no @BeforeEach*/
		BDDMockito.when(animeServiceMock.findByNameContaining(ArgumentMatchers.anyString()))
				.thenReturn(Collections.emptyList());
		
		List<Anime> animeList = animeController.findByName("").getBody();
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isEmpty();
		
	}
	
	
	@Test
	@DisplayName("save returns anime when successful")
	void save_ReturnsAnime_WhenSuccessful() {	
		
		/*Ao inves de declarar diretamente um novo AnimePostRequestBody vazio
		 * eh melhor fazer uma nova classe para construir esse objeto*/
		Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();
		
		Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

	}
	
	@Test
	@DisplayName("replace updates anime when Successful")
	void replace_UpdatesAnime_WhenSuccessful() {	
		
		/*Ao inves de declarar diretamente um novo AnimePostRequestBody vazio
		 * eh melhor fazer uma nova classe para construir esse objeto*/
		Assertions.assertThatCode(()-> 
				animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
						.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());
		
		Assertions.assertThat(entity).isNotNull();
		
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	
	}
	
	@Test
	@DisplayName("delete removes anime when successful")
	void delete_RemovesAnime_WhenSuccessful() {	
		
		/*Ao inves de declarar diretamente um novo AnimePostRequestBody vazio
		 * eh melhor fazer uma nova classe para construir esse objeto*/
		Assertions.assertThatCode(()-> animeController.delete(1L))
							.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = animeController.delete(1L);		
		
		Assertions.assertThat(entity).isNotNull();
		
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	
	}
}
