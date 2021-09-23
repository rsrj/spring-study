package com.rsrj.devdojo.springboot2.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.repository.AnimeRepository;
import com.rsrj.devdojo.springboot2.requests.AnimePostRequestBody;
import com.rsrj.devdojo.springboot2.requests.AnimePutRequestBody;

import lombok.RequiredArgsConstructor;

/*Classe responsavel pela regra de negocio
 * O Controller passa a funcionar de maneira mais "clean"
 * a logica de negocio passa a atuar no Service*/
@Service
@RequiredArgsConstructor
public class AnimeService{
	
	private final AnimeRepository animeRepository;
		
	/*Aqui foi feito uma mudanca tirando a lista que foi definida estaticamente*/
	
	public List<Anime> listAll(){
		return animeRepository.findAll();
	} 
	
	public Anime findByIdOrThrowBadRequestException(long id){
		return animeRepository.findById(id)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
	}

	public Anime save(AnimePostRequestBody animePostRequestBody) {
			Anime anime = Anime.builder().name(animePostRequestBody.getName()).build();
			/*Salva e ja retorna a variavel com o ID atualizado*/
			return animeRepository.save(anime);
	}

	public void delete(long id) {
		/*Caso o item a ser excluido nao exista, retorna BAD_REQUEST, mantendo o padrao*/
		animeRepository.delete(findByIdOrThrowBadRequestException(id));		
	}

	public void replace(AnimePutRequestBody animePutRequestBody) {
		Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
		Anime anime = Anime.builder()
				/* Garante que o id eh o recuperado no banco de dados ou lanca uma exception
				 * dizendo que nao existe o objeto na memoria*/
				.id(savedAnime.getId())
				.name(animePutRequestBody.getName())
				.build();
		animeRepository.save(anime);
	} 
}
