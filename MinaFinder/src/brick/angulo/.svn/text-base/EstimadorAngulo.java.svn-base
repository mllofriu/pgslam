package brick.angulo;

import java.util.Properties;
import java.util.StringTokenizer;

import brick.distancia.MatrixFunctions;
import brick.marcas.ObservMarca;

/***
 * Utiliza el polinomio estimado para calcular estimado de la distancia a una marca.
 * Establece los criterios generales de calidad de una marca sensada para su utilizacion
 * para estimar la distancia.
 * @author ludo
 *
 */
public class EstimadorAngulo {

	private double [] coefsAng;
	MatrixFunctions mfunct;

	public EstimadorAngulo(Properties prop){
		// Inicializo estimacion distancia
		String coefsStr = prop.getProperty("coefsAng");
		StringTokenizer st = new StringTokenizer(coefsStr, ",");
		coefsAng = new double[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens()){
			coefsAng[i] = Double.parseDouble(st.nextToken());
			i++;
		}
		
		mfunct = new MatrixFunctions();
	}
	
	public double estimar(ObservMarca m){
		int x = m.getCenter().x;
		return mfunct.fx(x, coefsAng);
	}
	
	public static boolean aceptable(double x, double ang, float prob) {
		return prob > -40;
	}

	public boolean aceptable(ObservMarca m) {
		return aceptable(m.getMedianTam(), m.getDistancia(), m.getProb());
	}
	
}
