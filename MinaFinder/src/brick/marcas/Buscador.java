package brick.marcas;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.Move;
import lejos.util.Delay;
import brick.utils.Robot;

public class Buscador implements Runnable {

	private static final int POSITIVE_LIMIT = 60;
	private static final int NEGATIVE_LIMIT = -POSITIVE_LIMIT;
	private NXTRegulatedMotor brazo;
	private boolean terminar;
	private boolean turningLeft;
	
	public Buscador(Robot r){
		brazo = r.getBrazo();
//		brazo.resetTachoCount();
		
		turningLeft = true;
		
	}
	@Override
	public void run() {
		terminar = false;
		while(!terminar){
			if(turningLeft){
				brazo.rotateTo(POSITIVE_LIMIT, true);
				while(!terminar && brazo.getPosition() < POSITIVE_LIMIT)
					Delay.msDelay(100);
				if (!terminar)
					turningLeft = false;
			} else {
				brazo.rotateTo(NEGATIVE_LIMIT, true);
				while(!terminar && brazo.getPosition() > NEGATIVE_LIMIT)
					Delay.msDelay(100);
				if (!terminar)
					turningLeft = true;
			}
			
				
		}
		brazo.stop();
		brazo.flt();
	}
	
	public void terminar(){
		brazo.stop();
		brazo.flt();
		terminar = true;
	}
	
	public static void main(String[] args){
		new Thread(new Buscador(new Robot())).start();
		
		Button.waitForPress();
	}

}
