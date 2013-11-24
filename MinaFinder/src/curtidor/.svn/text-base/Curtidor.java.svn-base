package curtidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.subsumption.Behavior;
import brick.marcas.Buscador;
import brick.particulas.CmdProxy;
import brick.particulas.ProxyMoveProvider;
import brick.utils.Constantes;
import brick.utils.DetectorDist;
import brick.utils.Robot;
import curtidor.behaviors.FollowWayPointsBh;
import curtidor.behaviors.VisionBh;
import curtidor.behaviors.WallStopBh;
import curtidor.behaviors.WayPointCreatorBh;
import curtidor.utils.Arbitrator;
import curtidor.utils.DelayNavPathController;
import curtidor.utils.WayPointListenerInicio;
import curtidor.utils.WayPointOrden;

public class Curtidor {

	
	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static ProxyMoveProvider pmp;
	private static CmdProxy cmdProxy = null;

	public static void main(String[] args){
		//		RConsole.open();

		Robot r = new Robot();
		
		// Limpiar memoria cada un minuto
//		new Thread(new Runnable(){
//			@Override
//			public void run() {
//				Delay.msDelay(60000);
//				System.gc();
//			}
//		}).start();

//		if (Constantes.slam)
			initConMontyYProxys(r);

		// Bajo la prioridad de todos los threads de operacion continua
//		Thread.currentThread().setPriority(3);

//		DetectorLineas lineDet = new DetectorLineas(r);
		final DetectorDist distDet = new DetectorDist(r);
		
//		System.out.println("Curti: waypoints creados");
		DelayNavPathController nav;
		if(Constantes.slam)
			nav = new DelayNavPathController(r.getPilot(), cmdProxy);
		else {
			OdometryPoseProvider opp = new OdometryPoseProvider(r.getPilot());
			nav = new DelayNavPathController(r.getPilot(), opp);
		}
			
		
		nav.getPoseProvider().setPose(Constantes.initialPose);
//		nav.getPoseProvider().setPose(new Pose(400, 1200, 177));
		
		// Listener que envia los waypoints y reactiva los ultra de ser necesario
		WayPointListenerInicio wpList = new WayPointListenerInicio() {
			
			@Override
			public void pathComplete() {
			}
			
			@Override
			public void nextWaypoint(WayPoint wp) {
				// Mando un comando con el next waypoint
				if(cmdProxy != null)
					cmdProxy.push(new WayPointCmd(wp));
			}

			@Override
			public void wayPointComplete(WayPoint wp) {
				if(wp instanceof WayPointOrden && 
						((WayPointOrden)wp).getOrden() == WayPointOrden.Orden.activarUltra){
					// Si corresponde, reactivo el detector de objetos
					WallStopBh.setEnable(true);
				}
			}
		};
		nav.addListener(wpList);
		
//		DelayNavPathController nav = null;
//		
//		nav.followRoute(wayPoints, true);
//		nav.interrupt();
		
//		System.out.println("Curti: navpath creado");
		
		Buscador buscador = new Buscador(r);
		
		Behavior wayPointCreator = new WayPointCreatorBh(nav);
		Behavior followWayPoints = new FollowWayPointsBh(nav, buscador);
//		System.out.println("Curti: followWaypoints creado");
//		Behavior boton = new BotonBh();
//		Behavior volver = new VolverBh(nav);
		Behavior wallStop = new WallStopBh(distDet, cmdProxy, nav);
		
		Arbitrator arb;
		if(Constantes.slam){
			Behavior vision = new VisionBh(r, cmdProxy);
			Behavior[] comps = {wayPointCreator, followWayPoints, vision, wallStop};
			arb = new Arbitrator(comps);
		}else{
			Behavior[] comps = {wayPointCreator, followWayPoints, wallStop};
			arb = new Arbitrator(comps);
		}
		
		// Pongo esto para probar cuelgues
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
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

		
//		pmp = new ProxyMoveProvider(r.getPilot());
		
		System.out.println("Esperando orden de arranque");
		// Espero que el otro me avise que arranque
        try {
			String str = dis.readUTF();
//			System.out.println("Curti: arranque " + str);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        cmdProxy = new CmdProxy(r.getPilot(), dos, dis);
        
        Sound.beepSequenceUp();
//        try {
//			dis.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
