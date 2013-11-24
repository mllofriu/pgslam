package pc.comm;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import brick.utils.Constantes;


public class DoraIntegrator implements Runnable {



	private static final int MIN_FOUND = 4;
	/**
	 * Recibe paquetes del sistema de visión, se los envía a los jugadores y redibuja</p>
	 * @author Jorge Merlino
	 * @version 0.8
	 */

	private DatagramSocket visionSock;
	private DatagramPacket packet;
	private byte [] buffer = new byte [192];
	private int robotX, robotY, robotZ, robotAngleX, robotAngleY, robotAngleZ;
	private String received;
	private InetAddress visionHost;
	private String [] data;
	private int found = 0;

	/**
	 * Inicia el thread 
	 * @throws SocketException Si hay problemas con la red
	 */
	public DoraIntegrator() {
		try {
			visionSock = new DatagramSocket(Constantes.puertoVision);
			packet = new DatagramPacket (buffer, buffer.length);

			robotX = robotY = robotZ = robotAngleX = robotAngleY = robotAngleZ = 0;
			try {
				this.visionHost = InetAddress.getByName(Constantes.ipVision);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			new Thread(this).start();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		
	}

	/**
	 * Procesamiento del servidor de visión
	 */
	public void run () {
		while (true) {
			try {

				if(Constantes.debug)
					System.out.println("recibiendo paquetes de dora...");
				visionSock.receive(packet);
				try {
					received = new String(packet.getData(), "ascii");
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				String[] lineas = received.split("\n");
				String[] robot1 = lineas[2].split(" ");
				//	            String[] robot2 = lineas[3].split(" ");
				synchronized (this) {
					if(!robot1[2].equals("NoFnd")){
						found++;
					} else {
						found = 0;
					}
					
					if(found >= MIN_FOUND){
						robotY = ((int)Float.parseFloat(robot1[3]));
						robotX = ((int)Float.parseFloat(robot1[4]));
						robotZ = (int)Float.parseFloat(robot1[5]);
						robotAngleZ = 360-(int)(Float.parseFloat(robot1[6])*180/Math.PI+360)%360;
					}
				}
				
				
//				System.out.println(found);

				if(Constantes.debug)
					System.out.println("Robot 1: (" +robotX+","+robotY+","+robotAngleZ+")");
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int getX(){
		return this.robotX;
	}

	public int getY(){
		return this.robotY;
	}

	public void end(){
		visionSock.close();
	}
	
	public boolean isFound(){
		synchronized (this) {
			return found >= MIN_FOUND;
		}
		
	}

}
