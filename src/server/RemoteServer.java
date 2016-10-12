package server;

import java.awt.Event;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteServer {

	  private static Socket st;
	  private static ObjectInputStream objectIn;
	  private SendThread sendThread;
	  private EventThread eventThread;

	  public static void main(String[] args) {
	    ServerSocket server;
	    try {
	      server = new ServerSocket(1234);
	      st = server.accept();
	      System.out.println("已连接");
	      // 新开两个线程，一个发送截屏，一个接收鼠标键盘并进行模拟操作
	      new SendThread(st).start();
	      
	      //System.out.println("程序运行结束");
	      objectIn = new ObjectInputStream(st.getInputStream());
	      new EventThread(objectIn).start();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	  
	  
}