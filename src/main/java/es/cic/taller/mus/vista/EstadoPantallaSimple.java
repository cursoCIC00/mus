package es.cic.taller.mus.vista;

import java.util.List;

import es.cic.taller.mus.juego.Carta;
import es.cic.taller.mus.juego.Mano;
import es.cic.taller.mus.juego.TipoEventoPartida;

public class EstadoPantallaSimple {
	private TipoEventoPartida tipoEventoPartida;
	private String nombre;
	private String mensaje;
	private List<Carta> listaCartas;
	private int apuestaMayor;
	private int apuestaMenor;
	private int apuestaPares;
	private int apuestaJuego;
	private int apuestaActual;
	private boolean aceptar;
	private boolean pasar;
	private boolean mus;
	private boolean noHayMus;
	private boolean apostar;
	private int cuantoApuesta = 2;
	private boolean envidar;
	private boolean descartar;
	
	private boolean tengoPares;
	private boolean tengoJuego;
	
	private boolean manoInicial;
	private boolean manoHabla;
	
	private Mano mano;
	
	public EstadoPantallaSimple(TipoEventoPartida tipoEventoPartida, String nombre, String mensaje, List<Carta> listaCartas, boolean tengoPares, boolean tengoJuego, boolean manoInicial, boolean manoHabla, Mano mano) {
		super();
		this.tipoEventoPartida = tipoEventoPartida;
		this.nombre = nombre;
		this.mensaje = mensaje;
		this.listaCartas = listaCartas;
		this.tengoPares = tengoPares;
		this.tengoJuego = tengoJuego;
		this.manoInicial = manoInicial;
		this.manoHabla = manoHabla;
		this.mano = mano;
	}

	public EstadoPantallaSimple(TipoEventoPartida eventoPartida, String nombre, String mensaje, List<Carta> listaCartas,
			int apuestaMayor, int apuestaMenor, int apuestaPares, int apuestaJuego, int apuestaActual, boolean aceptar, boolean pasar,
			boolean mus, boolean noHayMus, boolean descartar, boolean apostar, int cuantoApuesta, boolean envidar, boolean tengoPares, boolean tengoJuego,
			boolean manoInicial, boolean manoHabla, Mano mano) {
		this(eventoPartida, nombre, mensaje, listaCartas, tengoPares, tengoJuego, manoInicial, manoHabla, mano);
		this.apuestaMayor = apuestaMayor;
		this.apuestaMenor = apuestaMenor;
		this.apuestaPares = apuestaPares;
		this.apuestaJuego = apuestaJuego;
		this.apuestaActual = apuestaActual;
		this.aceptar = aceptar;
		this.setPasar(pasar);
		this.mus = mus;
		this.noHayMus = noHayMus;
		this.descartar = descartar;
		this.apostar = apostar;
		this.cuantoApuesta = cuantoApuesta;
		this.envidar = envidar;

	}

	public EstadoPantallaSimple() {
		super();
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<Carta> getListaCartas() {
		return listaCartas;
	}

	public void setListaCartas(List<Carta> listaCartas) {
		this.listaCartas = listaCartas;
	}



	public int getApuestaMayor() {
		return apuestaMayor;
	}

	public void setApuestaMayor(int apuestaMayor) {
		this.apuestaMayor = apuestaMayor;
	}

	public int getApuestaMenor() {
		return apuestaMenor;
	}

	public void setApuestaMenor(int apuestaMenor) {
		this.apuestaMenor = apuestaMenor;
	}

	public int getApuestaPares() {
		return apuestaPares;
	}

	public void setApuestaPares(int apuestaPares) {
		this.apuestaPares = apuestaPares;
	}

	public int getApuestaJuego() {
		return apuestaJuego;
	}

	public void setApuestaJuego(int apuestaJuego) {
		this.apuestaJuego = apuestaJuego;
	}

	public int getApuestaActual() {
		return apuestaActual;
	}

	public void setApuestaActual(int apuestaActual) {
		this.apuestaActual = apuestaActual;
	}

	public boolean isAceptar() {
		return aceptar;
	}

	public void setAceptar(boolean aceptar) {
		this.aceptar = aceptar;
	}

	public boolean isMus() {
		return mus;
	}

	public void setMus(boolean mus) {
		this.mus = mus;
	}

	public boolean isNoHayMus() {
		return noHayMus;
	}

	public void setNoHayMus(boolean noHayMus) {
		this.noHayMus = noHayMus;
	}

	public boolean isApostar() {
		return apostar;
	}

	public void setApostar(boolean apostar) {
		this.apostar = apostar;
	}

	public int getCuantoApuesta() {
		return cuantoApuesta;
	}

	public void setCuantoApuesta(int cuantoApuesta) {
		this.cuantoApuesta = cuantoApuesta;
	}


	public boolean isTengoPares() {
		return tengoPares;
	}

	public void setTengoPares(boolean tengoPares) {
		this.tengoPares = tengoPares;
	}

	public boolean isTengoJuego() {
		return tengoJuego;
	}

	public void setTengoJuego(boolean tengoJuego) {
		this.tengoJuego = tengoJuego;
	}

	public TipoEventoPartida getTipoEventoPartida() {
		return tipoEventoPartida;
	}

	public void setTipoEventoPartida(TipoEventoPartida tipoEventoPartida) {
		this.tipoEventoPartida = tipoEventoPartida;
	}

	public boolean isEnvidar() {
		return envidar;
	}

	public void setEnvidar(boolean envidar) {
		this.envidar = envidar;
	}

	public boolean isManoInicial() {
		return manoInicial;
	}

	public void setManoInicial(boolean manoInicial) {
		this.manoInicial = manoInicial;
	}

	public boolean isManoHabla() {
		return manoHabla;
	}

	public void setManoHabla(boolean manoHabla) {
		this.manoHabla = manoHabla;
	}

	public Mano getMano() {
		return mano;
	}

	public void setMano(Mano mano) {
		this.mano = mano;
	}

	public boolean isDescartar() {
		return descartar;
	}

	public void setDescartar(boolean descartar) {
		this.descartar = descartar;
	}

	public boolean isPasar() {
		return pasar;
	}

	public void setPasar(boolean pasar) {
		this.pasar = pasar;
	}

}