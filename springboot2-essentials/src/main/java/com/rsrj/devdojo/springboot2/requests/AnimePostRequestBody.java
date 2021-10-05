package com.rsrj.devdojo.springboot2.requests;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*Cria uma classe para fazer a resposta e nao usar a classe Anime
 * diretamente no controller*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
	@NotEmpty(message = "The anime name cannot be empty")
	/*NotEmpty tambem verifica se eh nulo*/
	//@NotNull(message = "The anime name cannot be null")
	private String name;
}
