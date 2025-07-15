package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.GameInitializer;
import game.GameLogic;
import model.Player;

public class GameOverPanel {
	private JDialog GameOverDialog;
	private JPanel endPanel; //親
	private JPanel endTopPanel;//上
	private JPanel endCenterPanel;//真ん中
	private JPanel endBottomPanel;//下

	private JLabel label; //文章
	private JLabel nameLabel;//勝利したプレイヤー

	private JButton Redo = new JButton("やり直し"); //やり直しボタン
	private JButton top = new JButton("トップ"); //トップボタン

	private String end_P1_Img = "images/end1.png";
	private String end_P2_Img = "images/end2.png";

	GameLogic gameLogic;
	JFrame firstFrame;

	public GameOverPanel(JFrame frame, Player player1, Player player2, GameLogic gameLogic) {
		GameOverDialog = new JDialog(frame, "終了", true);// モーダル設定(trueの場合は一時停止中状態）
		//フレームを消してます
		GameOverDialog.setUndecorated(true);
		//===========================================================

		this.gameLogic = gameLogic;
		this.firstFrame = frame;

		//画面サイズ
		GameOverDialog.setSize(400, 600);
		endPanel = new JPanel(null);
		GameOverDialog.add(endPanel);
		int x = frame.getX() + (frame.getWidth() - 400) / 2;
		int y = frame.getY() + (frame.getHeight() - 600) / 2;
		GameOverDialog.setLocation(x, y);

		// 画像の上半分を描画
		// 先に初期化
		endTopPanel = new JPanel(null);
		URL endUrl;
		if (player1.getResult() == true) {
			endUrl = getClass().getResource(end_P1_Img);//ここのend_P2_Imgをend_P1_Imgと切り替えると画像が切り替わる。
			System.out.println("player1 WIN");//デバックLog用
		} else {
			endUrl = getClass().getResource(end_P2_Img);
			System.out.println("player2 WIN");//デバックLog用
		}

		if (endUrl != null) {
			int endImageWidth = 400; // 実際の画像幅
			int endImageHeight = 257; // 実際の画像高さ
			Image fullImage = new ImageIcon(endUrl).getImage();
			endCenterPanel = new JPanel(null) {
				protected void paintComponent(Graphics g) {
					g.drawImage(fullImage, 0, 0, getWidth(), getHeight(),
							0, 0, endImageWidth, endImageHeight, this);
					super.paintComponent(g);
				}
			};
			endCenterPanel.setBounds(0, 0, endImageWidth, endImageHeight);
			endCenterPanel.setOpaque(false);
			endPanel.add(endCenterPanel);
			endBottomPanel = new JPanel(null);
		}
		//文章　パネル		
		endPanel.add(endTopPanel);
		// やり直しとトップに戻るボタン　パネル
		endPanel.add(endBottomPanel);
		endTopPanel.setBackground(Color.black);
		endBottomPanel.setBackground(Color.black);

		//ラベル
		label = new JLabel("👑 WIN 👑");
		label.setFont(new Font("SansSerif", Font.BOLD, 50));//フォントを設定 ゴシック　太字　サイズ３０
		label.setForeground(Color.ORANGE);
		label.setHorizontalAlignment(JLabel.CENTER);//表示される文字を中央にする		
		endTopPanel.add(label);

		//勝者表示
		nameLabel = new JLabel("");//8は仮

		if (player1.getResult() == true) {
			nameLabel.setText("やったね！" + Integer.toString(player1.getScore()) + "点");
			System.out.println(player1.getScore() + "点");//デバックLog用
		} else {
			nameLabel.setText("やったね！" + Integer.toString(player2.getScore()) + "点");
			System.out.println(player2.getScore() + "点");//デバックLog用
		}

		nameLabel.setFont(new Font("SansSerif", Font.BOLD, 26));//フォントを設定 ゴシック　太字　サイズ３０
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);//表示される文字を中央にする		
		endBottomPanel.add(nameLabel);

		//やり直しボタン
		Redo.setFont(new Font("SansSerif", Font.BOLD, 20));
		endBottomPanel.add(Redo);
		Redo.addActionListener(e -> {
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

		//トップボタン
		top.setFont(new Font("SansSerif", Font.BOLD, 20));
		endBottomPanel.add(top);
		top.addActionListener(e -> {
			frame.dispose();
			GameOverDialog.dispose();
			new TopMenu();
		});

		layout();

		GameOverDialog.setVisible(true);
	}

	private void layout() {
		int DialogWidth = GameOverDialog.getWidth();//パネル幅		
		int DialogHeight = GameOverDialog.getHeight();//パネル全体の高さ			
		// 各パネルの高さ設定
		int topHeight = DialogHeight / 7;
		int bottomHeight = topHeight * 3;
		int centerHeight = DialogHeight - topHeight - bottomHeight;

		// パネル配置
		endTopPanel.setBounds(0, 0, DialogWidth, topHeight);
		endBottomPanel.setBounds(0, topHeight + centerHeight, DialogWidth, bottomHeight);
		endCenterPanel.setBounds(0, topHeight, DialogWidth, centerHeight);

		//labelの配置
		int labelWidth = DialogWidth / 3 * 2;//label幅
		int labelHeight = topHeight - 20;//label高さ
		int labelX = (DialogWidth - labelWidth) / 2;
		int labelY = (topHeight - labelHeight) / 2;
		label.setBounds(labelX, labelY, labelWidth, labelHeight);

		//nameLabelの配置
		int nameLabelwidth = DialogWidth / 2;//nameLabel幅
		int nameLabelHeight = bottomHeight / 3;//nameLabel高さ
		int nameLabelX = (DialogWidth - nameLabelwidth) / 2;
		int nameLabelY = 10;
		nameLabel.setBounds(nameLabelX, nameLabelY, nameLabelwidth, nameLabelHeight);

		//Redoボタンの配置
		int butwidth = DialogWidth / 3;//ボタン幅
		int butHeight = bottomHeight / 4;//ボタン高さ
		int buttomX = 42;
		int topbuttomX = buttomX * 2 + butwidth;
		int topY = endCenterPanel.getHeight() - 140;

		Redo.setBounds(buttomX, topY, butwidth, butHeight);
		//topボタンの配置
		top.setBounds(topbuttomX, topY, butwidth, butHeight);
	}

	//========================変更しました上野=================================================
	public static void breakDialog(JFrame frame, String message) {
		JDialog dialog = new JDialog(frame, "選択確認", true); // **true にするとモーダル**
		dialog.setSize(360, 150);
		dialog.setLayout(new java.awt.FlowLayout());
		dialog.getContentPane().setBackground(Color.decode("#ffffcc"));
		int x = frame.getX() + (frame.getWidth() - dialog.getWidth()) / 2;
		int y = frame.getY() + (frame.getHeight() - dialog.getHeight()) / 2;
		dialog.setLocation(x, y);

		JLabel breaklabel = new JLabel(message);
		breaklabel.setPreferredSize(new Dimension(325, 50));
		breaklabel.setHorizontalAlignment(JLabel.CENTER);
		dialog.add(breaklabel);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				dialog.dispose();
			}
		});
		//=================================================================

		JButton cancelButton = new JButton("キャンセル");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		dialog.add(okButton);
		dialog.add(cancelButton);
		dialog.setVisible(true);
	}
}
