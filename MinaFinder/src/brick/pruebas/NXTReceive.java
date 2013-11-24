package brick.pruebas;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.nxt.comm.USB;
import lejos.util.TextMenu;

/**
 * Receive data from another NXT, a PC, a phone, 
 * or another device. Allow the use of Bluetooth,
 * USB or RS485. Allow either packet based or RAW
 * connections
 * 
 * Waits for a connection, receives an int and returns
 * its negative as a reply, continues until the remote
 * system closes the connection.
 * 
 * @author Andy Shaw
 * @author toqueteada por pgslam
 */
public class NXTReceive
{

    public static void main(String[] args) throws Exception
    {
        LCD.clear();
        while (true)
        {
        	LCD.drawString("conectando", 0, 0);

//            NXTConnection con = connectors[connectionType].waitForConnection(0, modes[mode]);
            NXTConnection con = RS485.getConnector().waitForConnection(0, NXTConnection.PACKET);

            LCD.drawString("Connected...", 0, 2);

            DataInputStream dis = con.openDataInputStream();
            DataOutputStream dos = con.openDataOutputStream();

            while(true)
            {
                String data;
                try{
                    data = dis.readUTF();
                    LCD.drawString(data, 0, 0);
                } catch (EOFException e)
                {
                    break;
                }
                
            }

            LCD.drawString("Closing...  ", 0, 2);
            dis.close();
            dos.close();
            con.close();
        }
    }
}

