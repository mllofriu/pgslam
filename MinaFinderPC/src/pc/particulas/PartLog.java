package pc.particulas;

import pc.comm.Comunication;
import pc.comm.DoraIntegrator;
import pc.comm.PartUpdater;
import pc.comm.WPUpdater;
import brick.comm.Mensaje;
import brick.particulas.Command;
import brick.particulas.MCLParticleSet;
import brick.particulas.mapa.IEEERecorridaMap;
import brick.particulas.mapa.MapaMarcasLineas;
import brick.utils.Observable;
import brick.utils.Observer;
/***
 * Clase que establece una conexión bluetooth con el robot, abre un input stream e inicializa
 * y mantiene el dibujo del mapa sobre el cual están las partículas que son enviadas por el robot
 * @author fede
 *
 */

public class PartLog{

	public static void main(String[] args) {
		PartLog pl = new PartLog();
		pl.testPC();
	}

	public void testPC(){
//		LineMap map = MapaIEEE.getIEEEMap();
		MapaMarcasLineas map = new IEEERecorridaMap();
		
		// Inicializo las particulas vacias
		DoraIntegrator dora;
		//			dora = new DoraIntegrator();
		dora = null;
		MCLParticleSet mclps = new MCLParticleSet();
		ParticleFrame frame = new ParticleFrame(map, mclps, dora);
//		Comunication com = PCCom.getInstance();
		Comunication com = SocketCliCom.getInstance();
		frame.setCom(com);
		
		PartUpdater part = new PartUpdater(mclps, com);	
		CamMeassureUpdater obsUpdater = new CamMeassureUpdater(frame, com);
		WPUpdater wpUp = new WPUpdater(frame, com);
		
		com.addObserver(new Observer(){
			@Override
			public void update(Observable o, Object arg) {
				Mensaje mensaje = (Mensaje) arg;
				if(mensaje.getId() == Comunication.IDCOM){
					Command cmd = Command.parseCommand(mensaje.getStr());
					System.out.println(cmd.toString());
				}
			}
		});

		while(true)
			com.recibir();		
	}



}
