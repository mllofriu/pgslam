package brick.marcas;

import java.util.List;

import lejos.nxt.Sound;
import lejos.util.Timer;
import lejos.util.TimerListener;
import brick.angulo.EstimadorAngulo;
import brick.distancia.EstimadorDistancia;
import brick.particulas.CmdProxy;
import brick.particulas.ProxyMoveProvider;
import brick.particulas.UpdateCmdMarcas;
import brick.particulas.smodel.MarcaMeassure;
import brick.utils.Constantes;
import brick.utils.Robot;

public class Vision  implements Runnable{


	private static final double DETECT_SPEED = 75;
	private static final int SLOW_TIMEOUT = 3000;
	private static final int BRAZO_DET_SPEED = 3;
	private final float MIN_PROB = -50;
	private final int UMBRAL_UPDATE = 1000;
	private long updateTime = 0;
	private Robot robot;
	private EstimadorDistancia estDist;
	private CmdProxy cmdProxy;
	private Timer timer;
	private ProxyMoveProvider pmp;
	private EstimadorAngulo estAng;

	public Vision(final Robot robot, CmdProxy cmdProxy, ProxyMoveProvider pmp){
		this.robot = robot;
		this.estDist = robot.getEstimadorDist();
		this.estAng = robot.getEstAng();
		this.cmdProxy = cmdProxy;
		this.pmp = pmp;

		timer = new lejos.util.Timer(SLOW_TIMEOUT, new TimerListener() {
			@Override
			public void timedOut() {
				robot.resetSpeed();
				timer.stop();
			}
		});

//		Thread buscador = new Thread(new Buscador(robot));
//		buscador.setPriority(Thread.NORM_PRIORITY);
//		buscador.start();
//		
//		Thread dete = new Thread(this);
//		dete.setPriority(Thread.NORM_PRIORITY);
//		dete.start();
	}

	@Override
	public void run() {
		// Deteccion de marcas
		Camara cam = new Camara();
		Detector det = new Detector();
//		Camara cam = robot.getCam();
//		Detector det = robot.getDetect();
//		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

		while(true){
			// Obtengo los blobs de dos capturas
			List<ObjCamara> objs = cam.getObjs();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			objs.addAll(cam.getObjs());


			ObservMarca marca = det.detectarUna(objs, MIN_PROB);
			if(marca != null){
				Sound.beepSequenceUp();
				robot.setSpeed(DETECT_SPEED);
				robot.getBrazo().setSpeed(BRAZO_DET_SPEED);
				timer.start();
				
				// Establezco distancia

				float dist = (float)estDist.estimar(marca);
				// Agrando la distancia medida para reflejar distancia de la camara y volumen del cubo
				dist += Robot.DIST_CAM_CENTRO;
				dist += Constantes.LADO_CUBITO / 2;
				
				float ang = (float)estAng.estimar(marca);
				ang -= robot.getBrazo().getTachoCount();
				
				// Agrego el comando a la cola
				// Antes genero un movimiento intermedio para que el update se haga sobre algo actualizado
				pmp.interruptMove();
				cmdProxy.push(new UpdateCmdMarcas( 
						new MarcaMeassure(dist, marca.getId(), ang)));
				updateTime = System.currentTimeMillis();
//				robot.resetSpeed();
			}
				
//				Sound.beep();
//				if(System.currentTimeMillis() - updateTime > UMBRAL_UPDATE){
//					timer.start();
//					robot.setSpeed(DETECT_SPEED);
//					// Start timer to fallback to normal velocity
//					timer.start();
//				}
//				
//				// Si no lei un id antes
//				if(id == 0){
//					robot.setSpeed(DETECT_SPEED);
//					// Start timer to fallback to normal velocity
//					timer.start();
//					id = marca.getId();
//					Sound.beepSequence();
//				} else {
//					if (marca.getId() == id || marca.getId() == idRuido){
//						if(estDist.aceptable(marca) && System.currentTimeMillis() - updateTime > UMBRAL_UPDATE){
//
//							Sound.beepSequenceUp();
//							// Establezco distancia
//
//							float dist = (float)estDist.estimar(marca);
//							float ang = (float)estAng.estimar(marca);
//							// Agrego el comando a la cola
//							// Antes genero un movimiento intermedio para que el update se haga sobre algo actualizado
//							pmp.interruptMove();
//							cmdProxy.push(new UpdateCmdMarcas( 
//									new MarcaMeassure(dist, marca.getId(), ang)));
//							updateTime = System.currentTimeMillis();
//							robot.resetSpeed();
//						} 
//							
//						if (marca.getId() == idRuido)
//							id = idRuido;
//						
//						
//					} else {
//						Sound.beepSequence();
//						idRuido = marca.getId();
//					}
//				}
//			} 
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

}


