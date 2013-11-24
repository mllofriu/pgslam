package pc.parametrizacion;

import org.jgap.IChromosome;


public class Parametrizacion {

	

	private float ruidoDesp;
	private float ruidoAng;
	private float covSensado;
	private float minVarW;
	
	
	public Parametrizacion(float ruidoDesp, float ruidoAng, float covSensado, float minVarW) {
		super();
		this.ruidoDesp = ruidoDesp;
		this.ruidoAng = ruidoAng;
		this.covSensado = covSensado;
		this.minVarW = minVarW;
	}
	@Override
	public String toString() {
		return ruidoDesp + "," + ruidoAng + "," + covSensado + "," + minVarW;
	}
	@Override
	public boolean equals(Object arg) {
		if(! (arg instanceof Parametrizacion))
			return false;
		
		Parametrizacion p = (Parametrizacion) arg;
		return ruidoDesp == p.ruidoDesp &&
				ruidoAng == p.ruidoAng &&
				covSensado == p.covSensado &&
				minVarW == p.minVarW;
	}
	@Override
	public int hashCode() {
		return (int) (ruidoDesp + ruidoAng * 10 + covSensado * 100 + minVarW * 1000);
	}
	public static Parametrizacion fromChrom(IChromosome fc) {
		float dn = (float) (getParamAtGene(fc, 0) / 100.);
		float an = (float) (getParamAtGene(fc, 1) / 100.);
		float covSensado = (float) (getParamAtGene(fc, 2));
		float minVarW = (float) (getParamAtGene(fc, 3) / 1000.);
		return new Parametrizacion(dn, an, covSensado, minVarW);
	}
	
	public static int getParamAtGene( IChromosome a_potentialSolution,
			int a_position )
	{
		Integer param =
				(Integer) a_potentialSolution.getGene(a_position).getAllele();

		return param.intValue();
	}
	
	public float getRuidoDesp() {
		return ruidoDesp;
	}
	public void setRuidoDesp(float ruidoDesp) {
		this.ruidoDesp = ruidoDesp;
	}
	public float getRuidoAng() {
		return ruidoAng;
	}
	public void setRuidoAng(float ruidoAng) {
		this.ruidoAng = ruidoAng;
	}
	public float getCovSensado() {
		return covSensado;
	}
	public void setCovSensado(float covSensado) {
		this.covSensado = covSensado;
	}
	public float getMinVarW() {
		return minVarW;
	}
	public void setMinVarW(float minVarW) {
		this.minVarW = minVarW;
	}
	
	
	
}
