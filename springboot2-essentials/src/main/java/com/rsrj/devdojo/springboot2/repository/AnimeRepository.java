package com.rsrj.devdojo.springboot2.repository;

import java.util.List;

import com.rsrj.devdojo.springboot2.domain.Anime;

/* Aqui sera representado o banco de dados
 * Todas as querys que serao responsaveis pelo relacionamento
 * com o banco de dados*/
public interface AnimeRepository {
	
	List <Anime> listAll();

}
