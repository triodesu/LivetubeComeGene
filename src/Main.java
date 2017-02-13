import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main implements ActionListener {

	final int COMENTREC = 5;
	JTextField livetubeUrlFld = new JTextField();
	JButton startBtn = new JButton("開始");
	JButton entBtn = new JButton("終了");
	JLabel  label = new JLabel("URL");
	//配信ID取得用
	String haishinID;
	//最新コメント番号保持用
	int latestCom = 0;
	//コメント保持用
	ArrayList commentList = new ArrayList();
	//コメント用フレーム
	JFrame comFrame = new JFrame("コメントフレーム");

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
				URL url = new URL(livetubeUrlFld.getText());
				//テキストフィールドに入力されたURLからIDを取得しにいく
				haishinID = getId(url);

				//最新コメント番号を取得ににいき、新しいコメントがあれば、コメントをながす
				    latestCom = getlatestCom();
				    System.out.println(latestCom);
//					insComment();
				    test();

			}catch(Exception e){

			}

		}else if(arg0.getActionCommand().equals("終了")){

		}
	}

	public void test() throws IOException{

			//コメント取得用URL生成
			String urlStr = "http://livetube.cc/stream/" + haishinID + ".comments." + String.valueOf(latestCom);
			System.out.println(2);
			URL url = new URL(urlStr);
			System.out.println(3);
			InputStream in = url.openStream();
			System.out.println(4);
			StringBuilder sb = new StringBuilder();
			System.out.println(5);
			try {
				BufferedReader bf = new BufferedReader(new InputStreamReader(in));
				System.out.println(6);
				String s;
				while ((s=bf.readLine())!=null) {
					sb.append(s);
				}

				System.out.println("結果　"+URLDecoder.decode(sb.toString()));

			} finally {
				in.close();
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

		} finally {
			in.close();
		}
		return haishinID;
	}
	public int getlatestCom() throws IOException{

		//最新コメント番号を取得しにいく
		int comNo = 0;

			//コメント取得用URL生成
			String urlStr = "http://livetube.cc/stream/" + haishinID + ".comments";
			URL url = new URL(urlStr);
			InputStream in = url.openStream();
			StringBuilder sb = new StringBuilder();
			try {
				BufferedReader bf = new BufferedReader(new InputStreamReader(in));

				String s;

				String joken = "^([0-9]+)\\s:\\s\\s+([0-9]+\\/[0-9]+\\s[0-9]+\\:[0-9]+)\\s(\\S+)$";
//				String joken = "^([0-9]+)\\s:\\s(\\S+)\\s+([0-9]+\\/[0-9]+\\s[0-9]+\\:[0-9]+)\\s(\\S+)$";
				Pattern p = Pattern.compile(joken);
				Matcher m;
				while ((s=bf.readLine())!=null) {
					sb.append(s);
					sb.append("\n");
					m = p.matcher(URLDecoder.decode(s.toString()));
					if(m.find()){
						System.out.println("group1:" + m.group(1));
						System.out.println("group2:" + m.group(2));
						System.out.println("group3:" + m.group(3));
					}else{
						System.out.println("まっちできてない→" + URLDecoder.decode(s.toString()));
					}
					comNo++;
				}
//				System.out.println(URLDecoder.decode(sb.toString()));
			} finally {
				in.close();
			}
		return comNo;
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
