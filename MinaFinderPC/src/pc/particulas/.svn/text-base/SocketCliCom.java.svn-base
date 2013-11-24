package pc.particulas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import pc.comm.Comunication;


public class SocketCliCom extends Comunication {

	private static SocketCliCom instance = null;
	
	public static SocketCliCom getInstance(){
		if(instance == null)
			instance = new SocketCliCom();
		return instance;
	}
	@Override
	public void connect() {
		Socket socket = null;
		boolean done = false;
		while(!done){
	        try {
	            socket = new Socket("montyfox", 4444);
	            this.dis = new DataInputStream(socket.getInputStream());
	            this.dos = new DataOutputStream(socket.getOutputStream());
	            done = true;
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host: montyfox.");
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for "
	                               + "the connection to: montyfox.");
	            System.exit(1);
	        }
		}
	}

}
