package es.cic.taller.mus.juego;

import es.cic.taller.mus.vista.EstadoPantallaEvento;

public interface PartidaListener {
	String getNombreJugadorActual();
	void onPartidaEvent(EstadoPantallaEvento estadoPantallaEvento);
	
}
