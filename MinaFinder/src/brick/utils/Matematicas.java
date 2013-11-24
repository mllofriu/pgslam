package brick.utils;

import java.util.ArrayList;

import lejos.geom.Point;

public class Matematicas {

	/***
	 * Retorna la pseudoprobabilidad de un valor de x para una gaussiana dada.
	 * @param mu
	 * @param var
	 * @param dist
	 * @return
	 */
	public static float g(float mu, float var, float dist) {
		float res = (float) ((1 / Math.sqrt(2 * Math.PI * var * var)) * Math.exp(- (dist - mu)*(dist - mu) / (2*var*var)));
		return res;
	}

	public static double gMulti(Point media, MatrixFloat var, Point x) {
		float[][] diffVals = {{x.x - media.x},{x.y - media.y}};
		MatrixFloat diff = new MatrixFloat(diffVals);
		float alpha = (float) (diff.transpose().times(var.inverse()).times(diff)).times(-0.5f).get(0, 0);
		float mult = (float) (1 / (2 * Math.PI * Math.sqrt(det(var)))) ;
		
		return mult * Math.exp(alpha);
	}

	private static double det(MatrixFloat var) {
		return var.get(0,0) * var.get(1,1) - var.get(1, 0) * var.get(0, 1);
	}

	public static float toRad(float f) {
		return (float) (f / 180 * Math.PI);
	}

	public static float angleDiff(float orig, float dest) {
		float ang1 = Math.abs(dest - orig);
		float ang2 = Math.abs(dest + 360 - orig);
		float ang3 = Math.abs(orig + 360 - dest);
		return Math.min(Math.min(ang1, ang2), ang3);
	}
	
	public static float angleDiffSig(float orig, float dest) {
		float[] angs = new float[3];
		
		angs[0] = dest - orig;
		angs[1] = dest + 360 - orig;
		angs[2] = -(orig + 360 - dest);
		
		ArrayList<Float> orden = new ArrayList<Float>();
		for(int i = 0; i < 3; i++){
			int index = 0;
			while(index < orden.size() && Math.abs(orden.get(index)) < Math.abs(angs[i]))
				index ++;
			orden.add(index, angs[i]);
		}
		
		return orden.get(0);
	}
}
