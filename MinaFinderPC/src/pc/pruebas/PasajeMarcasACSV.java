package pc.pruebas;

public class PasajeMarcasACSV {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LectorMarcasTS lect = new LectorMarcasTS();
		lect.pasarACSV("/mnt/datos/fing/proyecto/pgslamrepo/pruebasCamara/distancia/distanciaPro/marcas.txt",
				"/mnt/datos/fing/proyecto/pgslamrepo/pruebasCamara/distancia/distanciaPro/marcas.csv");
	}

}
