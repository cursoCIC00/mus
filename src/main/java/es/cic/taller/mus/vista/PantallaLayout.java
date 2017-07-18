package es.cic.taller.mus.vista;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.cic.taller.mus.juego.Baraja;
import es.cic.taller.mus.juego.Carta;
import es.cic.taller.mus.juego.Juego;
import es.cic.taller.mus.juego.Mano;
import es.cic.taller.mus.juego.Partida;
import es.cic.taller.mus.juego.PartidaListener;
import es.cic.taller.mus.juego.Ronda;
import es.cic.taller.mus.juego.TipoEventoPartida;


public class PantallaLayout extends GridLayout implements PartidaListener {

	private ManoForm manoFormJugador1;
	private ManoForm manoFormJugador2;
	private ManoForm manoFormJugador3;
	private ManoForm manoFormJugador4;
	
	private MyUI myUI;
	
	private Partida partida;
	
	private EstadoPantallaEvento estadoPantallaEvento;
	
	private Button mus = new Button("Mus");
	private Button noHayMus = new Button("No hay mus");
	private Button descartar = new Button("Descartar");
	
	private Button aceptar = new Button("Aceptar");
	private Button pasar = new Button("Pasar");
	private Button ordago = new Button("Órdago");
	private Button envido = new Button("Envido");
	
	private Button siguienteRonda = new Button("Siguiente Ronda");
	private Button siguienteJuego = new Button("Siguiente Juego");
	
	private Label labelEstado = new Label();
	
	private Grid<Apuesta> gridApuesta = new Grid<>(Apuesta.class);

	private Grid<Juego> grid = new Grid<>(Juego.class);
	
	public PantallaLayout(MyUI myUI, Partida partida) {
		this.partida = partida;
		final Styles styles = Page.getCurrent().getStyles();
		 
        // inject the new color as a style
        styles.add(".v-gridlayout-slot {border-style: dotted;}"
        		);
        this.setHideEmptyRowsAndColumns(false);
		this.myUI = myUI;
	
		
		
		manoFormJugador1 = new ManoForm();
		
		manoFormJugador2 = new ManoForm();
		
		manoFormJugador3 = new ManoForm();
		
		manoFormJugador4 = new ManoForm();

		setRows(5);
		setColumns(3);
		
		
		
		addComponent(manoFormJugador1, 1, 2);
		addComponent(manoFormJugador2, 1, 0);
		addComponent(manoFormJugador3, 0, 1);
		addComponent(manoFormJugador4, 2, 1);
		
	
		
		HorizontalLayout acciones = new HorizontalLayout();
		acciones.addComponents(mus, noHayMus, descartar, aceptar, pasar, ordago, envido, siguienteRonda, siguienteJuego);
		acciones.setSizeFull();
		this.setSizeFull();
		
		mus.setEnabled(false);
		noHayMus.setEnabled(false);
		descartar.setEnabled(false);
		aceptar.setEnabled(false);
		pasar.setEnabled(false);
		ordago.setEnabled(false);
		envido.setEnabled(false);
		siguienteRonda.setEnabled(false);
		siguienteJuego.setEnabled(false);
		
		
		mus.addClickListener(e -> partida.getRondaActual().mus(estadoPantallaEvento.getMano()));
		noHayMus.addClickListener(e -> partida.getRondaActual().noHayMus(estadoPantallaEvento.getMano()));
		descartar.addClickListener(e -> {
			Mano mano = manoFormJugador1.getMano();
			partida.getRondaActual().descartar(mano);
										});
		pasar.addClickListener(e -> partida.getRondaActual().pasar());
		ordago.addClickListener(e -> partida.getRondaActual().ordago(estadoPantallaEvento.getMano()));
		aceptar.addClickListener(e -> partida.getRondaActual().aceptar(estadoPantallaEvento.getMano()));
		envido.addClickListener(e -> partida.getRondaActual().envidar(estadoPantallaEvento.getMano()));
		
		siguienteRonda.addClickListener(e -> partida.lanzaRonda());
		siguienteJuego.addClickListener(e -> partida.lanzaRonda());
		
		HorizontalLayout accionesApuestas = new HorizontalLayout();

		VerticalLayout apuestas = new VerticalLayout();
		
		grid.setColumns("puntuacionEquipo1", "puntuacionEquipo2");
		
		gridApuesta.setColumns("apuesta", "cantidad");
		
		addComponent(grid, 0, 0);
		
		addComponent(gridApuesta, 0, 2);
		
		accionesApuestas.addComponents(acciones, apuestas);
		
		addComponent(accionesApuestas, 0, 3, 2,3);
		
		
		
		addComponent(labelEstado,0,4);
		
		partida.addPartidaListener(this);
		
		estadoPantallaEvento = new EstadoPantallaEvento(TipoEventoPartida.PIDE_DATOS, partida, null, null, null, null, 0, 0, 0, 0, 0, false, false, false, false, false, false, 0, false, null, null, null, false, false, false, false, null);
		estadoPantallaEvento = partida.getRondaActual().getEstadoPantallaEvento(estadoPantallaEvento, myUI.getNombreJugadorActual());
		carga(estadoPantallaEvento);
	}

	private void carga(EstadoPantallaEvento estadoPantallaEvento) {
		labelEstado.setValue(estadoPantallaEvento.getSuerteActual());
		manoFormJugador1.setMano(estadoPantallaEvento.getMano(), estadoPantallaEvento.isDescartar(),
				estadoPantallaEvento.getNombre(), estadoPantallaEvento.isManoHabla(), estadoPantallaEvento.isManoInicial());
		manoFormJugador2.setMano(estadoPantallaEvento.getEnfrente().getMano(), estadoPantallaEvento.getEnfrente().isDescartar(), 
				estadoPantallaEvento.getEnfrente().getNombre(), estadoPantallaEvento.getEnfrente().isManoHabla(), estadoPantallaEvento.getEnfrente().isManoInicial());
		manoFormJugador3.setMano(estadoPantallaEvento.getIzquierda().getMano(), estadoPantallaEvento.getIzquierda().isDescartar(), 
				estadoPantallaEvento.getIzquierda().getNombre(), estadoPantallaEvento.getIzquierda().isManoHabla(), estadoPantallaEvento.getIzquierda().isManoInicial());
		manoFormJugador4.setMano(estadoPantallaEvento.getDerecha().getMano(), estadoPantallaEvento.getDerecha().isDescartar(), 
				estadoPantallaEvento.getDerecha().getNombre(), estadoPantallaEvento.getDerecha().isManoHabla(), estadoPantallaEvento.getDerecha().isManoInicial());
		
		mus.setEnabled(estadoPantallaEvento.isMus());
		noHayMus.setEnabled(estadoPantallaEvento.isNoHayMus());
		descartar.setEnabled(estadoPantallaEvento.isDescartar());
		aceptar.setEnabled(estadoPantallaEvento.isAceptar());
		pasar.setEnabled(estadoPantallaEvento.isPasar());
		ordago.setEnabled(estadoPantallaEvento.isApostar());
		envido.setEnabled(estadoPantallaEvento.isApostar());
		
		siguienteRonda.setEnabled(estadoPantallaEvento.isManoInicial() 
						&& partida.getRondaActual().getSuerteActual() == Ronda.SUERTE_ACABADO	
						&& !partida.getRondaActual().getJuego().isTerminado() );
			
		siguienteJuego.setEnabled(estadoPantallaEvento.isManoInicial() 
						&& partida.getRondaActual().getJuego().isTerminado()
						&& partida.haySiguienteJuego()	);


		gridApuesta.setItems(getListaApuestas(estadoPantallaEvento));
		grid.setItems(partida.getListaJuegos());
	}
	
	@Override
	public String getNombreJugadorActual() {
		return myUI.getNombreJugadorActual();
	}

	@Override
	public void onPartidaEvent(EstadoPantallaEvento estadoPantallaEvento) {
		this.estadoPantallaEvento = estadoPantallaEvento;
		carga(estadoPantallaEvento);
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	
	private List<Apuesta> getListaApuestas(EstadoPantallaEvento estadoPantallaEvento) {
		List<Apuesta> listaApuestas = new ArrayList<>();
		
		listaApuestas.add(new Apuesta("Mayor", estadoPantallaEvento.getApuestaMayor()));
		listaApuestas.add(new Apuesta("Menor", estadoPantallaEvento.getApuestaMenor()));
		listaApuestas.add(new Apuesta("Pares", estadoPantallaEvento.getApuestaPares()));
		listaApuestas.add(new Apuesta("Juego", estadoPantallaEvento.getApuestaJuego()));
		listaApuestas.add(new Apuesta("Actual", estadoPantallaEvento.getApuestaActual()));
		
		return listaApuestas;
	}
	
	public class Apuesta{
		private String apuesta;
		private int cantidad;
		
		public Apuesta() {
			
		}
		public Apuesta(String apuesta, int cantida) {
			this.apuesta = apuesta;
			this.cantidad = cantida;
		}
		
		public String getApuesta() {
			return apuesta;
		}
		public void setApuesta(String apuesta) {
			this.apuesta = apuesta;
		}
		public int getCantidad() {
			return cantidad;
		}
		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
	}
	
}
