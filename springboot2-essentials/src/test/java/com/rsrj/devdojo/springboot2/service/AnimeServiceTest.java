package com.rsrj.devdojo.springboot2.service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;


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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.exception.BadRequestException;
import com.rsrj.devdojo.springboot2.repository.AnimeRepository;
import com.rsrj.devdojo.springboot2.util.AnimeCreator;
import com.rsrj.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import com.rsrj.devdojo.springboot2.util.AnimePutRequestBodyCreator;


@ExtendWith(SpringExtension.class)
public class AnimeServiceTest {

	@InjectMocks
	private AnimeService animeService;

	/*Utilizado para todas as classes internas a classe testada
	 * Nesse caso a classe interna eh o AnimeRepository*/
	@Mock
	private AnimeRepository animeRepositoryMock;
	
	
	@BeforeEach
	void setUp() {

		PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
				.thenReturn(animePage);

		BDDMockito.when(animeRepositoryMock.findAll())
				.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		/*O Mock retorna sempre o anime esperado quando o metodo findByIdOrThrowBadRequestException eh usado*/
		BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
			.thenReturn(Optional.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepositoryMock.findByNameContaining(ArgumentMatchers.anyString()))
			.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
			.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
	
	
	}
	
	@Test
	@DisplayName("listAll returns list of anime inside page object when successful")
	void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();	
		/*Embora o animePage seja iniciado com nulo, o mock cria o retorno
		 * esperado, ou seja, um Page*/
		Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));
		
		Assertions.assertThat(animePage).isNotNull();
		
		Assertions.assertThat(animePage.toList())
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
			
	}

	@Test
	@DisplayName("listAllNonPageable returns list of anime when successful")
	void listAllNonPageable_ReturnsListOfAnimes_WhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();	
		
		List<Anime> animeList = animeService.listAllNonPageable();
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
			
	}
	
	@Test
	@DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
	void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessful() {
		
		Long expectedId = AnimeCreator.createValidAnime().getId();	
		
		Anime anime = animeService.findByIdOrThrowBadRequestException(1);
		
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
		
	}
	
	@Test
	@DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
	void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound() {
		
		BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
				.thenReturn(Optional.empty());
	

		Assertions.assertThatExceptionOfType(BadRequestException.class)
				.isThrownBy(()->animeService.findByIdOrThrowBadRequestException(1));
		
	}
	
	@Test
	@DisplayName("findByNameContaining returns list of animes when successful")
	void findByNameContaining_ReturnsListOfAnimes_WhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();	
		
		List<Anime> animeList = animeService.findByNameContaining("Anime");
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
		
		Assertions.assertThat(animeList.get(0).getName()).isNotNull().isEqualTo(expectedName);
		
	}

	
	@Test
	@DisplayName("findByNameContaining returns an empty list of anime when anime is not found")
	void findByNameContaining_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
		
		/*Sobrescrevendo a instrucao determinada no @BeforeEach*/
		BDDMockito.when(animeRepositoryMock.findByNameContaining(ArgumentMatchers.anyString()))
				.thenReturn(Collections.emptyList());
		
		List<Anime> animeList = animeService.findByNameContaining("Anime");
		
		Assertions.assertThat(animeList)
				.isNotNull()
				.isEmpty();
		
	}
	
	
	@Test
	@DisplayName("save returns anime when successful")
	void save_ReturnsAnime_WhenSuccessful() {	
		
		/*Ao inves de declarar diretamente um novo AnimePostRequestBody vazio
		 * eh melhor fazer uma nova classe para construir esse objeto*/
		Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());
		
		Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

	}
	
	@Test
	@DisplayName("replace updates anime when Successful")
	void replace_UpdatesAnime_WhenSuccessful() {	
		
		/*Ao inves de declarar diretamente um novo AnimePostRequestBody vazio
		 * eh melhor fazer uma nova classe para construir esse objeto*/
		Assertions.assertThatCode(()-> 
				animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
						.doesNotThrowAnyException();
	
	}
	
	@Test
	@DisplayName("delete removes anime when successful")
	void delete_RemovesAnime_WhenSuccessful() {	
		
		/*Ao inves de declarar diretamente um novo AnimePostRequestBody vazio
		 * eh melhor fazer uma nova classe para construir esse objeto*/
		Assertions.assertThatCode(()-> animeService.delete(1L))
							.doesNotThrowAnyException();	
	}
	
}
