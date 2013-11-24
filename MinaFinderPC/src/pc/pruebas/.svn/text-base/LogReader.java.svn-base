package pc.pruebas;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import brick.marcas.Detector;
import brick.marcas.ObjCamara;
import brick.marcas.ObservMarca;

public class LogReader extends Thread implements Reader {
	
	private static final long SLEEP = 100;
	private BufferedReader log;
	private CanvasCamara canvas;
	private boolean actualizar;

	public LogReader(BufferedReader log, CanvasCamara canvas, boolean actualizar){
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
		try {
			while(true){
				Detector det = new Detector();
				LectorObjCam lect = new LectorObjCam();
				
				if(actualizar){					
					String line = log.readLine();
					List<ObjCamara> objs = lect.leerObjetos(line);
//					line = log.readLine();
//					objs.addAll(lect.leerObjetos(line));
					canvas.setObjsCam(det.filter(objs));
					List<ObservMarca> marcas = det.detectar(objs);
					canvas.setMarcas(marcas);
					
					canvas.repaint();
					if(!marcas.isEmpty()){
						Thread.sleep(SLEEP*10);
					} else 
						Thread.sleep(SLEEP);
				}
			}
		} catch (FileNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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
