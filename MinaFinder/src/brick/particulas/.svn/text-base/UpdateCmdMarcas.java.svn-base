package brick.particulas;

import java.util.StringTokenizer;

import brick.particulas.smodel.MarcaMeassure;

public class UpdateCmdMarcas extends Command{

	private MarcaMeassure m;

	public UpdateCmdMarcas(float dist, int id, float ang){
		m = new MarcaMeassure(dist, id, ang);
	}
	
	public UpdateCmdMarcas( MarcaMeassure marcaMeassure){
		this.m = marcaMeassure;
	}

	@Override
	public void execute(MCLPoseProvider mcl) {
		mcl.update(m);
	}
	
	@Override
	public String toString() {
		return "updateMarcas,"+m.toString();
	}

	public static UpdateCmdMarcas fromString(String strLine) {
		StringTokenizer stok = new StringTokenizer(strLine, ",");
		if(stok.nextToken().equals("updateMarcas")){
			return new UpdateCmdMarcas(MarcaMeassure.fromString(stok));
		} else
			return null;
	}

	public MarcaMeassure getM() {
		return m;
	}

	public void setM(MarcaMeassure m) {
		this.m = m;
	} 
}
