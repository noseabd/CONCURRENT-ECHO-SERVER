package SERVER;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TCPServer {

  public final static int SOCKET_PORT = 7070;  
 
   public static  JFileChooser fileDlg;
  public static void main (String [] args ) throws IOException {
    FileInputStream fis = null;  
    BufferedInputStream bis = null;
    OutputStream os = null;
    ServerSocket servsock = null;
    Socket sock = null;
    try {
      servsock = new ServerSocket(SOCKET_PORT);
      JOptionPane.showMessageDialog(null, "Welcome to CSE321 Chat Server!"+ "\n"+"Server is running on port:" + SOCKET_PORT);
      while (true) {
        System.out.println("Waiting...");
        try {
          sock = servsock.accept();
          System.out.println("Accepted connection : " + sock);
          // send file
          if(sock!=null)
          {
        JOptionPane.showMessageDialog(null, "Choose a file to  Sent from server to Client:");
        fileDlg = new JFileChooser();
        fileDlg.showOpenDialog(fileDlg);
        String filename = fileDlg.getSelectedFile().getAbsolutePath();
       // TF.setText(filename);
        System.out.println(filename);
          File myFile = new File (filename);
          byte [] mybytearray  = new byte [(int)myFile.length()];
          fis = new FileInputStream(myFile);
          bis = new BufferedInputStream(fis);
          bis.read(mybytearray,0,mybytearray.length);
          os = sock.getOutputStream();
          JOptionPane.showMessageDialog(null,"Sending " + filename + "(" + mybytearray.length + " bytes)");
          System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
          os.write(mybytearray,0,mybytearray.length);
          os.flush();
          System.out.println("Done.");
        }}
        finally {
          if (bis != null) bis.close();
          if (os != null) os.close();
          if (sock!=null) sock.close();
        }
      }
    }
    finally {
      if (servsock != null) servsock.close();
    }
  }
}
