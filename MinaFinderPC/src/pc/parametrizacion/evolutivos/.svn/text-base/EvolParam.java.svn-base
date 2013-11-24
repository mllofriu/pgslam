package pc.parametrizacion.evolutivos;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import org.jgap.InvalidConfigurationException;

import pc.logger.Logger;


public class EvolParam {

	private static final int CORRIDASPORPARAM = 10;
	private static int mutRate[] = {2,5,10,50};
	private static double crossRate[] = {.3, .5, .65, .8, .95};
	private static int popSize[] = {20, 50, 100};

	public static void main(String[] args) throws InvalidConfigurationException {
		// Para division del espacio de prueba
		int inicio = 1;
		int fin = mutRate.length * crossRate.length * popSize.length;
		String idProc = "";
		if(args.length == 2){
			inicio = Integer.parseInt(args[0]);
			fin = Integer.parseInt(args[1]);
			NumberFormat formatter = new DecimalFormat("0000");
			idProc = formatter.format(inicio) + "-" + formatter.format(fin);
		}
		int count = 1;
		
		Logger log = new Logger("paramEvols" + idProc + ".txt");
		for(int mr : mutRate)
			for (double cr : crossRate)
				for(int ps : popSize){
					if(count >= inicio && count < fin){
						List<Double> errores = new LinkedList<Double>();
						System.out.println(cr + "," + mr + "," + ps);
						// TODO: correr tomando la media de 5
						for(int i = 0; i< CORRIDASPORPARAM; i++){
							EvolutivosAlgoritmo alg = new EvolutivosAlgoritmo(
									new File("./corridasVisionBh/"),cr,mr,ps, log);
							errores.add(alg.test());
						}
						double errProm = 0;
						for(double err : errores){
							errProm += err / CORRIDASPORPARAM;
						}
						double errVar = 0;
						for(double err : errores)		
							errVar += Math.pow(errProm - err, 2) / CORRIDASPORPARAM;
								
						errVar = Math.sqrt(errVar);
						log.addLog(cr + "," + mr + "," + ps + "," + errProm + "," + errVar);
					}
					
					count ++;
				}
		log.endLog();
	}


}
