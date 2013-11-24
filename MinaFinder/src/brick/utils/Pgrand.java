package brick.utils;

import java.util.Random;


/***
 * Clase singleton que encapsula un objeto random inicializado con una
 * semilla de tiempo. El valor de la semilla es almacenado y puede ser consultado
 * en cualquier momento.
 * @author fede
 *
 ***/


public class Pgrand {
	private static Pgrand pgrand;
	private Random rand;
	private long seed;
	public Pgrand(){	
		seed = System.currentTimeMillis();
		rand = new Random(this.seed);		
	}
	
	public Pgrand getInstance(){
		if (pgrand == null){
			pgrand = new Pgrand();
		}
		return pgrand;
	}
	
	public long getSeed(){
		return this.seed;
	}
	
	public Random getRand(){
		return this.rand;
	}
}
