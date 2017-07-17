package es.cic.taller.mus.juego;

import java.util.Map;

public class Juego {
	private int puntuacionEquipo1;
	private int puntuacionEquipo2;
	
	public static int PUNTUACION_MAXIMA = 40;
	
	public boolean isTerminado() {
		return puntuacionEquipo1 == PUNTUACION_MAXIMA || puntuacionEquipo2 == PUNTUACION_MAXIMA;
	}
	
	public int getPuntuacionEquipo1() {
		return puntuacionEquipo1;
	}
	public void setPuntuacionEquipo1(int puntuacionEquipo1) {
		this.puntuacionEquipo1 = puntuacionEquipo1;
	}
	public int getPuntuacionEquipo2() {
		return puntuacionEquipo2;
	}
	public void setPuntuacionEquipo2(int puntuacionEquipo2) {
		this.puntuacionEquipo2 = puntuacionEquipo2;
	}

	public void sumaPuntos(Jugador jugador, int puntos) {
		sumaPuntos(jugador.getEquipo(), puntos);
	}
	public void sumaPuntos(Equipo equipo, int puntos) {
		if (equipo.getNumeroEquipo() == 1) {
			puntuacionEquipo1 += puntos;
			if (puntuacionEquipo1 > PUNTUACION_MAXIMA) {
				puntuacionEquipo1 = PUNTUACION_MAXIMA;
			}
		} else {
			puntuacionEquipo2 += puntos;
			if (puntuacionEquipo2 > PUNTUACION_MAXIMA) {
				puntuacionEquipo2 = PUNTUACION_MAXIMA;
			}
		}
	}
	
	public void ganaOrdago(Jugador jugador) {
		ganaOrdago(jugador.getEquipo());
	}
	
	private void ganaOrdago(Equipo equipo) {
		if (equipo.getNumeroEquipo() == 1) {
			puntuacionEquipo1 = PUNTUACION_MAXIMA;
		} else {
			puntuacionEquipo2 = PUNTUACION_MAXIMA;
		}
	}
	
}
