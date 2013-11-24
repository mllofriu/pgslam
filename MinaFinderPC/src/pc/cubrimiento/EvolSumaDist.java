package pc.cubrimiento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pc.logger.Logger;

import com.panayotis.gnuplot.JavaPlot;

public class EvolSumaDist {

	private static float[] minDists;

	public static void main(String [] args){
		JavaPlot plotter = new JavaPlot();
		plotter.setTitle("Title");

		Logger log = new Logger("cambioDists.log");
		
		int cantDatos = 100;
		minDists = new float[cantDatos];
		for (int i = 0; i < cantDatos; i ++)
			minDists[i] = i;
		
		float[][] distsSlam = new float[cantDatos][2];
		float[][] distsOdom = new float[cantDatos][2];

		int i = 0;
		for (float minDist : minDists ){
			File dirSlam = new File("./cubrimiento/slam/");
			File dirOdom= new File("./cubrimiento/odom/");

			List<Corrida> corridasSlam = new ArrayList<Corrida>();	
			for(File f : dirSlam.listFiles()){
				corridasSlam.add(Metricas.procesar(f, minDist));
			}

			List<Corrida> corridasOdom = new ArrayList<Corrida>();	
			for(File f : dirOdom.listFiles()){
				corridasOdom.add(Metricas.procesar(f, minDist));
			}

			float distanciaOdom = 0;
			float distanciaSlam = 0;


			for (Corrida c : corridasOdom){
				distanciaOdom += c.getDistRecorrida();
			}		

			for (Corrida c : corridasSlam){
				distanciaSlam += c.getDistRecorrida();
			}

			distsSlam[i][0] = minDist;
			distsSlam[i][1] = distanciaSlam;
			distsOdom[i][0] = minDist;
			distsOdom[i][1] = distanciaOdom;
			i++;
//			log.addLog(minDist + "," + distanciaOdom + "," + distanciaSlam);
		}
		
		plotter.addPlot(distsSlam);
		plotter.addPlot(distsOdom);
		
		plotter.plot();
	}
}
