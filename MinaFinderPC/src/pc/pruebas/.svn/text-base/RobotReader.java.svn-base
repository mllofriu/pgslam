package pc.pruebas;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import brick.marcas.Detector;
import brick.marcas.ObjCamara;

public class RobotReader extends Thread implements Reader {

	private BufferedWriter log;
	private CanvasCamara canvas;
	private boolean actualizar;

	public RobotReader(BufferedWriter log, CanvasCamara canvas, boolean actualizar){
		this.log = log;
		this.canvas = canvas;
		this.actualizar = actualizar;
	}

	public boolean isActualizar() {
		return actualizar;
	}

	public void setActualizar(boolean actualizar) {
		this.actualizar = actualizar;
	}

	@Override
	public void run() {
		NXTComm nxtComm;
		try {
//			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
//			NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH,"proy","00:16:53:10:D5:05");
//			System.out.println("Abriendo conexion");
//			nxtComm.open(nxtInfo);
			//			NXTInfo info = new NXTInfo(NXTCommFactory.BLUETOOTH, "mina", "00:16:53:05:C1:A6");
			//			nxtComm.open(info);
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			NXTInfo[] infos = nxtComm.search("proy", NXTComm.PACKET);
			nxtComm.open(infos[0]);
			System.out.println("Conexion establecida");
			DataInputStream dis = new DataInputStream(nxtComm.getInputStream());
			Detector det = new Detector();
			LectorObjCam lect = new LectorObjCam();
			Thread.sleep(2000);



			System.out.println("Mensaje inicial: " + readLine(dis));
			while(true){
				String line = readLine(dis);
				//				String line = dis.readLine();
				log.write(line + "\n");

				if(actualizar){
					if(lect.isObjsLine(line)){
						canvas.emtpyObjsMarcas();
						List<ObjCamara> objs = lect.leerObjetos(line);
						canvas.setObjsCam(objs);
						canvas.setMarcas(det.detectar(objs));		
					} else if (lect.isMarcaLine(line)){
						canvas.setMarcas(lect.leerMarcas(line));
					} else {
						canvas.emtpyObjsMarcas();
					}
					canvas.repaint();
					System.out.println("Linea: " + line);
				}
			}
		} catch (NXTCommException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//		} catch (IOException e) {
			//			// TODO Auto-generated catch block
			//			e.printStackTrace();
		} catch (FileNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String readLine(DataInputStream dis) {
		String s = "";
		try {
			char c = dis.readChar();
			while(c != '\n'){
				s += c;
				c = dis.readChar();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;	

	}


}
