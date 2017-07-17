package es.cic.taller.mus.vista;

import java.util.List;

import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.cic.taller.mus.juego.Baraja;
import es.cic.taller.mus.juego.Carta;
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
	
	private Label labelApuestaMayor = new Label();
	private Label labelApuestaMenor = new Label();
	private Label labelApuestaPares = new Label();
	private Label labelApuestaJuego = new Label();

	private Label labelApuestaActual = new Label();
	
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
		apuestas.addComponents(labelApuestaMayor, labelApuestaMenor, labelApuestaPares, labelApuestaJuego, labelApuestaActual);
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
		manoFormJugador1.setMano(estadoPantallaEvento.getMano(), estadoPantallaEvento.isDescartar());
		manoFormJugador2.setMano(estadoPantallaEvento.getEnfrente().getMano(), estadoPantallaEvento.isDescartar());
		manoFormJugador3.setMano(estadoPantallaEvento.getIzquierda().getMano(), estadoPantallaEvento.isDescartar());
		manoFormJugador4.setMano(estadoPantallaEvento.getDerecha().getMano(), estadoPantallaEvento.isDescartar());
		
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
		
		labelApuestaMayor.setValue("Mayor: " + estadoPantallaEvento.getApuestaMayor());
		labelApuestaMenor.setValue("Menor: " + estadoPantallaEvento.getApuestaMenor());
		labelApuestaPares.setValue("Pares: " + estadoPantallaEvento.getApuestaPares());
		labelApuestaJuego.setValue("Juego: " + estadoPantallaEvento.getApuestaJuego());
		labelApuestaActual.setValue("Apuesta: " + estadoPantallaEvento.getApuestaActual());

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
	
	
	
}
