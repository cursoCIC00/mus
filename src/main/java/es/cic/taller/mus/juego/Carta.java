package es.cic.taller.mus.juego;

public class Carta {
	private Numero numero;
	private Palo palo;
	
	public Carta() {
		
	}
	
	public Carta(Numero numero, Palo palo) {
		this.numero = numero;
		this.palo = palo;
	}
	
	public Numero getNumero() {
		return numero;
	}
	public void setNumero(Numero numero) {
		this.numero = numero;
	}
	public Palo getPalo() {
		return palo;
	}
	public void setPalo(Palo palo) {
		this.palo = palo;
	}
	
	public String getNombreFichero() {
		String texto = palo.getNumero() + numero.getNumeroComoTexto() + ".png";
		return texto;
	}
	
	public static Carta getDorso() {
		return new Carta(Numero.UNO, Palo.ESPECIAL);
	}
	
	@Override
	public String toString() {		
		return numero.getTexto() + " de " + palo.getTexto();
	}
}
