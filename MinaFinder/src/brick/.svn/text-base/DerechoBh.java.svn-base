package brick;

import lejos.robotics.subsumption.Behavior;
import brick.marcas.Buscador;
import brick.utils.Robot;

public class DerechoBh implements Behavior {
	
	private Robot r;
	private boolean terminar;
	private Buscador buscador;
	private Thread hiloBuscador;

	public DerechoBh(Robot r){
		this.r = r;
		terminar = false;
		
		buscador = new Buscador(r);
		
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		r.stop();
		
		terminar = false; 
		
		// Inicio brazo
		hiloBuscador = new Thread(buscador);
		hiloBuscador.start();
		
		r.getPilot().forward();
		
//		while(!terminar)
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		synchronized (this) {
			try {
				// Uso buscador porque no me deja usar this
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void suppress() {
		r.stop();
		synchronized (this) {
			this.notify();
		}
		
		buscador.terminar();
	}

}
