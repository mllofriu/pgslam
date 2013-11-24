package pc.particulas;

import pc.comm.Comunication;
import brick.particulas.Command;
import brick.utils.Observable;
import brick.utils.Observer;
import curtidor.WayPointCmd;
import curtidor.utils.PoseStr;

public class WayPointProxy implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		Command c = (Command) arg;
		if(c instanceof WayPointCmd)
			SocketSrvCom.getInstance().enviar(Comunication.IDWAYPOINT,
					new PoseStr(((WayPointCmd)c).getWP().getPose()).toString());
	}

}
