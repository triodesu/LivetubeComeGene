import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class DrawFrame extends JPanel implements Runnable{
	JFrame frame = new JFrame();
	int x = 0;
	int y = 0;
	int fontSize;

	int windX;
	int windY;

	int commentHyojiSu;


	String moji = "";

	Color bgColor;
	Color fontColor;

	boolean koshin;

	//コメント保持用
	ArrayList commentList;

	//表示用コメント
	ArrayList commentImg = new ArrayList();


	DrawFrame(Color bg,Color font,int fontSize){

		bgColor = bg;
		fontColor = font;
		this.fontSize = fontSize;

        //ウィンドウ範囲を示す枠をつけておく
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(0, 0, 800, 800);
	    frame.setTitle("LiveTubeComment");
	    frame.add(this);
	    frame.setVisible(true);

	}

	public void setCommentList(ArrayList commentList) {
		this.commentList = commentList;
	}

	  public void paintComponent(Graphics g){
		    Graphics2D g2 = (Graphics2D)g;

		    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		    //背景色
		    g2.setBackground(bgColor);
		    //文字色
		    g2.setColor(fontColor);
		    //書体・文字サイズ
		    g2.setFont(new Font(null,Font.BOLD,fontSize));

		    g2.clearRect(0, 0, frame.getWidth(), frame.getHeight());

/*
			if(commentList != null && commentList.size() > 0){

				for(int i = 1; i < commentList.size() ; i++){
					Comment comment = (Comment)commentList.get(commentList.size() - i);

					int x = 10;
					int y = frame.getHeight() - 40 - (i * fontSize);

					g2.drawString(comment.getComStr(), x, y);

//					g2.drawImage(comment.getComImg(),100 , i * 10, this);

				}
			}
*/
		    if(koshin){
			    makeCommentImg();
		    }

			if(commentImg != null && commentImg.size() > 0){

				for(int i = 1; i < commentImg.size() ; i++){
					BufferedImage img = (BufferedImage)commentImg.get(i - 1);

					int x = 10;
					int y = frame.getHeight() - 40 - (i * img.getHeight());

					g2.drawImage(img, 10 ,y , this);


				}
			}



//		    g2.drawString(moji, x, 100);

	 }

	public void makeCommentImg(){

		Color shiro  = new Color(255,255,255);
		Color midori = new Color(0,225,0);
		Color nezumi = new Color(222,222,222);


		commentImg.clear();

		if(commentList != null && commentList.size() > 0){

			for(int i = 1; i < commentList.size() ; i++){

				int hyojiLine = 0;

				Comment comment = (Comment)commentList.get(commentList.size() - i);

				BufferedImage img = new BufferedImage(frame.getWidth(),fontSize * 5,BufferedImage.TYPE_INT_BGR);
				Graphics2D g = (Graphics2D)img.getGraphics();
				//初期化（背景色でぬりつぶし）
				g.setColor(bgColor);
				g.fillRect(0, 0, img.getWidth(),img.getHeight());

				//線を引く
				g.setColor(nezumi);
				g.drawLine(0, 2, img.getWidth(), 2);
				g.drawLine(0, img.getHeight()-2, img.getWidth(), img.getHeight()-2);

				//コメント番号
				g.setColor(fontColor);
				g.setFont(new Font(null,Font.PLAIN,fontSize));
				hyojiLine += fontSize + 5;
				g.drawString(comment.getComNo() + "  :  " + comment.getNichiJi(), 5 ,hyojiLine);

				//名前セット
				if(comment.isMidorikoteFlg()){
					//緑コテ用
					g.setColor(midori);
				}else{
					g.setColor(fontColor);
				}
				g.setFont(new Font(null,Font.ITALIC,fontSize));

				String name = "名無しさん";
				if(comment.getName() != null && !comment.getName().trim().equals("")){
					name = comment.getName();
				}
				hyojiLine += fontSize;
				g.drawString(name, 5 , hyojiLine + 5);


				//コメントセット
				g.setColor(fontColor);
				g.setFont(new Font(null,Font.BOLD,fontSize));
				hyojiLine += fontSize;
				g.drawString(comment.getComStr(), 5, hyojiLine + 10 );

				//画像セット
				if(comment.getComImg() != null){
					g.drawImage(comment.getComImg(),frame.getWidth() - comment.getComImg().getWidth(null) * 2,5,null);
				}

				commentImg.add(img);


				//新しいコメントを受信したときのみイメージを作るためのフラグ
				koshin = false;
			}
		}
	}


	public void run() {
		while(true){
			try{Thread.sleep(60);}catch(Exception e){e.printStackTrace();}


/*			//表示できるコメント数算出
			commentHyojiSu = frame.getHeight()/(fontSize * 5) - 3;


			//コメント数の分、コメント取得
			if(commentList != null && commentList.size() > 0){

				hyojiyoCommentList.clear();

				for(int i = 0; i <= commentHyojiSu ; i++){
					if(commentList.size() > i){
						hyojiyoCommentList.add(commentList.get(commentList.size()-1 -i));
					}
				}
			}
*/

			repaint();
		}

	}


	public void setKoshin(boolean koshin) {
		this.koshin = koshin;
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
