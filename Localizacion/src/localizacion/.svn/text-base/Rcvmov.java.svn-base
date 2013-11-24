package localizacion;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;

public class Rcvmov implements MoveProvider, Runnable {

	private Collection<MoveListener> listeners = new LinkedList<MoveListener>();
	private Move currentMove;
	private DataInputStream dis;

	public Rcvmov(){



		NXTComm nxtComm;

		try {
			//inicializo las cosas del bluetooth
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH,"proy","00:16:53:10:D5:05");
			nxtComm.open(nxtInfo);
			dis = new DataInputStream(nxtComm.getInputStream());
			System.out.println("coneccion establecida");

			new Thread(this).start();

		} catch (NXTCommException e) {	// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns the move made since the move started, but before it has completed. This method is used
	 * by GUI maps to display the movement of a robot in real time. The robot must be capable of determining
	 * the move while it is in motion.  	
	 * @return The move made since the move started.
	 */
	public Move getMovement(){
		//devuelve el current
		return currentMove;
	}

	/**
	 * Adds a MoveListener that will be notified of all movement events.
	 * @param listener the move listener
	 */
	public void addMoveListener(MoveListener listener){
		listeners.add(listener);
	}

	@Override
	public void run() {
		Move move = null;
		String datain;
		//leer del bluethoot
		//reportar el inicio de los movmimientos y el fin a los listeners
		String[] moveData = new String[4];			

		while (true){
			try {
				datain = dis.readUTF();				//leo del bluetooth
				moveData = datain.split(",");		//parseo el move


				if (moveData[0].equals("update")){ 
					for(MoveListener l : listeners){
						((MCLLinePoseProvider)l).update();
					}
				}else{ 
					//traver, ang, ismoving -- le pase un signo de menos al angulo xq me embolaba arreglar el mapa
					move = new Move(Float.parseFloat(moveData[1]),-Float.parseFloat(moveData[2]), Boolean.parseBoolean(moveData[3]));
					currentMove = move;
					if (moveData[0].equals("mstopped")){
						System.out.println("mstopped bro " + moveData[1] + " "+ moveData[2] +" " + moveData[3]);
						for(MoveListener l : listeners){
							l.moveStopped(move, this);
						}
					}else if (moveData[0].equals("mstarted")){
						System.out.println("mstarted bro " + moveData[1] + " " + moveData[2] + " " + moveData[3]);
						for(MoveListener l : listeners){
							l.moveStarted(move, this);
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}


