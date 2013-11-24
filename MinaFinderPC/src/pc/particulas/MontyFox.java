package pc.particulas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.robotics.navigation.Pose;
import pc.comm.Comunication;
import pc.comm.HeadingProxy;
import pc.comm.Proxy;
import pc.comm.ProxyEnvioCmd;
import brick.utils.Constantes;

public class MontyFox {

	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static Comunication com;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//abro comunicacion con el otro robot
		try{
			NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			NXTInfo[] infos = nxtComm.search("monty", NXTComm.PACKET);
			nxtComm.open(infos[0]);
			System.out.println("Conexion establecida");
			dis = new DataInputStream(nxtComm.getInputStream());
			dos = new DataOutputStream(nxtComm.getOutputStream());
		}catch(Exception e){
			System.out.println("error en la comunicacion");	
			e.printStackTrace();
		}
		
//		LCD.drawString("conec ok", 0, 0);

//		if(Constantes.debugPc)
//			com = NXTCom.getInstance();
		if(Constantes.debugPc)
			com = SocketSrvCom.getInstance();

		//		LineMap map = MapaIEEE.getIEEEMap();
		Pose initialPose = Constantes.initialPose;

		CmdProvider cmdp = new CmdProvider(dis, dos, Constantes.NUMPART, initialPose);

		if(Constantes.debugPc){
			HeadingProxy hp = new HeadingProxy();
			cmdp.addObserver(hp);
			if(Constantes.envioParts){
				System.out.println("Iniciando proxy particulas");
				Proxy proxy = new Proxy(cmdp, com);
				cmdp.addObserver(proxy);
			}
			WayPointProxy wpp = new WayPointProxy();
			cmdp.addObserver(wpp);
			ProxyEnvioCmd envioCmds = new ProxyEnvioCmd();
			cmdp.addObserver(envioCmds);
		}

		// Avisarle al otro brick que arranque
		try {
			dos.writeUTF("Arranca noma");
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cmdp.startSendingPose();
	}

}
