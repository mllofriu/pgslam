package brick.utils;

import lejos.util.Matrix;

public class MatrixCovar extends MatrixFloat {
	
	public MatrixCovar(float covar){
		super(2,2);
		//inicializo la matriz de covarianza del error en la medida
		set(0, 0, covar);
		set(1, 0, 0);
		set(0,1, 0);
		set(1, 1, covar);
	}
}
