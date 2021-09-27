package com.rsrj.devdojo.springboot2.controller;

import java.time.LocalDateTime;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsrj.devdojo.springboot2.domain.Anime;
import com.rsrj.devdojo.springboot2.requests.AnimePostRequestBody;
import com.rsrj.devdojo.springboot2.requests.AnimePutRequestBody;
import com.rsrj.devdojo.springboot2.service.AnimeService;
import com.rsrj.devdojo.springboot2.util.DateUtil;
import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/*Modifiquei aqui para testar o GIT*/
/*Modificando novamente para testar no outro*/

/*O devtools foi adicionado para possibilitar o hotswap que permite que a atualizacao
 * de algumas partes do codigo nao precisem que o servidor seja totalmente reiniciado
 * no exemplo fiz a refatoracao de DBZ pra Dragon Ball Z e o que levava 1,5seg levou
 * 200ms */

@RestController
/*Normalmente esse mapping eh deixado no plural*/
@RequestMapping("animes")
@Log4j2
/*Eh possivel utilizar annotations do lombok para inserir o construtor diretamente
 * @RequiredArgsConstructor - Constroi so para os atributos final
 * @AllArgsConstructor*/
@RequiredArgsConstructor
public class AnimeController {
	
	
	private final AnimeService animeService;
	//Injecao de dependencias
	//@Autowired
	private final DateUtil dateUtil;

	
	/*Para seguir o curso estou usando o Lombok*/
	/*Injecao de dependencias via @Autowired nao eh recomendado
	 * o melhor eh criar um construtor*/
	/*public AnimeController(DateUtil dateUtil) {
		this.dateUtil = dateUtil;
	}*/
	
	
	// @GetMapping(path = "list")
	/* Se esse Mapping for deixado sem o path, a requisicao animes
	 * retornara a lista de animes*/
	@GetMapping
	public ResponseEntity<Page<Anime>> list(Pageable pageable){
		/*Para funcionar esse codigo precisei instalar o plugin por:
		 * https://www.youtube.com/redirect?event=video_description&redir_token=QUFFLUhqa1hUTTZQNW92ZlZ2Q054QnZNQlVRVTJLcktfUXxBQ3Jtc0ttejJGeGs4aGVlWXlGRUVYQ0ppUTFDWEI2WHBqd293SmVKWDRhaTlWUFlkM2t1YndaUEJKZVhoWTdoYjhWVUJ6OE5OVnRpU1BtU3p0YkNoQUxaNGw5OGhrVHhkaFh5SER3UUhDR1NlQkJpcFFsVHE3Yw&q=https%3A%2F%2Fprojectlombok.org%2Fdownloads%2Flombok.jar*/
		log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
		
		/* Para desacoplar os modulos foi criado a classe Service para cuidar das regras de negocio,
		 * o animeService eh responsavel por armazenar a lista de animes.
		 * O lombok @AllArgsConstructor fica responsavel por criar o construtor da classe ja com os
		 * atributos utilizados. Mas para que a operacao funcione AnimeService precisa ser colocado
		 * como um Bean (nesse caso do tipo @Service), assim como DateUtil(@Component) */
		/* Ao inves de retornar diretamente a string eh bom retornar um ResponseEntity que coloca
		 * tambem o status da requisicao*/
		return ResponseEntity.ok(animeService.listAll(pageable));
	}

	/* Eh provavel que haja mais de um get por contexto
	 * por exemplo, que retorne um objeto Anime ao inves de uma lista */
	@GetMapping(path = "/{id}")
	public ResponseEntity<Anime> findById(@PathVariable long id){
		return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
	}
	
	/*Deixa com o mesmo caminho dara conflito mesmo que o codigo funcione*/
	@GetMapping(path = "/find")
	/*@RequestParam permite colocar os parametros diretamente na URL*/
	public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
		return ResponseEntity.ok(animeService.findByNameContaining(name));
	}
	
	/*Realizando requisicoes do tipo POST*/
	/*Caso so haja um post por contexto nao eh necessario definir um path*/
	@PostMapping
	public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
		return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
	}
	
	/*Realizando requisicoes do tipo DELETE*/
	/*Documentacao do protocolo HTTP RFC7231
	 * Delete eh um metodo Idempotente - nao devem alter o estado do servidor*/
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		animeService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	/*Substitui o estado inteiro do objeto*/
	@PutMapping
	public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody) {
		animeService.replace(animePutRequestBody);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
