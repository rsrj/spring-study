package com.rsrj.devdojo.springboot2.requests;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

/*Cria uma classe para fazer a resposta e nao usar a classe Anime
 * diretamente no controller*/
@Data
@Builder
public class AnimePostRequestBody {
	@NotEmpty(message = "The anime name cannot be empty")
	/*NotEmpty tambem verifica se eh nulo*/
	//@NotNull(message = "The anime name cannot be null")
	private String name;
}
