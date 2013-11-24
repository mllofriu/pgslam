package brick.utils.calib;

import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import brick.marcas.Camara;
import brick.marcas.Detector;
import brick.marcas.ObservMarca;
import brick.utils.Robot;

/***
 * Permite probar la deteccion de marcas.
 * Cuando detecta una marca pita y muestra su probabilidad
 * @author ludo
 *
 */
public class DetectM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Robot r = new Robot();
		
		
		Detector d = new Detector();
		Camara c = new Camara();
		
		
		
//		r.getPilot().setTravelSpeed(r.TRAVELSPEED/4);
		
//		r.getPilot().forward();
		
		while(!Button.ESCAPE.isPressed()){
			List<ObservMarca> ms = d.detectar(c.getObjs());
			if(!ms.isEmpty()){
				Sound.beep();
				LCD.drawString("Prob: " + ms.get(0).getProb(), 0, 0); 
			}
				
		}
	}

}
