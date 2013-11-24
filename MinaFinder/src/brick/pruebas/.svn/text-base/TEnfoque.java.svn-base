package brick.pruebas;

import java.util.Collection;
import java.util.List;

import brick.marcas.Camara;
import brick.marcas.ObservMarca;
import brick.utils.Robot;

public class TEnfoque {


	public static void main(String[] arg)
	{
		Robot r = new Robot();
		List<ObservMarca> marcasSensadas = null;
		r.initCam();
		do{
			marcasSensadas = r.getDetect().detectarFusion();
		} while(marcasSensadas.size() == 0);
		
		ObservMarca m = marcasSensadas.get(0);
		int x = m.getCenter().x;
		if(x > Camara.TAM_PANTALLA / 2)
			r.getBrazo().rotate(15);
		else
			r.getBrazo().rotate(-15);
	}
}
