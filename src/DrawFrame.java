import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class DrawFrame extends JPanel implements Runnable{
	JFrame frame = new JFrame();
	int x = 0;
	int y = 0;
	int fontSize;
	//コメントの始点
	int commentSitenX = 5;

	int windX;
	int windY;

	int commentHyojiSu;

	String moji = "";
	Color bgColor;
	Color fontColor;


	boolean koshin;
	int koshinX = 0;
	int koshinY = 0;

	//コメント保持用
	ArrayList commentList;

	//表示用コメント
	ArrayList<BufferedImage> commentImg = new ArrayList<BufferedImage>();

	//*******折り返し検知に使用
	//検知する範囲
	int chkpic = 10;
	//作成するイメージの高さ用
	int imageSizeY;
	//検知された座標の保持用
	int[] hitZahyo = new int[2];
	//折り返しフラグ
	boolean isOrikaeshi = false;

	//折り返し考慮後のコメント
	ArrayList<String> comStrList = new ArrayList<String>();
	//*******折り返し検知に使用

	boolean animeFlg = false;


	Debug debug = new Debug();


	DrawFrame(Color bg,Color font,int fontSize){

		bgColor = bg;
		fontColor = font;
		this.fontSize = fontSize;
		imageSizeY = fontSize * 5;

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

//		    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//		                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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

		    	int x = 10;
		    	int y = frame.getHeight() - 40;
		    	for(int i = 1; i < commentImg.size() ; i++){
		    		BufferedImage img = (BufferedImage)commentImg.get(i - 1);

		    		y -= img.getHeight();
		    		g2.drawImage(img, x ,y , null);

		    		//コメントは10個まで表示
		    		if(i == 10){
		    			break;
		    		}

		    	}

			    if(animeFlg){
				    int xStart = frame.getWidth() - koshinX;

				    if(xStart <= 20){
				    	xStart = 10;
				    }

				    int yStart = frame.getHeight() - 40 - commentImg.get(0).getHeight();
				    g2.setColor(bgColor);
				    g2.fillRect(0,yStart, frame.getWidth() , frame.getHeight());
				    g2.drawImage((BufferedImage)commentImg.get(0), xStart,yStart , null);
				    return;
			    }
			}



//		    g2.drawString(moji, x, 100);

	 }

	public void makeCommentImg(){

		Color midori = new Color(0,225,0);


		commentImg.clear();

		if(commentList != null && commentList.size() > 0){

			for(int i = 1; i < commentList.size() ; i++){

				int hyojiLine = 0;

				Comment comment = (Comment)commentList.get(commentList.size() - i);
				comStrList.clear();
				comStrList.add(comment.getComStr());
//				comStrList.add("ソフトオンデマンドにハッキングして全動画ダウンロードした時のお話聞かせて");

//				BufferedImage img = new BufferedImage(frame.getWidth(),fontSize * commentSitenX,BufferedImage.TYPE_INT_BGR);
//				Graphics2D g = (Graphics2D)img.getGraphics();


				BufferedImage img = maikeImg();
				Graphics2D g = (Graphics2D)img.getGraphics();


//				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//						RenderingHints.VALUE_ANTIALIAS_OFF);

				//初期化（背景色でぬりつぶし）
				g.setColor(bgColor);
				g.fillRect(0, 0, img.getWidth(),img.getHeight());

				//線を引く
//				g.setColor(nezumi);
//				g.drawLine(0, 2, img.getWidth(), 2);
//				g.drawLine(0, img.getHeight()-2, img.getWidth(), img.getHeight()-2);


				//コメント番号
				g.setColor(fontColor);
				g.setFont(new Font(null,Font.PLAIN,fontSize));
				hyojiLine += fontSize + commentSitenX;
				g.drawString(comment.getComNo() + "  :  " + comment.getNichiJi(), commentSitenX ,hyojiLine);

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
				g.drawString(name, commentSitenX , hyojiLine + 5);


				//コメントセット
				g.setColor(fontColor);
				g.setFont(new Font(null,Font.BOLD,fontSize));
				hyojiLine += 10;
				for(int ia = 0; ia < comStrList.size(); ia++){
					hyojiLine += fontSize;
					g.drawString(comStrList.get(ia), commentSitenX, hyojiLine);
				}


//				for(int x = 0; x < 20; x++){
//				g.drawLine(frame.getWidth()- x - 27,0,frame.getWidth() - x - 27,frame.getHeight());
//				}




				//画像セット
				if(comment.getComImg() != null){
					g.drawImage(comment.getComImg(),frame.getWidth() - comment.getComImg().getWidth(null) * 2,10,null);
				}


//				g.drawString("■■■",649,20);

				commentImg.add(img);


				//新しいコメントを受信したときのみイメージを作るためのフラグ
				koshin = false;
				animeFlg = true;
			}
		}
	}

	public BufferedImage maikeImg(){

		BufferedImage img = new BufferedImage(frame.getWidth(),imageSizeY,BufferedImage.TYPE_INT_BGR);

//		g.drawString((String)comStrList.get(0), commentSitenX, fontSize);
		drawComment(img);

		if(chkOrikaeshi(img)){
			img = setOrikaeshi(img);
		}else{
			img = new BufferedImage(frame.getWidth(),imageSizeY,BufferedImage.TYPE_INT_BGR);
			return img;
		}


		return img;
	}

	public void drawComment(BufferedImage img){
		Graphics2D g = (Graphics2D)img.getGraphics();
		//初期化（背景色でぬりつぶし）
		g.setColor(bgColor);
		g.fillRect(0, 0, img.getWidth(),img.getHeight());

		g.setColor(fontColor);

		int size = comStrList.size();
		g.setFont(new Font(null,Font.BOLD,fontSize));
		g.drawString((String)comStrList.get(size - 1),commentSitenX,fontSize);

//		debug.drawImg(img);

	}

	public void drawCommentChk(BufferedImage img,int count){
		Graphics2D g = (Graphics2D)img.getGraphics();
		//初期化（背景色でぬりつぶし）
		g.setColor(bgColor);
		g.fillRect(0, 0, img.getWidth(),img.getHeight());

		g.setColor(fontColor);
		int size = comStrList.size();
		g.setFont(new Font(null,Font.BOLD,fontSize));
		g.drawString((String)comStrList.get(size - 1).substring(0,count),commentSitenX, fontSize);

	}

	public BufferedImage setOrikaeshi(BufferedImage img){
		Graphics2D g = (Graphics2D)img.getGraphics();
		int count = comStrList.get(comStrList.size() - 1).length();
		boolean shokai = true;
		while(true){
			count--;
//			debug.drawImg(img);
			try{
				drawCommentChk(img,count);
				if(chkOrikaeshi(img) == false){
					String saishuGyo = (String)comStrList.get(comStrList.size()-1);
					comStrList.remove(comStrList.size()-1);
					comStrList.add(saishuGyo.substring(0,count - 1));
					comStrList.add(saishuGyo.substring(count - 1));

					count = comStrList.get(comStrList.size() - 1).length();
					drawCommentChk(img,count);
					if(chkOrikaeshi(img) == false){
						break;
					}
				}
			}catch(Exception e){
				break;
			}

/*            int colorint = img.getRGB(hitZahyo[0],hitZahyo[1]);// 座標の色を取得
            int red   = (colorint & 0xff0000) >> 16; // 赤抽出
            int green = (colorint & 0x00ff00) >> 8; // 緑抽出
            int blue  = (colorint & 0x0000ff) >> 0; // 青抽出
            Color getColor = new Color(red,green,blue);

            if(getColor.equals(fontColor)){
//            	debug.drawImg(img);
//            	System.out.println("■■■■■■■■■■■");
            	hitMoji = count - 1;
            	//文字を編集
            	String saishuGyo = (String)comStrList.get(comStrList.size()-1);
//            	comStrList.add(comStrList.size()-1,saishuGyo.substring(0,hitMoji));
            	comStrList.remove(comStrList.size()-1);
            	comStrList.add(saishuGyo.substring(0,hitMoji));
            	comStrList.add(saishuGyo.substring(hitMoji));
            	drawComment(img);
            	if(chkOrikaeshi(img) == false){
//            		debug.drawImg(img);
//            		System.out.println(comStrList.get(0));
//            		System.out.println(comStrList.get(1));
//            		System.out.println(comStrList.get(3));
//            		System.out.println(comStrList.get(4));
//            		System.out.println(comStrList.get(5));
            		break;
            	}
            }*/
		}

		img = new BufferedImage(frame.getWidth(),imageSizeY + comStrList.size() * fontSize,BufferedImage.TYPE_INT_BGR);

		return img;
	}

	public boolean chkOrikaeshi(BufferedImage img){

		for(int x = 1; x < chkpic; x++){
			for(int y = 0; y < img.getHeight(); y++){
//				System.out.println(img.getWidth() - x);
                int colorint = img.getRGB(img.getWidth()- x, y);// 座標の色を取得
                int red   = (colorint & 0xff0000) >> 16; // 赤抽出
                int green = (colorint & 0x00ff00) >> 8; // 緑抽出
                int blue  = (colorint & 0x0000ff) >> 0; // 青抽出

                Color getColor = new Color(red,green,blue);


                if(colorint != -1){
//                	System.out.println("黒");
                }


                //ヒットした座標の取得
                if(getColor.equals(fontColor)){
                	isOrikaeshi = true;
                	hitZahyo[0] = img.getWidth() - x;
                	hitZahyo[1] = y;
//                	System.out.println("XXXXXXX" + (img.getWidth() - x));
//                	System.out.println("YYYYYYY" + y);
                	break;
                }else{
                	isOrikaeshi = false;
                }
			}
			if(isOrikaeshi){
				break;
			}
		}
		return isOrikaeshi;
	}


	public void run() {
		while(true){


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


			if(animeFlg){
				for(int ite = koshinX; ite < frame.getWidth(); ite+=20){
					koshinX = ite;
					repaint();
					try{Thread.sleep(20);}catch(Exception e){e.printStackTrace();}
				}
				koshinX = 0;
				animeFlg = false;
			}else{
				try{Thread.sleep(1000);}catch(Exception e){e.printStackTrace();}
				repaint();
			}
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
