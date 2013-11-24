package brick.pruebas;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;


public class RoboLner implements MoveListener{
	public MoveProvider mimp;
	public DataOutputStream dos;

	public RoboLner(MoveProvider mp, DataOutputStream dos){
		mimp = mp;
		mp.addMoveListener(this);		
		this.dos = dos;		
	}

	@Override
	public void moveStarted(Move event, MoveProvider mp) {
		LCD.drawString("el lisener", 0,0);
		try{
			//			Move move = mp.getMovement();
			dos.writeUTF("mstarted" + "," + event.getDistanceTraveled() 
					+ "," + event.getAngleTurned() + "," + event.isMoving());			
			dos.flush();
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	@Override
	public void moveStopped(Move event, MoveProvider mp) {
		Sound.beep();
		try{
			//			Move move = mp.getMovement();
			dos.writeUTF("mstopped" + "," + event.getDistanceTraveled() 
					+ "," + event.getAngleTurned() + "," + event.isMoving());			
			dos.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void sendupdate() {
		try{
			//			Move move = mp.getMovement();
			dos.writeUTF("update");			
			dos.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}


}
