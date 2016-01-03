package simplechat;

import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
//===========================================================
public class TCPServer extends JFrame
{
    private TextArea src;
    public String name[]=new String[25];  //CLIENT name 25 name  
    public int count=0;
	public TCPServer(Socket connectionSocket[]) throws Exception // PASSING CLIENTS
	{
 ///////////////////////////////////////////////// INITIALIZATION           
            super("Server");
            for(int i=0;i<25;i++)
                name[i]=new String();/// INITIALLY ALL CLIENTS BE NULL NAMED
            src=new TextArea();
            src.setEditable(false);
            src.setText("");
            add(src);
            setSize(500,400);
            setResizable(false);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
/////////////////////////////////////////////////INITIALIZATION      END
            DataOutputStream outToClient[]=new DataOutputStream[15];
            BufferedReader inp[]=new BufferedReader[15];
            
            new onchk(outToClient,name); // CHECKING FOR EXISTING CLIENTS 
            System.out.print("in server\n");
		//while (true)
                new add(connectionSocket,outToClient,inp,name,src);// PASSING CLIENTS ,outToClient[],inp[],NAME[],SRC TEXT AREA
                
	}
}

class add extends Thread{  // CREATING NEW SERVER SOCKET IN NEW THREAD FOR NEW  CLIENT ,AND THEN SEARCH FOR EXISTING CLIENTS  

    ServerSocket welcomeSocket; // FOR CREATING NEW SERVER SOCKET
    Socket connectionSocket[]=new Socket[15];
    DataOutputStream out[]=new DataOutputStream[15];
    BufferedReader inFromClient[]=new BufferedReader[15];
    String name[]=new String[15];
    String PrivateChat_name[]=new String[15];
    TextArea src=new TextArea();
    
    add(Socket conn[],DataOutputStream outToClient[],BufferedReader inp[],String nam[],TextArea sr) throws IOException
    {
	welcomeSocket= new ServerSocket(6789);// NEW SERVER SOCKET INITIALIZING , INVITE ALL CLIENTS 
        out=outToClient; 
        inFromClient=inp;
        connectionSocket=conn; // FOR 15 DIFFERENT CLIENTS SOCKET
        name=nam;
        src=sr; //
		        
        start();//// SEARCHING FOR CLIENTS 
    }
    public void run()//// SEARCHING FOR CLIENTS 
    {
        
		for (int i=0;i<15;i++)
		{
           /*         ///////////////////////////////////////// PRIVATE CHAT
                   privateChat pri = new privateChat();
                   pri.setVisible(true);
                   pri.setTitle(this.getName());
                   int privatChatSignal = pri.signal_for_private_chat; 
                    if (privatChatSignal==1) {
                try { 
                  connectionSocket[i] = welcomeSocket.accept();// TRY TO ESTABLISH  ACTUAL CONNECTION FOR 15 CLIENTS
                  inFromClient[i] = new BufferedReader(new InputStreamReader(connectionSocket[i].getInputStream()));// READING FROM CLIENT 
                  out[i] = new DataOutputStream(connectionSocket[i].getOutputStream());

                  out[i].writeBytes("S\n");
                  String s=inFromClient[i].readLine();
                  name[i]=s;
                  System.out.println("\nClients Found : " + name[i]); // printing existing clients 
                    for (int j = 0; j < 15; j++) {
                        if (PrivateChat_name[j].equals(name[i])) {
                       System.out.println("\nClients Found : " + name[i]); // printing existing clients 
                
                      new SThread(i, inFromClient[i], out,name,src);// FOR i NO CLIENT 
                        
                      }
                        
                    }
                     continue; 
                      
                } catch (IOException ex) {
                    Logger.getLogger(add.class.getName()).log(Level.SEVERE, null, ex);
                }
                    }
                     
                    
          ///////////////////////////////////////// PRIVATE CHAT END     */
                  
            try {   //// SEARCHING FOR CLIENTS , ESTABLISH CONNECTION WITH HIM              
                connectionSocket[i] = welcomeSocket.accept();// TRY TO ESTABLISH  ACTUAL CONNECTION FOR 15 CLIENTS
                inFromClient[i] = new BufferedReader(new InputStreamReader(connectionSocket[i].getInputStream()));// READING FROM CLIENT 
                out[i] = new DataOutputStream(connectionSocket[i].getOutputStream());

                out[i].writeBytes("S\n");
                String s=inFromClient[i].readLine();
                name[i]=s;
                System.out.println("\nClients Found : " + name[i]); // printing existing clients 
                
                new SThread(i, inFromClient[i], out,name,src);// FOR i NO CLIENT 
            } catch (IOException ex) {
                Logger.getLogger(add.class.getName()).log(Level.SEVERE, null, ex);
            }             
		}
    }
}



class SThread extends Thread{
	BufferedReader inFromClient;
	DataOutputStream outToClient[]=new DataOutputStream[15];
	String clientSentence;
        int who;
        int ii,num=-1;
        String na[]=new String[15];
        String name="";
        TextArea src=new TextArea();
	public SThread(int cl,BufferedReader in, DataOutputStream out[],String nam[], TextArea sr)
        {
		inFromClient=in;
		outToClient=out;
                who=cl;
                na=nam;
                src=sr;
		start();
	}
	public void run(){
		while (true){
			try{
                                name="";
                                num=-1;
				clientSentence=inFromClient.readLine();// READING FROM PARTICULAR CLIENT i
                                
                                for(ii=0;clientSentence.charAt(ii)!=' ';ii++)// FIRST READ NAME OF CLIENT 
                                    name+=clientSentence.charAt(ii);
                                
                                    src.setText(clientSentence+'\n'+src.getText());// echoing clients name 
                                    System.out.println(clientSentence+'\n'+"pul "+src.getText());
                                   for(int i=0;i<15;i++)
                                    if(na[i].equals(name))//WHICH CUSTOMER , FIND HERE , SAY CUSTOMER NO num
                                    {
                                        num=i;
                                        break;
                                    }
                                if(num==-1)
                                {
                                    int j;
                                    for(j=0;j<clientSentence.length();j++)
                                     if(clientSentence.charAt(j)=='-')
                                     {
                                        break;
                                      }
                                     String mod="";
                                    for(j=j;j<clientSentence.length();j++)
                                      mod+=clientSentence.charAt(j);
                                    for(int i=0;i<15;i++)// FOR GROUP CHAT 
                                    {
                                        try{
                                            // SEND mod TO ALL , in GROUP CHAT 
                                            outToClient[i].writeBytes("mGroup Chat"+mod+'\n');
                                            }
                                        catch(Exception ex)
                                        {
                                        }
                                    }
                                    continue;

                                }
                              

				
                                    try{
                                            int j;
                                            for(j=0;j<clientSentence.length();j++)
                                                if(clientSentence.charAt(j)=='-')
                                                {
                                                    break;
                                                }
                                            String mod="";
                                            for(j=j;j<clientSentence.length();j++)
                                                mod+=clientSentence.charAt(j);
                                // SEND mod TO WHO , in PERSONAL CHAT              
                                            outToClient[who].writeBytes("mMe " + mod+ '\n');
                                


                                        if(num!=who)
                                            outToClient[num].writeBytes("m"+clientSentence.substring(ii+1)+'\n');
                                    }
                            catch(Exception ex)
                            {
                            }
                        }
			catch(Exception e){
                        try {
                            Thread.sleep(150);// ALLOW CONTEX SWITCH
                        } catch (InterruptedException ex) {
                        }
			}
		}
	}
}
class onchk extends Thread{ // CHECKING WHO IS ALIVE AND STORE THEIR UPCOMING STREAM
    DataOutputStream cc[]=new DataOutputStream[15];
    int fn[]=new int[15];
    String name[]=new String[15];
    onchk( DataOutputStream cl[],String nam[])
    {
         cc=cl;  // CC = OUT_TO_CLIENT
         name=nam;
         start();
    }
    @Override
    public void run()
    {
        while(true)// PERIODICALLY CHECKING FOR ALIVE CLIENT , INFINITE LOOP
        {
            for (int i = 0; i < 15; i++)
            {
                try{
                    cc[i].writeBytes("0\n");
                    fn[i]=1;
                    System.out.print(i+" "+ name[i]+"\n");
                }
                catch(Exception e){
                    fn[i]=0;
                }
            }
           
            for(int i=0;i<15;i++)
                for(int j=0;j<15;j++)
                {
                    if(fn[j]==1)
                    {
                        try {
                            if(name[j]!=null)
                            cc[i].writeBytes("a" + j +" "+name[j]+ "\n");
                        } catch (Exception ex) {                           
                        }
                    }
                    else
                    {
                        try{
                            cc[i].writeBytes("b"+j+" "+"\n");
                        }
                        catch(Exception e){}
                    }
                }

//            System.out.println();
            try {
                Thread.sleep(1000);  // ALLOW CONTEX SWITCH
            } catch (InterruptedException ex) {
            }
        }
    }
}