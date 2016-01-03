package simplechat;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener,KeyListener {

     
    String nm;
    static InetAddress inetAddress;
    DataOutputStream outToServer;
    Socket clientSocket;
    BufferedReader inFromServer;
     public JTextField inptext=new JTextField();
     public TextArea showtext=new TextArea();
    JLabel jnam;
    JPanel jp;
    JButton btn[]=new JButton[15];// FOR 15 DIFFERENT CLIENTS TO CONCURRENTLY CHAT WITH
    String cl_no=null;
    int privatChatSignal=0;
    String PrivateNameCollection_here[]  = new String[15];
    Socket client_list[] = new Socket[15];
    public Client(String host,String name,Socket cl[]) throws Exception /// HOST , MY_NAME , EXISTING CLIENTS SOCKET
    {   
        super("Client app");
       client_list = cl;
        this.setTitle(name+"'s Chat Interface");
        nm=name;
        System.out.println("\nNew Client Have Bourne Now named : "+nm);
            clientSocket = new Socket(host,6789); // CREATE A PHYSICAL SOCKET FOR THIS CLIENT 
            outToServer= new  DataOutputStream(clientSocket.getOutputStream());   //FOR  CLIENT'S TALK WIH SERVER       
            inFromServer = new  BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// FOR LISTENING FREOM SERVER 
 ///////////////////////////////////////////   design 
            inptext.setForeground(Color.red);
        inptext.setColumns(15);
      
        add(inptext,BorderLayout.PAGE_END);
       showtext.setEditable(false);
       showtext.setFocusable(false);
        Cursor cs=new Cursor(12);//A class to encapsulate the bitmap representation of the mouse cursor.
       showtext.setCursor(cs);
        inptext.addKeyListener(this);
       showtext.setForeground(Color.BLUE);
        add(showtext,BorderLayout.EAST);
        jnam=new JLabel();
        jp=new JPanel();
     
        jp.setLayout(new GridLayout(15,1));
        pack();
        jnam.setText("   Online Friends   ");
        //jp.add(jnam);// ADDING ON THIS PANEL 
 ///////////////////////////////////////////   design end 
        JButton group=new JButton();
        group.setText("Group Chat");
        group.addActionListener(this);
        group.setVisible(true);
       jp.add(group);
        for(int i=0;i<15;i++)
        {
            btn[i]=new JButton();
            btn[i].setText(i+"");
            btn[i].addActionListener(this);
            btn[i].setVisible(false);
            jp.add(btn[i]);
        }
        add(jp,BorderLayout.WEST);
        pack();
        CThread read=new CThread(inFromServer, showtext,btn,outToServer,nm);
          inptext.validate();
          showtext.validate();
          
         /*privateChat pri = new privateChat();
                   pri.setVisible(true);
                 
                   privatChatSignal = pri.signal_for_private_chat; 
                   PrivateNameCollection_here = pri.PrivateNameCollection;
                   pri.setTitle(nm);*/
          setSize(500,600);
          pack();
          setVisible(true);
          pack();
          setResizable(false);   
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       
        
        String str=e.getActionCommand();
        cl_no=str;
    }
 
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==10)
        {
            String sentence=inptext.getText();
            inptext.setText("");
         
            if (privatChatSignal==1) {
                
                privatChatSignal=0;
                for (int i = 0; i < 15; i++) {
                    
                    for (int j = 0; j < 15; j++) {
                        if (PrivateNameCollection_here[i].equals(client_list[j])) {
               try 
                {
                    outToServer.writeBytes(client_list[j]+" "+nm+" --> : "+sentence+'\n');
                } 
               catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                        }
                    }
                    }
                    
                }
                


            else{
                   try {                              
                outToServer.writeBytes(cl_no+" "+nm+" --> : "+sentence+'\n');// WRITING TO SERVER 
               System.out.println(cl_no+" "+nm+" --> : "+sentence+'\n');
            } catch (IOException ex) {
               // System.out.println(":)");
            }
            }
         
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
class CThread extends Thread{
	BufferedReader inFromServer;
	TextArea ja;
        JButton btn[]=new JButton[15];
        String ss,nm;
        DataOutputStream out;
    private Object inptext = this.inptext;

        
	public CThread(BufferedReader in, TextArea sw,JButton bt[],DataOutputStream ou,String n) throws UnknownHostException, IOException{
            
		inFromServer=in;
                ja=sw;
                btn=bt;
                out=ou;

                nm=n;
		start();
	}
	public void run(){
		String sentence;
                int num;
               
		try{
			while (true){
					sentence=inFromServer.readLine();
                                        if(sentence.equals("0"))
                                            continue;
                                            
                                        if(sentence.charAt(0)=='S')
                                        {
                                            try{
                                                out.writeBytes(nm+"\n");
                                            }
                                            catch(Exception e){}
                                        }

                                        if(sentence.charAt(0) == 'a')
                                        {
                                            num=0;
                                            int i;
                                            for(i=1;sentence.charAt(i)!=' ';i++)
                                                num=num*10+(sentence.charAt(i)-'0');
                                           // System.out.println("FROM CTHREAD : "+nm+" "+sentence.substring(i+1).equals("B"));
                                            
                                            
                                            if(!nm.equals(sentence.substring(i+1)))//one to one chatCLIENT'S NAME = sentence.substring(i+1)
                                            {
                                                btn[num].setVisible(true);
                                                btn[num].setText(sentence.substring(i+1));
                                            
                                            }
                                            else
                                                btn[num].setVisible(false);
                                        //    System.out.println(sentence);
                                        }
                                        else if(sentence.charAt(0)=='b')
                                        {
                                            num=0;
                                            for(int i=1;sentence.charAt(i)!=' ';i++)
                                                num=num*10+(sentence.charAt(i)-'0');
                                            btn[num].setVisible(false);
                                        }
                                        else
                                        {
                                           // System.out.println(sentence );
                                            ss='\n'+ja.getText();
                                            ja.setText(sentence.substring(1)+ss);
                                        }
	//			}
			}		
		}catch(Exception e){}		
	}
}