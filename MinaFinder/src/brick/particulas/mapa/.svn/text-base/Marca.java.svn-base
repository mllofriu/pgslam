package brick.particulas.mapa;



import lejos.geom.Point;
import brick.utils.Constantes;
import brick.utils.MatrixCovar;
import brick.utils.MatrixFloat;

public class Marca {

//	@Override
//	public boolean equals(Object aOther) {
//		if (aOther instanceof Marca)
//			return ((Marca)aOther).id == id;
//		else
//			return false;
//	}

	private float x, y;
	private MatrixFloat cov;
	private static final float[][] idVals = {{1,0},{0,1}};
	private static final MatrixFloat I = new MatrixFloat(idVals);
	
	private static final float initialCov = 10000f;
	private static final MatrixCovar initialCovM = new MatrixCovar(initialCov);
	
	public Marca(float x, float y, MatrixFloat cov){
		this.x = x;
		this.y = y;
		
		//copia limpia de la matriz de covarianza
		this.cov = cov.copy();
	}
/**
 * Creo una marca con un id definido, el punto inicial (0,0) y la matriz de covarianza con un valor bien grande
 * @param id
 */
	public Marca() {
		this(0f,0f, initialCovM);		
	}
	
	public Marca(float x, float y){
		this(x, y, initialCovM);
	}
	
//	/**
//	 * FIXME: este constructor creo q esta al pedo
//	 * @param id
//	 * @param x
//	 * @param y
//	 * @param dist
//	 * @param ang
//	 * @param covMeasure
//	 */
//	public Marca(int x, int y, int dist, float ang, MatrixFloat covMeasure) {
//		this(new Point(0,0), new MatrixFloat(2,2));
//		//inicializo la matriz de covarianza
////		System.out.println("----------Matrix covarianza constructor-------------------------");	
//		this.cov.set(0, 0, initialCov);
//		this.cov.set(0, 1, 0);
//		this.cov.set(1, 0,0);
//		this.cov.set(0, 0, initialCov);		
////		cov.print(null);
//				
//	}

	public Marca(Marca marca) {
		this(marca.x, marca.y, marca.cov);
	}
	
	public Marca(byte[] d) {
		x = Float.intBitsToFloat(byteArrayToInt(d[0],d[1],d[2],d[3]));
		y = Float.intBitsToFloat(byteArrayToInt(d[4],d[5],d[6],d[7]));
	}
	public static final int byteArrayToInt(byte b1, byte b2, byte b3, byte b4) {
		return (b1 << 24)
				+ ((b2 & 0xFF) << 16)
				+ ((b3 & 0xFF) << 8)
				+ (b4 & 0xFF);
	}
	
	public MatrixFloat getCov() {
		return cov;
	}

	public void setCov(MatrixFloat cov) {
		this.cov = cov;
	}
	
	public void update(float cx, float cy, MatrixFloat covMesure){
		//usando la prior y la observacicon calculo el nuevo (x,y)
		
//		System.out.println("----------matriz z observaciones -------------------------");	
		MatrixFloat z = new MatrixFloat(2,1); 
		z.set(0, 0, cx);
		z.set(1, 0, cy);
		
//		System.out.println("----------matriz x prior----------------------");	
		MatrixFloat X = new MatrixFloat(2,1);
		X.set(0, 0, x);
		X.set(1, 0, y);
		
		
		// Modelo de sensado es la identidad
		MatrixFloat error =  z.minus(I.times(X));
		MatrixFloat S = cov.times(I.transpose()).plus(covMesure);
		MatrixFloat K = cov.times(I.transpose().times(S.inverse()));
		X = X.plus(K.times(error));  
		x = X.get(0, 0);
		y = X.get(1, 0);
//		System.out.println("----------matriz x -------------------------");	
//		x.print(null);
		
		cov = (I.minus(K)).times(cov);
//		System.out.println("----------matriz cov -------------------------");	
//		cov.print(null);
		
		
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
	public Point getMedia(){
		return new Point(x,y);
	}
//	public void copy(Marca marca) {
//		x = marca.x;
//		y = marca.y;
//		cov.copy(marca.cov);
//	}
	public byte[] dump(int i) {
		int x = Float.floatToIntBits(this.x);
		int y = Float.floatToIntBits(this.y);
		byte[] res = {
				(byte)(i >>> 24),(byte)(i >>> 16),(byte)(i >>> 8),(byte)i,
				(byte)(x >>> 24),(byte)(x >>> 16),(byte)(x >>> 8),(byte)x,
				(byte)(y >>> 24),(byte)(y >>> 16),(byte)(y >>> 8),(byte)y};
		return res;
	}
}
