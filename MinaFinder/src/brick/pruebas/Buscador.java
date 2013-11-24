package brick.pruebas;

import java.util.ArrayList;
import java.util.List;

import brick.marcas.Camara;
import brick.marcas.Detector;
import brick.marcas.ObservMarca;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.OpticalDistanceSensor;

public class Buscador {

	private static final int VEL_BRAZO = 50;
	private static final int MAX_INTENTOS = 30;
	private static final int CENTRO_CAM = 176 / 2;
	private static final int THRS_CENTRO_CAM = 10;
	private static final int MAX_ANGLE = 120;
	private static final int AJUSTE_X = 0;
	private static final int CANT_MEDIDAS_DIST = 50;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Buscador b = new Buscador();
		ObservMarca m = b.buscar();
		Sound.beepSequenceUp();
		
		System.out.println("Distancia: " + m.getDistancia());
		System.out.println("Area: " + m.getArea());
		Button.ENTER.waitForPress();
		
		Button.ENTER.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				System.exit(0);
			}

			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub

			}
		});
	}

	private NXTRegulatedMotor brazoCam;
	private Detector det;
	private Camara camara;
//	private OpticalDistanceSensor distancia;

	public Buscador(){
		camara = new Camara();
		det = new Detector();
		brazoCam = Motor.C;
		brazoCam.setSpeed(VEL_BRAZO);
		brazoCam.resetTachoCount();
//		distancia = new OpticalDistanceSensor(SensorPort.S3);

		
	}

	public ObservMarca buscar(){
		final int buscando = 1;
		final int centrando = 2;
		int estado = buscando;
		boolean forwarding = false;
		int marcaEncontrada = 0;

		// Salgo solo con el return
		while(true){
			if (estado == buscando){
				brazoCam.setSpeed(VEL_BRAZO);
				// Avanzo hasta ver una marca
				// Me mantengo en un rango
				if (brazoCam.getTachoCount() < -MAX_ANGLE / 2){
					brazoCam.forward();
					forwarding = true;
				} else if (brazoCam.getTachoCount() > MAX_ANGLE / 2){
					brazoCam.backward();
					forwarding = false;
				} else if (forwarding) {
					brazoCam.forward();
					forwarding = true;
				} else {
					brazoCam.backward();
					forwarding = false;
				}
				List<ObservMarca> marcas;
				do{
					marcas = det.detectar(camara.getObjs());
//					System.out.println(brazoCam.getTachoCount());
				} while(marcas.isEmpty() 
						&& brazoCam.getTachoCount() > -MAX_ANGLE/2 && brazoCam.getTachoCount() < MAX_ANGLE/2);
				if (!marcas.isEmpty()){
					Sound.beep();
					marcaEncontrada = marcas.get(0).getId();
					estado = centrando;
					brazoCam.stop();
				}
			} else if (estado == centrando){
				// Mas despacio para centrar
				brazoCam.setSpeed(VEL_BRAZO / 10);
				ObservMarca m = getMarca(marcaEncontrada, MAX_INTENTOS);
				// Si no esta, vuelvo a buscar
				if (m == null)
					estado = buscando;
				else {
					// Decido hacia donde ir en funcion de donde esta
					int x = m.getCenter().x + AJUSTE_X;
					System.out.println("x: " + x);
					if (x < CENTRO_CAM - THRS_CENTRO_CAM)
						brazoCam.forward();
					else if(x > CENTRO_CAM + THRS_CENTRO_CAM)
						brazoCam.backward();
					else {
						m = getMarca(marcaEncontrada, MAX_INTENTOS);
						if (m == null)
							estado = buscando;
						else {
							brazoCam.stop();
//							m.setDistancia(getMedDist(distancia, CANT_MEDIDAS_DIST));
							return m;
						}
					}


				}

			}
		}
	}

	private int getMedDist(OpticalDistanceSensor distancia, int cantMedidasDist) {
		ArrayList<Integer> medidas = new ArrayList<Integer>(cantMedidasDist);
		for(int i = 0; i < cantMedidasDist; i++){
			// Insertar ordenada
			int j = 0;
			int dist = distancia.getDistance();
			while(medidas.size() > j && medidas.get(j) > dist)
				j++;
			medidas.add(j,dist);	
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return medidas.get(medidas.size() / 2 + medidas.size() % 2);
	}

	private ObservMarca getMarca(int marcaEncontrada, int maxIntentos) {
		List<ObservMarca> marcas;
		int intentos = 0;
		boolean tieneMarcaBuscada = false;
		int i;
		do{
			// Busco la marca que encontre en buscar
			marcas = det.detectar(camara.getObjs());
			for (i = 0; i < marcas.size() && !tieneMarcaBuscada; i++)
				tieneMarcaBuscada = marcas.get(i).getId() == marcaEncontrada;
			intentos++;
		} while(!tieneMarcaBuscada && intentos < MAX_INTENTOS);
		
		if (intentos >= MAX_INTENTOS)
			return null;
		else 
			return marcas.get(i-1);
	}

}
