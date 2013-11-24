package pc.cubrimiento;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pc.logger.Logger;
import brick.particulas.mapa.MapaIEEE;
import brick.utils.Constantes;
import brick.utils.DetectorDist;

public class Metricas {

	private static final float MIN_DIST = 5f;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File dirSlam = new File("./cubrimiento/slam/");
		File dirOdom= new File("./cubrimiento/odom/");
		
		List<Corrida> corridasSlam = new ArrayList<Corrida>();	
		for(File f : dirSlam.listFiles()){
			corridasSlam.add(procesar(f, MIN_DIST));
		}
		
		List<Corrida> corridasOdom = new ArrayList<Corrida>();	
		for(File f : dirOdom.listFiles()){
			corridasOdom.add(procesar(f, MIN_DIST));
		}
		
		float cubrimientoUtilOdom = 0;
		float cubrimientoUtilSlam = 0;
		float cubrimientoInutilSlam = 0;
		float cubrimientoInutilOdom = 0;
		float distanciaOdom = 0;
		float distanciaSlam = 0;
		float cubrimientoDistOdom = 0;
		float cubrimientoDistSlam = 0;
		
		
		for (Corrida c : corridasOdom){
			cubrimientoUtilOdom += c.getCubrimientoUtil();
			cubrimientoInutilOdom += c.getCubrimientoInutil();
			distanciaOdom += c.getDistRecorrida();
			cubrimientoDistOdom += (c.getCubrimientoUtil() / c.getDistRecorrida()) / corridasOdom.size();
//			System.out.println((c.getCubrimientoUtil() / c.getDistRecorrida()));
		}		
		
		for (Corrida c : corridasSlam){
			cubrimientoUtilSlam += c.getCubrimientoUtil();
			cubrimientoInutilSlam += c.getCubrimientoInutil();
			distanciaSlam += c.getDistRecorrida();
			cubrimientoDistSlam += (c.getCubrimientoUtil() / c.getDistRecorrida()) / corridasSlam.size();
//			System.out.println((c.getCubrimientoUtil() / c.getDistRecorrida()));
		}
		
		System.out.println("Cubrimiento util Slam " + cubrimientoUtilSlam);
		System.out.println("Cubrimiento util Odom " + cubrimientoUtilOdom);
		System.out.println("Cubrimiento inutil Slam " + cubrimientoInutilSlam);
		System.out.println("Cubrimiento inutil Odom " + cubrimientoInutilOdom);
		System.out.println("Distancia Slam " + distanciaSlam);
		System.out.println("Distancia Odom " + distanciaOdom);
		System.out.println("CubrimientoUtil/Distancia Slam " + cubrimientoDistSlam);
		System.out.println("CubrimientoUtil/Distancia Odom " + cubrimientoDistOdom);
	}

	public static Corrida procesar(File f, float minDist) {
		Logger logCubrimiento = new Logger(f.getName() + ".cub");
		
		boolean[][] cubierto = new boolean[(int) (MapaIEEE.getAncho()/Constantes.PRECISION_GRILLA)]
				[(int) (MapaIEEE.getLargerSide() / Constantes.PRECISION_GRILLA)];
//		System.out.println("Lenght " + cubierto.length + " " + cubierto[0].length);
		for (int i = 0; i < cubierto.length; i++)
			for(int j = 0; j < cubierto[i].length; j++)
				cubierto[i][j] = false;
		
		float distancia = 0;
		int lastX = -1;
		int lastY = -1;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while(line != null){
				StringTokenizer stok = new StringTokenizer(line, ",");
				long time = Long.parseLong(stok.nextToken());
				String tipo = stok.nextToken();
				if(tipo.equals("posdora")){
					int x = Integer.parseInt(stok.nextToken());
					int y = Integer.parseInt(stok.nextToken());
					cubrir(x, y, cubierto);
					if(lastX != -1){
						double incremento = Math.sqrt((x-lastX)*(x-lastX)+(y-lastY)*(y-lastY));
						if(incremento > minDist)
							distancia += incremento;
					}
					lastX = x;
					lastY = y;
					
					int cubrimientoUtil = 0;
					int bound = DetectorDist.MAXDIST * 10 - Constantes.ANCHOCOBERTURA / 2;
					for (int i = 0; i < cubierto.length; i++)
						for(int j = 0; j < cubierto[i].length; j++)
							if(cubierto[i][j])
								if(!(i * Constantes.PRECISION_GRILLA < bound ||
										i * Constantes.PRECISION_GRILLA > 2000 - bound ||
										j * Constantes.PRECISION_GRILLA < 200 ||
										j * Constantes.PRECISION_GRILLA > 2400 - bound))
									cubrimientoUtil++;
					
//					logCubrimiento.addLog("" + cubrimientoUtil);
					
				}
				
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int cubrimientoUtil = 0;
		int cubrimientoInutil = 0;
		int bound = DetectorDist.MAXDIST * 10 - Constantes.ANCHOCOBERTURA / 2;
		for (int i = 0; i < cubierto.length; i++)
			for(int j = 0; j < cubierto[i].length; j++)
				if(cubierto[i][j])
					if(i * Constantes.PRECISION_GRILLA < bound ||
							i * Constantes.PRECISION_GRILLA > 2000 - bound ||
							j * Constantes.PRECISION_GRILLA < 200 ||
							j * Constantes.PRECISION_GRILLA > 2400 - bound)
						cubrimientoInutil++;
					else
						cubrimientoUtil++;
		
		return new Corrida(cubrimientoUtil,cubrimientoInutil, distancia);
	}

	static void cubrir(int x, int y, boolean [][] cubierto) {
		Point p = new Point(x, y); 
		//			System.out.println("dora " + dora.getX() + " " + dora.getY());
		for(int i = (p.x - Constantes.ANCHOCOBERTURA/2) /Constantes.PRECISION_GRILLA;
				i < (p.x + Constantes.ANCHOCOBERTURA/2) / Constantes.PRECISION_GRILLA; i++)
			for(int j = (p.y - Constantes.ANCHOCOBERTURA/2) /Constantes.PRECISION_GRILLA;
					j < (p.y + Constantes.ANCHOCOBERTURA/2) / Constantes.PRECISION_GRILLA; j++)
				if(i > 0 && j > 0 && i < cubierto.length && j < cubierto[i].length){
					cubierto[i][j] = true;
					//						System.out.println(i + " " + j + " impresos");
				} else {
					//						System.out.println("No imprime " + i + " " + j);
				}
	}

}
