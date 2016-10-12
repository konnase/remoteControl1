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
	     * 捕获屏幕尺寸，为截屏做准备
	     */
	    try {

	      robot = new Robot();
	      Point p = new Point(0, 0);
	      // 获得屏幕大小
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
	        // 根据矩形rect大小进行截屏，得到BUfferedImage对象
	        img = robot.createScreenCapture(rect);
	        // BUfferedImage没有序列化，先包装成ImageIcon再写出去
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