package brick.angulo;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Sound;
import brick.distancia.MatrixFunctions;
import brick.distancia.Pair;
import brick.marcas.ObservMarca;

/***
 * Estima un polinomio que interpola los datos registrados. Se utiliza para estimar
 * un polinomio de interpolacion de la distancia en funcion del tamanio de los BLOBs.
 * @author ludo
 *
 */
public class EstPolAng {

	public static final int GRADO_POLY = 3;
	List<Pair> datos;

	public EstPolAng() {
		datos = new ArrayList<Pair>();
	}

	public void agregarDato(ObservMarca m){
		double x = m.getCenter().getX();
		double ang = m.getAngulo();
		datos.add(new Pair(x, ang));
	}

	public int getCantDatos(){
		return datos.size();
	}



	public double[] calcPoly() {
		MatrixFunctions mfunct = new MatrixFunctions();
		return mfunct.polyregress(datos.toArray(new Pair[datos.size()]), GRADO_POLY);
	}
}
