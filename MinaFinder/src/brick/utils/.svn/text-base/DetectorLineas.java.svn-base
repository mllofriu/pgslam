package brick.utils;

import lejos.nxt.SystemSettings;

public class DetectorLineas implements Runnable {

	private Robot r;
	private boolean sawLine;
	
	public DetectorLineas(Robot r){
		this.r = r;
		Thread hilo = new Thread(this);
		hilo.setPriority(Thread.MAX_PRIORITY);
		hilo.start();
		
		sawLine = false;
	}
	
	@Override
	public void run() {
		while(true){
			synchronized(this){
				sawLine = sawLine || (r.getLuzIzq() < 50) || (r.getLuzDer() < 50);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean sawLine(){
		synchronized(this){
			return sawLine;
		}
		
	}
	
	public void resetSawLine(){
		synchronized(this){
			sawLine = false;
		}
	}

}
