package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Player;

public class SwitchPanel {
	private JDialog  switchDialog;
	private JPanel mainPanel;
	private String gameImg;
	public SwitchPanel(JFrame frame, Player currentDefender, Player expectedDefender) {
		switchDialog = new JDialog(frame, "ラウンド", true);// モーダル設定(trueの場合は一時停止中状態）
		switchDialog.setResizable(false);
		switchDialog.setSize(300,300);			
		switchDialog.setLocationRelativeTo(frame);
		switchDialog.setUndecorated(true);
		
		//パネル
		mainPanel = new JPanel(null);
		mainPanel.setBackground(Color.BLUE);
		
		if(currentDefender == expectedDefender) {
			gameImg = "images/loseCat.png";
		}else {
			gameImg = "images/loseDog.png";
		}
		
		URL gameUrl = getClass().getResource(gameImg);
		//System.out.println("画像のURL: " + gameUrl); // ← ここ追加
		if (gameUrl != null) {
			Image fullImage = new ImageIcon(gameUrl).getImage();
			  
			mainPanel = new JPanel(null) {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(fullImage, 0, 0, getWidth(), getHeight(),
							0, 0, fullImage.getWidth(this),fullImage.getHeight(this),this);
				}
			};	
			
		  Timer timer = new Timer(1200, e -> switchDialog.dispose());
		    timer.setRepeats(false); // 1回だけ実行
		    timer.start();
		
			switchDialog.add(mainPanel);	
			switchDialog.setVisible(true);
	
        }
    }
}