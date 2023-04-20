package sg.edu.iss.nus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws NumberFormatException, UnknownHostException, IOException
    {
        String serverHost = args[0];
        String serverPort = args [1];

        // establish connection to server
        // *** server must be started first
        Socket socket = new Socket(serverHost, Integer.parseInt(serverPort));

        // 1. setup console input from keyboard
        // 2. variable to save keyboard inputs
        // 3. variable to save msgReceived
        Console con = System.console();
        String keyBoardInput = "";
        String msgReceived = "";

        // similar to server - Slide 9
        try (InputStream is = socket.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            try (OutputStream os = socket.getOutputStream()){
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                // while loop here
                while (!keyBoardInput.equals("close")){
                    keyBoardInput = con.readLine("Enter a command");

                    // send message across through the communication tunnel
                    dos.writeUTF(keyBoardInput);
                    dos.flush();

                    // recieve message across through communication tunnel
                    msgReceived = dis.readUTF();
                    System.out.println(msgReceived);
                }

                //closes the output stream in reverse order
                dos.close();
                bos.close();
                os.close();

            } catch (Exception ex){

                bis.close();
                dis.close();
                is.close();
            }
        } catch (EOFException ex) {

            ex.printStackTrace();
            socket.close();

        }

    }
}
