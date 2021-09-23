package com.rsrj.devdojo.springboot2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rsrj.devdojo.springboot2.domain.Anime;

/* Aqui sera representado o banco de dados
 * Todas as querys que serao responsaveis pelo relacionamento
 * com o banco de dados*/
public interface AnimeRepository extends JpaRepository<Anime, Long>{
	/* No argumento do Jpa se coloca Anime como classe a ser armazenada 
	 * o atributo que representa o IDe*/	
	//Jah tem metodos disponiveis no JPA repositorio para listagem
	//dentre outras funcoes
	//List <Anime> findAll();

	
}
