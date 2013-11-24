package brick.distancia;

import java.util.Properties;
import java.util.StringTokenizer;

import brick.marcas.ObservMarca;

/***
 * Utiliza el polinomio estimado para calcular estimado de la distancia a una marca.
 * Establece los criterios generales de calidad de una marca sensada para su utilizacion
 * para estimar la distancia.
 * @author ludo
 *
 */
public class EstimadorDistancia {

	private double [] coefsDist;
	MatrixFunctions mfunct;

	public EstimadorDistancia(Properties prop){
		// Inicializo estimacion distancia
		String coefsStr = prop.getProperty("coefsDist");
		StringTokenizer st = new StringTokenizer(coefsStr, ",");
		coefsDist = new double[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens()){
			coefsDist[i] = Double.parseDouble(st.nextToken());
			i++;
		}
		
		mfunct = new MatrixFunctions();
	}
	
	public double estimar(ObservMarca m){
//		int tam = m.getMedianTam();
		double lowPixel = m.getLowerPixel();
		return mfunct.fx((int)lowPixel, coefsDist);
	}
	
	public static boolean aceptable(int lowerPixel, double dist, float prob) {
		return /*dist > 700 && dist < 1200 && */prob > -25;
	}

	public boolean aceptable(ObservMarca m) {
		return aceptable((int) m.getLowerPixel(), estimar(m), m.getProb());
	}
	
}
