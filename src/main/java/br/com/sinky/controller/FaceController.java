package br.com.sinky.controller;

import java.io.File;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sinky.exception.ExcecaoGenericaServidorException;
import br.com.sinky.exception.RecursoNaoEncontradoException;
import br.com.sinky.service.FaceService;

@RestController
@RequestMapping("/api")

public class FaceController {
	@Autowired
	private FaceService faceService;

	
	@RequestMapping(value = "/getFile/{path}", method = RequestMethod.POST)
	public String getFile(@PathVariable String path) {
		System.out.println(path);
		String retorno = faceService.processarImagem(path);  
		System.out.println(retorno);
		return retorno;
	}
	

	@PostMapping
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file){
		String fileName = file.getOriginalFilename();
		try {
			file.transferTo(new File("/home/aurelio/"+fileName));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			// TODO: handle exception
		}
		return ResponseEntity.ok("SUCESSO");
	}
	@GetMapping("/processar")
	public String consultarObjeto() {
	
		try {
			
			String retorno = faceService.processarImagem("/home/aurelio/FONTES/DL4J/Arnold_Schwarzenegger.jpg");       
	        return retorno;
		} catch (NoSuchElementException e) {
			throw new RecursoNaoEncontradoException("Registro não localizado [Consultar Objeto PdvTipoPlano].");
		} catch (Exception e) {
			throw new ExcecaoGenericaServidorException(
					"Erro no Servidor [PROCESSAR] - Exceção: " + e.getMessage());
		}
	}
	
	@GetMapping("/treinar")
	public void treinar() {
	
		try {
			faceService.carregarFotos();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
