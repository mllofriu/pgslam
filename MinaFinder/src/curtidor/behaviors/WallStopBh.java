package curtidor.behaviors;

import java.util.ArrayList;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import brick.particulas.CmdProxy;
import brick.particulas.mapa.MapaIEEE;
import brick.utils.Constantes;
import brick.utils.DetectorDist;
import brick.utils.Matematicas;
import curtidor.utils.DelayNavPathController;
import curtidor.utils.WayPointOrden;

public class WallStopBh implements Behavior {

	private static final int MIN_DIFF = 45;
	private static final int MINDIST = 20;
	private static final double BACKWARDS = -150;
	private static DetectorDist distDet;
	private DelayNavPathController nav;
	private CmdProxy cmdp;
	private boolean volviendo;
	private static boolean enable;

	public WallStopBh(DetectorDist distDet, CmdProxy cmdp, DelayNavPathController nav) {
		WallStopBh.distDet = distDet;
		this.nav = nav;
		this.cmdp = cmdp;
		enable = true;
		volviendo = false;
	}

	@Override
	public boolean takeControl() {
		// Si recien me active, descanso
		if (!enable ) return false;

		if (! distDet.sawObs()) return false;

		// Si vio objeto, retorna true si esta horizontal
		// Si hay proxy, pido el cacheado, si no pido directo
		Pose p;
		if(cmdp != null)
			p =  cmdp.getPose(false);
		else
			p = nav.getPoseProvider().getPose();
		
		boolean verticalYAlFinal = (! volviendo && Matematicas.angleDiff(p.getHeading(), 90) < MIN_DIFF) ||
				(volviendo && Matematicas.angleDiff(p.getHeading(), 270) < MIN_DIFF);
		boolean horizontal = Math.min(
				Matematicas.angleDiff(p.getHeading(), 0),
				Matematicas.angleDiff(p.getHeading(), 180)) < MIN_DIFF ;
		return verticalYAlFinal || horizontal;
	}

	@Override
	public void action() {
		System.out.println("Action de wall");
		// Para que se envie el applymove
		Delay.msDelay(500);

		System.gc();
		
		// A diferencia del take control, pido pose fresca
		Pose p = nav.getPoseProvider().getPose();
		boolean horizontal = Math.min(
				Matematicas.angleDiff(p.getHeading(), 0),
				Matematicas.angleDiff(p.getHeading(), 180)) < MIN_DIFF ;

		boolean verticalYAlFinal = (! volviendo && Matematicas.angleDiff(p.getHeading(), 90) < MIN_DIFF) ||
				(volviendo && Matematicas.angleDiff(p.getHeading(), 270) < MIN_DIFF);
//		verticalYAlFinal = verticalYAlFinal && p.getY() > MapaIEEE.getLargerSide() * 7. / 12 &&
//				p.getY() < MapaIEEE.getLargerSide() * 5. / 12 ;
		// Solo tomo accion si esta horizontal
		if(horizontal){
			System.out.println("Wall horiz");
//			Sound.beep();
			setEnable(false);
			
			// Si quede muy cerca voy un poco para atras
			if(distDet.getMinDist() < MINDIST){
				nav.getMoveController().travel(BACKWARDS, false);
				Delay.msDelay(500);
				// Vuelvo a pedir la pose actualizada
				p = nav.getPoseProvider().getPose();
			}
			
			distDet.resetObs();
			
			
			// Dos WayPoint, uno que doble y reactive, otro que haga el resto del camino
			float newY = volviendo ? p.getY() - Constantes.ANCHOCOBERTURA  : p.getY() + Constantes.ANCHOCOBERTURA ;
			WayPoint doblar = new WayPointOrden(new Pose(p.getX(), p.getY(), volviendo ? -90 : 90),
					WayPointOrden.Orden.activarUltra, true);
			WayPoint avanzar = new WayPoint(p.getX(), newY);
			ArrayList<WayPoint> wayp = new ArrayList<WayPoint>();
			wayp.add(doblar);
			wayp.add(avanzar);
			nav.setRoute(wayp);

			
		} else if(verticalYAlFinal){
			System.out.println("Wall Vert");
			// Si estoy vertical hago el camino que falta y marco la vuelta
//			Sound.beepSequence();
			setEnable(false);
			distDet.resetObs();
			
			float ang = p.getX() < MapaIEEE.getAncho()/2 ? 0 : 180;
			WayPoint doblar = new WayPointOrden(new Pose(p.getX(), p.getY(), ang), 
					WayPointOrden.Orden.activarUltra, true);
			ArrayList<WayPoint> wps = new ArrayList<WayPoint>();
			wps.add(doblar);
			nav.setRoute(wps);
			
//			// Pongo un delay para reactivarlo cuando este viajando en sentido de las x
//			new Timer().schedule(new TimerTask() {
//				@Override
//				public void run() {
////					distDet.resetObs();
//					Sound.beepSequenceUp();
//					setEnable(true);
//				}
//			}, SETENABLE_DELAY);
			
			volviendo = ! volviendo;
		} else {
			distDet.resetObs();
		}
	}

	@Override
	public void suppress() {
		System.out.println("Supress de wall");
	}

	public static void setEnable(boolean val){
		// Si reactivo, reseteo los objetos vistos 	
		if(val) distDet.resetObs();
		enable = val;
		
		
	}

}
