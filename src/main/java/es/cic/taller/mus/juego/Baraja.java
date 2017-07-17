package es.cic.taller.mus.juego;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Baraja {
	private List<Carta> listaCartas = new ArrayList<>();
	
	private List<Carta> listaMonton = new ArrayList<>();
	
	public Baraja() {
		generarCartas();
	}

	private void generarCartas() {
		listaCartas.addAll(getPalo(Palo.OROS));
		listaCartas.addAll(getPalo(Palo.COPAS));
		listaCartas.addAll(getPalo(Palo.BASTOS));
		listaCartas.addAll(getPalo(Palo.ESPADAS));
		barajear();
	}
	
	public void tirarAlMonton(Collection<Carta> descartadas) {
		listaMonton.addAll(descartadas);		
	}
	
	private List<Carta> getPalo(Palo palo) {
		
		List<Carta> listaCartasPalo = new ArrayList<>();
		
		for (int i= 1; i <= Palo.CUANTAS_CARTA_POR_PALO; i++) {
			Carta carta = new Carta();
			Numero numero = Numero.getNumero(i);
			carta.setNumero(numero);
			carta.setPalo(palo);
			listaCartasPalo.add(carta);
		}
		return listaCartasPalo;
	}
	
	public void barajear() {
		Collections.shuffle(listaCartas);
	}
	
	public void resetear() {
		listaCartas.clear();
		listaMonton.clear();
		
		generarCartas();
	}
	
	private Carta getCarta() {
		if (listaCartas.isEmpty()) {
			listaCartas.addAll(listaMonton);
			barajear();
			listaMonton.clear();
		}
		Carta carta = listaCartas.remove(0);
		return carta;		
	}
	
	public Collection<Carta> getCartas(int cuantas) {
		Collection<Carta> nuevasCartas = new ArrayList<>();
		for(int i = 0; i < cuantas; i++) {
			nuevasCartas.add(getCarta());
		}
		return nuevasCartas;
	}
	
	public Mano getMano() {
		Mano mano = new Mano();
		mano.setCarta1(getCarta());
		mano.setCarta2(getCarta());
		mano.setCarta3(getCarta());
		mano.setCarta4(getCarta());
	
		return mano;
	}
}
