package brick.particulas.mapa;


import lejos.geom.Point;
import lejos.util.Matrix;

public class IEEERecorridaMap extends MapaMarcasLineas {

	public IEEERecorridaMap(){
		// Lineas de IEEE
		super(MapaIEEE.getLineasCompletasIEEE(), MapaIEEE.getRectangleIEEE());
		
		// Marcas
		// Depositos 
		addMarca(6,new Marca(1900, 600));
		addMarca(4,new Marca(1900,1200));
		addMarca(2,new Marca(1900, 1800));
		// Cosecha
		addMarca(1,new Marca(100, 600));
		addMarca(3,new Marca(100, 1200));
		addMarca(5,new Marca(100, 1800));
		
		
	}

	
}
