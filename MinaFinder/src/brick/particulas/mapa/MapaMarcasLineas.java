package brick.particulas.mapa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.robotics.mapping.LineMap;

public class MapaMarcasLineas extends LineMap {

	public static final int MAX_MARCAS = 6;
	
	private Marca[] marcas;

	public MapaMarcasLineas(Marca[] m, Line[] lines, Rectangle rect){
		super(lines, rect);
		
		this.marcas = new Marca[MAX_MARCAS];
		for(int i = 0; i < MAX_MARCAS; i++)
			marcas[i] = m[i];
			
	}

	public MapaMarcasLineas(Line[] lineas, Rectangle rect) {
		super(lineas, rect);
		this.marcas = new Marca[MAX_MARCAS];
		for(int i = 0; i < MAX_MARCAS; i++)
			marcas[i] = null;
	}

	public Marca getMarca(int id){
		return marcas[id-1];
	}
	
	public boolean isMarca(int id){
		return marcas[id-1] != null;
	}
	
	public void addMarca(int id, Marca marca) {
		marcas[id-1] = marca;
	}

	public Marca[] getMarcas() {
		return marcas;
	}
}
