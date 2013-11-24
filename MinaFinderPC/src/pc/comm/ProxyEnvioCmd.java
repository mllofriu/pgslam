package pc.comm;

import pc.particulas.SocketSrvCom;
import brick.particulas.Command;
import brick.utils.Observable;
import brick.utils.Observer;

public class ProxyEnvioCmd implements Observer{

	public ProxyEnvioCmd(){
		
	}

	@Override
	public void update(Observable o, Object arg) {
		Command c = (Command) arg;
		SocketSrvCom.getInstance().enviar(Comunication.IDCOM, c.toString());
	}
}
