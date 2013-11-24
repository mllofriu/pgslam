package pc.pruebas;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import brick.marcas.ObservMarca;

public class TrainSetReader extends Thread implements Reader {
	
	private static final long SLEEP = 300;
	private BufferedReader log;
	private CanvasCamara canvas;
	private boolean actualizar;

	public TrainSetReader(BufferedReader log, CanvasCamara canvas, boolean actualizar){
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
			List<ObservMarca> marcas = new LinkedList<ObservMarca>();
			while(true){
				LectorMarcasTS lect = new LectorMarcasTS();
				
				if(actualizar){					
					String line = log.readLine();
					marcas.clear();
					marcas.add(lect.leerMarca(line));
					if(marcas.get(0).getProb() > -10){
						canvas.setObjsCam(marcas.get(0).getObjs());
						canvas.setMarcas(marcas);
						canvas.repaint();
						Thread.sleep(SLEEP);
					}
					
					
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
