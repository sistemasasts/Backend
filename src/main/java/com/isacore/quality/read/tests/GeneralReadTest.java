package com.isacore.quality.read.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GeneralReadTest {

	@Autowired
	private TestReadTraction rTTraction;

	@Autowired
	private TestReadCizalla rTCizalla;

	@Autowired
	private TestReadDesgarreClavo rTDesgarroClavo;

	@Autowired
	private TestReadPiedra rTPiedra;

	@Autowired
	private TestReadAdhesividad rTAdhesividad;

	@Autowired
	private TestReblandecimiento rTReblandecimiento;

	// @Scheduled(cron = "0 59 23 * * ?")
	public void execute() {
		this.rTTraction.run();

		this.rTCizalla.run();

		this.rTDesgarroClavo.run();

		this.rTPiedra.run();

		this.rTAdhesividad.run();

	}

	// Método que ejecuta la lectura de los archivos planos del Equipo de Reblandeciento. UNIDAD-RED Z:\\
	public void executeTReblandecimeinto(String batch) {
		
		this.rTReblandecimiento.run(batch);
	}

}
