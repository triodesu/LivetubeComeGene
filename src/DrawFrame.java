import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class DrawFrame extends JPanel implements Runnable{
	JFrame frame = new JFrame();
	int ite = 0;
	String moji;
	//コメント保持用
	ArrayList commentList;
	DrawFrame(){
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(0, 0, 1920, 1080);
	    frame.setTitle("タイトル");
	    frame.add(this);
	    frame.setVisible(true);

	}
	  public void paintComponent(Graphics g){
		    Graphics2D g2 = (Graphics2D)g;

		    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		    g2.setFont(new Font(null,Font.BOLD,40));

		    g2.clearRect(0, 0, 1920, 1080);
		    g2.drawString(moji, ite, 100);

	 }
	public void run() {
		while(true){
			try{Thread.sleep(60);}catch(Exception e){e.printStackTrace();}
			if(commentList != null && commentList.size() > 0){
			ite+=10;
			Comment comment = (Comment)commentList.get(commentList.size()-1);
			moji = comment.getComStr();
			repaint();
			if(ite == 1080)
				ite = 0;
			}
		}

	}

//		    g2.draw(new Line2D.Double(100.0d, 100.0d, 200.0d, 100.0d));
		  }
	/*
	public void paint(Graphics g){
		setFont(new Font(null,Font.BOLD,40));
		setBackground(Color.RED);
//		for(int i = 1920; i == 0; i-=10){
		g.drawString("テストおまんまん",1000 , 500);
		repaint();
		try{Thread.sleep(100);}catch(Exception e){e.printStackTrace();}
		//}
	}
	*/
