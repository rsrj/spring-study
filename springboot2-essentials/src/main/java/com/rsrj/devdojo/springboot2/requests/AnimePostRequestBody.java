package com.rsrj.devdojo.springboot2.requests;

import lombok.Data;

/*Cria uma classe para fazer a resposta e nao usar a classe Anime
 * diretamente no controller*/
@Data
public class AnimePostRequestBody {
	private String name;
}
