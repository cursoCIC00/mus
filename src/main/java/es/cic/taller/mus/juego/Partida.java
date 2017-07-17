package es.cic.taller.mus.juego;

import java.util.ArrayList;
import java.util.List;

import es.cic.taller.mus.vista.EstadoPantallaEvento;
import es.cic.taller.mus.vista.EstadoPantallaSimple;

public class Partida implements PartidaNotifier {
	private static final int NUMERO_DE_JUEGOS = 5;
	private Equipo equipo1;
	private Equipo equipo2;
	
	private List<Juego> listaJuegos = new ArrayList<>();
	
	private Juego juegoActual;
	private Jugador manoActual;
	
	private Ronda rondaActual;
	
	private List<PartidaListener> listaPartidaListener = new ArrayList<>();
	
	private static int contador=0;
	
	private String nombrePartida;
	
	public Partida(Equipo equipo1, Equipo equipo2) {
		nombrePartida = "Partida " + ++contador;
		this.setEquipo1(equipo1);
		this.setEquipo2(equipo2);

		for(int i= 0; i<NUMERO_DE_JUEGOS; i++) {
			listaJuegos.add(new Juego());
		}
		
		juegoActual = listaJuegos.get(0);
		
		lanzaRonda();
	}

	public void lanzaRonda() {
		if (juegoActual.isTerminado()) {
			cambiaJuego();
		}

		if (juegoActual != null) {
			pasarASiguienteMano();
			rondaActual = new Ronda(this, juegoActual, getJugadoresDesdeMano());
		}
		firePartidaEvento(getEstadoPantallaEventoTipoAccion(TipoEventoPartida.ACCION_MUS));
	}
	
	public Ronda getRondaActual() {
		return rondaActual;
	}

	public void setRondaActual(Ronda rondaActual) {
		this.rondaActual = rondaActual;
	}

	public boolean haySiguienteJuego() {
		if (juegoActual == null) {
			return false;
		}
		int indiceJuegoActual = listaJuegos.indexOf(juegoActual);
		return indiceJuegoActual != listaJuegos.size() - 1;
	}
	
	private void cambiaJuego() {
		if (haySiguienteJuego()) {
			int indiceJuegoActual = listaJuegos.indexOf(juegoActual);
			juegoActual = listaJuegos.get(indiceJuegoActual + 1);
		} else {
			juegoActual = null;
		}
	}
	
	private List<Jugador> getJugadores() {
		List<Jugador> listaJugadores = new ArrayList<>();
		listaJugadores.add(equipo1.getJugador1());
		listaJugadores.add(equipo2.getJugador1());
		listaJugadores.add(equipo1.getJugador2());
		listaJugadores.add(equipo2.getJugador2());
		return listaJugadores;
	}
	
	private void sorteoManoInicial() {
		manoActual = getJugadores().get((int)Math.floor(Math.random() * 4));
	}
	
	private void pasarASiguienteMano() {
		if (manoActual == null) {
			sorteoManoInicial();
		} else {
			List<Jugador> jugadores = getJugadores();
			int indiceJugadorManoActual = jugadores.indexOf(manoActual);
			if (indiceJugadorManoActual == 3) {
				manoActual = getJugadores().get(0);
			} else {
				manoActual = getJugadores().get(indiceJugadorManoActual+ 1);
			}
		}		
	}
	
	private List<Jugador> getJugadoresDesdeMano() {
		List<Jugador> jugadores = getJugadores();
		int indiceJugadorManoActual = jugadores.indexOf(manoActual);
		
		List<Jugador> listaJugadoresDesdeMano = new ArrayList<>();
		listaJugadoresDesdeMano.addAll(jugadores.subList(indiceJugadorManoActual, jugadores.size()));
		listaJugadoresDesdeMano.addAll(jugadores.subList(0, indiceJugadorManoActual));
		return listaJugadoresDesdeMano;
	}
	
	public Equipo getEquipo1() {
		return equipo1;
	}

	public void setEquipo1(Equipo equipo1) {
		this.equipo1 = equipo1;
	}

	public Equipo getEquipo2() {
		return equipo2;
	}

	public void setEquipo2(Equipo equipo2) {
		this.equipo2 = equipo2;
	}

	@Override
	public void firePartidaEvento(EstadoPantallaEvento estadoPantallaEvento) {
		for (PartidaListener listener: listaPartidaListener) {
			
			String nombreJugadorActual = listener.getNombreJugadorActual();
			
			EstadoPantallaEvento estadoPantalla = rondaActual.getEstadoPantallaEvento(estadoPantallaEvento, nombreJugadorActual);
			
			listener.onPartidaEvent(estadoPantalla);
		}
		
	}

	@Override
	public void addPartidaListener(PartidaListener partidaListener) {
		listaPartidaListener.add(partidaListener);
	}
	
	private static PartidaNotifier partidaNotifierGeneral;
	
	private static List<Partida> listadoPartidas = new ArrayList<>();
	
	public static List<Partida> getListadoPartidas() {
		return listadoPartidas;
	}
	
	public static PartidaNotifier getNotificadorGeneral() {
		if (partidaNotifierGeneral == null) {
			partidaNotifierGeneral = new PartidaNotifier() {
				private List<PartidaListener> listaPartidaListener = new ArrayList<>();
				
				@Override
				public void addPartidaListener(PartidaListener partidaListener) {
					listaPartidaListener.add(partidaListener);
				}

				@Override
				public void firePartidaEvento(EstadoPantallaEvento estadoPantallaEvento) {
					if (estadoPantallaEvento.getTipoEventoPartida() == TipoEventoPartida.PARTIDA_INICIADA) {
						listadoPartidas.add(estadoPantallaEvento.getPartida());
					}
					for (PartidaListener listener: listaPartidaListener) {						
						EstadoPantallaEvento aux = new EstadoPantallaEvento(estadoPantallaEvento.getTipoEventoPartida(), estadoPantallaEvento.getPartida(), null, null, null, null, 0, 0, 0, 0, 0, false, false, false, false, false, false, 0, false, null, null, null, false, false, false, false, null);
						
						listener.onPartidaEvent(aux);
					}
				}
				
			};
		}
		return partidaNotifierGeneral;
	}

	public String getNombrePartida() {
		return nombrePartida;
	}

	public void setNombrePartida(String nombrePartida) {
		this.nombrePartida = nombrePartida;
	}
	public EstadoPantallaEvento getEstadoPantallaEventoTipoAccion(TipoEventoPartida tipoEstadoEvento) {
		return new EstadoPantallaEvento(tipoEstadoEvento, this, null, null, null, null, 0, 0, 0, 0, 0, false, false, false, false, false, false, 0, false, null, null, null, false, false, false, false, null);
	}

	public List<Juego> getListaJuegos() {
		return listaJuegos;
	}
}
