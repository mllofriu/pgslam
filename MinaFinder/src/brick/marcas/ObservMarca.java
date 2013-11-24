package brick.marcas;


import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



public class ObservMarca implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3813773868106953548L;
	private List<ObjCamara> objs;
	private int id;
	private float prob; 
	private float distancia;
	private float angulo;
	private Rectangle rect;
	
	public ObservMarca(List<ObjCamara> objs, int id, float prob, float distancia, float angulo) {
		super();
		this.objs = objs;
		this.id = id;
		this.prob = prob;
		this.distancia = distancia;
		this.angulo = angulo;
		
		if(objs != null)
			rect = getRFromObj();
		
	}

	public ObservMarca(List<ObjCamara> objs, int id, float prob, float distancia) {
		this(objs, id, prob, distancia, 0);

	}
	
	public float getDistancia() {
		return distancia;
	}
	
	public float getAngulo(){
		return this.angulo;
	}
	
	public void setAngulo(double angulo){
		this.angulo = (float) angulo;
	}

	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}

	public ObservMarca(List<ObjCamara> objs, int id, float prob) {
		super();
		this.objs = objs;
		this.id = id;
		this.prob = prob;
		
		if(objs != null)
			rect = getRFromObj();
	}
	public ObservMarca(List<ObjCamara> objs, int id) {
		super();
		this.objs = objs;
		this.id = id;
		
		rect = getRFromObj();
	}
	public ObservMarca(ObjCamara o1, ObjCamara o2, ObjCamara o3, float verosimil) {
		objs = new ArrayList<ObjCamara>();
		objs.add(o1);
		objs.add(o2);
		objs.add(o3);

		id = calcId(o1,o2,o3);

		this.prob = verosimil;
		
		if(objs != null)
			rect = getRFromObj();
		
		this.distancia = calcularDist(getR().getBounds().height);
	}
	public ObservMarca(int x, int y, int w, int h, int id, float dist, float prob) {
		this.rect = new Rectangle(x,y,w,h);
		this.id = id;
		this.distancia = dist;
		this.prob = prob;
	}

	public ObservMarca(ObjCamara objCamara, ObjCamara objCamara2,
			ObjCamara objCamara3, float prob2, float dist) {
		this(objCamara, objCamara2, objCamara3, prob2);
		this.distancia = dist;
	}

	private ObservMarca() {
		// TODO Auto-generated constructor stub
	}

	private float calcularDist(int height) {
		return 0.014633f * height * height - 2.9883f * height + 192.84f;
	}
	
	private int calcId(){
		return calcId(objs.get(0), objs.get(1), objs.get(2));
	}

	private int calcId(ObjCamara o1, ObjCamara o2, ObjCamara o3) {
		ObjCamara objs[] = new ObjCamara[3];
		objs[o1.getColor()] = o1;
		objs[o2.getColor()] = o2;
		objs[o3.getColor()] = o3;
		
		if(objs[0].getCentro().y > objs[1].getCentro().y){
			if(objs[0].getCentro().y > objs[2].getCentro().y){				
				if(objs[1].getCentro().y > objs[2].getCentro().y){
					return 1;
				} else {
					return 2;
				}
			} else {
				return 3;
			}
		} else {
			if(objs[1].getCentro().y > objs[2].getCentro().y){
				if(objs[0].getCentro().y > objs[2].getCentro().y){
					return 4;
				} else {
					return 5;
				}
			} else {
				return 6;
			}
		}
	}
	public List<ObjCamara> getObjs() {
		return objs;
	}
	public void setObjs(List<ObjCamara> objs) {
		this.objs = objs;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Point getCenter(){
		Point res = new Point(0, 0);
		for (ObjCamara o : objs){
			Point c = o.getCentro();
			res.x += c.x;
			res.y += c.y;
		}

		res.x /= objs.size();
		res.y /= objs.size();

		return res;
	}
	public float getProb() {
		return prob;
	}
	public void setProb(float prob) {
		this.prob = prob;
	}
	public Rectangle getR() {
		return this.rect;
	}
	
	private Rectangle getRFromObj(){
		// TODO: tomar en cuenta todas los cubos
		
		// Selecciono altura media
		List<Integer> hs = new ArrayList<Integer>(3);
		for(int i = 0; i < objs.size(); i++){
			int j = 0;
			while(j < hs.size() && hs.get(j) < objs.get(i).getR().height)
				j++;
			
			hs.add(j, (int) objs.get(i).getR().height);
		}
			
		int extra = 5;
		int width = (int) (objs.get(0).getR().width + extra);
//		int heigth = objs.get(0).getR().height * 3 + extra;
		int heigth = hs.get(1) * 3 + extra;
		return new Rectangle(getCenter().x - width / 2, getCenter().y - heigth / 2, width, heigth);
	}

	public int getArea() {
//		int area = 0;
//		for(ObjCamara o : objs)
//			area += o.getR().width * o.getR().height;
//		return area;
		List<Integer> areas = new ArrayList<Integer>();
		
		for(ObjCamara o : objs){
			int i = 0;
			while(o.getArea() < areas.get(i))
				i++;
			areas.add(o.getArea(), i);
		}
		
		// Mediana
		return areas.get(1);
			
			
	}
	
	public String toString(){
		String res = "";
		for(ObjCamara o : objs)
			res += o.toString() + ",";
		res += prob + "," + distancia;
		return res;
	}
	
	public static ObservMarca fromString(String line){
		ObservMarca res = new ObservMarca();
		StringTokenizer st = new StringTokenizer(line,",");
		String [] toks = new String[5];
		// Para cada cuadrado
		res.objs = new ArrayList<ObjCamara>();
		for(int i = 0; i < 3; i++){
			res.objs.add(new ObjCamara(new Rectangle(
					Integer.parseInt(st.nextToken()),
					Integer.parseInt(st.nextToken()),
					Integer.parseInt(st.nextToken()),
					Integer.parseInt(st.nextToken())), 
					Integer.parseInt(st.nextToken())));
		}
		res.prob = Float.parseFloat(st.nextToken());
		res.distancia = Float.parseFloat(st.nextToken());
		res.id = res.calcId();
		return res;
	}

	public int getMedianTam() {
		int tam1 = (int) objs.get(0).getR().height;
		int tam2 = (int) objs.get(1).getR().height;
		int tam3 = (int) objs.get(2).getR().height;
		
		if(tam1 < tam2)
			if(tam2 < tam3)
				return tam2;
			else if (tam1 < tam3)
				return tam3;
			else
				return tam1;
		else
			if(tam1 < tam3)
				return tam1;
			else if (tam3 > tam2)
				return tam3;
			else
				return tam2;
				
	}

	public double getLowerPixel() {
		// Los pixel crecen hacia abajo
//		double lowPixel = 0;
//		for(int i = 0; i < 3; i++)
//			if(objs.get(i).getR().getMinY() > lowPixel)
//				lowPixel = objs.get(i).getR().getMaxY();
		
		// Hallo el i con altura media altura media
		List<Integer> hs = new ArrayList<Integer>();
		for(int i = 0; i < 3; i++){
			int index = 0;
			while(index < hs.size() && objs.get(i).getR().height > objs.get(hs.get(index)).getR().height)
				index++;
			hs.add(index, i);
		}
		
		int indexMedia = hs.get(1);
		
		// Hallo donde esta ese objcam, ordenando por centro
		List<Integer> ys = new ArrayList<Integer>();
		for(int i = 0; i < 3; i++){
			int index = 0;
			while(index < ys.size() && objs.get(i).getR().getCenterY() > objs.get(hs.get(index)).getR().getCenterY())
				index++;
			ys.add(index, i);
		}
		
		int ordenMedia = 0;
		for(int i = 0; i < 3; i++){
			if(ys.get(i) == indexMedia){
				ordenMedia = i;
				break;
			}
		}
		
		// El lower pixel es el centro del de la media
		// Mas media altura
		// Mas una altura por cada posicion mas arriba que este del cero (el cero es el de mas abajo)
		double lowPixel = objs.get(indexMedia).getR().getCenterY() + objs.get(indexMedia).getR().getHeight() / 2 +
				ordenMedia * objs.get(indexMedia).getR().getHeight();
		
		return lowPixel;
	}
}
