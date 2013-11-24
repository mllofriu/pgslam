package pc.pruebas;



import lejos.geom.Point;
import lejos.util.Matrix;

public class Marca {

	@Override
	public boolean equals(Object aOther) {
		if (aOther instanceof Marca)
			return ((Marca)aOther).id == id;
		else
			return false;
	}

	private Point media;
	private Matrix cov;
	private int id;
	
	private double initialCov = 10000;
	
	Marca(int id,Point media, Matrix cov){
		this.id = id;
		this.media = media;
		//this.cov = cov.copy();
		this.cov = cov;
		System.out.println("----------Matrix covarianza -------------------------");	
		this.cov.set(0, 0, initialCov);
		this.cov.set(0, 1, 0);
		this.cov.set(1, 0,0);
		this.cov.set(1, 1, initialCov);		
		cov.print(null);
		
	}

	public Marca(int id) {
		this(id, new Point(0,0), new Matrix(2,2));
	}
	
	public Marca(int id, int x, int y, int dist, float ang, Matrix covMeasure) {
		this(id, new Point(0,0), new Matrix(2,2));
		//inicializo la matriz de covarianza
		System.out.println("----------Matrix covarianza -------------------------");	
		this.cov.set(0, 0, initialCov);
		this.cov.set(0, 1, 0);
		this.cov.set(1, 0,0);
		this.cov.set(0, 0, initialCov);		
		cov.print(null);
				
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getMedia() {
		return media;
	}

	public void setMedia(Point media) {
		this.media = media;
	}

	public Matrix getCov() {
		return cov;
	}

	public void setCov(Matrix cov) {
		this.cov = cov;
	}
	
	public void update(int cx, int cy, int dist, float ang, Matrix covMesure){
		//usando la prior y la observacicon calculo el nuevo (x,y)
		
		//matrix idendidad
		System.out.println("----------matriz identidad -------------------------");	
		Matrix I = new Matrix(2,2);
		I.set(0, 0, 1);
		I.set(0, 1, 0);
		I.set(1, 0, 0);
		I.set(1, 1, 1);
		I.print(null);
		
		System.out.println("----------matriz H -------------------------");		
		Matrix H = new Matrix(2,2);
		H.set(0, 0, 1);
		H.set(0, 1, 0);
		H.set(1, 0, 0);
		H.set(1,1,1);
		H.print(null);
		
		System.out.println("----------matriz z observaciones -------------------------");	
		Matrix z = new Matrix(2,1); 
		z.set(0, 0, cx);
		z.set(1, 0, cy);
		z.print(null);
		
		System.out.println("----------matriz x prior----------------------");	
		Matrix x = new Matrix(2,1);
		x.set(0, 0, this.media.getX());
		x.set(1, 0, this.media.getY());
		x.print(null);
		
		
		Matrix error =  z.minus(H.times(x));
		Matrix S = H.times(cov.times(H.transpose())).plus(covMesure);
		Matrix K = cov.times(H.transpose().times(S.inverse()));
		x = x.plus(K.times(error));  //chequear que se hagan bien los cálculos
		this.media.setLocation(x.get(0, 0), x.get(1, 0));
		System.out.println("----------matriz x -------------------------");	
		x.print(null);
		
		cov = (I.minus(K.times(H))).times(cov);
		System.out.println("----------matriz cov -------------------------");	
		cov.print(null);
		
		
	}
}
