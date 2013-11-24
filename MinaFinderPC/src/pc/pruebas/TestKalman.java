package pc.pruebas;

import lejos.util.Matrix;


public class TestKalman {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("creamos una marca");
		Marca m = new Marca(0);
		Matrix R = new Matrix(2,2);
		System.out.println("----------matriz R -------------------------");	
		R.set(0, 0, 1);
		R.set(1, 0, 0);
		R.set(0,1,0);
		R.set(1, 1, 1);
		R.print(null);

		m.update(1,5, 10, 0, R );
		for (int i = 0; i<100;i++){
			m.update(3,7,10, 4, R);
		}
		
		System.out.println("coseno 90 = " + Math.cos(90) + " coseno 30= " + Math.cos(30));

	}
}
