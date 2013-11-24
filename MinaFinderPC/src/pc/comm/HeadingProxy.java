package pc.comm;

import pc.particulas.SocketSrvCom;
import brick.particulas.Command;
import brick.particulas.UpdateCmd;
import brick.utils.Observable;
import brick.utils.Observer;

public class HeadingProxy implements Observer {

	public HeadingProxy(){
		
	}
	@Override
	public void update(Observable o, Object arg) {
		Command c = (Command) arg;
		if(c instanceof UpdateCmd)
			SocketSrvCom.getInstance().enviar(Comunication.IDMEDIDAHEADING,
					((UpdateCmd)c).getOrientation() + "");
	}

}
