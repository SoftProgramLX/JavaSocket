# JavaSocket
使用java的socket搭建一个简单的客户端与服务端的交互。

效果图如下：<br>
![screen.gif](http://upload-images.jianshu.io/upload_images/301102-df9207d0ec005c5d.gif?imageMogr2/auto-orient/strip)<br>

代码和功能都很简单，就不用解释了，直接上代码。
服务端代码如下：<br>
```
public class SimpleChatServer {  
    ArrayList<PrintWriter> clientOutputStreams;  
    public static void main(String[] args){  
        new SimpleChatServer().go();  
    }  
      
    public class ClientHandler implements Runnable{  
        BufferedReader reader;  
        Socket sock;  
        public ClientHandler(Socket clientSocket){  
            try{  
                sock = clientSocket;  
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());  
                reader = new BufferedReader(isReader);  
            }catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }  
        @Override  
        public void run() {  
            String message;  
            try{  
                while((message = reader.readLine()) != null){  
                    System.out.println("server: read: " + message);  
                    tellEveryone(message);  
                }  
            }catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }  
    }  
      
    public void tellEveryone(String message){  
        Iterator<PrintWriter> it = clientOutputStreams.iterator();  
        while(it.hasNext()){  
            try{  
                PrintWriter writer = (PrintWriter)it.next();  
                writer.println(message);  
                writer.flush();  
            }catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }  
    }  
      
    public void go(){  
        clientOutputStreams = new ArrayList<PrintWriter>();  
        try{  
            ServerSocket serverSock = new ServerSocket(5000);  
            while(true){  
                Socket clientSocket = serverSock.accept();  
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());  
                clientOutputStreams.add(writer);  
                Thread t = new Thread(new ClientHandler(clientSocket));  
                t.start();  
                System.out.println("got a connection");  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
    }  
} 
```

客户端代码如下：<br>
```
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
                writer.println(clientName+" : "+outgoing.getText());  
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
```

<br>
源码请点击[github地址](https://github.com/SoftProgramLX/JavaSocket)下载。
---
QQ:2239344645    [我的github](https://github.com/SoftProgramLX?tab=repositories)<br>
