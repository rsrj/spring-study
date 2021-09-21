package com.rsrj.devdojo.springboot2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Anime {
	private Long id;
	/*Por default o Jackson vai mapear pelo nome do atributo
	 * caso o nome do atributo esteja diferente eh preciso destacar
	 * atraves da Annotation abaixo qual atributo realmente se deseja.*/
	//@JasonProperty("name")
	private String name;
/*	
	public Anime (String name) {
		this.setName(name);	
	}
	public Anime () {
		
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
*/
}
