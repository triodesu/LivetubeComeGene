import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main implements ActionListener, Runnable{

	//メインフレーム
	JFrame frame;

	JTextField livetubeUrlFld = new JTextField();
	JTextField bgRFld = new JTextField();
	JTextField bgGFld = new JTextField();
	JTextField bgBFld = new JTextField();

	JTextField fontRFld = new JTextField();
	JTextField fontGFld = new JTextField();
	JTextField fontBFld = new JTextField();

	JTextField fontSizeFld = new JTextField();

	Color bgColor;
	Color fontColor;
	int fontSize;


	JButton startBtn = new JButton("開始");
	JButton entBtn = new JButton("終了");
	JLabel  label = new JLabel("URL");
	JLabel  label2 = new JLabel("背景色（RGB）");
	JLabel  label3 = new JLabel("文字色（RGB）");
	JLabel  label4 = new JLabel("文字サイズ");

	//配信ID取得用
	String haishinID;
	//最新コメント番号保持用
	int latestCom = 0;
	//最新コメント番号バッファ用
	int latestComBack = 0;
	//コメント保持用
	ArrayList commentList = new ArrayList();
	//コメント用フレーム
	JFrame comFrame = new JFrame("コメントフレーム");
	//描画用フレーム
	DrawFrame drawFrame;

	public void newFrame(){
		frame = new JFrame("らいぶちゅーぶコメントじぇねれーた");
		frame.setSize(700,300);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//URLテキストフィールド
		livetubeUrlFld.setBounds(50, 100, 400, 50);

		//背景色RGBA
		bgRFld.setBounds(50, 190, 30, 20);
		bgGFld.setBounds(80, 190, 30, 20);
		bgBFld.setBounds(110, 190, 30, 20);

		bgRFld.setText("255");
		bgGFld.setText("255");
		bgBFld.setText("255");

		//フォント色RGBA
		fontRFld.setBounds(230, 190, 30, 20);
		fontGFld.setBounds(260, 190, 30, 20);
		fontBFld.setBounds(290, 190, 30, 20);

		fontRFld.setText("0");
		fontGFld.setText("0");
		fontBFld.setText("0");

		//フォントサイズ
		fontSizeFld.setText("12");
		fontSizeFld.setBounds(380, 190, 30, 20);

		//各種ボタン
		startBtn.setBounds(500, 50, 100, 50);
		entBtn.setBounds(500, 150, 100, 50);

		//各種ボタンにリスナーセット
		startBtn.addActionListener(this);
		entBtn.addActionListener(this);

		//各種ラベル
		label.setBounds(50, 50, 400, 50);
		label2.setBounds(50, 120, 100, 100);
		label3.setBounds(230, 120, 100, 100);
		label4.setBounds(380, 120, 100, 100);


		//各種add
		frame.add(livetubeUrlFld);

		frame.add(bgRFld);
		frame.add(bgGFld);
		frame.add(bgBFld);

		frame.add(fontRFld);
		frame.add(fontGFld);
		frame.add(fontBFld);

		frame.add(fontSizeFld);

		frame.add(startBtn);
		frame.add(entBtn);
		frame.add(label);
		frame.add(label2);
		frame.add(label3);
		frame.add(label4);


		frame.setVisible(true);

	}

	public static void main(String[] args) {
		Main main = new Main();
		main.newFrame();
	}

	//★★★★★アクション
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("開始")){
			try{

				//URLを読み込み
				URL url = new URL(livetubeUrlFld.getText());

				//背景色、文字色、文字サイズを読み込み
				bgColor = new Color(toInt(bgRFld.getText()),toInt(bgGFld.getText()),toInt(bgBFld.getText()));
				fontColor = new Color(toInt(fontRFld.getText()),toInt(fontGFld.getText()),toInt(fontBFld.getText()));
				fontSize = toInt(fontSizeFld.getText());

				//描画用クラスインスタンス化
				drawFrame = new DrawFrame(bgColor,fontColor,fontSize);

				//テキストフィールドに入力されたURLからIDを取得しにいく
				haishinID = getId(url);

				latestCom = getlatestCom();

				drawFrame.setCommentList(commentList);

				Thread thread = new Thread(this);
				thread.start();

				Thread thread2 = new Thread(drawFrame);
				thread2.start();


			}catch(Exception e){
				e.printStackTrace();
			}

		}else if(arg0.getActionCommand().equals("終了")){

		}
	}
	public int toInt(String str){
		int i;
		try{
			i = Integer.parseInt(str);
		}catch(Exception e){
			i = 0;
		}

		return i;

	}

	public void run() {
		try{
		while(true){
			latestCom = getlatestCom();

			drawFrame.setCommentList(commentList);

//		    if(latestCom != latestCom){

//		    }
		    latestComBack = latestCom;
		}

		}catch(Exception e){
			e.getStackTrace();
		}
	}

	//★★★★★動画ID取得メソッド
	public String getId(URL url) throws IOException {
		InputStream in = url.openStream();
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String s;
			while ((s=bf.readLine())!=null) {
				sb.append(s);
			}
			haishinID = sb.substring(sb.indexOf("var comment_entry_id =") + 24, sb.indexOf("var comment_entry_id =") + 37);
			System.out.println(haishinID);
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			in.close();
		}
		return haishinID;
	}
	public int getlatestCom() throws IOException{

			//コメント取得用URL生成
			URL url;
			if(latestCom == 0){
				//初回ロードの場合
				url = new URL("http://livetube.cc/stream/" + haishinID +".comments");
			}else{
				//二回目以降のロードの場合
				url = new URL("http://livetube.cc/stream/" + haishinID +".comments." + latestCom);
			}

			URLConnection urlcon = url.openConnection();
			urlcon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.63 Safari/537.36");

			InputStream in = urlcon.getInputStream();
			StringBuilder sb = new StringBuilder();

			try {
				BufferedReader bf = new BufferedReader(new InputStreamReader(in));

				String s;

				while ((s=bf.readLine())!=null) {
					sb.append(s);
					sb.append("\n");
				}
//				System.out.println(sb.toString());
				Comment commentClass = new Comment();
				commentClass.addComment(commentList, sb.toString());

			}catch(Exception e){
				e.printStackTrace();
			}
			finally {
				in.close();
			}
		return commentList.size();
	}


}
