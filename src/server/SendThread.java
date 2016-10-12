package server;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

class SendThread extends Thread {
	  Robot robot;
	  ObjectOutputStream os;
	  BufferedOutputStream bos;
	  ImageOutputStream ios;
	  Rectangle rect;
	  private boolean isAlive = true;
	  Socket st;

	  public SendThread(Socket st) {
	    this.st = st;
	    /*
	     * ������Ļ�ߴ磬Ϊ������׼��
	     */
	    try {

	      robot = new Robot();
	      Point p = new Point(0, 0);
	      // �����Ļ��С
	      Toolkit tool = Toolkit.getDefaultToolkit();
	      Dimension dis = tool.getScreenSize();
	      rect = new Rectangle(p, dis);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  public void run() {
	    try {
	      os = new ObjectOutputStream(st.getOutputStream());
	      BufferedImage img = null;
	      while (isAlive) {
	        // ���ݾ���rect��С���н������õ�BUfferedImage����
	        img = robot.createScreenCapture(rect);
	        // BUfferedImageû�����л����Ȱ�װ��ImageIcon��д��ȥ
	        ImageIcon icon = new ImageIcon(img);
	        os.writeObject(icon);
	        os.flush();
	        Thread.sleep(5000);
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	}