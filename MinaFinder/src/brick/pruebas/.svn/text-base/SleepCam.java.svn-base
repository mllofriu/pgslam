package brick.pruebas;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.util.Delay;
import brick.marcas.Camara;
import brick.utils.Robot;

public class SleepCam {

	private static boolean terminar = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot r = new Robot();
		
//		r.getBrazo();
		
//		Delay.msDelay(2000);
		
//		Camara c = new Camara();
		r.initCam();
		
//		Delay.msDelay(500);
		
//		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (!terminar);
//			}
//		});
//		t.setPriority(Thread.MAX_PRIORITY);
//		t.start();
		
		Delay.msDelay(10000);
		
		terminar  = true;
		
//		Delay.msDelay(2000);
		
		while (!Button.ESCAPE.isPressed()){
			if(!r.getDetect().detectarFusion().isEmpty())
				Sound.beep();
			Delay.msDelay(200);
		}
			
	}

}
