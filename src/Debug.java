import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class Debug extends Canvas{
	boolean flg = true;
	BufferedImage img;

	public void drawImg(BufferedImage img){
		if(flg){
			this.img = img;
			JFrame f = new JFrame();
			f.setSize(img.getWidth()* 2, img.getHeight() * 2);
			f.add(this);
			f.setVisible(true);
			flg = false;


			for(int y = 0; y < img.getHeight(); y++){
			for(int x = 0; x < img.getWidth(); x++){
	                int colorint = img.getRGB(x, y);// 座標の色を取得
	                int red   = (colorint & 0xff0000) >> 16; // 赤抽出
	                int green = (colorint & 0x00ff00) >> 8; // 緑抽出
	                int blue  = (colorint & 0x0000ff) >> 0; // 青抽出

	                Color getColor = new Color(red,green,blue);


	                if(colorint != -1){
	                	System.out.print("■");
	                }else{
	                	System.out.print("□");
	                }
				}
	        	System.out.println();
			}
		}
	}

	public void paint(Graphics g){
	    Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(img,20,20,null);
	}


}
