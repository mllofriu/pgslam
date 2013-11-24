package brick.tests;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import brick.angulo.EstimadorAngulo;
import brick.marcas.Camara;
import brick.marcas.Detector;
import brick.marcas.ObservMarca;
import brick.utils.Robot;

public class TEstAng {

	private static final float MINPROB = -40;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonReleased(Button b) {
				System.exit(0);
			}
			
			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Robot r= new Robot();
		
		r.initCam();
		Detector d = r.getDetect();
		EstimadorAngulo est = r.getEstAng();
		while(true){
			ObservMarca m = null;
			do{
				m = d.detectarUna(MINPROB);
			} while(m == null);
			Sound.beep();
			LCD.drawString("Angulo" + est.estimar(m), 0, 7);
		}
	}

}
