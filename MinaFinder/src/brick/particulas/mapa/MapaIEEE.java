package brick.particulas.mapa;
import java.util.ArrayList;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.robotics.mapping.LineMap;

/**
 * Clase que implementa el mapa del entorno utilizado en la competencia.
 * @author ludo
 *
 */
public class MapaIEEE {
	private final static float TRUNK_SIDE = 300.0f;
	private final static float TAPE_SIDE = 20.0f;
	private final static float CULTIVO_ZONE_LENGTH = 773.0f;
	private final static float CULTIVO_ZONE_WIDTH = 260.0f;
	private final static float BENEFICIADERO_ZONE_LENGTH = 456.0f;
	private final static float BENEFICIADERO_ZONE_WIDTH = 460.0f;
	//private final static float ALBERCA_ZONE_LENGTH = 450.0f;
	private final static float ALBERCA_ZONE_WIDTH = 200.0f;
	private final static float ARENA_ZONE_LENGTH = 2400.0f;
	private final static float ARENA_ZONE_WIDTH = 2000.0f;
	private static final float ALBERCA_FREE_SPACE = 500;

	private final static float X0 = 0.0f;
	private final static float X1 = 0f;
	private final static float X2 = 500;
	private final static float X3 = 740;
	private final static float X4 = 1200;
	private final static float X5 = ARENA_ZONE_WIDTH;
	private final static float Y0 = 0.0f;
	private final static float Y1 = BENEFICIADERO_ZONE_LENGTH+TAPE_SIDE;
	private final static float Y2 = CULTIVO_ZONE_LENGTH+TAPE_SIDE*3/2;
	private final static float Y3 = BENEFICIADERO_ZONE_LENGTH*2+TAPE_SIDE*3;
	private final static float Y4 = BENEFICIADERO_ZONE_LENGTH*3+TAPE_SIDE*3;
	private final static float Y5 = CULTIVO_ZONE_LENGTH*3+TAPE_SIDE*7/2;
	private final static float Y6 = BENEFICIADERO_ZONE_LENGTH*4+TAPE_SIDE*5;
	private final static float Y7 = ARENA_ZONE_LENGTH;
	private final static float Y8 = CULTIVO_ZONE_LENGTH*2+TAPE_SIDE*5/2;

	/**
	 * 
	 * @return Un mapa de lineas correspondiente al entorno de trabajo de la competencia.
	 */
	public static LineMap getIEEEMap()
	{

		//LineMap map = new LineMap(larray, new Rectangle(0.0f,0.0f,2000.0f,2400.0f));
		LineMap map = new LineMap(getLineasIEEE(), getRectangleIEEE());
		return map;
	}
	
	public static Rectangle getRectangleIEEE() {
		return new Rectangle(X0, Y0, X5, Y7);
	}



	public static Line[] getLineasIEEE() {
		//ArrayList<Line> lines = new ArrayList<Line>(12);
		ArrayList<Line> lines = new ArrayList<Line>(20);
		// Lineas exteriores
		//lines.add(new Line(0.0f, 0.0f, 0.0f, 2400.0f));
		//lines.add(new Line(2000.0f, 0.0f, 2000.0f, 2400.0f));
		//lines.add(new Line(0.0f, 0.0f, 2000.0f, 0.0f));
		//lines.add(new Line(0.0f, 2400.0f, 2000.0f, 2400.0f));
		//				lines.add(new Line(X0, Y0, X5, Y0));
		//				lines.add(new Line(X5, Y0, X5, Y5));
		//				lines.add(new Line(X5, Y5, X0, Y5));
		//				lines.add(new Line(X0, Y5, X0, Y0));

		lines.add(new Line(X1, Y0, X4, Y0));
		lines.add(new Line(X1, Y7, X4, Y7));

		// Lineas verticales interiores
		//lines.add(new Line(300.0f, 0.0f, 300.0f, 2400.0f));
		//lines.add(new Line(1500.0f, 0.0f, 1500.0f, 2400.0f));
		lines.add(new Line(X1, Y0, X1, Y5));
		lines.add(new Line(X4, Y0, X4, Y5));

		// Zona de cosecha
		//lines.add(new Line(0.0f, 813.0f, 300.0f, 813.0f));
		//lines.add(new Line(0.0f, 1626.0f, 300.0f, 1626.0f));
		//				lines.add(new Line(X0, Y2, X1, Y2));
		//				lines.add(new Line(X0, Y8, X1, Y8));

		// Zona de beneficiadero
		//for(int i=1; i <= 4; i++)
		//lines.add(new Line(1500.0f, i*496.0f, 2000.0f, i*496.0f));
		//				lines.add(new Line(X4, Y1, X5, Y1));
		//				lines.add(new Line(X4, Y3, X5, Y3));
		//				lines.add(new Line(X4, Y4, X5, Y4));
		//				lines.add(new Line(X4, Y6, X5, Y6));
		//				
		// Lineas agua 1
		// Largo 450 ancho 200
		//lines.add(new Line(800.0f, 500.0f, 1000.0f, 500.0f));
		//lines.add(new Line(800.0f, 500.0f, 800.0f, 950.0f));
		//lines.add(new Line(1000.0f, 500.0f, 1000.0f, 950.0f));
		//lines.add(new Line(800.0f, 950.0f, 1000.0f, 950.0f));
		lines.add(new Line(X2, Y1, X3, Y1));
		lines.add(new Line(X3, Y1, X3, Y3));
		lines.add(new Line(X3, Y3, X2, Y3));
		lines.add(new Line(X2, Y3, X2, Y1));

		// Lineas agua 2
		// Largo 450 ancho 200
		//lines.add(new Line(800.0f, 1450.0f, 1000.0f, 1450.0f));
		//lines.add(new Line(800.0f, 1450.0f, 800.0f, 1900.0f));
		//lines.add(new Line(1000.0f, 1450.0f, 1000.0f, 1900.0f));
		//lines.add(new Line(800.0f, 1900.0f, 1000.0f, 1900.0f));
		lines.add(new Line(X2, Y4, X3, Y4));
		lines.add(new Line(X3, Y4, X3, Y6));
		lines.add(new Line(X3, Y6, X2, Y6));
		lines.add(new Line(X2, Y6, X2, Y4));

		Line[] larray = new Line[lines.size()];
		return lines.toArray(larray);
	}
	
	public static Line[] getLineasCompletasIEEE() {
		//ArrayList<Line> lines = new ArrayList<Line>(12);
		ArrayList<Line> lines = new ArrayList<Line>(20);
		// Lineas exteriores
		lines.add(new Line(0.0f, 0.0f, 0.0f, 2400.0f));
		lines.add(new Line(2000.0f, 0.0f, 2000.0f, 2400.0f));
		lines.add(new Line(0.0f, 0.0f, 2000.0f, 0.0f));
		lines.add(new Line(0.0f, 2400.0f, 2000.0f, 2400.0f));
//		lines.add(new Line(X0, Y0, X5, Y0));
//		lines.add(new Line(X5, Y0, X5, Y5));
//		lines.add(new Line(X5, Y5, X0, Y5));
//		lines.add(new Line(X0, Y5, X0, Y0));

//		lines.add(new Line(X1, Y0, X4, Y0));
//		lines.add(new Line(X1, Y7, X4, Y7));

		// Lineas verticales interiores
		lines.add(new Line(300.0f, 0.0f, 300.0f, 2400.0f));
		lines.add(new Line(1500.0f, 0.0f, 1500.0f, 2400.0f));
//		lines.add(new Line(X1, Y0, X1, Y5));
//		lines.add(new Line(X4, Y0, X4, Y5));

		// Zona de cosecha
		lines.add(new Line(0.0f, 813.0f, 300.0f, 813.0f));
		lines.add(new Line(0.0f, 1626.0f, 300.0f, 1626.0f));
//		lines.add(new Line(X0, Y2, X1, Y2));
//		lines.add(new Line(X0, Y8, X1, Y8));

		// Zona de beneficiadero
		for(int i=1; i <= 4; i++)
			lines.add(new Line(1500.0f, i*496.0f, 2000.0f, i*496.0f));
//		lines.add(new Line(X4, Y1, X5, Y1));
//		lines.add(new Line(X4, Y3, X5, Y3));
//		lines.add(new Line(X4, Y4, X5, Y4));
//		lines.add(new Line(X4, Y6, X5, Y6));
		//				
		// Lineas agua 1
		// Largo 450 ancho 200
		lines.add(new Line(800.0f, 500.0f, 1000.0f, 500.0f));
		lines.add(new Line(800.0f, 500.0f, 800.0f, 950.0f));
		lines.add(new Line(1000.0f, 500.0f, 1000.0f, 950.0f));
		lines.add(new Line(800.0f, 950.0f, 1000.0f, 950.0f));
//		lines.add(new Line(X2, Y1, X3, Y1));
//		lines.add(new Line(X3, Y1, X3, Y3));
//		lines.add(new Line(X3, Y3, X2, Y3));
//		lines.add(new Line(X2, Y3, X2, Y1));

		// Lineas agua 2
		// Largo 450 ancho 200
		lines.add(new Line(800.0f, 1450.0f, 1000.0f, 1450.0f));
		lines.add(new Line(800.0f, 1450.0f, 800.0f, 1900.0f));
		lines.add(new Line(1000.0f, 1450.0f, 1000.0f, 1900.0f));
		lines.add(new Line(800.0f, 1900.0f, 1000.0f, 1900.0f));
//		lines.add(new Line(X2, Y4, X3, Y4));
//		lines.add(new Line(X3, Y4, X3, Y6));
//		lines.add(new Line(X3, Y6, X2, Y6));
//		lines.add(new Line(X2, Y6, X2, Y4));

		Line[] larray = new Line[lines.size()];
		return lines.toArray(larray);
	}

	public static LineMap getIEEEPartMap() {
		ArrayList<Line> lines = new ArrayList<Line>(12);

		// Arboles
		//for(int i = 0; i < 3; i++)
		//lines.add(new Line(150.0f, 813.0f*i+(813.0f-300.0f)/2.0f, 150.0f, (813.0f-300.0f)/2.0f+300.0f));
		lines.add(new Line(X1/2, TAPE_SIDE+(CULTIVO_ZONE_LENGTH-TRUNK_SIDE)/2, X1/2, TAPE_SIDE+(CULTIVO_ZONE_LENGTH+TRUNK_SIDE)/2));
		lines.add(new Line(X1/2, TAPE_SIDE*2+CULTIVO_ZONE_LENGTH+(CULTIVO_ZONE_LENGTH-TRUNK_SIDE)/2, X1/2, TAPE_SIDE*2+CULTIVO_ZONE_LENGTH+(CULTIVO_ZONE_LENGTH+TRUNK_SIDE)/2));
		lines.add(new Line(X1/2, TAPE_SIDE*3+CULTIVO_ZONE_LENGTH*2+(CULTIVO_ZONE_LENGTH-TRUNK_SIDE)/2, X1/2, TAPE_SIDE*3+CULTIVO_ZONE_LENGTH*2+(CULTIVO_ZONE_LENGTH+TRUNK_SIDE)/2));

		// Nada m�s
		// TODO: incluir informacion aprendida sobre ubicacion de 
		// los recipientes en el mapa
		Line[] array = new Line[3];
		lines.toArray(array);
		//LineMap map = new LineMap(array, new Rectangle(0.0f, 0.0f, 2000.0f, 2400.0f));
		LineMap map = new LineMap(array, new Rectangle(X0, Y0, X5, Y7));
		return map;
	}

	public static double getLargerSide() {
		//return 2400.0d;
		return Y7;
	}

	public static double getAncho() {
		return X5;
	}
}

