package pc.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class PCCom extends Comunication implements Runnable{
	
	private static PCCom instance = null;
	
	public static PCCom getInstance(){
		if(instance == null)
			instance = new PCCom();
		return instance;
	}
	
	private PCCom(){
		super();
		
//		// En la pc implemento un thread de escucha para desentenderme del poleo
//		Thread recibidor = new Thread(new Runnable() {
//			public void run() {
//				System.out.println("Thread recibidor iniciado");
//				while (true){
//					recibir();
//				}
//			}
//		});
//		recibidor.setPriority(Thread.MAX_PRIORITY);
//		recibidor.start();
	}

	@Override
	public void connect() {
		System.out.println("PCCom: conectando");
		NXTComm nxtComm;
		try{
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH,"monty","00:16:53:12:67:D7");
			System.out.println("Abriendo conexion");
			nxtComm.open(nxtInfo);
			System.out.println("Conexion establecida");
			dis = new DataInputStream(nxtComm.getInputStream());
			dos = new DataOutputStream(nxtComm.getOutputStream());
		}catch(Exception e){
			System.out.println("error en la comunicacion");	
			e.printStackTrace();
		}
	}

	
	

}
