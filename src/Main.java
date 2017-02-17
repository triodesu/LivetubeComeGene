import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main implements ActionListener, Runnable{

	final int COMENTREC = 5;
	JTextField livetubeUrlFld = new JTextField();
	JButton startBtn = new JButton("開始");
	JButton entBtn = new JButton("終了");
	JLabel  label = new JLabel("URL");
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
		JFrame frame = new JFrame("らいぶちゅーぶコメントじぇねれーた");
		frame.setSize(700,300);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		livetubeUrlFld.setBounds(50, 100, 400, 50);
		startBtn.setBounds(500, 50, 100, 50);
		entBtn.setBounds(500, 150, 100, 50);
		label.setBounds(50, 50, 400, 50);

		startBtn.addActionListener(this);
		entBtn.addActionListener(this);

		frame.add(livetubeUrlFld);
		frame.add(startBtn);
		frame.add(entBtn);
		frame.add(label);

		frame.setVisible(true);

	}

	public static void main(String[] args) {
		Main main = new Main();
		main.newFrame();
	}

	//★★★★★アクション
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("開始")){
			//URLを読み込みID取得
			try{
				drawFrame = new DrawFrame();
				URL url = new URL(livetubeUrlFld.getText());
				//テキストフィールドに入力されたURLからIDを取得しにいく
				haishinID = getId(url);

				latestCom = getlatestCom();

				Thread thread = new Thread(this);
				thread.start();

				Thread thread2 = new Thread(drawFrame);
				thread2.start();

//					insComment();
//				    test();

			}catch(Exception e){
				e.printStackTrace();
			}

		}else if(arg0.getActionCommand().equals("終了")){

		}
	}

	public void run() {
		try{
		while(true){
			latestCom = getlatestCom();
			drawFrame.commentList = this.commentList;

//		    if(latestCom != latestCom){

//		    }
		    latestComBack = latestCom;
		}

		}catch(Exception e){
			e.getStackTrace();
		}
	}
	public void testDraw() throws IOException{

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

	public void insComment() throws IOException{

		String urlStr = "http://livetube.cc/stream/" + haishinID + ".comments." + String.valueOf((latestCom - COMENTREC) > 0 ? (latestCom - COMENTREC) : 0);
		URL url = new URL(urlStr);
		InputStream in = url.openStream();

		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String s;
			while ((s=bf.readLine())!=null) {
				commentList.add(s);
			}

			comFrame.setSize(700,300);
			comFrame.setLayout(null);
			comFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JLabel comLabel;
			int comIchi = 0;
			for(int i = commentList.size() - COMENTREC - 1; i < commentList.size() ; i++){
				comLabel = new JLabel(URLDecoder.decode((String)commentList.get(i)));
				comLabel.setBounds(0, 0, 700, comIchi);
				comIchi = comIchi + 30;
				comFrame.add(comLabel);
			}

			comFrame.setVisible(true);

		} finally {
			in.close();
		}
	}


}
