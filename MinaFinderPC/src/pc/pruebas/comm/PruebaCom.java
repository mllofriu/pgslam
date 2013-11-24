package pc.pruebas.comm;

import pc.comm.Comunication;
import pc.comm.PCCom;

public class PruebaCom {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
//			
			Comunication com = PCCom.getInstance();
			InputListener ip = new InputListener(com);
			while (true){
//				System.out.println(com.recibir());
				com.recibir();
				com.enviar(1, "compas puto");
			}

			//obtener las particulas
			//dibujarlas o almacenarlas
		}catch(Exception e){
			System.out.println("faul");
		}

	}

}
