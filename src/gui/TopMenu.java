package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import game.GameInitializer;
import game.GameLogic;
import sound.SoundPlayer;

/*アプリケーションの最初の画面を表示するクラス
  ボタン処理を受け取る役割、GameWindowへ移動、もしくはRuleConfirmへ移動する*/
public class TopMenu extends JFrame {
	private JFrame firstFrame;

	private JPanel firstPanel;
	private JPanel firstTopPanel;
	private JPanel firstCenterPanel;
	private JPanel firstBottomPanel;
	
	private String titleImg = "images/title.png";
	private String titleIconImg = "images/Icon_ship.png";
	
	private JButton startButton = new JButton("スタート");
	private JButton storyButton = new JButton("ものがたり");
	private JLabel titleLabel = new JLabel("水曜日のパイレーツ");
	private JLabel centerLabel = new JLabel();
	private GameLogic gameLogic;

	public TopMenu() {
		//ウィンドウの設定
		firstFrame = new JFrame("水曜日のパイレーツ💣");
		firstFrame.setSize(500, 700);//タイトル入力
		firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//×で閉じる
		Image booticon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/favicon.png"));
		firstFrame.setIconImage(booticon);
		firstFrame.setLocationRelativeTo(null);//トップに画面表示
		firstFrame.setResizable(false);
		firstPanel = new JPanel(null);
		firstFrame.add(firstPanel);

		firstTopPanel = new JPanel(null);
		firstCenterPanel = new JPanel(null);
		firstBottomPanel = new JPanel(null);

		firstPanel.add(firstTopPanel);
		firstPanel.add(firstCenterPanel);
		firstPanel.add(firstBottomPanel);

		firstPanel.setBackground(Color.black);
		firstTopPanel.setBackground(Color.black);
		firstCenterPanel.setBackground(Color.black);
		firstBottomPanel.setBackground(Color.black);
		
		//タイトル
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));		
		titleLabel.setHorizontalAlignment(JLabel.RIGHT);
		titleLabel.setForeground(Color.WHITE);
		URL skeletonImgUrl = getClass().getResource(titleIconImg);
		if (skeletonImgUrl != null) {
			ImageIcon icon = new ImageIcon(skeletonImgUrl);
			Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
			titleLabel.setIcon(new ImageIcon(scaledImage));
			titleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		} else {
			System.out.println("画像が見つかりません: " + titleIconImg);
		}		
		firstTopPanel.add(titleLabel);

		//真ん中画像
		centerLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		centerLabel.setHorizontalAlignment(JLabel.CENTER);
		URL imageUrl = getClass().getResource(titleImg);
		if (imageUrl != null) {
			ImageIcon icon = new ImageIcon(imageUrl);
			Image scaledImage = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
			centerLabel.setIcon(new ImageIcon(scaledImage));
		} else {
			System.out.println("画像が見つかりません: " + titleImg);
		}
		firstCenterPanel.add(centerLabel);

		//startボタン
		startButton.setFont(new Font("SansSerif", Font.BOLD, 20));
		startButton.setBackground(Color.ORANGE);
		firstBottomPanel.add(startButton);
		
		SoundPlayer.playBGMFromResource("BGM/真剣勝負.wav");
		startButton.addActionListener(e -> {
			SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
			startButton.setEnabled(false);

		// ゲームウィンドウを先に生成する
		gameLogic = GameInitializer.initializeGame("Player1", "Player2");
		new GameWindow(firstFrame, gameLogic);
		new RuleConfirm();
		firstFrame.dispose();
		});

		//遊び方ボタン
		storyButton.setFont(new Font("SansSerif", Font.BOLD, 20));
		storyButton.setBackground(Color.ORANGE);
		firstBottomPanel.add(storyButton);
		storyButton.addActionListener(e -> {
			SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
			new StoryConfirm();
		});
		firstLayout();
		firstFrame.setVisible(true);
	}

	private void firstLayout() {
		int firstWidth = firstFrame.getWidth();
		int firstHeight = firstFrame.getHeight();

		// 各パネルの高さ設定
		int topHeight = firstHeight / 6;
		int bottomHeight = topHeight * 2;
		int centerHeight = firstHeight - topHeight - bottomHeight;

		// パネル配置
		firstTopPanel.setBounds(0, 0, firstWidth, topHeight);
		firstBottomPanel.setBounds(0, topHeight + centerHeight, firstWidth, bottomHeight);
		firstCenterPanel.setBounds(0, topHeight, firstWidth, centerHeight);

		//タイトル
        int titleWidth = (firstWidth/5) * 4;
        int titleX = (firstWidth - titleWidth) / 2;
        int titleY = (topHeight - 60) /2;
        int imgLabelWidth =50;
        titleLabel.setBounds(titleX, titleY,titleWidth  ,60);
		titleLabel.setBounds(titleX - imgLabelWidth, titleY, titleWidth, 60);

		//画像　配置
		centerLabel.setBounds(0, 0, firstWidth - 20, centerHeight);

		//ボタン
		int btmWidth = (firstWidth) / 3;
		int btmX = (firstWidth - btmWidth) / 2;
		int btmY = 30;
		startButton.setBounds(btmX, btmY, btmWidth, 40);
		storyButton.setBounds(btmX, btmY + 60, btmWidth, 40);
	}
}