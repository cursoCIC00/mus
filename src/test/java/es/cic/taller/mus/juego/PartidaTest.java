package es.cic.taller.mus.juego;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PartidaTest {

	private Partida sut;
	
	private Equipo equipo1;
	private Equipo equipo2;
	
	private Ronda ronda;
	private Mano manoInicial;

	@Before
	public void setUp() throws Exception {
		equipo1 = new Equipo(1, new Jugador("jugador1"), new Jugador("jugador3"));
		equipo2 = new Equipo(2, new Jugador("jugador2"), new Jugador("jugador4"));
		
		sut = new Partida(equipo1, equipo2);
		
		ronda = sut.getRondaActual();
		
		manoInicial = ronda.getManoInicial();
	}

	@Test
	public void testSuerteMus() {
		List<Mano> manosPasadas = new ArrayList<>();

		Mano manoHabla = ronda.getManoHabla();
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);
		
		assertNotNull(manoInicial);
		assertEquals(manoInicial, manoHabla);
		
		verificaAccionesSuerteMus(ronda, manoHabla, manosPasadas);
		
		manoHabla = mus(ronda, manoHabla);

		verificaAccionesSuerteMus(ronda, manoHabla, manosPasadas);

		manoHabla = mus(ronda, manoHabla);
		
		verificaAccionesSuerteMus(ronda, manoHabla, manosPasadas);

		manoHabla = mus(ronda, manoHabla);
		
		verificaAccionesSuerteMus(ronda, manoHabla, manosPasadas);

		manoHabla = mus(ronda, manoHabla);

		assertEquals(manoInicial, manoHabla);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_DESCARTE);		
	}

	
	@Test
	public void testSuerteNoHayMus() {
		Mano manoHabla = llevarSinMusAMayor(manoInicial);
	}
	
	@Test
	public void testSuerteDescarte() {
		Mano manoHabla = ronda.getManoHabla();
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);
		
		assertNotNull(manoInicial);
		assertEquals(manoInicial, manoHabla);
		
		
		manoHabla = mus(ronda, manoHabla);
		manoHabla = mus(ronda, manoHabla);
		manoHabla = mus(ronda, manoHabla);
		manoHabla = mus(ronda, manoHabla);

		assertEquals(manoInicial, manoHabla);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_DESCARTE);		

		List<Mano> manosPasadas = new ArrayList<>();
		
		verificaAccionesSuerteDescarte(ronda, manoHabla, manosPasadas);
		
		manoHabla.estableceDescartadas(new ArrayList<>());
		manoHabla = descartar(ronda, manoHabla);
		
		verificaAccionesSuerteDescarte(ronda, manoHabla, manosPasadas);
		
		manoHabla.estableceDescartadas(new ArrayList<>());
		manoHabla = descartar(ronda, manoHabla);
		
		verificaAccionesSuerteDescarte(ronda, manoHabla, manosPasadas);
		
		manoHabla.estableceDescartadas(new ArrayList<>());
		manoHabla = descartar(ronda, manoHabla);
		
		verificaAccionesSuerteDescarte(ronda, manoHabla, manosPasadas);
		
		manoHabla.estableceDescartadas(new ArrayList<>());
		manoHabla = descartar(ronda, manoHabla);
		
		assertEquals(manoInicial, manoHabla);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);	
		
	}

	@Test
	public void testSuerteDescarteTiroCartas() {
		Mano manoHabla = ronda.getManoHabla();
		
		manoHabla = mus(ronda, manoHabla);
		manoHabla = mus(ronda, manoHabla);
		manoHabla = mus(ronda, manoHabla);
		manoHabla = mus(ronda, manoHabla);

		final Mano manoAverificar = manoHabla;
		List<Carta> cartasDescartadasJugador1 = new ArrayList<>();
		cartasDescartadasJugador1.add(manoHabla.getCarta2());
		cartasDescartadasJugador1.add(manoHabla.getCarta3());
		List<Carta> cartasConservadasJugador1 = new ArrayList<>();
		cartasDescartadasJugador1.add(manoHabla.getCarta1());
		cartasDescartadasJugador1.add(manoHabla.getCarta4());
		
		manoHabla.estableceDescartadas(cartasDescartadasJugador1);
		manoHabla = descartar(ronda, manoHabla);
		
		
		manoHabla.estableceDescartadas(new ArrayList<>());
		manoHabla = descartar(ronda, manoHabla);
		
		manoHabla.estableceDescartadas(new ArrayList<>());
		manoHabla = descartar(ronda, manoHabla);
		
		manoHabla.estableceDescartadas(new ArrayList<>());
		manoHabla = descartar(ronda, manoHabla);
		
		assertEquals(manoInicial, manoHabla);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);	
		

		assertEquals(4, manoHabla.getListaCartas().size());
		assertEquals(0, manoHabla.getListaDescartadas().size());
		assertTrue(manoHabla.getListaCartas().containsAll(cartasConservadasJugador1));
		assertEquals(0, 
				cartasDescartadasJugador1.stream()
				.filter(c -> manoAverificar.getListaCartas().contains(c))
				.count());
	}
	
	@Test
	public void testSuerteMayorEnPaso() {
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);
		ronda.noHayMus(manoInicial);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MAYOR);

		Mano manoHabla = ronda.getManoHabla();
		assertEquals(manoInicial, manoHabla);
		
		manoHabla = suerteEnPaso(ronda);
		
		assertEquals(manoInicial, manoHabla);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MENOR);	
		assertEquals(0, ronda.getApuestaMayor());
		assertNull(ronda.getEquipoGanaMayorEnPaso());
	}

	@Test
	public void testSuerteMenorEnPaso() {
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);
		ronda.noHayMus(manoInicial);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MAYOR);

		Mano manoHabla = suerteEnPaso(ronda);
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MENOR);
		
		manoHabla = ronda.getManoHabla();
		assertEquals(manoInicial, manoHabla);

		manoHabla = suerteEnPaso(ronda);
		
		if (ronda.hayPares()) {
			assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_PARES);	
			assertEquals(0, ronda.getApuestaMenor());
		} else {
			if (ronda.unSoloEquipoTieneJuego()) {
				assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_ACABADO);
				assertEquals(1, ronda.getApuestaMenor());
			} else {
				assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_JUEGO);
				assertEquals(0, ronda.getApuestaMenor());
			}
		}
		
		assertNull(ronda.getEquipoGanaMenorEnPaso());
	}
	
	@Test
	public void testSuerteParesEnPaso() {
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);
		ronda.noHayMus(manoInicial);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MAYOR);

		Mano manoHabla = suerteEnPaso(ronda);
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MENOR);
		
		manoHabla = ronda.getManoHabla();
		assertEquals(manoInicial, manoHabla);

		manoHabla = suerteEnPaso(ronda);
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_PARES 
				|| ronda.getSuerteActual() == Ronda.SUERTE_JUEGO 
				|| ronda.getSuerteActual() == Ronda.SUERTE_ACABADO);
		
		if (ronda.getSuerteActual() == Ronda.SUERTE_PARES && ronda.hayPares()) {
			for (Mano mano: ronda.getListaMano()) {
				if (mano.hasPares()) {
					ronda.pasar();
				}				
			}
		}

		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_JUEGO && !ronda.unSoloEquipoTieneJuego()
				|| ronda.getSuerteActual() == Ronda.SUERTE_ACABADO);

		
		if (ronda.hayPares() && ronda.getSuerteActual() == Ronda.SUERTE_ACABADO) {			
			assertEquals(1, ronda.getApuestaPares());
		} else {
			assertEquals(0, ronda.getApuestaPares());
		}
	}
	
	@Test
	public void testSuerteJuegoEnPaso() {
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);
		ronda.noHayMus(manoInicial);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MAYOR);

		Mano manoHabla = suerteEnPaso(ronda);
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MENOR);
		
		manoHabla = ronda.getManoHabla();
		assertEquals(manoInicial, manoHabla);

		manoHabla = suerteEnPaso(ronda);
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_PARES 
				|| ronda.getSuerteActual() == Ronda.SUERTE_JUEGO 
				|| ronda.getSuerteActual() == Ronda.SUERTE_ACABADO);
		
		if (ronda.getSuerteActual() == Ronda.SUERTE_PARES && ronda.hayPares()) {
			for (Mano mano: ronda.getListaMano()) {
				if (mano.hasPares()) {
					ronda.pasar();
				}				
			}
		}

		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_JUEGO && !ronda.unSoloEquipoTieneJuego()
				|| ronda.getSuerteActual() == Ronda.SUERTE_ACABADO);

		
		if (ronda.getSuerteActual() == Ronda.SUERTE_JUEGO && !ronda.unSoloEquipoTieneJuego()) {
			if (ronda.hayJuego()) {
				for (Mano mano: ronda.getListaMano()) {
					if (mano.hasJuego()) {
						ronda.pasar();
					}				
				}
			} else {
				manoHabla = suerteEnPaso(ronda);
			}			
		}
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_ACABADO);

		assertTrue(ronda.getJuego().getPuntuacionEquipo1() + ronda.getJuego().getPuntuacionEquipo2() > 0);
		assertEquals(1, ronda.getApuestaJuego());
	}

	@Test
	public void testMayorOrdagoNoquiero() {
		Mano manoHabla = llevarSinMusAMayor(manoInicial);
		
		manoHabla = lanzaOrdago(ronda, manoHabla);
		
		verificaAccionesSuerteMayorOrdago(ronda, manoHabla);
		
		assertTrue(ronda.isOrdago());
		
		manoHabla = pasar(ronda);
		verificaAccionesSuerteMayorOrdago(ronda, manoHabla);

		manoHabla = pasar(ronda);
		
		assertEquals(manoInicial, manoHabla);
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MENOR);
	}

	@Test
	public void testMayorOrdagoQuiero() {
		Mano manoHabla = llevarSinMusAMayor(manoInicial);
		
		manoHabla = lanzaOrdago(ronda, manoHabla);
		
		verificaAccionesSuerteMayorOrdago(ronda, manoHabla);
		
		assertTrue(ronda.isOrdago());
		
		ronda.aceptar(manoHabla);

		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_ACABADO);
		
		assertTrue(ronda.getJuego().getPuntuacionEquipo1() == Juego.PUNTUACION_MAXIMA 
				|| ronda.getJuego().getPuntuacionEquipo2() == Juego.PUNTUACION_MAXIMA);
	}
	
	@Test
	public void testSuerteMayorEnvidoPasoPaso() {
		Mano manoHabla = llevarSinMusAMayor(manoInicial);
		
		manoHabla = envidar(manoHabla);
		
		assertEquals(Ronda.SUERTE_MAYOR, ronda.getSuerteActual());
		assertNotEquals(manoInicial, manoHabla);

		assertEquals(0, ronda.getApuestaMayor());
		assertEquals(2, ronda.getApuestaActual());
		verificaAccionesEnvido(ronda, manoHabla);		
		
		manoHabla = pasar(ronda);
		
		
		assertEquals(Ronda.SUERTE_MAYOR, ronda.getSuerteActual());
		assertEquals(0, ronda.getApuestaMayor());
		assertEquals(2, ronda.getApuestaActual());
		verificaAccionesEnvido(ronda, manoHabla);
		
		manoHabla = pasar(ronda);
		
		assertEquals(Ronda.SUERTE_MENOR, ronda.getSuerteActual());
		
		assertEquals(0, ronda.getApuestaMayor());
		assertEquals(0, ronda.getApuestaActual());
		
		assertEquals(manoInicial.getJugador().getEquipo(), ronda.getEquipoGanaMayorEnPaso());
	}

	@Test
	public void testSuerteMayorEnvidoPasoAceptar() {
		Mano manoHabla = llevarSinMusAMayor(manoInicial);
		
		manoHabla = envidar(manoHabla);
		
		assertEquals(Ronda.SUERTE_MAYOR, ronda.getSuerteActual());
		assertNotEquals(manoInicial, manoHabla);

		assertEquals(0, ronda.getApuestaMayor());
		assertEquals(2, ronda.getApuestaActual());
		verificaAccionesEnvido(ronda, manoHabla);		
		
		manoHabla = pasar(ronda);
		
		
		assertEquals(Ronda.SUERTE_MAYOR, ronda.getSuerteActual());
		assertEquals(0, ronda.getApuestaMayor());
		assertEquals(2, ronda.getApuestaActual());
		verificaAccionesEnvido(ronda, manoHabla);
		
		ronda.aceptar(manoHabla);
		
		assertEquals(Ronda.SUERTE_MENOR, ronda.getSuerteActual());
		
		assertEquals(2, ronda.getApuestaMayor());
		assertEquals(0, ronda.getApuestaActual());
		
		assertNull(ronda.getEquipoGanaMayorEnPaso());
	}
	
	private Mano envidar(Mano manoHabla) {
		ronda.envidar(manoHabla);
		manoHabla = ronda.getManoHabla();
		return manoHabla;
	}

	private Mano llevarSinMusAMayor(Mano manoInicial) {
		Mano manoHabla = ronda.getManoHabla();
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MUS);
		
		manoHabla = mus(ronda, manoHabla);
		manoHabla = noHayMus(ronda, manoHabla);

		assertEquals(manoInicial, manoHabla);
		
		assertTrue(ronda.getSuerteActual() == Ronda.SUERTE_MAYOR);
		return manoHabla;
	}
	
	private Mano lanzaOrdago(Ronda ronda, Mano manoHabla) {
		ronda.ordago(manoHabla);
		manoHabla = ronda.getManoHabla();
		return manoHabla;
	}	
	
	
	private Mano suerteEnPaso(Ronda ronda) {
		Mano manoHabla;
		manoHabla = pasar(ronda);
		manoHabla = pasar(ronda);
		manoHabla = pasar(ronda);
		manoHabla = pasar(ronda);
		return manoHabla;
	}

	private Mano pasar(Ronda ronda) {
		ronda.pasar();
		return ronda.getManoHabla();
	}
	
	
	private Mano descartar(Ronda ronda, Mano manoHabla) {
		ronda.descartar(manoHabla);
		return ronda.getManoHabla();
	}

	private Mano noHayMus(Ronda ronda, Mano manoHabla) {
		ronda.noHayMus(manoHabla);
		manoHabla = ronda.getManoHabla();
		return manoHabla;
	}

	private Mano mus(Ronda ronda, Mano manoHabla) {
		ronda.mus(manoHabla);
		manoHabla = ronda.getManoHabla();
		return manoHabla;
	}
	
	
	private void verificaAccionesSuerteMus(Ronda ronda, Mano manoHabla, List<Mano> manosPasadas) {
		assertFalse(manosPasadas.contains(manoHabla));
		manosPasadas.add(manoHabla);		

		verificaAccionesSuerteMus(ronda, manoHabla);
	}

	private void verificaAccionesSuerteMus(Ronda ronda, Mano manoHabla) {
		verificacionesAcciones(ronda, manoHabla, false, false, false, false, false, true, true);
	}

	private void verificaAccionesSuerteDescarte(Ronda ronda, Mano manoHabla, List<Mano> manosPasadas) {
		assertFalse(manosPasadas.contains(manoHabla));
		manosPasadas.add(manoHabla);		

		verificaAccionesSuerteDescarte(ronda, manoHabla);
	}
	
	private void verificaAccionesSuerteDescarte(Ronda ronda, Mano manoHabla) {		
		verificacionesAcciones(ronda, manoHabla, false, false, true, false, false, false, false);
	}
	
	private void verificaAccionesSuerteMayor(Ronda ronda, Mano manoHabla, List<Mano> manosPasadas, boolean hayApuesta) {
		assertFalse(manosPasadas.contains(manoHabla));
		manosPasadas.add(manoHabla);		

		verificaAccionesSuerteMayor(ronda, manoHabla, hayApuesta);
	}
	
	public void verificaAccionesSuerteMayorOrdago(Ronda ronda, Mano manoHabla) {
		verificacionesAcciones(ronda, manoHabla, true, false, false, false, true, false, false);
	}
	
	public void verificaAccionesEnvido(Ronda ronda, Mano manoHabla) {
		verificacionesAcciones(ronda, manoHabla, true, true, false, true, true, false, false);
	}
	
	private void verificaAccionesSuerteMayor(Ronda ronda, Mano manoHabla, boolean hayApuesta) {		
		verificacionesAcciones(ronda, manoHabla, hayApuesta, true, false, true, true, false, false);
	}
	
	private void verificacionesAcciones(Ronda ronda, Mano manoHabla, 
			boolean aceptar, boolean apostar, boolean descartar, boolean envidar, boolean pasar, boolean mus, boolean noHayMus) {
		assertEquals(aceptar, ronda.puedeAceptar(manoHabla));
		assertEquals(apostar, ronda.puedeApostar(manoHabla));
		assertEquals(descartar, ronda.puedeDescartar(manoHabla));
		
		assertEquals(envidar, ronda.puedeEnvidar(manoHabla));
		assertEquals(pasar, ronda.puedePasar(manoHabla));
		
		assertEquals(mus, ronda.puedeMus(manoHabla));
		assertEquals(noHayMus, ronda.puedeNoHayMus(manoHabla));
		
	}
}
