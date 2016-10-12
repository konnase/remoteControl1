package client;

import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class RemoteClient extends JFrame {

	  private JPanel contentPane;
	  Socket socket;
	  ObjectInputStream ins;
	  boolean isAlive = true;
	  private ImageIcon icon;

	  public static void main(String[] args) {
	    Socket socket = null;
	    try {
	      socket = new Socket("192.168.85.133", 1234);
	      
	    } catch (UnknownHostException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    new RemoteClient(socket);
	  }

	  /**
	   * Create the frame.
	   */
	  public RemoteClient(Socket socket) {
	    this.socket = socket;
	    try {
	      ins = new ObjectInputStream(socket.getInputStream());
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(0, 0, 1200, 700);
	    contentPane = new MyPanel();

	    JScrollPane jsp = new JScrollPane(contentPane);
	    jsp.setAutoscrolls(true);
	    add(jsp);
	    setVisible(true);
	    new Receive(socket).start();
	  }

	  /**
	   * 接收服务器的图片
	   */
	  class Receive extends Thread {
		  
		  private ObjectOutputStream objectOut;
		  private Socket socket;
		  public Receive(Socket socket){
			  this.socket = socket;
			  try {
				objectOut = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }

	    public void run() {
	      try {
	        while (isAlive) {
	          System.out.println("开始读");
	          icon = (ImageIcon) ins.readObject();
	          System.out.println(icon);
	          System.out.println("读完了");
	          // 根据图片大小定义contentPane大小。
	          contentPane.setSize(icon.getIconWidth(),
	              icon.getIconHeight());
	          contentPane.repaint();
	          
	          //绑定键盘事件
	          addKeyListener(new KeyListener(){ 
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					sendEvent(e);
				}
				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					sendEvent(e); 
				}
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					sendEvent(e);
				} 

	          }); 
	          
	        //绑定鼠标事件 

	          contentPane.addMouseListener(new MouseListener(){ 

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					//sendEvent(e);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					//sendEvent(e);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					//sendEvent(e);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					sendEvent(e);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					sendEvent(e);
				} 

	          });  

	          //绑定鼠标移动事件 

	          contentPane.addMouseMotionListener(new MouseMotionListener(){ 

	              @Override 

	              public void mouseDragged(MouseEvent e) { 

	                  sendEvent(e); 

	              } 

	              @Override 

	              public void mouseMoved(MouseEvent e) { 

	                  sendEvent(e); 

	              } 

	          });

	          Thread.sleep(50);
	        }
	      } catch (Exception e1) {
	        e1.printStackTrace();
	      }
	    }
	    
	    public void sendEvent(InputEvent event){
	    	
	        try {
	        	
	            objectOut.writeObject(event);
	            objectOut.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	  }

	  class MyPanel extends JPanel {
	    public void paint(Graphics g) {
	      super.paint(g);
	      g.drawImage(icon.getImage(), 0, 0, contentPane);
	    }
	  }

	}