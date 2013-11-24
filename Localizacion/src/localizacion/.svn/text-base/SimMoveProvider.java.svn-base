package localizacion;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;

class SimMoveProvider implements MoveProvider {

	
	
	private Collection<MoveListener> listeners;
	private Iterator<WayPoint> route;
	private Pose currentPose;
	private Move currentMove;

	public SimMoveProvider(Collection<WayPoint> route){
		listeners = new LinkedList<MoveListener>();
		this.route = route.iterator();
		currentPose = this.route.next().getPose();
		System.out.println(currentPose.toString());
	}
	
	public void addMoveListener(MoveListener listener) {
		listeners.add(listener);
	}

	public Move getMovement() {
		return currentMove;			
	}
	
	public void nextMove(){
		if(route.hasNext()){
			WayPoint next = route.next();
//			System.out.println(next.toString());
			currentMove = calcMove(currentPose, next.getPose());
			float angleAnt = currentPose.getHeading();
			currentPose = next.getPose();
			currentPose.setHeading(currentMove.getAngleTurned() + angleAnt);
			System.out.println(currentPose.toString());
		} 
	}
	
	public void sendStarted(){	
		// Aplico rotacion para apuntar al destino
		Move rot = new Move(0, currentMove.getAngleTurned(), false);
		for(MoveListener l : listeners){
			l.moveStarted(rot, this);
			l.moveStopped(rot, this);
		}
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Aplico el movimiento sin angulo
		Move trans = new Move(currentMove.getDistanceTraveled(), 0, false);
		for(MoveListener l : listeners){
			l.moveStarted(trans, this);
		}
	}
	
	public void sendStopped(){
		// Aplico solo el movimiento sin angulo para que no roten demás
		Move trans = new Move(currentMove.getDistanceTraveled(), 0, false);
		for(MoveListener l : listeners)
			l.moveStopped(trans, this);
	}

	private Move calcMove(Pose p1, Pose p2){
		double distance = Math.sqrt((p2.getX() - p1.getX())*(p2.getX() - p1.getX()) + 
				(p2.getY() - p1.getY())*(p2.getY() - p1.getY()));
		double angle;
		if ((p2.getX() - p1.getX()) != 0){
			double tan = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
			angle = Math.toDegrees(Math.atan(tan));
//			System.out.println((float)angle);
		} else
			angle = 90;
		
		angle -= p1.getHeading();
		angle = angle > 359 ? angle - 360 : angle;
		angle = angle < 0 ? angle + 360 : angle;
		Move res = new Move((float)distance,(float) angle, false);
		System.out.println(res.getAngleTurned() + " " + res.getDistanceTraveled() + " " + res.getArcRadius());
		return res;
	}
}
