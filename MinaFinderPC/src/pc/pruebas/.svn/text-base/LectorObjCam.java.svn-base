package pc.pruebas;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import brick.marcas.ObjCamara;
import brick.marcas.ObservMarca;



public class LectorObjCam {

	public List<ObjCamara> leerObjetos(String line){
		List<ObjCamara> res = new LinkedList<ObjCamara>();
		
		Pattern pattern = Pattern.compile(
				"Cubo en \\((\\d*),(\\d*),(\\d*),(\\d*)\\) de color (\\d*)");
		Matcher matcher = pattern.matcher(line);
		
		while(matcher.find()){
			for (int i=0; i<=matcher.groupCount(); i++) {
		        String groupStr = matcher.group(i);
//		        System.out.println("Grupo " + i + " " + groupStr);
		    }
			int x = Integer.parseInt(matcher.group(1));
			int y = Integer.parseInt(matcher.group(2));
			int w = Integer.parseInt(matcher.group(3));
			int h = Integer.parseInt(matcher.group(4));
			int c = Integer.parseInt(matcher.group(5));
			res.add(new ObjCamara(new Rectangle(x, y, w, h), c));
		}
		
		return res;
	}
	
	public List<ObservMarca> leerMarcas(String line){
		List<ObservMarca> res = new LinkedList<ObservMarca>();
		
		Pattern pattern = Pattern.compile(
				"Marca en \\((\\d*),(\\d*),(\\d*),(\\d*)\\) id (\\d*) distancia (\\d*\\.\\d*) prob (-*\\d*\\.\\d*)");
		Matcher matcher = pattern.matcher(line);
		
		while(matcher.find()){
			for (int i=0; i<=matcher.groupCount(); i++) {
		        String groupStr = matcher.group(i);
//		        System.out.println("Grupo " + i + " " + groupStr);
		    }
			int x = Integer.parseInt(matcher.group(1));
			int y = Integer.parseInt(matcher.group(2));
			int w = Integer.parseInt(matcher.group(3));
			int h = Integer.parseInt(matcher.group(4));
			int id = Integer.parseInt(matcher.group(5));
			float dist = Float.parseFloat(matcher.group(6));
			float prob = Float.parseFloat(matcher.group(7));
			res.add(new ObservMarca(x, y, w, h, id, dist, prob));
		}
		
		return res;
	}

	public boolean isObjsLine(String line) {
		return line.startsWith("Cubo");
	}

	public boolean isMarcaLine(String line) {
		return line.startsWith("Marca");
	}
}
