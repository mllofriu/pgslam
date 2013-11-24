package pc.particulas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pc.comm.Comunication;


public class SocketSrvCom extends Comunication {

	private static SocketSrvCom instance = null;
	
	public static SocketSrvCom getInstance(){
		if(instance == null)
			instance = new SocketSrvCom();
		return instance;
	}
	
	@Override
	public void connect() {
		ServerSocket serverSocket;
		
		try {
		    serverSocket = new ServerSocket(4444);
		    Socket socket = serverSocket.accept();
		    this.dis = new DataInputStream(socket.getInputStream());
	        this.dos = new DataOutputStream(socket.getOutputStream());
		} 
		catch (IOException e) {
		    System.out.println("Error en la com");
		    System.exit(-1);
		}	
		
	}

}
