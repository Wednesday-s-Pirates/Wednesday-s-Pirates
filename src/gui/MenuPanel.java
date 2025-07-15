package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.GameInitializer;
import game.GameLogic;
import sound.SoundPlayer;

/*GameWindowを開いている際に、右上にメニューパネルを表示するためのクラス
  各クラスから動作を受け取る、もしくは各クラスのパネルを表示する役割を持つ*/
public class MenuPanel extends JFrame {
	private JPanel panel;
	private JDialog menuDialog;
	private JButton Reset = new JButton("リセット");//リセット
	private JButton end = new JButton("GAME終了");//終了確認用
	private JButton rule = new JButton("あそびかた");
	private JLabel menuLabel = new JLabel("設定");
	private JButton close = new JButton("設定閉じる");

	private GameLogic gameLogic;
	private JFrame firstFrame;

	public MenuPanel(JFrame frame, GameLogic gamelogic) {
		SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
		this.firstFrame = frame;
		this.gameLogic = gamelogic;
		menuDialog = new JDialog(frame, "設定", false);//モーダル設定(trueの場合は一時停止中状態）
		//===========6/16　300～250に変更上野============
		menuDialog.setSize(200, 250);
		//======================================
		menuDialog.setLocationRelativeTo(null); // 中央に表示
		int x = frame.getX() + (frame.getWidth() - 200);
		int y = frame.getY();
		menuDialog.setLocation(x, y);
		//フレームを消してます
		menuDialog.setUndecorated(true);
		//===========================================================
		panel = new JPanel(null);

		//リセット
		Reset.setFont(new Font("SansSerif", Font.BOLD, 12));

		//=================パネルに色を付けました==上野========================================
		panel.setBackground(Color.decode("#ffffcc"));
		panel.add(Reset);

		//リセット時の動作、ロジックとWindowの再生成を行う
		Reset.addActionListener(e -> {
			GameOverPanel.breakDialog(frame,"本当にリセットしてもよろしいですか？");
			if (this.gameLogic != null) {
				String name1 = gameLogic.getPlayer1().getName();
				String name2 = gameLogic.getPlayer2().getName();

				GameLogic newLogic = GameInitializer.initializeGame(name1, name2);
				new GameWindow(firstFrame, newLogic);
				firstFrame.dispose();
			} else {
				System.err.println("GameLogic が null です（MenuPanel）");
			}
		});

		//終了
		end.setFont(new Font("SansSerif", Font.BOLD, 12));
		panel.add(end);
		end.addActionListener(e -> {
			SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
			menuDialog.dispose();
			GameOverPanel.breakDialog(frame,"ゲーム中断しトップに戻ってもいいですか？");
			new TopMenu();
		});

		//遊び方
		rule.setFont(new Font("SansSerif", Font.BOLD, 12));
		panel.add(rule);
		rule.addActionListener(e -> {
			SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
			menuDialog.dispose();
			new RuleConfirm();
		});

		//閉じるボタン追加
		close.setFont(new Font("SansSerif", Font.BOLD, 12));
		panel.add(close);
		close.addActionListener(e -> {
			SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
			menuDialog.dispose();
		});
		//ラベル追加
		menuLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
		menuLabel.setHorizontalAlignment(JLabel.CENTER);
		menuLabel.setOpaque(true);
		menuLabel.setBackground(Color.WHITE);
		panel.add(menuLabel);
		menuDialog.add(panel);
		menuLayout();
		menuDialog.setVisible(true);
	}

	private void menuLayout() {
		int menuDialogWidth = 200;//パネルの幅サイズ
		int btmWidth = 110;
		int btmX = (menuDialogWidth - btmWidth) / 2;
		int btmY = 60;

		int age = 45;

		Reset.setBounds(btmX, btmY, btmWidth, 30);
		rule.setBounds(btmX, btmY + age, btmWidth, 30);
		end.setBounds(btmX, btmY + age * 2, btmWidth, 30);

		int closeWidth = 110;
		int closeX = (menuDialogWidth - closeWidth) / 2;

		close.setBounds(closeX, btmY + age * 3, closeWidth, 35);
		menuLabel.setBounds(0, 0, menuDialogWidth, 35);
	}
}