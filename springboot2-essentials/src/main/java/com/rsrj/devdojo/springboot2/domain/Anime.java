package com.rsrj.devdojo.springboot2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
/*Necessario um construtor sem argumentos para o hibernate*/
@NoArgsConstructor
@Entity
public class Anime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
