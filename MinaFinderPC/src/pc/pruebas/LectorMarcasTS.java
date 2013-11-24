package pc.pruebas;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import brick.marcas.ObjCamara;
import brick.marcas.ObservMarca;

public class LectorMarcasTS {

	public List<ObservMarca> leerMarcas(String fileName) {
		List<ObservMarca> marcas = new LinkedList<ObservMarca>();
		
		FileReader fr;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return marcas;
		}

		BufferedReader in = new BufferedReader(fr);
		
		String line;
		try {
			line = in.readLine();
			while(line != null && !line.equals("")){
				System.out.println("linea " + line);
				
				marcas.add(leerMarca(line));
				line = in.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return marcas;
	}
	
	public ObservMarca leerMarca(String line) {
		String[] chunks = line.split("\\|");

		ObjCamara[] objs = new ObjCamara[3];
		for(int i = 0; i < objs.length; i++)
			objs[i] = readObject(chunks[i]);
		float prob = Float.parseFloat(chunks[3]);
		float dist = Float.parseFloat(chunks[4]);
		
		return new ObservMarca(objs[0], objs[1], objs[2], prob, dist);
	}

	private static ObjCamara readObject(String line) {
//		System.out.println(line);
		
		Pattern pattern = Pattern.compile(
				"Rectangle\\[x=(-*\\d*),y=(-*\\d*),width=(-*\\d*),height=(-*\\d*)\\] (-*\\d*)");
		Matcher matcher = pattern.matcher(line);

		matcher.find();
		int x = Integer.parseInt(matcher.group(1));
		int y = Integer.parseInt(matcher.group(2));
		int w = Integer.parseInt(matcher.group(3));
		int h = Integer.parseInt(matcher.group(4));
		int c = Integer.parseInt(matcher.group(5));
		
		return new ObjCamara(new Rectangle(x, y, w, h), c);
	}
	
	public void pasarACSV(String fileIn, String fileOut){
		List<ObservMarca> marcas = leerMarcas(fileIn);
		
		FileWriter fr;
		try {
			fr = new FileWriter(new File(fileOut));
			BufferedWriter out = new BufferedWriter(fr);
			
			for(ObservMarca m : marcas)
				try {
					out.write(m.toString() + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

}

