package brick;

import java.util.Random;

import lejos.robotics.subsumption.Behavior;
import brick.utils.DetectorDist;
import brick.utils.Robot;
import curtidor.utils.SyncDiffPilot;

public class ReculaBh implements Behavior {

	private static final double PROB_CHANGE_TURN = 0.25;
	private static final int ANG_THRS = 2;
	private static final int TRAVEL_THRS = 2;
	private static enum Turn{left, right};

	private Robot r;
	private Random angRand;
	private brick.ReculaBh.Turn lastTurn;
	private boolean terminar;
	private float toRotate;
	private SyncDiffPilot dp;
	private float toTravel;
	private DetectorDist lineDet;

	public ReculaBh(Robot r, DetectorDist lineDet) {
		this.r = r;
		angRand = new Random();
		lastTurn = Turn.left;

		dp = r.getPilot();

		terminar = false;
		toRotate = 0;
		toTravel = 0;
		
		this.lineDet = lineDet;
	}

	@Override
	public boolean takeControl() {
		// Movimientos pendientes
		if (Math.abs(toRotate) > 0 || Math.abs(toTravel) > 0)
			return true;
		else
			return lineDet.sawObs();
		//		return true;
	}

	@Override
	public void action() {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		terminar = false;
		
		if(Math.abs(toRotate) > 0){
			rotate();
			return;
		}
		
		if(Math.abs(toTravel) > 0){
			travel();
			if(terminar)
				return;
		} else {
		
			toTravel = -200;
			travel();
	
			if(terminar){
				lineDet.resetObs();
				return;
			}
		}
		
		toRotate = 90 + angRand.nextInt(30) - 15;
//		toRotate = 90;

		// Con probabilidad 0.25 rotamos al reves
		Turn thisTurn;
		if (Math.random() < PROB_CHANGE_TURN){
			if(lastTurn == Turn.left){
				thisTurn = Turn.right;
			} else
				thisTurn = Turn.left;

		} else {
			thisTurn = lastTurn;
		}
		if(thisTurn == Turn.right)
			toRotate = -toRotate;
		lastTurn = thisTurn;

		
		rotate();

		if(terminar){
			lineDet.resetObs();
			return;
		}

		lineDet.resetObs();
	}


	private void travel() {
		dp.travel(toTravel, true);
		while(!terminar && dp.isMoving())
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(terminar)
			toTravel -= dp.getMovement().getDistanceTraveled();
		else
			toTravel = 0;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void rotate() {
		// Roto el doble para cumplir la condicion de pasarme
		dp.rotate(toRotate, true);
		while(!terminar && dp.isMoving())
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(terminar)
			toRotate -= dp.getMovement().getAngleTurned();
		else
			toRotate = 0;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void suppress() {
		// Solo ejecuto estos calculos una vez
		terminar = true;
		r.stop();
			//			RConsole.println(""+ang);
//			if(Math.abs(toRotate) > 0)
//				toRotate -= dp.getMovement().getAngleTurned();
//			
//			if(Math.abs(toTravel) > 0)
//				toTravel -= dp.getMovement().getDistanceTraveled();

			//			RConsole.println(""+finalAng);
			//			RConsole.println(""+initAng);
			//			RConsole.println(""+angleTurned);
			//			RConsole.println(""+ang);
		//		
		
	}

}
