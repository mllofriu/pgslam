package pc.pruebas;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumFormat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NumberFormat formatter = new DecimalFormat("0000");
		System.out.println(formatter.format(1)); 
		System.out.println(formatter.format(100));
		System.out.println(formatter.format(1000));
		System.out.println(formatter.format(999));
	}

}
