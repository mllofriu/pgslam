package brick.pruebas;

import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.Sound;

import brick.marcas.ObservMarca;
import brick.utils.Robot;

public class TVelocidad {

	private static final int MAX_VECES_VISTA = 2;
	private static int[] vecesVista;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot r = new Robot();
		r.initCam();

		vecesVista = new int[6];
		for(int i = 0; i < vecesVista.length; i++)
			vecesVista[i] = 0;

		while(!Button.ENTER.isPressed()){
//			long init = System.currentTimeMillis();
			test(r);
//			System.out.println(System.currentTimeMillis() - init);
			Sound.beep();
		}


	}

	private static boolean test(Robot r) {
		List<ObservMarca> marcas = null;

		if(marcas == null || marcas.isEmpty())
			marcas = r.getDetect().detectar();

		//		lastCheck = System.currentTimeMillis();

		if (marcas.isEmpty())
			return false;
		else {
			boolean res = otroIdNoVist(marcas, vecesVista);
			// Las marcas no me sirven, las elimino
			if(!res)
				marcas = null;
			return res;
		}
	}

	private static boolean otroIdNoVist(List<ObservMarca> marcas, int[] vecesVista) {		
		boolean noVista = false;
		int i = 0;
		while(!noVista && i < marcas.size()){
			noVista = vecesVista[marcas.get(i).getId() - 1] < MAX_VECES_VISTA;
			i++;
		}

		return noVista;
	}

}
