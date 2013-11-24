package brick.pruebas;

//import lejos.nxt.*;
//import lejos.nxt.comm.*;
//import lejos.util.TextMenu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.util.TextMenu;


/**
 * 
 * Test of NXT to NXT Bluetooth/RS485 communications.
 * 
 * Allows the user to choose the connection type and mode.
 * 
 * Connects to another NXT, sends 100 ints, and receives the 
 * replies. Then closes the connection and shuts down.
 * 
 * Works with the NXTReceive sample running on the slave NXT.
 * 
 * Change the name string to the name of your slave NXT. For Bluetooth
 * you need to make sure it is in the known devices list of the master NXT.
 * To do this, turn on the slave NXT and make sure Bluetooth is on and the
 * device is visible. Use the Bluetooth menu on the slave for this. Then,
 * on the master, select the Bluetooth menu and then select Search.
 * The name of the slave NXT should appear. Select Add to add
 * it to the known devices of the master. You can check this has
 * been done by selecting Devices from the Bluetooth menu on the
 * master.
 * 
 * @author Lawrie Griffiths/Andy Shaw
 * @author toqueteada por pgslam
 */
public class NXTConnectTest
{

    public static void main(String[] args) throws Exception
    {
        String name = "NXT";
        LCD.clear();
        LCD.drawString("Connecting...", 0, 0);
        NXTConnection con = RS485.getConnector().connect(name, NXTConnection.PACKET);

        if (con == null)
        {
            LCD.drawString("Connect fail", 0, 5);
            Thread.sleep(2000);
            System.exit(1);
        }

        LCD.drawString("Connected       ", 0, 3);
        LCD.refresh();

        DataInputStream dis = con.openDataInputStream();
        DataOutputStream dos = con.openDataOutputStream();

        while (true)
        {
            try
            {
                dos.writeUTF("hola bro");
                dos.flush();
                LCD.drawString("hola bro", 0, 0);
                Thread.sleep(1000);
                dos.writeUTF("chau bro");
                dos.flush();
                LCD.drawString("chau bro", 0, 0);
                Thread.sleep(1000);
                LCD.clear();
            }
            catch (IOException ioe)
            {
                LCD.drawString("Write Exception", 0, 5);
            }

//            try
//            {
//                LCD.drawString("Read: ", 0, 7);
//                LCD.drawInt(dis.readInt(), 8, 6, 7);
//            }
//            catch (IOException ioe)
//            {
//                LCD.drawString("Read Exception ", 0, 5);
//            }
        }

//        try
//        {
//            LCD.drawString("Closing...    ", 0, 3);
//            dis.close();
//            dos.close();
//            con.close();
//        }
//        catch (IOException ioe)
//        {
//            LCD.drawString("Close Exception", 0, 5);
//            LCD.refresh();
//        }
//        LCD.drawString("Finished        ", 0, 3);
//        Thread.sleep(2000);
    }
}
