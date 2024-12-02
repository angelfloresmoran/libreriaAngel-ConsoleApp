package com.angelodev.angelslibreria;

import com.angelodev.angelslibreria.principal.Principal;
import com.angelodev.angelslibreria.service.ConsumoAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AngelslibreriaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AngelslibreriaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal inicioPrograma = new Principal();
		inicioPrograma.inicializaElPrograma();
	}
}
