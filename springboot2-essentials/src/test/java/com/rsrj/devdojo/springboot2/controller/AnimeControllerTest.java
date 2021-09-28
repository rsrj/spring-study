package com.rsrj.devdojo.springboot2.controller;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.service.AnimeService;
import com.rsrj.devdojo.springboot2.util.AnimeCreator;

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
	}
	
	@Test
	@DisplayName("List returns list of anime inside page object when successful")
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

}
