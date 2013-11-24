package brick.particulas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

import curtidor.utils.PoseStr;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.nxt.comm.USB;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import brick.utils.Observable;

public class CmdProxy extends Observable implements Runnable, MoveListener, PoseProvider{


	//	private MCLPoseProvider mcl;
	private Queue<Command> colaCmds;
	private DataOutputStream dos;
	private DataInputStream dis;
	private Pose pose;
	private Object comLock;


	public CmdProxy(MoveProvider mp, DataOutputStream dos){
		colaCmds = new Queue<Command>();
		mp.addMoveListener(this);
		this.dos = dos;

		comLock = new Object();
		
		Thread hilo = new Thread(this);
		hilo.setPriority(Thread.MAX_PRIORITY-2);
		hilo.start();
	}

	public CmdProxy(MoveProvider mp, DataOutputStream dos, DataInputStream dis){
		this(mp, dos);

		this.dis = dis;
		Thread recibidor = new Recibidor();
		recibidor.setPriority(8);
		recibidor.start();
	}

	@Override
	public void run() {
		while(true){
			synchronized (colaCmds) {
				try {
					colaCmds.wait();
					while(!colaCmds.isEmpty()){
						Command c = (Command)colaCmds.pop();
						//hablarle al otro brick y pasarle la data

						try {
							//mando el mensaje al otro brick
							//							Sound.beep();
							dos.writeUTF(c.toString());
							dos.flush(); 
							//							LCD.drawString("msg sent", 0, 5);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							//							e.printStackTrace();
							Sound.buzz();
							System.out.println("Esperando reestablecimiento conexion");
							
							// Espero 5 segundos mientras el recibidor levanta la conexion
							synchronized (comLock) {
								comLock.wait(5000);
							}
//							NXTConnection con = null;
//							while(con == null){
//								Thread.sleep(200);
////								String name = "monty";
////								con = RS485.getConnector().connect(name, NXTConnection.PACKET);
//							}
////							dos = con.openDataOutputStream();
//							// Reinserto el comando para nuevo procesamiento
							colaCmds.insertElementAt(c, 0);
							
						}


					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public void applyMove(Move m){
		synchronized (colaCmds) {
			colaCmds.push(new ApplyMoveCmd(m));
			colaCmds.notify();
		}
	}

	public void update(boolean line, float orientation, boolean tocoizq,
			boolean tocoder){
		synchronized (colaCmds) {
			colaCmds.push(new UpdateCmd(line, orientation, tocoizq,	tocoder));
			colaCmds.notify();
		}
	}

	@Override
	public void moveStarted(Move event, MoveProvider mp) {
		// Requerido por move provider, no hago nada
	}

	@Override
	public void moveStopped(Move m, MoveProvider mp) {
		applyMove(m);
	}

	public void push(Command cmd) {
		synchronized (colaCmds) {
			colaCmds.push(cmd);
			colaCmds.notify();
		}

	}

	public void end() {
		synchronized (colaCmds) {
			colaCmds.empty();
			colaCmds.push(new ExitCommand());
			colaCmds.notify();
		}
	}

	@Override
	public Pose getPose() {
//		System.out.println("Obteniendo lock en getpose");
		synchronized (this) {
			try {
//				System.out.println("Esperando por posicion");
				this.wait();
//				System.out.println("Posicion obtenida");
				return pose;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
		return null;
	}
	
	public Pose getPose(boolean wait){
		if (!wait)
			return pose;
		else
			return getPose();
	}

	@Override
	public void setPose(Pose aPose) {
//		System.out.println("Estableciendo posicion");
		synchronized (this) {
			pose = aPose;
			this.notify();
		}
		
	}

	//	public MCLParticleSet getParticles() {
	//		return mcl.getParticles();
	//	}

	public class Recibidor extends Thread {

		@Override
		public void run() {
			while(true){
				try {
					String poseStr = dis.readUTF();
					Pose p = PoseStr.fromStr(poseStr);
//					System.out.println(poseStr);
					setPose(p);
				} catch (IOException e) {
					Sound.buzz();	
					// TODO Auto-generated catch block
//					e.printStackTrace();
					System.out.println("Restableciendo conexion");
					LCD.drawString("Waiting for con...", 0, 0);
					NXTConnection connection = USB.waitForConnection();
					LCD.drawString("Conection done.", 0, 0);
					dos = connection.openDataOutputStream();
					dis = connection.openDataInputStream();
					
					synchronized (comLock) {
						comLock.notify();
					}
				}
			}
		}
	}
}

