package es.cic.taller.mus.juego;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class StreamTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		List<String> listaCadenas = new ArrayList<>();
		
		listaCadenas.add("fsasadffsd");
		listaCadenas.add("hola");
		listaCadenas.add("adios");
		listaCadenas.add("otadsassad");
		listaCadenas.add("reterx");
		listaCadenas.add("sdfretewer");
		listaCadenas.add("dght");
		
		
		List<String> cadenasFiltradas = listaCadenas.stream()
											.filter(s -> s.length() > 5)
											.collect(Collectors.toList());
		assertEquals(4, cadenasFiltradas.size());
	}

}
