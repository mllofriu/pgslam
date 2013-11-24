package brick.tests;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Button;

import brick.distancia.MatrixFunctions;
import brick.distancia.Pair;
import brick.marcas.ObservMarca;

public class TPoly {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream(new File("marcas3.csv"));
			DataInputStream dis = new DataInputStream(fis);
			String line;
			line = dis.readLine();
			List<Pair> data = new ArrayList<Pair>();
			while(line != null && !line.equals("")){
				ObservMarca m = ObservMarca.fromString(line);
				int tam = m.getMedianTam();
				double dist = m.getDistancia();
				if(aceptables(tam, dist, m.getProb())){
					data.add(new Pair((double) tam, dist));
				}
				line = dis.readLine();
			}
			
			MatrixFunctions mfunct = new MatrixFunctions();
			double[] terms = mfunct.polyregress(data.toArray(new Pair[data.size()]), 2);
			int e = 0;
			for(double d : terms)
				System.out.println("Term " + e++ + ": " + d);
				
			System.out.println("Err: " + mfunct.std_error(data.toArray(new Pair[data.size()]), terms));
					
			Button.ENTER.waitForPress();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	private static boolean aceptables(int tam, double dist, float prob) {
		return dist < 1000 && tam > 5 && tam < 20 && prob > -25;
	}

}
