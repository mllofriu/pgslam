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
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

import lejos.nxt.comm.USB;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.*;

public class OdoDatSend {
	private static int DIST_ENTRE_RUEDAS = 163;
	private static int DIAMETRO_RUEDA = 56;

	//	private static final String LOGTOREAD = "exp1NoNeg.txt";

	public static void main(String[] args) {
		System.out.println("romina");
		
		DifferentialPilot dp = new DifferentialPilot(DIAMETRO_RUEDA,
				DIST_ENTRE_RUEDAS, Motor.A, Motor.C);
		
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

		PoseProvider pp = new OdometryPoseProvider((MoveProvider) dp); // uso dp
		// para
		// mostrar
		Pose pose = pp.getPose();

		Random aleatorio = new Random();
		dp.forward();
		long start = System.currentTimeMillis();
		long lecturaAnterior = System.currentTimeMillis();
		
		Random angRand = new Random();
		
				
		try {
			while(System.currentTimeMillis() - start < 60000){
				if ((ls.getLightValue() < 100) || (ls2.getLightValue() < 100)){
					dp.backward();
					Thread.sleep(2000);
					dp.rotate(angRand.nextInt(360)-180);
					dp.forward();
				}
				if(System.currentTimeMillis() - lecturaAnterior > 500){
					dos.writeFloat(pose.getX());
					dos.writeFloat(pose.getY());
					dos.writeFloat(pose.getHeading());				
					dos.flush();
					lecturaAnterior = System.currentTimeMillis();
				}
				

//				Thread.sleep(500);

				
				pose = pp.getPose();

			}
			
		dp.stop();
		//mando mi fecha de nacimiento para cotar la ejecución
		
		dos.writeFloat((float) 28.0182);
		dos.writeFloat(pose.getY());
		dos.writeFloat(pose.getHeading());				
		dos.flush();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(" write error" +e);
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//			RConsole.println("");
		//			RConsole.println("x=" + (pose.getX()) + " y=" + pose.getY() + " a="
		//					+ pose.getHeading());
		//			
		//			
		//
		//			// Button.waitForPress();
		//			dp.rotate(90);
		//			pose = pp.getPose();
		//			RConsole.println("x=" + (pose.getX()) + " y=" + pose.getY() + " a="
		//					+ pose.getHeading());




		pose = pp.getPose();
		//		RConsole.println("x=" + (pose.getX()) + " y=" + pose.getY() + " a=" + pose.getHeading());
		//		RConsole.close();
		connection.close();

	}
}
