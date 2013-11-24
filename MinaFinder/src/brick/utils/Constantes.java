package brick.utils;

import lejos.robotics.navigation.Pose;
import lejos.util.Matrix;

public class Constantes {

	public static final float MODOBS_DIV = 0;
	public static final long DURACION_TEST = 10*60*1000; // 10 minutos
	public static boolean logginRecorrida = true;
	public static String ipVision = "192.168.32.29";
	public static int puertoVision = 6363;
	public static boolean debug = false;
	public static int semilla = 12345;
	public static int cantMarcas = 6;  //cantidad de marcas en el entorno
	public static float covarMeasure = 4000;			//error en la lectura
	public static MatrixFloat covMesureMatrix = new MatrixCovar(covarMeasure);    //matriz de covarianza se inicializa en el constructor
	public static float distanceNoiseFactor = .05f;
	public static float angleNoiseFactor = 0.1f;
	
	
	//particulas
	public static int NUMPART = 500;
	public static int LADO_CUBITO = 50;
	public static float MIN_VAR_PESOS = 1/NUMPART * .95f;
	public static boolean debugPc = true;
	public static final boolean slam = true;
	public static final int PRECISION_GRILLA = 5;
	public static final int ANCHOCOBERTURA = 200;
	public static boolean online = true;
	public static boolean envioParts = true;
	public static Pose initialPose = new Pose(600,300 , 0);
	
	public static void configure(int numPart, float distanceNoiseFactor, float angleNoiseFactor,
			float covarMeasure){
		Constantes.configure(numPart, 1/numPart * .95f, distanceNoiseFactor, angleNoiseFactor, covarMeasure);
	}
	
	public static void configure(int numPart, float minVarPesos, float distanceNoiseFactor, float angleNoiseFactor,
			float covarMeasure){
		Constantes.covarMeasure = covarMeasure;
		Constantes.covMesureMatrix = new MatrixCovar(covarMeasure);
		Constantes.NUMPART = numPart;
		Constantes.MIN_VAR_PESOS = minVarPesos;
		Constantes.distanceNoiseFactor = distanceNoiseFactor;
		Constantes.angleNoiseFactor = angleNoiseFactor;
	}
	
}
