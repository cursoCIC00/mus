package es.cic.taller.mus.juego;

import es.cic.taller.mus.vista.EstadoPantallaEvento;

public interface PartidaNotifier {
	void firePartidaEvento(EstadoPantallaEvento estadoPartidaEvento);
	void addPartidaListener(PartidaListener partidaListener);
}
