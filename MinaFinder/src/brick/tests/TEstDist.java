package brick.tests;

import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import brick.marcas.Detector;
import brick.marcas.ObservMarca;
import brick.distancia.EstimadorDistancia;
import brick.utils.Robot;

/***
 * Test para probar la estimacion de distancias. 
 * Toma los coeficientes del archivo de propiedades
 * Estima la distancia a las marcas observadas, pita e imprime en LCD. 
 * @author ludo
 *
 */
public class TEstDist {

	private static boolean actualizar;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot r = new Robot();
		r.initCam();
		EstimadorDistancia est = r.getEstimadorDist();
		Detector det = r.getDetect();
		
		actualizar = true;
		
		Button.ENTER.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonReleased(Button b) {
				actualizar = !actualizar;
			}
			
			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub
				
			}
		});
		
		while(!Button.ESCAPE.isPressed()){
			if(actualizar){
				List<ObservMarca> ms = det.detectarFusion();
				if(!ms.isEmpty()){
					ObservMarca m = ms.get(0);
					if(est.aceptable(m)){
						Sound.beep();
						LCD.clear();
						LCD.drawString("Dist: " + est.estimar(m), 0, 0);
						LCD.drawString("LowPixel: " + m.getLowerPixel(), 0, 1);
						LCD.drawString("Prob: " + m.getProb(), 0, 2);
						LCD.drawString("0: " + m.getObjs().get(0).getR().getHeight() +
								" " + m.getObjs().get(0).getR().getCenterY(), 0,3);
						LCD.drawString("1: " +m.getObjs().get(1).getR().getHeight()+
								" " + m.getObjs().get(1).getR().getCenterY(),0, 4);
						LCD.drawString("2: " + m.getObjs().get(2).getR().getHeight()+
								" " + m.getObjs().get(2).getR().getCenterY(), 0, 5);
						Delay.msDelay(1000);
					}
				}
			}
		}
	}

}
