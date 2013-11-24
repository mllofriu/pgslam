package brick;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import curtidor.behaviors.VisionBh;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RConsole;
import lejos.nxt.comm.RS485;
import lejos.nxt.comm.USB;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import brick.particulas.CmdProxy;
import brick.particulas.ProxyMoveProvider;
import brick.particulas.mapa.MapaIEEE;
import brick.utils.Constantes;
import brick.utils.DetectorDist;
import brick.utils.Robot;

public class ProyBh {

	
	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static ProxyMoveProvider pmp;
	private static CmdProxy cmdProxy;

	public static void main(String[] args){
		//		RConsole.open();

		Robot r = new Robot();

		initConMontyYProxys(r);

		// Bajo la prioridad de todos los threads de operacion continua
//		Thread.currentThread().setPriority(3);

		DetectorDist lineDet = new DetectorDist(r);

		Behavior derecho = new DerechoBh(r);
		Behavior recula = new ReculaBh(r, lineDet);
		Behavior boton = new BotonBh();
		Behavior vision = new VisionBh(r, cmdProxy);

		Behavior[] comps = {derecho, recula, boton, vision};

		Arbitrator arb = new Arbitrator(comps);
		arb.start();
	}

	/**
	 * Inicia conexion con monty y aguarda mensaje de largada
	 * @param r
	 */
	private static void initConMontyYProxys(Robot r) {
		//conexion con el otro robot
//		String name = "monty";
//		NXTConnection con = RS485.getConnector().connect(name, NXTConnection.PACKET);
//		if (con == null)
//		{
//			LCD.drawString("Connect fail", 0, 5);
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.exit(1);
//		}
			
		//streams para comunicarse con el otro robot
//		dis = con.openDataInputStream();
//		dos = con.openDataOutputStream();
		LCD.drawString("Waiting for con...", 0, 0);
		NXTConnection connection = USB.waitForConnection();
		LCD.drawString("Conection done.", 0, 0);
		dos = connection.openDataOutputStream();
		dis = connection.openDataInputStream();

		
		pmp = new ProxyMoveProvider(r.getPilot());
		
		cmdProxy = new CmdProxy(pmp, dos);
		
		System.out.println("Esperando orden de arranque");
		// Espero que el otro me avise que arranque
        try {
			dis.readUTF();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Sound.beepSequenceUp();
//        try {
//			dis.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
