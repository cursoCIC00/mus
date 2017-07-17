package es.cic.taller.mus.vista;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Binder;
import com.vaadin.server.PaintTarget;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.cic.taller.mus.juego.Baraja;
import es.cic.taller.mus.juego.Equipo;
import es.cic.taller.mus.juego.TipoEventoPartida;
import es.cic.taller.mus.juego.Jugador;
import es.cic.taller.mus.juego.Mano;
import es.cic.taller.mus.juego.Partida;
import es.cic.taller.mus.juego.PartidaListener;


/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Push
public class MyUI extends UI implements PartidaListener {

	private VerticalLayout layout = new VerticalLayout();
	
	private Button botonComenzar = new Button("Comenzar");
	
	private String nombre;
	
	private Label labelNombre = new Label();
	
	private TabSheet sample;
	
	private List<Partida> listadoPartidas = new ArrayList<>();
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
    	nombre = vaadinRequest.getParameter("nombre");
    	labelNombre.setValue(nombre);
   	

        Jugador jugador = new Jugador("jugador1");
        jugador.setNombre(nombre);


    	
    	sample = new TabSheet();
        sample.setHeight(100.0f, Unit.PERCENTAGE);
        sample.addStyleName(ValoTheme.TABSHEET_FRAMED);
        sample.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);



        botonComenzar.addClickListener(e -> comenzarPartida());
        
        
        layout.addComponents(labelNombre, sample, botonComenzar);
        
        setContent(layout);
        
        inicializaTabsPartidas();
    	Partida.getNotificadorGeneral().addPartidaListener(this);
    }

    private void inicializaTabsPartidas() {
    	listadoPartidas.addAll(Partida.getListadoPartidas());
    	for (Partida partida: listadoPartidas) {
	        PantallaLayout pantallaLayout = new PantallaLayout(this, partida);
	        sample.addTab(pantallaLayout, partida.getNombrePartida());
    	} 	
    	
    }
    
    private void comenzarPartida() {
		Equipo equipo1 = new Equipo(1, new Jugador("jugador1"), new Jugador("jugador3"));
		Equipo equipo2 = new Equipo(2, new Jugador("jugador2"), new Jugador("jugador4"));
    	Partida partida = new Partida(equipo1, equipo2);
    	
    	EstadoPantallaEvento aux = new EstadoPantallaEvento(TipoEventoPartida.PARTIDA_INICIADA, partida, null, null, null, null, 0, 0, 0, 0, 0, false, false, false, false, false, false, 0, false, null, null, null, false, false, false, false, null);
    	
    	Partida.getNotificadorGeneral().firePartidaEvento(aux);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String getNombreJugadorActual() {
		return getNombre();
	}

	@Override
	public void onPartidaEvent(EstadoPantallaEvento estadoPantallaEvento) {
		if (estadoPantallaEvento.getTipoEventoPartida() == TipoEventoPartida.PARTIDA_INICIADA &&
			!listadoPartidas.contains(estadoPantallaEvento.getPartida())) {
			listadoPartidas.add(estadoPantallaEvento.getPartida());
	        PantallaLayout pantallaLayout = new PantallaLayout(this, estadoPantallaEvento.getPartida());
	        sample.addTab(pantallaLayout, estadoPantallaEvento.getPartida().getNombrePartida());
		}
	} 
}
