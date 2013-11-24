package pc.comm;

import pc.logger.Logger;
import brick.comm.Mensaje;
import brick.particulas.MCLParticleSet;
import brick.particulas.MCLPoseProvider;
import brick.utils.Observable;
import brick.utils.Observer;
import curtidor.utils.PoseStr;

public class PartUpdater implements Observer{

	private MCLParticleSet particles;
	private Logger log = null;
	private long startTime;

	public PartUpdater(MCLParticleSet particles, Comunication com) {
		this.particles = particles;
		com.addObserver(this);
	}

	public PartUpdater(MCLParticleSet mclps, Comunication com, Logger log, long time) {
		this(mclps, com);
		this.log  = log;
		
		this.startTime = time;
	}

	@Override
	public void update(Observable o, Object arg) {
//		System.out.println((Mensaje)arg);
		Mensaje m = (Mensaje) arg;
		
		System.out.println("PartUpdater id " + m.getId());
		//si los datos que llegan me interesan
		if(m.getId() == Comunication.IDPART){ //fijarme el id
			//				this.loadParticles(com.getDis());
			System.out.println("Loading particulas");
			particles.loadParticles(m.getBytes());
			if(log != null){
				MCLPoseProvider pp = new MCLPoseProvider(particles);
				log.addLog(System.currentTimeMillis() - startTime+ ",pose," + new PoseStr(pp.getPose()).toString());
			}
			//				this.dumpParticles(this.file);
		}
	}
}
