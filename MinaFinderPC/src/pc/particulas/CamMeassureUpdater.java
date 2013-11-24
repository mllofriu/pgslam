package pc.particulas;

import pc.comm.Comunication;
import pc.logger.Logger;
import brick.comm.Mensaje;
import brick.particulas.Command;
import brick.particulas.UpdateCmdMarcas;
import brick.utils.Observable;
import brick.utils.Observer;

public class CamMeassureUpdater implements Observer {
	
	private ParticleFrame frame;
	private Logger log = null;
	private long startTime;

	public CamMeassureUpdater(ParticleFrame frame, Comunication com){
		this.frame = frame;
		com.addObserver(this);
	}

	public CamMeassureUpdater(ParticleFrame frame2, Comunication com, Logger log, long startTime) {
		this(frame2, com);
		this.log  = log;
		this.startTime = startTime;
	}

	@Override
	public void update(Observable o, Object arg) {
		Mensaje m = (Mensaje) arg;
		if(m.getId() == Comunication.IDCOM){
			Command c = Command.parseCommand(m.getStr());
			
			if(c instanceof UpdateCmdMarcas){
				frame.setObservMarca(((UpdateCmdMarcas)c).getM().getIdMarca(),
						((UpdateCmdMarcas)c).getM().getDist());
				if(log != null)
					log.addLog(System.currentTimeMillis() - startTime + ",marcavista," + c.toString());
			}
		}
			
	}

}
