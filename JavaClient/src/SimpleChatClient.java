import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SimpleChatClient {  
    JTextArea incoming;  
    JTextField outgoing;  
    BufferedReader reader;  
    PrintWriter writer;  
    Socket sock;  
    static String clientName;  
  
    public static void main(String[] args) {  
        SimpleChatClient client = new SimpleChatClient();  
        clientName = JOptionPane.showInputDialog("Please input the client name :");  
        client.go();  
    }  
  
    public void go() {  
        // build GUI  
        JFrame frame = new JFrame(clientName + "'s Chat Client");  
        JPanel mainPanel = new JPanel();  
        incoming = new JTextArea(15,20);    
        incoming.setLineWrap(true);  
        incoming.setWrapStyleWord(true);  
        incoming.setEditable(false);  
  
        JScrollPane qScroller = new JScrollPane(incoming);  
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);  
  
        outgoing = new JTextField(20);  
  
        JButton sendButton = new JButton("Send");  
        sendButton.addActionListener(new SendButtonListener());  
  
        mainPanel.add(qScroller);  
        mainPanel.add(outgoing);  
        mainPanel.add(sendButton);  
  
        setUpNetworking();  
        Thread readerThread = new Thread(new IncomingReader());  
        readerThread.start();  
  
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);  
        frame.setSize(400,500);  
        frame.setVisible(true);  
  
    } // close go  
  
    private void setUpNetworking() {   
        try {  
            sock = new Socket("127.0.0.1", 5000);  
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());  
            reader = new BufferedReader(streamReader);  
  
            writer = new PrintWriter(sock.getOutputStream());  
  
            System.out.println("networking established");  
        } catch(IOException ex) {  
            ex.printStackTrace();  
        }  
    } // close setUpNetworking    
  
    public class SendButtonListener implements ActionListener {  
        public void actionPerformed(ActionEvent ev) {  
            try {  
                writer.println("cl:"+clientName+"client says: "+outgoing.getText());  
                writer.flush();  
            } catch(Exception ex) {  
                ex.printStackTrace();  
            }  
            outgoing.setText("");  
            outgoing.requestFocus();  
        }  
    }  // close SendButtonListener inner class  
  
    public class IncomingReader implements Runnable {  
        public void run() {  
            String message = null;              
            try {  
                while ((message = reader.readLine()) != null) {                         
                    System.out.println("client1: read: " + message);  
                    incoming.append(message + "\n");  
                } // close while  
            } catch(Exception ex) {ex.printStackTrace();}  
        } // close run  
    } // close inner class      
}  
