package pc.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import brick.comm.Mensaje;
import brick.particulas.MCLParticleSet;
import brick.utils.Constantes;
import brick.utils.Observable;

/*
 * Esta clase se encarga de encapsular la lectura y escritura en los canales de comunicación. Además encapsula el comportamiento
 * para que observers se notifiquen y les avisa cuando hay un dato para leer.
 * 
 * codigo operaciones
 * 00 - reserved
 * 01 - vienen particulas
 * 02 - terminar ejecucion test
 * 03 -
 * 04 -
 */
public abstract class Comunication extends Observable implements Runnable{

	protected DataInputStream dis;
	protected DataOutputStream dos;
	public static final int IDPART = 1;
	public static final int IDFIN = 2;
	public static final int IDMEDIDAHEADING = 3;
	public static final int IDMOVSTART = 4;
	public static final int IDMOVEND = 5;
	public static final int IDUPDATE = 6;
	public static final int IDCOM = 7;
	public static final int IDWAYPOINT = 8;
	private List<Mensaje> mensajes;
	private Mensaje particulas = null;
	private MCLParticleSet mclPartSet;
	// cantidad de particulas, cuatro floats, cantidad de marcas y 2 floats y un int por marca
	private static byte[] partsMsg = new byte[4 + (4 * 4 + 4 + Constantes.cantMarcas * 3 * 4) * Constantes.NUMPART];

	public Comunication(){
		mensajes = new ArrayList<Mensaje>();
		new Thread(this).start();
		connect();
	}
	
	public void setMCL(MCLParticleSet mclParticleSet){
		this.mclPartSet = mclParticleSet;
	}

	public void enviar(int id, String string){
		synchronized (mensajes) {
			Mensaje m = new Mensaje(id, string);

			// Optimizacion para no enconlar envios de particulas
			if(id == IDPART){
				if(particulas != null)
					mensajes.remove(particulas);
				particulas = m;
			}

			mensajes.add(m);
//			System.out.println(mensajes.size());
			mensajes.notify();
		}

	}

	public void enviar(int id, byte[] bytes){
		synchronized (mensajes) {
			Mensaje m = new Mensaje(id, bytes);

			// Optimizacion para no enconlar envios de particulas
			if(id == IDPART){
				if(particulas != null)
					mensajes.remove(particulas);
				particulas = m;
			}

			mensajes.add(m);
			System.out.println(mensajes.size());
			mensajes.notify();
		}

	}

	public synchronized void recibir(){
		byte [] data;
		try {
			int id = (int)dis.readInt();
//			System.out.println("Id: " + id);
			int len = dis.readInt();
			//			System.out.println(len + " bytes to read");
			data = new byte[len];
			int off = 0;
			while (len > 0){
				int read = dis.read(data, off, len);
				len = len - read;
				off += read;
				//				System.out.println("Quedan " + len + " bytes");
			}

			//			System.out.println("Comunication: llego data " + data);
			this.setChanged(); 
			this.notifyObservers(new Mensaje(id, data));
		} catch (IOException e) {
//			System.out.println("Comunicacion: error en la conexion al recibir");
//			e.printStackTrace();
			System.out.println("Comunicacion finalizada");
			System.exit(0);
		}

	}

	public void setDis(DataInputStream dis){
		this.dis = dis;
	}

	public void setDos(DataOutputStream dos){
		this.dos = dos;
	}

	@Override
	public void run() {
		while(true){
			boolean hayMensajes;
			synchronized (mensajes) {
				try {
					mensajes.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				hayMensajes = mensajes.size() > 0;
			}

//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
			while(hayMensajes){
				Mensaje m = null;

				try {
					synchronized (mensajes) {
						m = ((Mensaje)mensajes.remove(0));
						if(m.getId() == IDPART){
							particulas = null;
							mclPartSet.dumpParticles(partsMsg);
							m.setBytes(partsMsg);
						}
					}

					
					dos.writeInt(m.getId());
					dos.writeInt(m.getBytes().length);
//					System.out.println("Mandando " + m.getBytes().length + " bytes");
					dos.write(m.getBytes());
					dos.flush();

					synchronized (mensajes) {
						// Como soy el unico consumidor puedo darme este lujo
						hayMensajes = mensajes.size() > 0;
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					
				} 
			}
		}

	}

	public abstract void connect();

}
