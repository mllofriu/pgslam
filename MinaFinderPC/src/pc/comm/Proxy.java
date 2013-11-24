package pc.comm;

import pc.particulas.CmdProvider;
import brick.utils.Observable;
import brick.utils.Observer;
/***
 * Clase que actua como observer del mclline y se encarga de solicitar la serialización de las particulas
 * cuando mclline le ejecuta el método "update"
 * @author fede
 *
 */
public class Proxy implements Observer{

//	private CmdPoseProvider cmpp;
	private CmdProvider cmprovider;
	private Comunication com;
	private final int CAMBIOSENVIO = 1;
	private int cambios; // Mantengo un contador para enviar una particula cada CAMBIOSENVIO cambios
	

	public Proxy(CmdProvider cmprovider, Comunication com){
		this.cmprovider = cmprovider;
		this.com = com;		
		
		com.setMCL(cmprovider.getParticles());
		
		cambios = 0;
		
		// Envio inicial de las particulas
//		byte[] parts = cmprovider.getParticles().dumpParticles();
		this.com.enviar(Comunication.IDPART, "");
	}

	@Override
	public void update(Observable o, Object arg) {
		cambios ++;
		if(cambios % CAMBIOSENVIO == 0){
//			byte[] parts = cmprovider.getParticles().dumpParticles();
			this.com.enviar(Comunication.IDPART, "");
			cambios = 0;
		}
	}

}
