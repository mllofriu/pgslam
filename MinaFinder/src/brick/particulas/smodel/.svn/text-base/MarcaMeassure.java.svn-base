package brick.particulas.smodel;

import java.util.StringTokenizer;

public class MarcaMeassure extends Meassure {
	private float dist;
	private int idMarca;
	private float angle;

	public MarcaMeassure(float dist, int idMarca, float angle){
		this.dist = dist;
		this.idMarca = idMarca;
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getDist() {
		return dist;
	}

	public int getIdMarca() {
		return idMarca;
	}

	@Override
	public String toString() {
		return dist+","+idMarca+"," + angle;
	}

	public static MarcaMeassure fromString(StringTokenizer stok) {
		float dist = Float.parseFloat(stok.nextToken());
		int id = Integer.parseInt(stok.nextToken());
		float angle = Float.parseFloat(stok.nextToken());
		return new MarcaMeassure(dist, id, angle);
	}
}

