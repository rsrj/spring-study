package com.rsrj.devdojo.springboot2.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.rsrj.devdojo.springboot2.domain.Anime;

import lombok.extern.log4j.Log4j2;

/*Simulando uma requisicao de cliente para o servico desenvolvido*/
@Log4j2
public class SpringClient {
	
	public static void main(String[] args) {
		/*Transformando em uma ResponseEntity com varios outros parametros*/
		ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
		log.info(entity);
		/*Requisitando o objeto diretamente*/
		Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
		log.info(object);
		
		Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
		log.info(Arrays.toString(animes));
		
		ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all", 
				HttpMethod.GET, 
				null, 
				new ParameterizedTypeReference<List<Anime>>() {	
		});
		log.info(exchange.getBody());
	}
}
