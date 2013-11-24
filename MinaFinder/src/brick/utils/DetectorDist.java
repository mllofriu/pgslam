package brick.utils;

import lejos.nxt.Sound;
import lejos.util.Delay;


public class DetectorDist implements Runnable {

	public static final int MAXDIST = 40;
	private Robot r;
	private boolean sawObst;
	private int minDist;
	
	public DetectorDist(Robot r){
		this.r = r;
		Thread hilo = new Thread(this);
		hilo.setPriority(Thread.MAX_PRIORITY-1);
		hilo.start();
		
		sawObst = false;
		minDist = 0;
	}
	
	@Override
	public void run() {
		while(true){
			synchronized(this){
				if(!sawObst){
					// Tomo dos medidas para evitar ruido
					// Pido que ambas sean menor que la distancia
					int izq1, izq2, der1, der2;
					izq1 = r.getDistIzq().getDistance();
					der1 = r.getDistDer().getDistance();
					boolean obs = (der1 < MAXDIST) &&	(izq1 < MAXDIST);
					
					izq2 = r.getDistIzq().getDistance();
					der2 = r.getDistDer().getDistance();
					boolean obs2 = (der2 < MAXDIST) && (izq2< MAXDIST);
					
					sawObst = obs && obs2;
					if(sawObst){
						Sound.beep();
//						r.stop();
						minDist = Math.min(Math.min(izq1, izq2), Math.min(der1, der2));
					}
				}
			}
			Delay.msDelay(100);
		}
	}
	
	public boolean sawObs(){
		synchronized(this){
			return sawObst;
		}
		
	}
	
	public void resetObs(){
		synchronized(this){
			sawObst = false;
			minDist = 0;
		}
	}
	
	public int getMinDist(){
		return minDist;
	}

}
