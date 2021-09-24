package com.rsrj.devdojo.springboot2.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.exception.BadRequestException;
import com.rsrj.devdojo.springboot2.mapper.AnimeMapper;
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
	
	public List<Anime> findByName(String name){
		return animeRepository.findByName(name);
	}
	
	public Anime findByIdOrThrowBadRequestException(long id){
		return animeRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Anime not found"));
	}

	public Anime save(AnimePostRequestBody animePostRequestBody) {
			/*Ao inves de usar um builder aqui se utiliza o framework MapStruct*/
			return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
	}

	public void delete(long id) {
		/*Caso o item a ser excluido nao exista, retorna BAD_REQUEST, mantendo o padrao*/
		animeRepository.delete(findByIdOrThrowBadRequestException(id));		
	}

	public void replace(AnimePutRequestBody animePutRequestBody) {
		/*Primeiro verifica se o anime ja existe*/
		Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
		/*Faz o mapeamento do nome*/
		Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
		/*Atualiza o Id com o existente*/
		anime.setId(savedAnime.getId());
		animeRepository.save(anime);
	} 
}
