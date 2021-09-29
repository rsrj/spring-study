package com.rsrj.devdojo.springboot2.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.util.AnimeCreator;


/*Descreve os testes das interacoes com o banco de dados*/
@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {

	@Autowired
	private AnimeRepository animeRepository;
	
	@Test
	@DisplayName("Save persists anime when successful")
	void save_PersistAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		Assertions.assertThat(animeSaved).isNotNull();
		
		Assertions.assertThat(animeSaved.getId()).isNotNull();
		
		Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
		
	}

	@Test
	@DisplayName("Updates anime when successful")
	void save_UpdatesAnime_WhenSuccessful() {
		
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
	
		animeSaved.setName("Overlord");
	
		Anime animeUpdated = this.animeRepository.save(animeSaved);
		
		Assertions.assertThat(animeUpdated).isNotNull();
		
		Assertions.assertThat(animeUpdated.getId()).isNotNull();
		
		Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());
		
	}
	

	@Test
	@DisplayName("Delete removes anime when successful")
	void delete_RemovesAnime_WhenSuccessful() {
		
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		this.animeRepository.delete(animeSaved);
		
		Optional<Anime> animeOptional =	this.animeRepository.findById(animeSaved.getId());
		
		Assertions.assertThat(animeOptional).isEmpty();
	}
	
	@Test
	@DisplayName("Find by name returns a list of anime when Successful")
	void findByName_ReturnsListOfAnime_WhenSuccessful() {
		
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		String name = animeSaved.getName();
		
		List<Anime> animes = this.animeRepository.findByNameContaining(name);
				
		Assertions.assertThat(animes)
				.isNotEmpty()
				.contains(animeSaved);
		
		//Assertions.assertThat(animes).contains(animeSaved);
	}
	
	@Test
	@DisplayName("Find by name returns empty list when no anime is found")
	void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {	
		List<Anime> animes = this.animeRepository.findByNameContaining("xaxa");
				
		Assertions.assertThat(animes).isEmpty();
		
	}
	
	@Test
	@DisplayName("Save throw ConstraintViolationException when name is empty")
	void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
		Anime anime = new Anime();
//	Primeira forma de fazer	
//		Assertions.assertThatThrownBy(()->this.animeRepository.save(anime))
//				.isInstanceOf(ConstraintViolationException.class);
	
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
				.isThrownBy(()->this.animeRepository.save(anime))
				.withMessageContaining("The anime name cannot be empty");
	}



}
