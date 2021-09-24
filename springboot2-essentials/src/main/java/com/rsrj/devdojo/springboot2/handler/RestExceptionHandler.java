package com.rsrj.devdojo.springboot2.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rsrj.devdojo.springboot2.exception.BadRequestException;
import com.rsrj.devdojo.springboot2.exception.BadRequestExceptionDetails;

/* Ao usar a excecao do tipo associado o corpo de resposta seguira as definicoes dessa
 * classe*/

@ControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre){
		return new ResponseEntity<>(
				BadRequestExceptionDetails.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.title("Bad Request Exception, Check the Documentation")
				.details(bre.getMessage())
				.developerMessage(bre.getClass().getName())
				.build(), HttpStatus.BAD_REQUEST);
				
	}
	
}
