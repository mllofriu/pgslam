package brick.pruebas;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

import lejos.nxt.comm.USB;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.*;
/***
 * Clase que espera una conexión por bluetooh y luego comienza a hacer movimientos
 * aleatorios 
 * @author fede
 *
 */
public class OdoMoveSend {
	private static int DIST_ENTRE_RUEDAS = 177;
	private static int DIAMETRO_RUEDA = 60;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("romina");

		DifferentialPilot dp = new DifferentialPilot(DIAMETRO_RUEDA,
				DIST_ENTRE_RUEDAS, Motor.A, Motor.C);
		MoveProvider mp = (MoveProvider)dp;

		dp.setRotateSpeed(100);
		dp.setTravelSpeed(70);
		ColorSensor ls = new ColorSensor(SensorPort.S1);
		ColorSensor ls2 = new ColorSensor(SensorPort.S4);

		//		System.out.println("calibrar negro");
		//		int ii=1;
		//		while (ii<3){
		//			System.out.println(ls2.getLightValue());
		//		}
		ls.setHigh(205);
		ls.setLow(24);
		ls2.setHigh(205);
		ls2.setLow(24);


		//para enviar datos del brick a la pc
		LCD.drawString("Esperando...", 0, 0);
		//		NXTConnection connection = USB.waitForConnection(); 
		NXTConnection connection = Bluetooth.waitForConnection();
		LCD.drawString("Conexion establecida.", 0, 0);
		DataOutputStream dos = connection.openDataOutputStream();
		
		//me anoto como listener en un thread aparte
		RoboLner rl = new RoboLner(mp, dos);

		Random angRand = new Random();
		dp.forward();

		try {
			while(true){
				
//				dp.travel(100);
//				Thread.sleep(2000);
				
				if ((ls.getLightValue() < 75) || (ls2.getLightValue() < 50)){
					dp.stop();
					rl.sendupdate();
					Thread.sleep(1000);
					dp.travel(-200);
					Thread.sleep(1000);
					dp.rotate(angRand.nextInt(360)-180);
					Thread.sleep(1000);
					dp.forward();
				}	
				
//				dp.rotate(90);
//				Thread.sleep(5000);

			}	
		}catch (InterruptedException e) {
			e.printStackTrace();
		}

//		connection.close();

	}

}
