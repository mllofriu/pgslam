package brick.particulas;

import java.util.ArrayList;
import java.util.Collection;

import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import curtidor.utils.SyncDiffPilot;

/***
 * Clase que se encarga de cortar los movimientos largos para 
 * actualizar las particulas cada un determinado tiempo
 * @author ludo
 *
 */
public class ProxyMoveProvider implements MoveProvider, MoveListener, Runnable {

	private static final long TIMEOUT = 3000;
	private boolean moving;
	private SyncDiffPilot pilot;
	private Collection<MoveListener> listeners;
	
	public ProxyMoveProvider(SyncDiffPilot pilot){
		listeners = new ArrayList<MoveListener>();
		this.pilot = pilot;
		moving = false;
		pilot.addMoveListener(this);
		new Thread(this).start();
	}

	@Override
	public void moveStarted(Move move, MoveProvider mp) {
		// Mando mensaje de loggin
//		if (Constantes.logginRecorrida)
//			NXTCom.getInstance().enviar(Comunication.IDMOVSTART,
//					new MoveSerializable(move).toString());
		// Actualizo nocion interna y pongo a correr el cortador automatico
		synchronized (this){
			moving = true;
			// Solo activo el sistema para los travel, no para las rotaciones
			if(move.getMoveType().equals(Move.MoveType.TRAVEL))
				this.notify();
		}
		for(MoveListener ml : listeners)
		      ml.moveStarted(new Move(pilot.getMovement().getMoveType(),
		            pilot.getMovementIncrement(),
		            pilot.getAngleIncrement(), pilot.isMoving()), this);
	}

	@Override
	public void moveStopped(Move move, MoveProvider mp) {
		// Mando mensaje de loggin
//		if (Constantes.logginRecorrida)
//			NXTCom.getInstance().enviar(Comunication.IDMOVEND,
//					new MoveSerializable(move).toString());
		synchronized (this) {
			moving = false;
			// Solo activo el sistema para los travel, no para las rotaciones
			if(move.getMoveType().equals(Move.MoveType.TRAVEL))
				this.notify();
		}
		anunciarMovimiento();
	}

	@Override
	public void run() {
		while (true){
			try {
				synchronized (this) {
					this.wait();
					while(moving){
						this.wait(TIMEOUT);
						
						if(moving){
							anunciarMovimiento();
						}
							
					}
				}
				
				
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	private void anunciarMovimiento() {
		for(MoveListener ml : listeners)
		      ml.moveStopped(new Move(pilot.getMovement().getMoveType(),
		            pilot.getMovementIncrement(),
		            pilot.getAngleIncrement(), pilot.isMoving()), this);
				
		// Reseteo para que no acumule la odometria
		pilot.reset();
	}

	@Override
	public Move getMovement() {
		return pilot.getMovement();
	}

	@Override
	public void addMoveListener(MoveListener listener) {
		listeners.add(listener);
		
	}
	
	public void interruptMove(){
		synchronized (this) {
			if(moving)
				anunciarMovimiento();
		}
	}
}
