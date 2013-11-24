package brick.distancia;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Sound;

import brick.marcas.ObservMarca;

/***
 * Estima un polinomio que interpola los datos registrados. Se utiliza para estimar
 * un polinomio de interpolacion de la distancia en funcion del tamanio de los BLOBs.
 * @author ludo
 *
 */
public class EstPolDist {
	
	public static final int GRADO_POLY = 3;
	List<Pair> datos;
	
	public EstPolDist() {
		datos = new ArrayList<Pair>();
	}
	
	public void agregarDato(ObservMarca m){
//		int tam = m.getMedianTam();
		int lowerPixel = (int) m.getLowerPixel();
		double dist = m.getDistancia();
		if(EstimadorDistancia.aceptable(lowerPixel, dist, m.getProb())){
			datos.add(new Pair((double) lowerPixel, dist));
//			Sound.beepSequenceUp();
		}
	}
	
	public int getCantDatos(){
		return datos.size();
	}

	

	public double[] calcPoly() {
		MatrixFunctions mfunct = new MatrixFunctions();
		return mfunct.polyregress(datos.toArray(new Pair[datos.size()]), GRADO_POLY);
	}
}
