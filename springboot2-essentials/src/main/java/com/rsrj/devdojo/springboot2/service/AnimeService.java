package com.rsrj.devdojo.springboot2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rsrj.devdojo.springboot2.domain.Anime;

/*Classe responsavel pela regra de negocio
 * O Controller passa a funcionar de maneira mais "clean"
 * a logica de negocio passa a atuar no Service*/
@Service
public class AnimeService{
	private static List<Anime> animes;
	/*Como nao se tem banco de dados William usa uma Lista estatica e faz uma gambiarra pra
	 * poder torna o tamanho alteravel*/
	static {
		animes = new ArrayList<>(List.of(new Anime(1L,"Dragon Ball Z"), new Anime(2L,"Naruto"), new Anime(3L,"Inuyasha"), new Anime(4L,"Boruto")));
	}
	
	public List<Anime> listAll(){
		return animes;
	} 
	
	public Anime findById(long id){
		/*Procurar mais sobre os usos do Collection Framework*/
		/*Argumentos de filter - Predicate<? super T> predicate - procurar mais sobre Predicate*/
		return animes.stream()
				/* No fim das contas eh um argumento funcional como o usado em JavaScript*/
				.filter(anime -> anime.getId().equals(id))
				/* Retorna o primeiro dos elementos que foram filtrados*/
				.findFirst()
				/* Caso nao ache nenhum elemento lanca a Exception a ser tratada pelo Spring Boot
				 * Padrao seguido pelo William, caso hajam outros endpoints eh boa pratica manter
				 * o padrao*/
				/* A Exception lanca junto o stackTrace, para suprimir eh preciso mudar em resources*/
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
	}

	public Anime save(Anime anime) {
			anime.setId(ThreadLocalRandom.current().nextLong(5, 1000000));	
			animes.add(anime);
			return anime;
	}

	public void delete(long id) {
		/*Caso o item a ser excluido nao exista, retorna BAD_REQUEST, mantendo o padrao*/
		animes.remove(findById(id));
		
	}

	public void replace(Anime anime) {
		delete(anime.getId());
		animes.add(anime);
	} 
}
