/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserverup_iterative;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author PULAK
 */
public class SimpleClient1 implements Runnable{
     Thread t;
     int clln_no;
    
   SimpleClient1(int cln_no ) throws IOException {
        // TODO code application logic here
       clln_no=cln_no;
        t = new Thread(this);
t.start();     
         
   }
  public void run() {
        try {

    Socket soc[] = new Socket[100];//       
    String str[] = new String[100];
    BufferedReader br[] = new BufferedReader[100];
    DataOutputStream dos[] =new DataOutputStream[100];
     //BufferedReader kyrd[]= new BufferedReader(new InputStreamReader(System.in));
   
         soc[clln_no] = new Socket("127.0.0.1",2000);
        System.out.print(soc[clln_no].isConnected());
        
    
    
    
        br[clln_no] = new BufferedReader(new InputStreamReader(soc[clln_no].getInputStream()));
        dos[clln_no] = new DataOutputStream(soc[clln_no].getOutputStream());
    
    System.out.println("To start the dialog type the message in this client window \n Type exit to end"); 
     boolean more = true;
/*while (true) {
    
        //str[clln_no] = kyrd.readLine();
         dos[clln_no].writeBytes(str[clln_no]);
         dos[clln_no].write(13);
         dos[clln_no].write(10);
         dos[clln_no].flush();
        String s, s1;
        s =  br[clln_no].readLine();
        System.out.println("From server :" + s);
       
        if (s.equals("exit")) {
            break;
        }

}
 */
     // br[clln_no].close();
    // dos[clln_no].close();

    // soc[clln_no].close();

   
  
        } catch (UnknownHostException ex) {
            Logger.getLogger(SimpleClient1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimpleClient1.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
   
   
   
}