package curtidor.behaviors;

import brick.marcas.Buscador;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import curtidor.utils.DelayNavPathController;

public class FollowWayPointsBh implements Behavior {

	private DelayNavPathController nav;
	private Buscador buscador;
	private Thread hiloBuscador;

	public FollowWayPointsBh(DelayNavPathController nav, Buscador buscador){
		this.nav = nav;
		this.buscador = buscador;
	}

	@Override
	public boolean takeControl() {
		boolean res;
//		synchronized (nav) {
			res = nav.getWayPoint() != null;
//		}
		return res;
	}

	@Override
	public void action() {
//		Sound.beepSequence();
		System.out.println("Action follow");
		
		Delay.msDelay(500);
		
		hiloBuscador = new Thread(buscador);
		hiloBuscador.start();
		
//		System.out.println("Resume del nav");
		nav.resume();
		
		
		while(nav.isGoing()){
//			System.out.println("Esperando nav");
//			Thread.yield();
			Delay.msDelay(300);
		}
		
		// Espero a que se mande el mov
//		Delay.msDelay(300);
	}

	@Override
	public void suppress() {
		System.out.println("Supress Follow");
		// Interrumpo sin hacer sleep
		nav.interrupt(false);
		// Paro brazo rapido
		buscador.terminar();
		// Duermo para que el resto se entere de los movs
		Delay.msDelay(1000);
		
	}

}
