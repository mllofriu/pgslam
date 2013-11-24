package brick.utils.calib;

import curtidor.utils.SyncDiffPilot;
import brick.utils.Robot;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

/***
 * Clase que permite calibrar el radio de las ruedas.
 * Avanza por una linea guia sobre el sensor izquierdo. 
 * Cuando pasa la primer linea negra perpendicular a la guia, 
 * establece el odometro en cero. Cuando pasa la segunda linea negra
 * imprime la estimacion de la distancia obtenida. Si la distancia es mayor
 * a la real, se debe decrementar el radio de las ruedas y viceversa.
 * @author ludo
 *
 */
public class CalibDistPil {

	private static final int CANT_MEDIDAS = 2;
	private static final double DIVISOR = 200;
	private static final double NORM_LIGTH = 1;
	private static final int UMBRAL_NEGRO = 10;
	private static final float LARGO_CUADRADO = 200;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot r = new Robot();
		SyncDiffPilot p = r.getPilot();
		
		
		Button.ENTER.waitForPress();
		
		while(medirDer(r.luzDer) > UMBRAL_NEGRO){
			p.forward();
			// TODO: averiguar por que no retorna la luz entre 0 y 100
			int light = (int) (r.luzIzq.getLightValue() / NORM_LIGTH);
			r.mDer.setSpeed((int) ((1+ (light - 50) / DIVISOR) * Robot.TRAVELSPEED));
			r.mIzq.setSpeed((int) ((1 - (light - 50) / DIVISOR) * Robot.TRAVELSPEED));
			System.out.println(r.getPp().getPose().getX());
		}
		
		while(medirDer(r.luzDer) <= UMBRAL_NEGRO){
			p.forward();
			r.mDer.setSpeed(Robot.TRAVELSPEED);
			r.mIzq.setSpeed(Robot.TRAVELSPEED);
			Sound.beep();
		}	
		
		r.getPp().setPose(new Pose(0,0,0));
		
		while(medirDer(r.luzDer) > UMBRAL_NEGRO){
			p.forward();
			// TODO: averiguar por que no retorna la luz entre 0 y 100
			int light = (int) (r.luzIzq.getLightValue() / NORM_LIGTH);
			r.mDer.setSpeed((int) ((1+ (light - 50) / DIVISOR) * Robot.TRAVELSPEED));
			r.mIzq.setSpeed((int) ((1 - (light - 50) / DIVISOR) * Robot.TRAVELSPEED));
			System.out.println(r.getPp().getPose().getX());
		}

		while(medirDer(r.luzDer) <= UMBRAL_NEGRO){
			p.forward();
			r.mDer.setSpeed(Robot.TRAVELSPEED);
			r.mIzq.setSpeed(Robot.TRAVELSPEED);
			Sound.beep();
		}	
		
		p.stop();
		
		System.out.println(r.getPp().getPose().getX());
		
		Button.ENTER.waitForPress();
	}

	private static int medirDer(LightSensor s) {
		double avg = 0;
		for(int i = 0; i < CANT_MEDIDAS; i++)
			avg += s.getLightValue() / (double)CANT_MEDIDAS;
		//		System.out.println((int) avg);
		return (int) avg;
	}
}
