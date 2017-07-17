package es.cic.taller.mus.juego;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import es.cic.taller.mus.vista.EstadoPantallaEvento;
import es.cic.taller.mus.vista.EstadoPantallaSimple;

public class Ronda {
	public static final int SUERTE_MUS = 0;
	public static final int SUERTE_DESCARTE = -1;
	public static final int SUERTE_MAYOR = 1;
	public static final int SUERTE_MENOR = 2;
	public static final int SUERTE_PARES = 3;
	public static final int SUERTE_JUEGO = 4;
	public static final int SUERTE_ACABADO = 5;

	private List<Mano> listaMano = new ArrayList<>();
	
	private Baraja baraja;
	
	private int suerteActual;
	
	private Mano manoInicial;
	private Mano manoHabla;
	private Mano manoUltimaApuesta;
	
	public boolean ordago = false;
	
	private int ultimaApuesta = 0;
	
	private int apuestaMayor = 0;
	private Equipo equipoGanaMayorEnPaso;
	private int apuestaMenor = 0;
	private Equipo equipoGanaMenorEnPaso;
	private int apuestaPares = 0;
	private Equipo equipoGanaParesEnPaso;
	private int apuestaJuego = 0;
	private Equipo equipoGanaJuegoEnPaso;
	
	private Partida partida;
	private Juego juego;
	
	public Ronda(Partida partida, Juego juego, List<Jugador> listaJugadores) {
		this.partida = partida;
		this.juego = juego;
		
		baraja = new Baraja();
		
		for(Jugador jugador: listaJugadores) {
			generaManoJugador(jugador);
		}
		
		manoInicial = listaMano.get(0);

		inicia(SUERTE_MUS);	
	}
	
	private void generaManoJugador(Jugador jugador) {
		Mano mano = baraja.getMano();
		mano.setJugador(jugador);
		jugador.setManoActual(mano);
		listaMano.add(mano);
	}

	private boolean puedeHablar(Mano mano) {
		if (suerteActual == SUERTE_ACABADO) {
			return false;
		}
		return mano.equals(manoHabla);
	}

	public int getApuestaActual() {
		if (suerteActual == SUERTE_MAYOR) {
			return apuestaMayor + ultimaApuesta;
		}

		if (suerteActual == SUERTE_MENOR) {
			return apuestaMenor + ultimaApuesta;
		}

		if (suerteActual == SUERTE_PARES) {
			return apuestaPares + ultimaApuesta;
		}

		if (suerteActual == SUERTE_JUEGO) {
			return apuestaJuego + ultimaApuesta;
		}

		return 0;
	}
	
	public boolean isSuerteMus() {
		return suerteActual == SUERTE_MUS;
	}
	
	public boolean isSuerteDescarte() {
		return suerteActual == SUERTE_DESCARTE;
	}
	
	public boolean puedeEnvidar(Mano mano) {
		return puedeApostar(mano);
	}
	
	public boolean puedeApostar(Mano mano) {
		return puedeHablar(mano) && !isSuerteMus() && !isSuerteDescarte() && !ordago;
	}
	
	public boolean puedeAceptar(Mano mano) {
		return puedeHablar(mano) && !isSuerteMus() && !isSuerteDescarte() && (ordago || getApuestaActual() != 0); 
	}
	
	public boolean puedePasar(Mano mano) {
		return puedeHablar(mano) && !isSuerteMus() && !isSuerteDescarte() ;
	}
	
	public boolean puedeMus(Mano mano) {
		return puedeHablar(mano) && isSuerteMus();
	}

	public boolean puedeNoHayMus(Mano mano) {
		return puedeHablar(mano) && isSuerteMus();
	}
	
	public boolean puedeDescartar(Mano mano) {
		return puedeHablar(mano) && isSuerteDescarte();
	}
	
	public void mus(Mano mano) {
		manoHabla = getSiguienteManoQueHabla();
		
		if (manoHabla == null) {
			inicia(SUERTE_DESCARTE);
		}	
		this.partida.firePartidaEvento(partida.getEstadoPantallaEventoTipoAccion(TipoEventoPartida.ACCION_MUS));
	}
	
	public void noHayMus(Mano mano) {
		inicia(SUERTE_MAYOR);
		this.partida.firePartidaEvento(partida.getEstadoPantallaEventoTipoAccion(TipoEventoPartida.ACCION_MUS));
	}
	
	public void envidar(Mano mano) {
		apostar(mano, 2);
	}
	
	public void apostar(Mano mano, int apuesta) {
		if (suerteActual == SUERTE_MAYOR) {
			apuestaMayor += ultimaApuesta;
		}

		if (suerteActual == SUERTE_MENOR) {
			apuestaMenor += ultimaApuesta;
		}

		if (suerteActual == SUERTE_PARES) {
			apuestaPares += ultimaApuesta;
		}

		if (suerteActual == SUERTE_JUEGO) {
			apuestaJuego += ultimaApuesta;
		}	
		ultimaApuesta = apuesta;
		manoUltimaApuesta = mano;
		manoHabla = getSiguienteManoQueHabla();		
	}
	
	public void ordago(Mano mano) {
		this.ordago = true;
		manoUltimaApuesta = mano;
		manoHabla = getSiguienteManoQueHabla();	
		this.partida.firePartidaEvento(partida.getEstadoPantallaEventoTipoAccion(TipoEventoPartida.ACCION_MUS));
	}
	
	public void pasar() {		
		manoHabla = getSiguienteManoQueHabla();
		
		if (manoHabla == null) {
			if (manoUltimaApuesta != null) {
				Equipo equipoQueGanaSuerteActual = manoUltimaApuesta.getJugador().getEquipo();
				if (suerteActual == SUERTE_MAYOR) {
					equipoGanaMayorEnPaso = equipoQueGanaSuerteActual;
				}
	
				if (suerteActual == SUERTE_MENOR) {
					equipoGanaMenorEnPaso = equipoQueGanaSuerteActual;
				}
	
				if (suerteActual == SUERTE_PARES) {
					equipoGanaParesEnPaso = equipoQueGanaSuerteActual;
				}
	
				if (suerteActual == SUERTE_JUEGO) {
					equipoGanaJuegoEnPaso = equipoQueGanaSuerteActual;
				}
			}
			
			ultimaApuesta = 0;
			manoUltimaApuesta = null;
			ordago = false;
			cambiarSuerte();
			if (suerteActual == SUERTE_ACABADO) {
				calcularResultado();
			} else {
				manoHabla = getSiguienteManoQueHabla();
			}
		}	
		this.partida.firePartidaEvento(partida.getEstadoPantallaEventoTipoAccion(TipoEventoPartida.ACCION_MUS));
	}
	
	public void aceptar(Mano mano) {
		if (ordago) {
			calcularResultado();
			suerteActual = SUERTE_ACABADO;
		} else {
			if (suerteActual == SUERTE_MAYOR) {
				apuestaMayor += ultimaApuesta;
			}
	
			if (suerteActual == SUERTE_MENOR) {
				apuestaMenor += ultimaApuesta;
			}
	
			if (suerteActual == SUERTE_PARES) {
				apuestaPares += ultimaApuesta;
			}
	
			if (suerteActual == SUERTE_JUEGO) {
				apuestaJuego += ultimaApuesta;
			}	
			ultimaApuesta = 0;
			manoUltimaApuesta = null;
			cambiarSuerte();
			if (suerteActual == SUERTE_ACABADO) {
				calcularResultado();
			} else {
				manoHabla = getSiguienteManoQueHabla();
			}
		}
		this.partida.firePartidaEvento(partida.getEstadoPantallaEventoTipoAccion(TipoEventoPartida.ACCION_MUS));
	}
	
	private void calcularResultado() {
		if (ordago) {
			Mano manoGanadora = getGanadorSuerteActual();
			juego.ganaOrdago(manoGanadora.getJugador());
		} else {
			Equipo ganadorMayor = getEquipoGanadorMayor();
			if (apuestaMayor == 0) {
				apuestaMayor = 1;
			}
			juego.sumaPuntos(ganadorMayor, apuestaMayor);
			
			if (!juego.isTerminado()) {
				Equipo ganadorMenor = getEquipoGanadorMenor();
				if (apuestaMenor == 0) {
					apuestaMenor = 1;
				}
				juego.sumaPuntos(ganadorMenor, apuestaMenor);
			}
			if (!juego.isTerminado()) {
				if (hayPares()) {
					Equipo ganadorPares = getEquipoGanadorPares();
	
					if (apuestaPares == 0) {
						apuestaPares = 1;
					}
					
					int totalGanadoAPares = apuestaPares + cuantoSumaGanadorDePares(ganadorPares);
					juego.sumaPuntos(ganadorPares, totalGanadoAPares);
				}
			}					
			if (!juego.isTerminado()) {
				Equipo ganadorJuego = getEquipoGanadorJuego();
				if (apuestaJuego == 0) {
					apuestaJuego = 1;
				}
				
				int totalGanadoAJuego = apuestaJuego + cuantoSumaGanadorDeJuego(ganadorJuego);
				juego.sumaPuntos(ganadorJuego, totalGanadoAJuego);
			}								
		}
		
		partida.lanzaRonda();
	}
	
	private void cambiarSuerte() {
		if (suerteActual == SUERTE_MAYOR) {
			suerteActual = SUERTE_MENOR;
		} else if (suerteActual == SUERTE_MENOR) {
			if (hayPares()) {
				suerteActual = SUERTE_PARES;
			} else {
				if (unSoloEquipoTieneJuego()) {
					suerteActual = SUERTE_ACABADO;
				} else {
					suerteActual = SUERTE_JUEGO;
				}
			}
		} else if (suerteActual == SUERTE_PARES) {
			if (unSoloEquipoTieneJuego()) {
				suerteActual = SUERTE_ACABADO;
			} else {
				suerteActual = SUERTE_JUEGO;
			}
			
		} else if (suerteActual == SUERTE_JUEGO) {
			suerteActual = SUERTE_ACABADO;
		}	
	}
	
	public void descartar(Mano mano) {
		Collection<Carta> descartadas = mano.getListaDescartadas();
		baraja.tirarAlMonton(descartadas);
		
		manoHabla = getSiguienteManoQueHabla();
		if (manoHabla == null) {
			darCartas();
			inicia(SUERTE_MUS);
		}		
		this.partida.firePartidaEvento(partida.getEstadoPantallaEventoTipoAccion(TipoEventoPartida.ACCION_MUS));
	}
	
	public int cuantasPide(Mano mano) {
		return mano.getListaDescartadas().size();
	}
	
	public void darCartas() {
		for (Mano mano: listaMano) {
			Collection<Carta> nuevasCartas = baraja.getCartas(cuantasPide(mano));
			mano.estableceNuevasCartas(nuevasCartas);
		}		
	}
	
	private void inicia(int suerte) {
		suerteActual = suerte;
		manoHabla = null;
		manoUltimaApuesta = null;
		manoHabla = getSiguienteManoQueHabla();
		ultimaApuesta = 0;
		ordago = false;
	}
	
	public boolean hayUltimaApuesta() {
		return manoUltimaApuesta != null;
	}
	
	public boolean isSuerteTerminada() {
		return getSiguienteManoQueHabla() != null;
	}
	
	public Mano getSiguienteManoQueHabla() {
		Mano manoSiguiente;
		boolean manoInicialHaHablado = false;
		if (manoHabla == null) {
			manoSiguiente = manoInicial;
		} else {
			manoSiguiente = getSiguienteMano(manoHabla);
			manoInicialHaHablado = true;
		}
		int cuantos = 0;
		while (true) {		
			if (manoSiguiente.equals(manoUltimaApuesta) 
					|| !hayUltimaApuesta() && manoInicialHaHablado && manoSiguiente.equals(manoInicial)
					|| suerteActual == SUERTE_PARES && !hayPares()					
					) {
				return null;
			}
			
			if (hayUltimaApuesta() && !arePareja(manoUltimaApuesta, manoSiguiente) ||
					!hayUltimaApuesta() && 
						(suerteActual == SUERTE_MUS || 
						suerteActual == SUERTE_DESCARTE || 
						suerteActual == SUERTE_MAYOR || 
						suerteActual == SUERTE_MENOR ||
						suerteActual == SUERTE_PARES && manoSiguiente.hasPares() ||
						suerteActual == SUERTE_JUEGO && (hayJuego() && manoSiguiente.hasJuego() || !hayJuego()))
				) {
				return manoSiguiente;			
			} 
			manoSiguiente = getSiguienteMano(manoSiguiente);
			cuantos++;
			if (cuantos > 5) {
				throw new RuntimeException("Ciclo infinito");
			}
		}
	}

	private boolean arePareja(Mano mano1, Mano mano2) {
		if (mano1 == null || mano2 == null) {
			return false;
		}
		
		int indiceMano1 = listaMano.indexOf(mano1);
		int indiceMano2 = listaMano.indexOf(mano2);
		
		return (indiceMano1%2 == indiceMano2%2);
	}
	
	private Mano getSiguienteMano(Mano manoActual) {
		int indiceActual = listaMano.indexOf(manoActual);
		if (indiceActual < listaMano.size() - 1) {
			return listaMano.get(indiceActual +1);
		} else {
			return listaMano.get(0);
		}
	}
	
	public boolean hayPares() {
		return (listaMano.get(0).hasPares() || listaMano.get(2).hasPares()) &&  
				(listaMano.get(1).hasPares() || listaMano.get(3).hasPares());
	}

	public boolean hayJuego() {
		return (listaMano.get(0).hasJuego() || listaMano.get(2).hasJuego()) &&  
				(listaMano.get(1).hasJuego() || listaMano.get(3).hasJuego());
	}
	
	public boolean unSoloEquipoTieneJuego() {
		return ((listaMano.get(0).hasJuego() || listaMano.get(2).hasJuego()) &&  
				!listaMano.get(1).hasJuego() && !listaMano.get(3).hasJuego())
				||
				((listaMano.get(1).hasJuego() || listaMano.get(3).hasJuego()) &&  
				!listaMano.get(0).hasJuego() && !listaMano.get(2).hasJuego());
	}
	
	public Equipo getEquipoGanadorMayor() {
		if (equipoGanaMayorEnPaso != null) {
			return equipoGanaMayorEnPaso;
		} else {
			return getGanadorMayor().getJugador().getEquipo();
		}
	}
	
	public Mano getGanadorMayor() {
		return listaMano.stream().max(Mano::compararMayor).get();
	}
	
	public Equipo getEquipoGanadorMenor() {
		if (equipoGanaMenorEnPaso != null) {
			return equipoGanaMenorEnPaso;
		} else {
			return getGanadorMenor().getJugador().getEquipo();
		}
	}
	
	public Mano getGanadorMenor() {
		return listaMano.stream().max(Mano::compararMenor).get();
	}
	
	public Equipo getEquipoGanadorPares() {
		if (equipoGanaParesEnPaso != null) {
			return equipoGanaParesEnPaso;
		} else {
			return getGanadorPares().getJugador().getEquipo();
		}
	}
	
	public Mano getGanadorPares() {
		return listaMano.stream().max(Mano::compararPares).get();
	}
	
	public Equipo getEquipoGanadorJuego() {
		if (equipoGanaJuegoEnPaso != null) {
			return equipoGanaJuegoEnPaso;
		} else {
			return getGanadorJuego().getJugador().getEquipo();
		}
	}	
	
	public Mano getGanadorJuego() {
		return listaMano.stream().max(Mano::compararJuego).get();
	}
	
	private int cuantoSumaGanadorDePares(Equipo equipo) {
		int total = 0;
		Mano manoJugador1 = equipo.getJugador1().getManoActual();
		Mano manoJugador2 = equipo.getJugador2().getManoActual();
		
		total += cuantoSumaGanadorDePares(manoJugador1);
		total += cuantoSumaGanadorDePares(manoJugador2);
		
		return total;
	}
	
	private int cuantoSumaGanadorDePares(Mano mano) { 
		int total = 0;
		if (mano.hasDuples()) {
			total = 3;
		} else if (mano.hasMedias()){
			total = 2;
		} else if (mano.hasUnSoloPar()){
			total = 1;
		} 
		return total;
	}
	
	private int cuantoSumaGanadorDeJuego(Equipo equipo) {
		int total = 0;
		Mano manoJugador1 = equipo.getJugador1().getManoActual();
		Mano manoJugador2 = equipo.getJugador2().getManoActual();
		
		total += cuantoSumaGanadorDeJuego(manoJugador1);
		total += cuantoSumaGanadorDeJuego(manoJugador2);
		
		return total;
	}
	
	private int cuantoSumaGanadorDeJuego(Mano mano) { 
		int total = 0;
		if (mano.getPuntuacion() == 31) {
			total = 3;
		} else if (mano.getPuntuacion() == 32){
			total = 2;
		} else if (mano.hasJuego()){
			total = 1;
		} 
		return total;
	}
	
	public Mano getGanadorSuerteActual() {
		Mano manoGanadora = null;
		if (suerteActual == SUERTE_MAYOR) {
			manoGanadora = getGanadorMayor();
		}

		if (suerteActual == SUERTE_MENOR) {
			manoGanadora = getGanadorMenor();
		}

		if (suerteActual == SUERTE_PARES) {
			manoGanadora = getGanadorPares();
		}

		if (suerteActual == SUERTE_JUEGO) {
			manoGanadora = getGanadorJuego();
		}	
		return manoGanadora;	
	}
	
	
	public int getSuerteActual() {
		return this.suerteActual;
	}
	

	public Mano getManoInicial() {
		return manoInicial;
	}

	public void setManoInicial(Mano manoInicial) {
		this.manoInicial = manoInicial;
	}

	public Mano getManoHabla() {
		return manoHabla;
	}

	public void setManoHabla(Mano manoHabla) {
		this.manoHabla = manoHabla;
	}

	public Mano getManoUltimaApuesta() {
		return manoUltimaApuesta;
	}

	public void setManoUltimaApuesta(Mano manoUltimaApuesta) {
		this.manoUltimaApuesta = manoUltimaApuesta;
	}

	public int getApuestaMayor() {
		return apuestaMayor;
	}

	public void setApuestaMayor(int apuestaMayor) {
		this.apuestaMayor = apuestaMayor;
	}

	public Equipo getEquipoGanaMayorEnPaso() {
		return equipoGanaMayorEnPaso;
	}

	public void setEquipoGanaMayorEnPaso(Equipo equipoGanaMayorEnPaso) {
		this.equipoGanaMayorEnPaso = equipoGanaMayorEnPaso;
	}

	public int getApuestaMenor() {
		return apuestaMenor;
	}

	public void setApuestaMenor(int apuestaMenor) {
		this.apuestaMenor = apuestaMenor;
	}

	public Equipo getEquipoGanaMenorEnPaso() {
		return equipoGanaMenorEnPaso;
	}

	public void setEquipoGanaMenorEnPaso(Equipo equipoGanaMenorEnPaso) {
		this.equipoGanaMenorEnPaso = equipoGanaMenorEnPaso;
	}

	public int getApuestaPares() {
		return apuestaPares;
	}

	public void setApuestaPares(int apuestaPares) {
		this.apuestaPares = apuestaPares;
	}

	public Equipo getEquipoGanaParesEnPaso() {
		return equipoGanaParesEnPaso;
	}

	public void setEquipoGanaParesEnPaso(Equipo equipoGanaParesEnPaso) {
		this.equipoGanaParesEnPaso = equipoGanaParesEnPaso;
	}

	public int getApuestaJuego() {
		return apuestaJuego;
	}

	public void setApuestaJuego(int apuestaJuego) {
		this.apuestaJuego = apuestaJuego;
	}

	public Equipo getEquipoGanaJuegoEnPaso() {
		return equipoGanaJuegoEnPaso;
	}

	public void setEquipoGanaJuegoEnPaso(Equipo equipoGanaJuegoEnPaso) {
		this.equipoGanaJuegoEnPaso = equipoGanaJuegoEnPaso;
	}

	public List<Mano> getListaMano() {
		return Collections.unmodifiableList(listaMano);
	}

	public boolean isOrdago() {
		return ordago;
	}

	public Juego getJuego() {
		return juego;
	}
	
	private EstadoPantallaSimple getEstadoPantallaSimpleCartasTapadas(TipoEventoPartida eventoPartida, Mano mano) {	
		return new EstadoPantallaSimple(eventoPartida, mano.getJugador().getNombre(), mano.getJugador().getMensaje(), getListaCartaDorso(), 
				mano.hasPares(), mano.hasJuego(),
				mano.equals(manoInicial), mano.equals(manoHabla), 
				getManoDorso());
	}
	
	private EstadoPantallaSimple getEstadoPantallaSimpleCartas(TipoEventoPartida eventoPartida, Mano mano) {	
		return new EstadoPantallaSimple(eventoPartida, mano.getJugador().getNombre(), mano.getJugador().getMensaje(), mano.getListaCartas(), 
				mano.hasPares(), mano.hasJuego(),
				mano.equals(manoInicial), mano.equals(manoHabla), mano);
	}
	
	public EstadoPantallaEvento getEstadoPantallaEvento(EstadoPantallaEvento estadoPartidaEvento, String nombreJugador) {
		Mano mano = getManoJugador(nombreJugador);
		List<EstadoPantallaSimple> listaOtrasMano = getListaEstadoPantallaSimple(estadoPartidaEvento.getTipoEventoPartida(), mano);
		EstadoPantallaEvento estadoPantallaEvento = 
				new EstadoPantallaEvento(estadoPartidaEvento.getTipoEventoPartida(), 
									estadoPartidaEvento.getPartida(),
									getTextoSuerteActual(), 
									mano.getJugador().getNombre(), 
									mano.getJugador().getMensaje(),
									mano.getListaCartas(), 
									this.apuestaMayor,
									this.apuestaMenor, 
									this.apuestaPares, 
									this.apuestaJuego, 
									this.ultimaApuesta, 
									puedeAceptar(mano), 
									puedePasar(mano), 
									puedeMus(mano),
									puedeNoHayMus(mano),
									puedeDescartar(mano),
									puedeApostar(mano),
									2,
									puedeEnvidar(mano),
									listaOtrasMano.get(1),
									listaOtrasMano.get(2),
									listaOtrasMano.get(0),
									mano.hasPares(), 
									mano.hasJuego(),
									mano.equals(manoInicial), 
									mano.equals(manoHabla),
									mano);

		
		return estadoPantallaEvento;
	}

	private Mano getManoJugador(String nombreJugador) {
		return listaMano.stream()
				.filter(m -> m.getJugador().getNombre().equals(nombreJugador))
				.findFirst()
				.get();
	}
	
	private List<EstadoPantallaSimple> getListaEstadoPantallaSimple(TipoEventoPartida eventoPartida, Mano mano) {
		List<Mano> listaRestoMano = getListaRestoMano(mano);
		return listaRestoMano.parallelStream()
			.map(aux -> (suerteActual != SUERTE_ACABADO)? 
							getEstadoPantallaSimpleCartasTapadas(eventoPartida, aux): 
								getEstadoPantallaSimpleCartas(eventoPartida, aux))
			.collect(Collectors.toList());
	}
	
	private List<Mano> getListaRestoMano(Mano mano) {
		int indiceJugadorManoActual = listaMano.indexOf(mano);
		
		List<Mano> listaDesdeMano = new ArrayList<>();
		listaDesdeMano.addAll(listaMano.subList(indiceJugadorManoActual, listaMano.size()));
		listaDesdeMano.addAll(listaMano.subList(0, indiceJugadorManoActual));
		return listaDesdeMano;
	}
	
	
	private void limpiaMensajes() {
		for (Mano mano: listaMano) {
			mano.getJugador().setMensaje("");
		}
	}
		
	private String getTextoSuerteActual() {
		switch (suerteActual) {
			case SUERTE_MUS: 
			case SUERTE_DESCARTE:
				return "Descarte";
			case SUERTE_MAYOR:
				return "Mayor";
			case SUERTE_MENOR:
				return "Menor";
			case SUERTE_PARES:
				return "Pares";
			case SUERTE_JUEGO:
				if (hayJuego()) {
					return "Juego";
				} else {
					return "Punto";
				}
			case SUERTE_ACABADO:
				return "Juego terminado";
			default:
				throw new RuntimeException("Suerte actual no contemplada");
		}
	}
	
	private Mano getManoDorso() {
		Mano manoDorso = new Mano();
		manoDorso.setCarta1(Carta.getDorso());
		manoDorso.setCarta2(Carta.getDorso());
		manoDorso.setCarta3(Carta.getDorso());
		manoDorso.setCarta4(Carta.getDorso());
		return manoDorso;
	}
	
	private List<Carta> getListaCartaDorso() {
		List<Carta> listaCartaDorso = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			listaCartaDorso.add(Carta.getDorso());
		}
		return listaCartaDorso;
	}
	

}
