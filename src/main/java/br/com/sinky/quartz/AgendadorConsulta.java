package br.com.sinky.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.sinky.service.FaceService;

@Service

@DisallowConcurrentExecution
public class AgendadorConsulta {
@Autowired
private FaceService faceService;

    @Scheduled(initialDelay = (1000*60*10),fixedDelay = (1000*60*60))
    public void efetuaConsulta() {
        try {
        	faceService = new FaceService();
        	faceService.carregarFotos();
        } catch (Exception e) {
        	e.printStackTrace();
        
        }
    }
}
