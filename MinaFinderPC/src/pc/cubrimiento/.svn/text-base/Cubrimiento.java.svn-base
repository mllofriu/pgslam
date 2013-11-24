package pc.cubrimiento;

import java.awt.Point;
import java.util.Scanner;

import pc.comm.Comunication;
import pc.comm.DoraIntegrator;
import pc.comm.PartUpdater;
import pc.comm.WPUpdater;
import pc.logger.Logger;
import pc.particulas.CamMeassureUpdater;
import pc.particulas.ParticleFrame;
import pc.particulas.SocketCliCom;
import brick.comm.Mensaje;
import brick.particulas.Command;
import brick.particulas.MCLParticleSet;
import brick.particulas.mapa.IEEERecorridaMap;
import brick.particulas.mapa.MapaIEEE;
import brick.particulas.mapa.MapaMarcasLineas;
import brick.utils.Constantes;
import brick.utils.Observable;
import brick.utils.Observer;
/***
 * Clase que establece una conexión bluetooth con el robot, abre un input stream e inicializa
 * y mantiene el dibujo del mapa sobre el cual están las partículas que son enviadas por el robot
 * @author fede
 *
 */

public class Cubrimiento{


	private static final long LARGOPRUEBA = 60 * 100 * 1000;
	private static final long LOGTIME = 100;

	public static void main(String[] args) {
		Cubrimiento pl = new Cubrimiento();
		pl.setup();

		pl.medirCubrimiento();
	}

	private DoraIntegrator dora;
	private boolean[][] cubierto;
	private Logger log;
	private long lastLog;
	private long time;

	private void medirCubrimiento() {
		
		while(System.currentTimeMillis() - time < LARGOPRUEBA){
			while(!dora.isFound())
				Thread.yield();
			
			Point p = new Point(dora.getX(), dora.getY()); 
			//			System.out.println("dora " + dora.getX() + " " + dora.getY());
			for(int i = (p.x - Constantes.ANCHOCOBERTURA/2) /Constantes.PRECISION_GRILLA;
					i < (p.x + Constantes.ANCHOCOBERTURA/2) / Constantes.PRECISION_GRILLA; i++)
				for(int j = (p.y - Constantes.ANCHOCOBERTURA/2) /Constantes.PRECISION_GRILLA;
						j < (p.y + Constantes.ANCHOCOBERTURA/2) / Constantes.PRECISION_GRILLA; j++)
					if(i > 0 && j > 0 && i < cubierto.length && j < cubierto[i].length){
						cubierto[i][j] = true;
						//						System.out.println(i + " " + j + " impresos");
					} else {
						//						System.out.println("No imprime " + i + " " + j);
					}

			// Cuento los ocupados
			int ocupados = 0;
			for (int i = 0; i < cubierto.length; i++)
				for(int j = 0; j < cubierto[i].length; j++)
					if(cubierto[i][j])
						ocupados++;
			log.addLog((System.currentTimeMillis() - time) + ",posdora,"
					+ dora.getX() + "," + dora.getY() + "," + ocupados);

			try {
				Thread.sleep(LOGTIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int ocupados = 0;
		for (int i = 0; i < cubierto.length; i++)
			for(int j = 0; j < cubierto[i].length; j++)
				if(cubierto[i][j])
					ocupados++;
		System.out.println(ocupados / (cubierto.length * cubierto[0].length));
	}

	public void setup(){
		System.out.println("Ingrese el nombre del archivo donde grabar los comandos: ");
		Scanner sc = new Scanner(System.in);
		String filename = sc.next();
		log = new Logger(filename);
		time = System.currentTimeMillis();

		//		LineMap map = MapaIEEE.getIEEEMap();
		MapaMarcasLineas map = new IEEERecorridaMap();

		cubierto = new boolean[(int) (MapaIEEE.getAncho()/Constantes.PRECISION_GRILLA)]
				[(int) (MapaIEEE.getLargerSide() / Constantes.PRECISION_GRILLA)];
		System.out.println("Lenght " + cubierto.length + " " + cubierto[0].length);
		for (int i = 0; i < cubierto.length; i++)
			for(int j = 0; j < cubierto[i].length; j++)
				cubierto[i][j] = false;



		// Inicializo las particulas vacias
		dora = new DoraIntegrator();
		//		dora = null;
		MCLParticleSet mclps;
//		if(Constantes.slam)
//			mclps = new MCLParticleSet();
//		else
//			mclps = null;
		mclps = new MCLParticleSet();
		
		
		ParticleFrame frame = new ParticleFrame(map, mclps, dora);

		//		Comunication com = PCCom.getInstance();
		final Comunication com;
//		if(Constantes.slam){
			com = SocketCliCom.getInstance();
//			com = null;
			frame.setCom(com);
//		}

		frame.setCubrimiento(cubierto);

//		if (Constantes.slam){
			PartUpdater part = new PartUpdater(mclps, com, log, time);	
			CamMeassureUpdater obsUpdater = new CamMeassureUpdater(frame, com, log, time);
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

			new Thread(new Runnable() {

				@Override
				public void run() {
					while(true)
						com.recibir();			
				}
			}).start();

//		} 


		lastLog = 0;
	}



}
