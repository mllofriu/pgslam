import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TestInsoportable {

	private static String nombre = "Riera";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Por favor ingrese su nombre y apellido");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String line = bufferedReader.readLine();
		
		if (line.contains(nombre)){
			System.out.println("Usted es insoportable");
		} else {
			System.out.println("Usted es una persona comun, felicitaciones");
		}

	}

}
