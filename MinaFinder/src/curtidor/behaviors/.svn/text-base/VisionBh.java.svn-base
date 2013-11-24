package curtidor.behaviors;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import brick.distancia.EstimadorDistancia;
import brick.marcas.Detector;
import brick.marcas.ObservMarca;
import brick.particulas.CmdProxy;
import brick.particulas.UpdateCmdMarcas;
import brick.particulas.smodel.MarcaMeassure;
import brick.utils.Constantes;
import brick.utils.Robot;

public class VisionBh implements Behavior {

	private static final int MAX_VECES_VISTA = 1;
	private static final long ESPERA_VISTA = 60000;
	private static final int MAXINTENTOS = 40;
	private static final int TAM_PANTALLA = 174;
	private static final float INCREMENTO_DENTADAS = 1.5f;
	private static final double MAX_DIFF = 50;
	private static final int MINOBSERV = 3;
	private static final long ESPERA_OBSERVS = 10000;
	private List<ObservMarca> marcas;
	private Detector det;
	private Robot r;
	private long [] ultimaVista;
	private CmdProxy cmdProxy;
	private EstimadorDistancia estDist;
	private long ultimaObs;
	private Object lockMarcas;

	public VisionBh(Robot r, CmdProxy cmdProxy){
		this.r = r;
		r.initCam();
		det = r.getDetect();
		this.cmdProxy = cmdProxy;

		estDist = r.getEstimadorDist();
		
		ultimaObs = 0;

		marcas = null;
		lockMarcas = new Object();
		ultimaVista = new long[6];
		for(int i = 0; i < ultimaVista.length; i++)
			ultimaVista[i] = 0;
	}

	@Override
	public boolean takeControl() {
		
//		if(System.currentTimeMillis() - ultimaObs < ESPERA_OBSERVS)
//			return false;
		
		if(marcas == null || marcas.isEmpty())
			marcas = det.detectarFusion();

		if (marcas.isEmpty())
			return false;
		else {
			boolean res = otroIdNoVist(marcas, ultimaVista);
			
			// Las marcas no me sirven, las elimino
			if(!res){
				synchronized (lockMarcas) {
					marcas = null;
					return false;
				}
				
			}

			return true;
		}


	}

	private boolean otroIdNoVist(List<ObservMarca> marcas, long[] ultimaVista) {		
		if (marcas == null)
			return false;
		
		boolean noVista = false;
		long currTime = System.currentTimeMillis();
		int i = 0;
		while(!noVista && i < marcas.size()){
			noVista = currTime - ultimaVista[marcas.get(i).getId() - 1] > ESPERA_VISTA;
			i++;
		}

		return noVista;
		
	}

	@Override
	public void action() {
		System.out.println("Action vision");

		// Espero para estabilizar la camara
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Creo una version local para que no me afecte posibles llamadas al take control
		List<ObservMarca> marcasLocal;
		synchronized (lockMarcas) {
			if(marcas == null)
				return;
			
			marcasLocal = marcas;
		}
		

		
		

		System.out.println("Vision parte 1");
		// Observo MAXINTENTOS / 2 veces
		List<ObservMarca> quietas = observar(marcasLocal);

		// Clasifico por id
		List<List<ObservMarca>> porId = new ArrayList<List<ObservMarca>>();
		
		clasificar(marcasLocal, quietas, porId);
		
		System.out.println("Vision parte 2");
		for(ObservMarca m : marcasLocal){
			int x = m.getCenter().x;
			if(x > TAM_PANTALLA / 2)
				r.getBrazo().rotate(-15);
			else
				r.getBrazo().rotate(15);

			Delay.msDelay(200);
			
			List<ObservMarca> enfocadas = observar(marcasLocal);
			clasificar(marcasLocal, enfocadas, porId);

			if(marcasLocal.isEmpty()) break;
			
			if(x > TAM_PANTALLA / 2)
				r.getBrazo().rotate(15);
			else
				r.getBrazo().rotate(-15);
		}

		System.out.println("Vision parte 3");
		long currTime = System.currentTimeMillis();
		for(List<ObservMarca> observs : porId){
			if(observs.size() > 0){
				System.out.println("lista de tam " + observs.size());
				if(observs.size() >= MINOBSERV){
					List<ObservMarca> dists = new ArrayList<ObservMarca>();
					for(ObservMarca m : observs){
						int i = 0;
						while(i < dists.size() && m.getDistancia() < dists.get(i).getDistancia())
							i++;
						dists.add(i, m);
					}

					int media = dists.size() % 2 == 0 ? dists.size() / 2 : dists.size() / 2 + 1;

					Sound.beepSequenceUp();
					System.out.println("Marca det " + dists.get(media).getId());
					// Update del cmd
					updateCmd(dists.get(media));
					ultimaVista[observs.get(0).getId() - 1] = currTime;
				} else {
					// Si no vi ninguno, espero un sexto de la espera
					ultimaVista[observs.get(0).getId() - 1] = currTime - 5 * ESPERA_VISTA / 6;
				}
			}
		}
		
		System.out.println("Vision marcas en null");
		marcas = null;

		ultimaObs = currTime;
				
	}

	private void clasificar(List<ObservMarca> orig,
			List<ObservMarca> nuevas, List<List<ObservMarca>> porId) {
		// Clasifico por id
		while(!nuevas.isEmpty()){
			List<ObservMarca> mismoId = new ArrayList<ObservMarca>();
			ObservMarca primera = nuevas.get(0);
			mismoId.add(primera);
			
			for(ObservMarca om : nuevas)
				if(om.getId() == primera.getId() && om != primera){
					mismoId.add(om);
				}
			
			for(ObservMarca om : mismoId)
				nuevas.remove(om);
						
			// Busco una lista que ya este
			int i = 0;
			while(i < porId.size() && ! porId.get(i).isEmpty() 
					&& porId.get(i).get(0).getId() != primera.getId())
					i++;
			// Si esta agrego a esa, si no agrego la lista
			if(i < porId.size())
				porId.get(i).addAll(mismoId);
			else
				porId.add(mismoId);
		}

		// Saco las marcas que corresponden a las que ya observe MINOBSERV veces
		for(List<ObservMarca> listaId : porId){
			if(listaId.size() >= MINOBSERV){
				List<ObservMarca> aBorrar = new ArrayList<ObservMarca>();
				for(int i = 0; i < orig.size(); i++)
					if(orig.get(i).getId() == listaId.get(0).getId()){
						aBorrar.add(orig.get(i));
						System.out.println("marca id " + orig.get(i).getId() + " removida");
					}
				orig.removeAll(aBorrar);
				
			}
		}
		
		
	}

	private List<ObservMarca> observar(List<ObservMarca> orig) {
		List<ObservMarca> res = new ArrayList<ObservMarca>();
		int observadas = 0;
		for(int i = 0; i < MAXINTENTOS / 2 && observadas  <= MINOBSERV * orig.size(); i++){
			List<ObservMarca> ms = det.detectarFusion();
			observadas += ms.size();
			res.addAll(ms);
//			Delay.msDelay(20);
		}
		
		for(ObservMarca om : res){
			float dist = (float) estDist.estimar(om);
			om.setDistancia(dist);
			
			float ang = (float)r.getEstAng().estimar(om);
			ang += r.getBrazo().getTachoCount() / INCREMENTO_DENTADAS;
			om.setAngulo(ang);
			System.out.println("Marca de id " + om.getId());
//			Sound.beep();
		}
		
//		System.out.println(res.size() + " marcas observadas");

		return res;
	}

	private void updateCmd(ObservMarca marca) {
		// Agrego el comando a la cola
		// Antes genero un movimiento intermedio para que el update se haga sobre algo actualizado
		// No se hace porque ya se paro para ver la marca
		//		pmp.interruptMove();
		// Mando comando al proxy
		if(cmdProxy != null)
			cmdProxy.push(new UpdateCmdMarcas( 
					new MarcaMeassure(marca.getDistancia(), marca.getId(), marca.getAngulo())));
	}

	@Override
	public void suppress() {
		System.out.println("Supress vision");
	}

}
