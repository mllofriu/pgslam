package pc.particulas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;
import brick.particulas.Command;
import brick.particulas.MCLParticleSet;
import brick.particulas.MCLPoseProvider;
import brick.particulas.mmodel.GaussianMModel;
import brick.particulas.smodel.MarcaSModel;
import brick.utils.Constantes;
import brick.utils.Observable;
import curtidor.utils.PoseStr;

/***
 * Front end del filtro de particulas. 
 * Lee los comandos, los encola y los notifica
 * @author fede
 * FIXME: ahora tiene que pasar a recibir los mensajes y encolarlos
 */
public class CmdProvider extends Observable implements Runnable {

	private MCLPoseProvider mcl;
	private DataInputStream dis;
	private List<Command> colaCmds;
	private DataOutputStream dos;
	private Object comLock;

	public CmdProvider(DataInputStream dis, DataOutputStream dos, int numPart, Pose initialPose){
		//		mcl = new MCLPoseProvider(null, map, numParticles, p,
		//				new LineOrientSModel(87,31), new GaussianMModel(0.14f,.11f));
		if(Constantes.slam)
			Constantes.configure(500,1f / Constantes.NUMPART *.071f, .12f, .10f, 7158);
		else
			Constantes.configure(1, .00f, .0f, .0f, 0);
		mcl = new MCLPoseProvider(null, Constantes.NUMPART,initialPose,
					new MarcaSModel(), new GaussianMModel());
	
		this.dis = dis;
		this.dos = dos;

		comLock = new Object();

		colaCmds = new ArrayList<Command>();


		Thread hilo = new Thread(this);
		hilo.setPriority(Thread.MAX_PRIORITY);
		hilo.start();

		Thread hiloEjec = new Thread(new HiloEjec());
		hiloEjec.start();
	}

	@Override
	public void run() {
		while(true){
			try {
				String cmdStr;
				//					if (dis.available()>0) {
				//				System.gc();
				cmdStr = dis.readUTF();
				//				Sound.buzz();
				Command c = Command.parseCommand(cmdStr);
				System.out.println(c.toString());
				synchronized (colaCmds) {
					colaCmds.add(c);
					colaCmds.notify();
				}


			} catch (IOException e) {
				synchronized (comLock) {
					// Espero a que el enviador restablezca la conexion
					try {
						comLock.wait(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
			//			Thread.yield();
		}
	}




	public MCLParticleSet getParticles() {
		return mcl.getParticles();
	}

	class HiloEjec implements Runnable{

		@Override
		public void run() {
			while(true){
				boolean hayCmds;
				synchronized (colaCmds) {
					try {
						colaCmds.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					hayCmds = colaCmds.size() > 0;
				}

				while(hayCmds){
					Command c;
					synchronized (colaCmds) {
						c = colaCmds.remove(0);
					}
					//					if(c instanceof ExitCommand)
					//						com.enviar(Comunication.IDFIN, "");
//					if(Constantes.slam) 
					c.execute(mcl);
					setChanged();
					notifyObservers(c);

					synchronized (colaCmds) {
						hayCmds = colaCmds.size() > 0;
					}
				}
			}

		}

	}

	public class HiloEnvioPos implements Runnable {

		@Override
		public void run() {
			while (true){
				try {
					PoseStr posestr = new PoseStr(mcl.getPose());
					dos.writeUTF(posestr.toString());
					dos.flush();
//					System.out.println("CmdProvider: pose enviada " + posestr.toString());
				} catch (IOException e) {
					System.out.println("Restableciendo conexion");
					NXTComm nxtComm;
					boolean done = false;
					while(!done){
						try {
							nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
							NXTInfo[] infos = nxtComm.search("monty", NXTComm.PACKET);
							if(infos.length > 0){
								nxtComm.open(infos[0]);
								dis = new DataInputStream(nxtComm.getInputStream());
								dos = new DataOutputStream(nxtComm.getOutputStream());
								done = true;
							}
						} catch (NXTCommException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					System.out.println("Conexion establecida");
					synchronized (comLock) {
						comLock.notify();
					}
					
				}

				Delay.msDelay(500);
			}
		}

	}

	public void startSendingPose() {
		Thread hiloEnvioPos = new Thread(new HiloEnvioPos());
		hiloEnvioPos.start();		
	}
}

