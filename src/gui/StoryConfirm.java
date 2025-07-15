package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/*ルール説明のウィンドウを表示するためのクラス*/
public class StoryConfirm extends JFrame implements ActionListener{
	
	//コンストラクタ
	public StoryConfirm() {
		setUIFont(new Font("Meiryo UI", Font.PLAIN, 16));
		showRule();
	}
	
	/*テキスト本文の内容を記載*/
	private String getStoryText() {
		return
				"海でいろんな冒険をしてきた、なかよしの海賊がふたり。"
				+ "\n" + "このふたり、なぜか毎週水曜日になると、ちょっとふしぎなことをするんだ。"
				+ "\n"
				+ "\n" + "それはね、おたがいに集めた12個の宝を持ちよって"
				+ "\n" + "「どっちがその宝を手に入れるか」を、ゲームで決めるっていうもの。"
				+ "\n"
				+ "\n" + "でもこのゲーム、ただの遊びってわけじゃない。"
				+ "\n" + "頭も使うし、ちょっと運もいる。ズルはなしってのが海賊たちのルール。"
				+ "\n"
				+ "\n" + "勝ったほうは、宝ぜんぶをゲット！"
				+ "\n" + "負けたほうは、くやしがりながら次の水曜日にリベンジをたくらむ。"
				+ "\n"
				+ "\n" + "いったいどっちが、いちばん多くの宝を手に入れるのか――？"
				+ "\n" + "毎週水曜日、今日もまた、ふたりの勝負の火ぶたが切って落とされる！";
	}

	/*操作を受け取ったルール説明のウィンドウを表示する動作*/
	public void actionPerformed(ActionEvent e) {
		setUIFont(new Font("Meiryo UI", Font.PLAIN, 16));
		showRule();
	}
	
	private void showRule() {
		JOptionPane.showMessageDialog(null,
				getStoryText(),
				"ものがたり",
				JOptionPane.INFORMATION_MESSAGE);
	}
	

	// フォント設定の動作
	private static void setUIFont(Font font) {
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, new javax.swing.plaf.FontUIResource(font));
			}
		}
	}
	
}
