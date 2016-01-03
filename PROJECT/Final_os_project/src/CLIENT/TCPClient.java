package CLIENT;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TCPClient {
    public static JFrame Main_Frame = new JFrame("File Client-Server");
    public static JButton SB = new JButton();
    public static JButton RB = new JButton();
    public static JButton CB = new JButton();
    public static JTextField TF = new JTextField();
    public static  JFileChooser fileDlg;
   public static Socket sock = null;

 

  public final static int FILE_SIZE = 666386; 
                                               
  public static void main (String [] args ) throws IOException {
        configure_main_frame();
        //connect();
     MainWindow_Action();
   
  }
 public static void configure_main_frame()
    {
    Main_Frame.setBackground(new java.awt.Color(255,255, 255)); 
 Main_Frame.setSize(600, 320);
 Main_Frame.getContentPane().setLayout(null);
 Main_Frame.setVisible(true);
 
  /*SB.setBackground(new java.awt.Color(0,0, 255)); 
 SB.setForeground(new java.awt.Color(255,255, 255)); 
 SB.setText("Send Flie");
 Main_Frame.getContentPane().add(SB);
 SB.setBounds(150, 40 ,100 , 25);
 */
  RB.setBackground(new java.awt.Color(0,0, 255)); 
 RB.setForeground(new java.awt.Color(255,255, 255)); 
 RB.setText("Receive Flie");
 Main_Frame.getContentPane().add(RB);
 RB.setBounds(350, 40 ,120 , 25);
 
  CB.setBackground(new java.awt.Color(0,0, 255)); 
 CB.setForeground(new java.awt.Color(255,255, 255)); 
 CB.setText("Connect");
 Main_Frame.getContentPane().add(CB);
 CB.setBounds(200, 40 ,100 , 25);
 
    }
    public static void MainWindow_Action()
{


RB.addActionListener(
         new java.awt.event.ActionListener() {

       
        public void actionPerformed(java.awt.event.ActionEvent e) {
                 try {
                     receiveFile();
                 } catch (IOException ex) {
                     Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
                 }
               
        }
    }
        
        );

CB.addActionListener(
         new java.awt.event.ActionListener() {

       
        public void actionPerformed(java.awt.event.ActionEvent e) {
                 try {
                     connect();
                 } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Connot Connect to the Server!!");
                 }
        }
    }
        
        );
 
}
    
       //IP Address input nilam.
 private static String getServerAddress() {
        return JOptionPane.showInputDialog(
            Main_Frame,
            "Enter IP Address of the Server:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }

//server er port input nilam.
private static String getServerPort() {
        return JOptionPane.showInputDialog(
             Main_Frame,
            "Enter Port of the Server:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }

      public static void connect()
    {
    
        
    try {
         String serverAddress = getServerAddress();
    String serverPort = getServerPort();
    final int  port = Integer.parseInt(serverPort);
            sock = new Socket(serverAddress, port);
    JOptionPane.showMessageDialog(null, "Connected to port: "+port);
           // sock = new Socket(SERVER, SOCKET_PORT);
     // System.out.println("Connecting...");
           // stdin = new BufferedReader(new InputStreamReader(System.in));
             System.out.println(" connect to the server, .");
        } catch (Exception e) {
            System.err.println("Cannot connect to the server, try again later.");
            //System.exit(1);
        }
//  Actions();
    }
      
      private static String SetFileName() {
        return JOptionPane.showInputDialog(
             Main_Frame,
            "Enter the path and File name for the receiving file:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }
     public static void  receiveFile() throws IOException
      {
      int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    
    try {
      

      // receive file
        String FILE_TO_RECEIVED= SetFileName();
      byte [] mybytearray  = new byte [FILE_SIZE];
      InputStream is = sock.getInputStream();
      fos = new FileOutputStream(FILE_TO_RECEIVED);
      bos = new BufferedOutputStream(fos);
      bytesRead = is.read(mybytearray,0,mybytearray.length);
      current = bytesRead;

      do {
         bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));
         if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);

      bos.write(mybytearray, 0 , current);
      bos.flush();
      JOptionPane.showMessageDialog(null,"File " + FILE_TO_RECEIVED
          + " downloaded (" + current + " bytes read)" );
      System.out.println("File " + FILE_TO_RECEIVED
          + " downloaded (" + current + " bytes read)");
       Main_Frame.dispose();

    }
    finally {
      if (fos != null) fos.close();
      if (bos != null) bos.close();
      if (sock != null) sock.close();
    }
      }
}