package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/*ルール説明のウィンドウを表示するためのクラス*/
public class RuleConfirm extends JFrame implements ActionListener{
	
	//コンストラクタ
	public RuleConfirm() {
		setUIFont(new Font("Meiryo UI", Font.PLAIN, 16));
		showRule();
	}
	
	private String getRuleText() {
		return
				"①守り側は、宝箱をとられないように1～12の数字がかかれた宝箱を1つを選んでバクダンを仕掛ける。"
				+ "\n" + "②攻め側は好きな宝箱を1つ選び、バクダンが入ってなければ、その数字と同じポイントをゲット！その宝箱はなくなるよ"
				+ "\n" + "③もしバクダンにあたると、あつめたポイントは全部なくなる...でもその宝箱はそのまま残るよ"
				+ "\n" + "④攻守を交代しながら、最大で8ラウンドまでゲームはつづく。"
				+ "\n" + "⑤勝者が決まるか宝箱が1つだけになった時点でゲームはおわり！"
				+ "\n"
				+ "\n" + "【勝利条件】"
				+ "\n" + "・40点以上集める"
				+ "\n" + "・相手を3回バクダンにあてる"
				+ "\n" + "・ゲームがおわったとき相手より得点が多い"
				+ "\n"
				+ "\n" + "※1 相手のプレイ中は画面を見ないようにしよう！"
				+ "\n" + "※2 もう一度あそびかたを見たいときはメニューから確認できるよ！";
	}

	/*操作を受け取ったルール説明のウィンドウを表示する動作*/
	public void actionPerformed(ActionEvent e) {
		setUIFont(new Font("Meiryo UI", Font.PLAIN, 16));
		showRule();
	}
	
	private void showRule() {
		JOptionPane.showMessageDialog(null,
				getRuleText(),
				"あそびかた",
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
