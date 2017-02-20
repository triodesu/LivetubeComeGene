import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Comment {
 public int comNo;//OK
 public String nichiJi;//OK
 public String name;//OK
 public String comStr;//OK
 public String imgUrl;//OK
 public Image comImg;
 public boolean midorikoteFlg;//OK


public ArrayList addComment(ArrayList comList, String htmlStr) throws Exception{
	//渡されたHTMLをコメントごとにカット(50コメントあるなら要素数51のができる)
	String[] cutHtml = htmlStr.split("<div style=\"margin-top: 5px;\">");

	int i = 0;
	while(cutHtml.length - 1  > i++){

		//コメントは５行固定なので、分解したhtmlをさらに分解
		String[] cutComment = cutHtml[i].split("\n");

		//■■■■■■コメント画像URL抽出■■■■■■
		comImg = null;
		if(cutComment[1] != null && !cutComment[1].equals("")){
			//画像がある場合URL抽出
			int index = indexOfKetsu(cutComment[1],"\"");
			imgUrl = "http://livetube.cc" + cutComment[1].substring(index,cutComment[1].indexOf("\"",index));

			//画像読み込み
			try{
				URL url = new URL(imgUrl);
				comImg  = ImageIO.read(url);
			}catch(Exception e){
				e.printStackTrace();
			}

		}

		//■■■■■■コメント番号抽出■■■■■■
		comNo = Integer.parseInt(cutComment[2].substring(0,cutComment[2].indexOf(":")).trim());

		//■■■■■■緑コテかどうかの判定■■■■■■
		if(cutComment[2].indexOf("<span style=\"font-size:1em;color:green; font-weight:bolder;\">") != -1){
			midorikoteFlg = true;
		} else{
			midorikoteFlg = false;
		}

		//■■■■■■名前の抽出■■■■■■
		if(midorikoteFlg){
			//緑コテの場合
			int index = indexOfKetsu(cutComment[2],("<span style=\"font-size:1em;color:green; font-weight:bolder;\">"));
			name = cutComment[2].substring(index, cutComment[2].indexOf("</span></a>"));
//			System.out.println("緑コテ名前" + name);
		}else{
			//緑コテ以外
			name = cutComment[2].substring(indexOfKetsu(cutComment[2],":"),cutComment[2].indexOf("<span style=\"\">"));
//			System.out.println("緑コテ外の名前" + name);
		}

		//■■■■■■日時の抽出■■■■■■
		//緑コテ以外
		int index = indexOfKetsu(cutComment[2],"<span style=\"\">");
		nichiJi = cutComment[2].substring(index,cutComment[2].indexOf("</span>",index));
//		System.out.println("日時" + nichiJi);

		//■■■■■■コメントの抽出■■■■■■
		index = indexOfKetsu(cutComment[3],"<span style=\"font-weight:bold;margin-bottom:0px; padding-bottom:0px;\">");
		comStr = cutComment[3].substring(index,cutComment[3].indexOf("</span>",index));
//		System.out.println("コメント" + comStr);


		//セット用コメントクラス
		Comment comment = new Comment();

		comment.setComNo(comNo);
		comment.setNichiJi(nichiJi);
		comment.setName(name);
		comment.setComStr(comStr);
		comment.setImgUrl(imgUrl);
		comment.setComImg(comImg);
		comment.setMidorikoteFlg(midorikoteFlg);

		comList.add(comment);



	}
	return comList;
}

public int indexOfKetsu(String str,String kensakumoji){
	int ketsu = str.indexOf(kensakumoji) + kensakumoji.length();
	return ketsu;
}

public int getComNo() {
	return comNo;
}
public void setComNo(int comNo) {
	this.comNo = comNo;
}
public String getNichiJi() {
	return nichiJi;
}
public void setNichiJi(String nichiJi) {
	this.nichiJi = nichiJi;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getComStr() {
	return comStr;
}
public void setComStr(String comStr) {
	this.comStr = comStr;
}
public boolean isMidorikoteFlg() {
	return midorikoteFlg;
}
public void setMidorikoteFlg(boolean midorikoteFlg) {
	this.midorikoteFlg = midorikoteFlg;
}
public String getImgUrl() {
	return imgUrl;
}
public void setImgUrl(String imgUrl) {
	this.imgUrl = imgUrl;
}

public Image getComImg() {
	return comImg;
}

public void setComImg(Image comImg) {
	this.comImg = comImg;
}


}
