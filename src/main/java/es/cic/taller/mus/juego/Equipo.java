package es.cic.taller.mus.juego;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
	private List<Jugador> listaJugadores = new ArrayList<>();
	
	private int numeroEquipo;
	
	public Equipo(int numeroEquipo, Jugador jugador1, Jugador jugador2) {
		this.setNumeroEquipo(numeroEquipo);
		jugador1.setEquipo(this);
		jugador2.setEquipo(this);
		listaJugadores.add(jugador1);
		listaJugadores.add(jugador2);
	}
	
	public Jugador getJugador1() {
		return listaJugadores.get(0);
	}
	
	public Jugador getJugador2() {
		return listaJugadores.get(1);
	}

	public int getNumeroEquipo() {
		return numeroEquipo;
	}

	public void setNumeroEquipo(int numeroEquipo) {
		this.numeroEquipo = numeroEquipo;
	}
}
