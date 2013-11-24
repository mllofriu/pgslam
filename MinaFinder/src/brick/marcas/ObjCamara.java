package brick.marcas;


import java.awt.Point;
import java.awt.Rectangle;






public class ObjCamara {
	private Rectangle r;
	private int color;
	
	public ObjCamara(Rectangle r, int color) {
		super();
		this.r = r;
		this.color = color;
	}
	
	public Rectangle getR() {
		return r;
	}
	public void setR(Rectangle r) {
		this.r = r;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}

	public Point getCentro() {
		return new Point(r.x + r.width / 2, r.y + r.height / 2);
	}

	public int getArea() {
		return (int) (r.width * r.height);
	}
	
	public String toStringAnt(){
		return r.toString() + " " + color; 
	}
	
	public String toString(){
		String res = "";
		res += r.x + ",";
		res += r.y + ",";
		res += r.width + ",";
		res += r.height + ",";
		res += color;
		return res;
	}
	
}
