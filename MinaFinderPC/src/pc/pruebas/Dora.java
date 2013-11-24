package pc.pruebas;

import pc.comm.DoraIntegrator;

public class Dora {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DoraIntegrator di;
		di = new DoraIntegrator();
		while(true){
			if(di.isFound()) 
				System.out.println("x = " + di.getX() + " y = " + di.getY());
		}


	}
}
