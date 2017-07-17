package es.cic.taller.mus.juego;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mano {

	private Jugador jugador;
	
	private List<Carta> listaCartas = new ArrayList<>();
	
	private List<Carta> listaDescartadas = new ArrayList<>();

	
	public Mano() {
		for (int i = 0 ; i < 4; i++) 
			listaCartas.add(null);
	}

	public Mano(Jugador jugador) {
		this();
		this.setJugador(jugador);
	}
	

	public Carta getCarta1() {
		return listaCartas.get(0);
	}
	public void setCarta1(Carta carta1) {
		estableceCartaEnIndice(0, carta1);
	}
	
	public Carta getCarta2() {
		return listaCartas.get(1);
	}
	public void setCarta2(Carta carta2) {
		estableceCartaEnIndice(1, carta2);
	}
	public Carta getCarta3() {
		return listaCartas.get(2);
	}
	public void setCarta3(Carta carta3) {
		estableceCartaEnIndice(2, carta3);
	}
	public Carta getCarta4() {
		return listaCartas.get(3);
	}
	public void setCarta4(Carta carta4) {
		estableceCartaEnIndice(3, carta4);
	}

	private void estableceCartaEnIndice(int indice, Carta carta) {
		if (listaCartas.size() > indice ) {
			listaCartas.remove(indice);
			listaCartas.add(indice, carta);
		} else {
			this.listaCartas.add(carta);
		}		
	}
	
	public void estableceDescartadas(Collection<Carta> listaCartasDescartadas) {
		listaDescartadas.clear();
		listaDescartadas.addAll(listaCartasDescartadas);
	}
	
	public Collection<Carta> getListaDescartadas() {
		return listaDescartadas;
	}
	
	public void estableceNuevasCartas(Collection<Carta> nuevasCartas) {
		if (listaDescartadas.size() != nuevasCartas.size()) {
			throw new RuntimeException(String.format("Descartadas %d cartas, pero sólo tengo %d nuevas", listaDescartadas.size(), nuevasCartas.size()));
		}
		int i = 0;
		List<Carta> listaNuevasCartas = new ArrayList<>(nuevasCartas);
		for (Carta c:listaDescartadas) {
			int indiceDescartada = listaCartas.indexOf(c);
			estableceCartaEnIndice(indiceDescartada, listaNuevasCartas.get(i));
			i++;
		}
		listaDescartadas.clear();
	}
	
	
	public boolean puedeHablarPares() {
		return getPesoPares() > 1;
	}
	
	public boolean puedeHablarJuego() {
		return getPuntuacion() > 30;
	}
	
	public int getPuntuacion() {
		return 
				getCarta1().getNumero().getValor() +
				getCarta2().getNumero().getValor() + 
				getCarta3().getNumero().getValor() + 
				getCarta4().getNumero().getValor();
	}
	
	public static int compararMayor(Mano mano1, Mano mano2) {
		List<Carta> listaCartasMano1 = mano1.getCartasOrdenadas();
		List<Carta> listaCartasMano2 = mano2.getCartasOrdenadas();
		
		int resultado = 0;
		
		for (int i = 0 ; i< listaCartasMano1.size(); i++) {
			int numero1 = listaCartasMano1.get(i).getNumero().getNumeroReal();
			int numero2 = listaCartasMano2.get(i).getNumero().getNumeroReal();
			
			if (numero1 != numero2) {
				resultado = numero2 - numero1;
				break;
			}
		}
		return resultado;		
	}	
	
	public static int compararMenor(Mano mano1, Mano mano2) {
		List<Carta> listaCartasMano1 = mano1.getCartasOrdenadas();
		List<Carta> listaCartasMano2 = mano2.getCartasOrdenadas();
		
		int resultado = 0;
		
		for (int i = listaCartasMano1.size() - 1; i>= 0 ; i--) {
			int numero1 = listaCartasMano1.get(i).getNumero().getNumeroReal();
			int numero2 = listaCartasMano2.get(i).getNumero().getNumeroReal();
			
			if (numero1 != numero2) {
				resultado = numero1 - numero2;
				break;
			}
		}
		return resultado;		
	}

	public static int compararPares(Mano mano1, Mano mano2) {
		int diferenciaTipoPares = mano2.getPesoPares() - mano1.getPesoPares();
		
		int resultado = 0;
		
		if (diferenciaTipoPares == 0) {
			if (mano1.hasDuples()) {
				if (mano1.getParMayor() != mano2.getParMayor()) {
					resultado = mano2.getParMayor() - mano1.getParMayor();
				} else {
					resultado = mano2.getParMenor() - mano1.getParMenor();
				}
			} else if (mano1.hasMedias()) {
				resultado = mano2.getMedias() - mano1.getMedias();
			} else if (mano1.hasUnSoloPar()) {
				resultado = mano2.getParMayor() - mano1.getParMayor();
			} else {
				resultado = compararMayor(mano1, mano2);
			}
		} else 
			resultado = diferenciaTipoPares;
		
		
		return resultado;
	}
	
	private int getParMayor() {
		return getMapaDeCuantasDeCadaCarta().entrySet().stream()
			.filter(e -> e.getValue() == 4 || e.getValue() == 2)
			.max( (e1, e2) -> e1.getKey() - e2.getKey())
			.get()
			.getKey().intValue();
	}
	
	private int getParMenor() {
		return getMapaDeCuantasDeCadaCarta().entrySet().stream()
			.filter(e -> e.getValue() == 4 || e.getValue() == 2)
			.min( (e1, e2) -> e1.getKey() - e2.getKey())
			.get()
			.getKey().intValue();
	}	
	
	private int getMedias() {
		return getMapaDeCuantasDeCadaCarta().entrySet().stream()
			.filter(e -> e.getValue() == 3).findFirst()
			.get()
			.getKey().intValue();
	}	
	
	private int getPesoPares() {
		int resultado = 1;
		if (hasDuples()) resultado = 4;
		if (hasMedias()) resultado = 3;
		if (hasUnSoloPar()) resultado = 2;
		return resultado;
	}
	
	public static int compararJuego(Mano mano1, Mano mano2) {
		return correctorValorJuego(mano2) - correctorValorJuego(mano1);
	}
	
	private static int correctorValorJuego(Mano mano) {
		int puntuacion = mano.getPuntuacion();
		
		switch (puntuacion) {
			case 31: return 42;
			case 32: return 41;
			default: return puntuacion;
		}
	}
	
	private List<Carta> getCartasOrdenadas() {
		List<Carta> listaCartas = new ArrayList<>();
		listaCartas.add(getCarta1());
		listaCartas.add(getCarta2());
		listaCartas.add(getCarta3());
		listaCartas.add(getCarta4());
		
		Collections.sort(listaCartas, 
				(c1, c2) -> 
					c2.getNumero().getNumeroReal() - c1.getNumero().getNumeroReal()
				);	
		return listaCartas;
	}
	
	public boolean hasJuego() {
		return getPuntuacion() > 30;
	}
	
	public boolean hasPares() {
		Map<Integer, Long> cuantasDeCadaCarta = getMapaDeCuantasDeCadaCarta();
		
		return cuantasDeCadaCarta.keySet().size() < 4;
	}
	
	public boolean hasUnSoloPar() {
		long cuantosParesDiferentes = getMapaDeCuantasDeCadaCarta().values().stream().
				filter(c -> c.longValue() == 2).count();
		
		return cuantosParesDiferentes == 1;
	}
	
	public boolean hasMedias() {
		long cuantasMedias = getMapaDeCuantasDeCadaCarta().values().stream().
				filter(c -> c.longValue() == 3).count();
		return cuantasMedias == 1;
	}
	
	public boolean hasDuples() {
		boolean hasDosParesDiferentes = getMapaDeCuantasDeCadaCarta().values().stream().
				filter(c -> c.longValue() == 2).count() == 2;
		
		boolean hasCuatroIguales = getMapaDeCuantasDeCadaCarta().values().stream().
				filter(c -> c.longValue() == 4).count() == 1;
		
		return (hasCuatroIguales || hasDosParesDiferentes);
	}
	
	private Map<Integer, Long> getMapaDeCuantasDeCadaCarta() {
		Map<Integer, Long> cuantasDeCadaCarta = new HashMap<>();
		
		cuantasDeCadaCarta = getCartasOrdenadas().stream()
			.map(c -> c.getNumero().getNumeroReal())
			.collect(
					Collectors.groupingBy(Function.identity(),Collectors.counting())
					);
		return cuantasDeCadaCarta;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public List<Carta> getListaCartas() {
		return listaCartas;
	}
}
