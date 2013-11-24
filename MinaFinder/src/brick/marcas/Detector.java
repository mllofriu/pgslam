package brick.marcas;


import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lejos.util.Delay;

public class Detector {

	private static final float VAR_Y = .1f;
	private static final float VAR_H = .1f;
	private static final float VAR_W = .2f;
	private static final float VAR_X = .2f;
	private static final float MIN_VEROSIMIL = 0;
	private static final float HW_TOL = .9f;
	private static final double MIN_SIZE = 10;
	private static final double MAX_SIZE = 20;

	private Collection<Integer> marcasAnt;
	private DataOutputStream dos;
	private Camara cam;
	
	public Detector(){
		marcasAnt = new ArrayList<Integer>();
		dos = null;
	}
	
	public Detector(Camara c){
		marcasAnt = new ArrayList<Integer>();
		dos = null;
		this.cam = c;
	}
	
	public Detector(DataOutputStream dos){
		marcasAnt = new ArrayList<Integer>();
		this.dos = dos;
	}
	
	public List<ObservMarca> detectar(){
		return detectar(cam.getObjs());
	}
	
	public List<ObservMarca> detectarFusion(){
//		Delay.msDelay(Math.round(1./Camara.FRAMESSEC * 1000));
		List<ObjCamara> objs = cam.getObjs();
		Delay.msDelay(Math.round(1./Camara.FRAMESSEC * 1000));
		objs.addAll(cam.getObjs());
		return detectar(objs);
	}

	public List<ObservMarca> detectar(List<ObjCamara> observed){
		// Filtro
		List<ObjCamara> objs = filter(observed);
		
		
//		System.out.println(objs.size());
		List<ObservMarca> todas = new ArrayList<ObservMarca>();

		List<List<ObjCamara>> objsClasif = new ArrayList<List<ObjCamara>>();
		for(int i = 0; i < 3; i++)
			objsClasif.add(i,new ArrayList<ObjCamara>());
		for(ObjCamara o : objs){
			objsClasif.get(o.getColor()).add(o);
		}

		//		for(Integer i : objsClasif.keySet())
		//			for(ObjCamara o : objsClasif.get(i))
		//				System.out.println("Objeto de color " + o.getColor());

		// Si no estan los tres colores presentes retorno lista vacia
		boolean unoVacio = false;
		for(List<ObjCamara> l : objsClasif)
			unoVacio |= l.isEmpty();
		if (unoVacio){
			return todas;
		}
				
		

		// Busco el de menor cardinal
		int menorCardinal = 16;
		int colorMenor = 0;
		for(int i = 0; i < 3; i++)
			if(objsClasif.get(i).size() < menorCardinal){
				colorMenor = i;
				menorCardinal = objsClasif.get(i).size();
			} 

		// Encuentro la lista de los otros colores
//		List<List<ObjCamara>> otrosColores = new ArrayList<List<ObjCamara>>();
//		for(int i = 0; i < 3; i++)
//			if(i != colorMenor){
//				otrosColores.add(objsClasif.get(i));
//			} 
		// Busco todas las marcas pertinentes para cada uno de los objetos del grupo de menor cardinal
//		System.out.println("Probabilidades");
//		Sound.beep();
		for(ObjCamara o : objsClasif.get(colorMenor)){
			ObservMarca m = detectarMarcas(o,objsClasif,colorMenor, marcasAnt);
			if (m != null)
				todas.add(m);
		}
		
//		marcasAnt.clear();
//		for(ObservMarca m : res)
//			marcasAnt.add(m.getId());

		// Filtro una por id
		List<ObservMarca> res = new ArrayList<ObservMarca>();
		for(int i = 1; i <= 6; i++){
			ObservMarca max = null;
			for(ObservMarca om : todas){
				if (om.getId() == i)
					if(max == null || om.getProb() > max.getProb())
						max = om;
			}
			if(max != null)
				res.add(max);
		}
				
		return res;
	}

	public List<ObjCamara> filter(List<ObjCamara> observed) {
		List<ObjCamara> objs = new ArrayList<ObjCamara>();
		for(ObjCamara oc : observed){
			double w = oc.getR().getWidth();
			double h = oc.getR().getHeight();
			
			if(w < (1+HW_TOL)*h && w > (1-HW_TOL)*h && w > MIN_SIZE && h > MIN_SIZE && w < MAX_SIZE && h < MAX_SIZE)
				objs.add(oc);
		}
		return objs;
	}

	/**
	 * Busca las marcas mayores a una probabilidad preestablecida.
	 * Para cada conjunto de tres marcas diferentes calcula la verosimilitud.
	 * Los valores de la marca del colorMenor se fijan como valores de medida
	 * @param o
	 * @param objsClasif
	 * @param colorMenor
	 * @param marcasAnt2 
	 */
	private ObservMarca detectarMarcas(ObjCamara o, List<List<ObjCamara>> objsClasif, int colorMenor, Collection<Integer> marcasAnt2) {
		List<ObservMarca> res = new ArrayList<ObservMarca>();

		List<Integer> indices = new ArrayList<Integer>();
		for(int i = 0; i < 3; i++)
			if (i != colorMenor)
				indices.add(i);
		
		// Calculo todas las posibles
//		System.out.println("Probs");
		for(ObjCamara o1 : objsClasif.get(colorMenor)){
			for(ObjCamara o2 : objsClasif.get(indices.get(0))){
				for(ObjCamara o3 : objsClasif.get(indices.get(1))){
					ObservMarca mVeros = verosimilitud(o1, o2, o3, marcasAnt);
					if(mVeros.getProb() > MIN_VEROSIMIL){
						res.add(mVeros);
						
//						if(dos != null){
//							Rectangle r = mVeros.getR();
//							try {
//								dos.writeChars("Marca en (" + r.x + "," + r.y + "," + r.width + "," +
//										r.height + ")" + " id " + mVeros.getId() + 
//										" distancia " + mVeros.getDistancia() + " prob " + mVeros.getProb() + "|");
//							} catch (IOException e) {
//							}
//						}
							
					}
//					System.out.println(mVeros.getProb());
				}
			}
		}
//		if(!res.isEmpty() && dos != null)
//			try {
//				dos.writeChars("\n");
//			} catch (IOException e) {
//			}
		
		// Si no hay marcas, retorno minima
		if(res.isEmpty())
			return null;
		
		// Objengo el maximo
		ObservMarca max = res.get(0);
		for(ObservMarca m : res)
			if(m.getProb() > max.getProb())
				max = m;
				
		return max;			
	}

	private ObservMarca verosimilitud(ObjCamara o1, ObjCamara o2, ObjCamara o3,
			Collection<Integer> marcasAnt) {
		int ys[] = calcularYs(o1, o2, o3);

		float prob = 0;

		// Agrego versosimilitud de otras dos marcas
		prob += Math.log(g((o1.getCentro().x-o2.getCentro().x)/o2.getR().width, VAR_X,0)) +
				Math.log(g((ys[1]-o2.getCentro().y)/o1.getR().height, VAR_Y,0 )) +
				Math.log(g((o1.getR().width- o2.getR().width)/ o1.getR().width, VAR_W,0)) +
				Math.log(g((o1.getR().height-o2.getR().height)/o1.getR().height, VAR_H,0 ));
		
		prob += Math.log(g((o1.getCentro().x - o3.getCentro().x)/o1.getR().width, VAR_X, 0)) +
				Math.log(g((ys[2]-o3.getCentro().y)/o1.getR().height, VAR_Y, 0)) +
				Math.log(g((o1.getR().width-o3.getR().width)/o1.getR().width, VAR_W, 0))+
				Math.log(g((o1.getR().height-o3.getR().height)/o1.getR().height, VAR_H, 0));
		
		// TODO multiplicar por el a priori de la marca
		if(prob > -1000 && prob < 100000){
			ObservMarca m = new ObservMarca(o1,o2,o3,prob);
			// Si vi la marca en el frame anterior sumo una inercia
//			if(marcasAnt.contains(new Integer(m.getId()))){
////				System.out.println("Sumando inercia");
//				m.setProb(prob + INERCIA);
//			}
			return m;
		} else
			// Marca con cero probabilidad
			return new ObservMarca(null, 0, -1000);
	}

	/***
	 * Retorna la pseudoprobabilidad de un valor de x para una gaussiana dada.
	 * @param mu
	 * @param var
	 * @param x
	 * @return
	 */
	private float g(float mu, float var, float x) {
		float res = (float) ((1 / Math.sqrt(2 * Math.PI * var * var)) * Math.exp(- (x - mu)*(x - mu) / (2*var*var)));
		return res;
	}

	private int[] calcularYs(ObjCamara o1, ObjCamara o2, ObjCamara o3) {
		int ys[] = new int[3];
		int height = o1.getR().height;
		ys[0] = o1.getCentro().y;
		if(o1.getCentro().y > o2.getCentro().y){
			if(o1.getCentro().y > o3.getCentro().y){				
				if(o2.getCentro().y > o3.getCentro().y){
					ys[1] = o1.getCentro().y - height;
					ys[2] = o1.getCentro().y - 2 * height;
				} else {
					ys[1] = o1.getCentro().y - 2 * height;
					ys[2] = o1.getCentro().y - height;
				}
			} else {
				ys[1] = o1.getCentro().y - height;
				ys[2] = o1.getCentro().y + height;
			}
		} else {
			if(o2.getCentro().y > o3.getCentro().y){
				if(o1.getCentro().y > o3.getCentro().y){
					ys[1] = o1.getCentro().y + height;
					ys[2] = o3.getCentro().y - height;
				} else {
					ys[1] = o1.getCentro().y + 2 * height;
					ys[2] = o1.getCentro().y + height;
				}
			} else {
				ys[1] =  o1.getCentro().y + height;
				ys[2] = o1.getCentro().y + 2 * height;
			}
		}
		return ys;
	}

	public ObservMarca detectarUna(List<ObjCamara> objs, float minProb) {
		List<ObservMarca> marcas = detectar(objs);
		if(marcas.size() != 0){
			// Elijo la mas probable
			ObservMarca max = marcas.get(0);
			for(ObservMarca marca : marcas){
				if(marca.getProb() > max.getProb())
					max = marca;
			}
			if(max.getProb() >= minProb)
				return max;
		}
		
		return null;
	}

	public ObservMarca detectarUna(float minProb) {
		List<ObservMarca> marcas = detectarFusion();
		if(marcas.size() != 0){
			// Elijo la mas probable
			ObservMarca max = marcas.get(0);
			for(ObservMarca marca : marcas){
				if(marca.getProb() > max.getProb())
					max = marca;
			}
			if(max.getProb() >= minProb)
				return max;
		}
		
		return null;
	}
}
