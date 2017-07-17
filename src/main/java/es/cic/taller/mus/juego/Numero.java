package es.cic.taller.mus.juego;

public enum Numero {
	UNO(1, 1, "uno"),
	DOS(2, 1, "dos"),
	TRES(3, 10, "tres"),
	CUATRO(4, 4, "cuatro"),
	CINCO(5, 5, "cinco"),
	SEIS(6, 6, "seis"),
	SIETE(7, 7, "siete"),
	SOTA(10, 10, "sota"),
	CABALLO(11, 10, "caballo"),
	REY(12, 10, "rey");
	
	private final int numero;
	private final int valor;
	private final String texto;
	
	Numero(int numero, int valor, String texto) {
		this.numero = numero;
		this.valor = valor;
		this.texto = texto;
	}
	
	public String getTexto() {
		return this.texto;
	}
	
	public int getValor() {
		return valor;
	}
	
	public int getNumero() {
		return numero;
	}
	
	public String getNumeroComoTexto() {
		if (numero < 10) {
			return "0" + numero;
		}
		return "" + numero;
	}
	
	public int getNumeroReal() {
		switch(numero) {
			case 2: return 1; 
			case 3: return 12;
			default: return numero;
		}
	}
	
	public static Numero getNumero(int numero) {
		switch (numero) {
		case 1: return Numero.UNO;
		case 2: return Numero.DOS;
		case 3: return Numero.TRES;
		case 4: return Numero.CUATRO;
		case 5: return Numero.CINCO;
		case 6: return Numero.SEIS;
		case 7: return Numero.SIETE;
		case 8: return Numero.SOTA;
		case 9: return Numero.CABALLO;
		case 10: return Numero.REY;
		default: throw new RuntimeException("Carta no soportada");
		}
	}
}
