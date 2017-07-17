package es.cic.taller.mus.vista;

import java.io.File;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;

import es.cic.taller.mus.juego.Carta;
import es.cic.taller.mus.juego.Mano;


public class ManoForm extends FormLayout {
	private HorizontalLayout horizontalLayout = new HorizontalLayout();

	
	private Image imagen1 = new Image();
	private Image imagen2 = new Image();
	private Image imagen3 = new Image();
	private Image imagen4 = new Image();
	

	private Mano mano;
	
	private boolean puedeDescartar;
	
	public ManoForm() {
	
		horizontalLayout.addComponents(imagen1, imagen2, imagen3, imagen4);

		imagen1.addClickListener(new Seleccion());
		imagen2.addClickListener(new Seleccion());
		imagen3.addClickListener(new Seleccion());
		imagen4.addClickListener(new Seleccion());
		
		addComponents(horizontalLayout);
		this.setSizeFull();
	}
	
	private Resource getImageResource(String recurso) {
		String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath +
                "/images/" + recurso));
        return resource;
	}
	
	public void setMano(Mano mano, boolean puedeDescartar) {		
		this.mano = mano;
		this.puedeDescartar = puedeDescartar;
		
		cargaCarta(mano.getCarta1(), imagen1);
		cargaCarta(mano.getCarta2(), imagen2);
		cargaCarta(mano.getCarta3(), imagen3);
		cargaCarta(mano.getCarta4(), imagen4);		
		
		estableceDescartadaImagen(imagen1);
		estableceDescartadaImagen(imagen2);
		estableceDescartadaImagen(imagen3);
		estableceDescartadaImagen(imagen4);
	}

	private void cargaCarta(Carta carta, Image imagen) {
		imagen.setSource(getImageResource(carta.getNombreFichero()));
		imagen.setWidth("100px");
		imagen.setHeight("200px");
	}
	
	private void estableceDescartadaImagen(Component componente) {
		Carta carta = getCartaImagen(componente);
		
		boolean descartada = isCartaDescartada(carta);
		
		estableceSeleccionado(componente, descartada);
	}

	private boolean isCartaDescartada(Carta carta) {
		return mano.getListaDescartadas().contains(carta);
	}
	
	private Carta getCartaImagen(Component componente) {
		Carta carta = null;
		
		if (componente.equals(imagen1)) {
			carta = mano.getCarta1();
		} else if (componente.equals(imagen2)) {
			carta = mano.getCarta2();
		} else if (componente.equals(imagen3)) {
			carta = mano.getCarta3();
		} else if (componente.equals(imagen4)) {
			carta = mano.getCarta4();
		}
		return carta;
	}
	
	private void estableceSeleccionado(Component componente, boolean seleccionado) {
		if (!seleccionado) {
			componente.setWidth("100px");
			componente.setHeight("200px");
		} else {
			componente.setWidth("200px");
			componente.setHeight("400px");
		}
	}
	
	class Seleccion implements ClickListener {
		@Override
		public void click(ClickEvent event) {
			if (!puedeDescartar) {
				return;
			}
			Component componente = event.getComponent();
			Carta carta = getCartaImagen(componente);
			
			boolean descartada = isCartaDescartada(carta);
			boolean descartar = !descartada;
			
			if (descartar) {
				mano.getListaDescartadas().add(carta);
			} else {
				mano.getListaDescartadas().remove(carta);
			}
			
			estableceSeleccionado(componente, descartar);
		}

		
	}

	public Mano getMano() {
		return mano;
	}
}
