package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import game.GameLogic;
import game.GameLogic.GameState;
import model.Box;
import model.Player;
import sound.SoundPlayer;

/*ゲーム内容をWindowに表示するためのクラス
  ゲームの流れはGameLogicクラスから受け取っている */
//他のクラスを継承できるようにJFrame、JPanel継承なしで作成
public class GameWindow implements ActionListener {
	private JFrame frame;
	private JPanel panel; //親
	private JPanel topPanel;//上
	private JPanel centerPanel;//真ん中
	private RoundPanel bottomPanel;//下

	public JLabel actionResult = new JLabel("");
	public JLabel playerTurnLabel; //現在のプレイヤー表示
	private JLabel illust = new JLabel("画像");
	private JLabel[] nowScoren = new JLabel[4];
	private JLabel[] lifeLabels = new JLabel[2];

	private JButton setting = new JButton("Ξ"); //設定ボタン
	private JButton roundScore = new JButton("▲"); //設定ボタン
	public JButton[] boxes = new JButton[12]; //１２個の箱 //一時的に可視化private

	private String gameImg = "images/game.png";
	private String[] nowScorenNnme = { " 0 点", "P1", "P2", "0 点" };
	private String[] boxsnum = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }; //箱の番号ラベル
	private String[] imagePaths = {
			"images/box1.png", "images/box2.png", "images/box3.png",
			"images/box4.png", "images/box5.png", "images/box6.png",
			"images/box7.png", "images/box8.png", "images/box9.png",
			"images/box10.png", "images/box11.png", "images/box12.png"
	};

	//2025.6.15 東追記　箱の情報を保持するMap
	private Player player1, player2;
	private Map<Integer, JButton> boxMap = new HashMap<>();
	//ボタンの情報を保持するMap
	private Map<JButton, Box> buttonToBox = new HashMap<>();

	public Map<Integer, JButton> getBoxMap() {
		return boxMap;
	}
	//ここまで///////////////////////////////

	private GameLogic gameLogic;
	private boolean isBoxSelectable = true; //Boxボタンの操作を受け付けるかどうかの判定
	
	public GameWindow(JFrame firstFrame, GameLogic logic) {

		//ゲームロジックとプレイヤー情報を初期化
		this.gameLogic = logic;
		this.player1 = logic.getPlayer1();
		this.player2 = logic.getPlayer2();
		gameLogic.setGameWindow(this);

		// 初回ターン開始処理をイベントディスパッチスレッドで遅延実行
		// → GUI表示後にstepTurn()を呼ぶため、SwingUtilities.invokeLater() を使用
		// → 初期状態（GameState.TOP）から防衛フェーズ（GameState.DEFENSE）に遷移させる
		SwingUtilities.invokeLater(() -> {
			logic.setGameState(GameState.DEFENSE); //初期状態の明示的設定
			gameLogic.stepTurn(); //ターン進行処理
		});

		//ウィンドウの設定
		frame = new JFrame("水曜日のパイレーツ💣");//アプリ名
		frame.setSize(500, 700);//タイトル入力
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//×で閉じる	
		Image booticon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/favicon.png"));
		frame.setIconImage(booticon);
		panel = new JPanel(null); // 独自でレイアウトするため（null）
		frame.setResizable(false);
		frame.add(panel);

		//ここからトップの位置に合わせてGameWindow表示設定。
		int topMenuX = firstFrame.getX();
		int topMenuY = firstFrame.getY();
		frame.setLocation(topMenuX, topMenuY);

		//下記コード内容 ３つのパネルに一枚の画像を背景として設定	
		URL gameUrl = getClass().getResource(gameImg);
		if (gameUrl != null) {
			Image fullImage = new ImageIcon(gameUrl).getImage();
			int gameImageWidth = 500; // 実際の画像サイズに合わせて
			int gameImageHeight = 700;
			int topPanelHeight = 140;
			int bottompanelHeight = topPanelHeight;
			int centerpanelHeight = gameImageHeight - topPanelHeight - bottompanelHeight;

			// 画像の上半分を描画   
			topPanel = new JPanel(null) {
				protected void paintComponent(Graphics g) {
					g.drawImage(fullImage, 0, 0, getWidth(), getHeight(),
							0, 0, gameImageWidth, topPanelHeight, this);
					super.paintComponent(g);
				}
			};
			topPanel.setBounds(0, 0, gameImageWidth, topPanelHeight);
			topPanel.setOpaque(false);
			panel.add(topPanel);

			// 画像の下半分を描画
			centerPanel = new JPanel(null) {
				protected void paintComponent(Graphics g) {
					g.drawImage(fullImage, 0, 0, getWidth(), getHeight(),
							0, topPanelHeight, gameImageWidth, topPanelHeight + centerpanelHeight, this);
					super.paintComponent(g);

				}
			};
			centerPanel.setBounds(0, topPanelHeight, gameImageWidth, topPanelHeight + centerpanelHeight);
			centerPanel.setOpaque(false);
			panel.add(centerPanel);

			// 画像の下半分を描画
			bottomPanel = new RoundPanel(frame) {
				protected void paintComponent(Graphics g) {
					g.drawImage(fullImage, 0, 0, getWidth(), getHeight(),
							0, topPanelHeight + centerpanelHeight, gameImageWidth, gameImageHeight, this);
					super.paintComponent(g);
				}
			};
			bottomPanel.setLayout(null);
			bottomPanel.setBounds(0, topPanelHeight + centerpanelHeight - 30, gameImageWidth - 14, bottompanelHeight);
			bottomPanel.setOpaque(false);
			panel.add(bottomPanel);
		} else {
			System.out.println("画像が見つかりません: " + gameImg);
		}
		
		//イラスト
		illust.setFont(new Font("SansSerif", Font.BOLD, 10));
		//illust.setBorder(BorderFactory.createLineBorder(Color.RED));
		topPanel.add(illust);

		//playerの行動結果 追加　中山
		actionResult.setFont(new Font("SansSerif", Font.BOLD, 16));
		topPanel.add(actionResult);
		
		//設定ボタン
		setting.setFont(new Font("SansSerif", Font.BOLD, 10));
		topPanel.add(setting);
		setting.addActionListener(e -> {
			new MenuPanel(frame , gameLogic);
		});

		//現在のプレイヤー表示設定。
		playerTurnLabel = new JLabel("");
		playerTurnLabel.setFont(new Font("SansSerif", Font.BOLD, 14));//フォントを設定 ゴシック　太字　サイズ３０
		playerTurnLabel.setHorizontalAlignment(JTextField.CENTER);//表示される文字を中央にする	
		
		centerPanel.add(playerTurnLabel);
		playerTurnLabel.setVisible(true);//ここをtrueかfalseに変えるとplayerTurnLabel表示変更可能。

		/***6.15東変更、箱のリストを生成***/
		List<Box> sharedBoxes = logic.getBoxes();
		for (int i = 0; i < sharedBoxes.size(); i++) {
			Box box = sharedBoxes.get(i);
			JButton btn = new JButton(boxsnum[i]); // 表示用ラベル
			boxes[i] = btn; // ← これも重要：boxes[] に入れてレイアウトでも使う
			btn.addActionListener(this);
			btn.setFont(new Font("SansSerif", Font.BOLD, 10));
			// 画像読み込み
			URL imageUrl = getClass().getResource(imagePaths[i]);
			if (imageUrl != null) {
				ImageIcon icon = new ImageIcon(imageUrl);
				Image scaledImage = icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
				btn.setIcon(new ImageIcon(scaledImage));
			} else {
				System.out.println("画像が見つかりません: " + imagePaths[i]);
			}
			btn.setContentAreaFilled(false); // ボタンの背景を透明にする
			btn.setBorderPainted(false);
			btn.setBounds(0, 0, 60, 50); // 箱のサイズを決める

			// マップ登録
			int boxId = box.getId();
			boxMap.put(boxId, btn);
			buttonToBox.put(btn, box);

			centerPanel.add(btn);
		}

		//6.15東追記、GameLogicにBoxを渡す
		gameLogic.registerBoxes(sharedBoxes); // GameLogicにBoxを渡す

		//player1.2の現スコア―表示
		for (int i = 0; i < nowScoren.length; i++) {
			nowScoren[i] = new JLabel(nowScorenNnme[i]);
			if (i == 0 || i == 3) {
				nowScoren[i].setOpaque(true);//背景の設定をするかしないか
				nowScoren[i].setBackground(Color.WHITE);//背景色
				nowScoren[i].setForeground(Color.black);
			} else {
				nowScoren[i].setForeground(Color.ORANGE);
			}
			nowScoren[i].setHorizontalAlignment(JLabel.CENTER);
			nowScoren[i].setFont(new Font("SansSerif", Font.BOLD, 20));

			bottomPanel.add(nowScoren[i]);
		}

		//ライフ表示		
		for (int i = 0; i < lifeLabels.length; i++) {
			lifeLabels[i] = new JLabel("❤❤❤"); // 初期ライフ表示
			lifeLabels[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
			lifeLabels[i].setForeground(Color.RED); // ハートの色
			bottomPanel.add(lifeLabels[i]);
		}
		lifeLabels[1].setHorizontalAlignment(JLabel.RIGHT);

		/***2025.6.12 6限 東追加,変更、if-else文の追加、roundPanelを使いまわせるように6/18上野調整***/
		roundScore.setFont(new Font("SansSerif", Font.BOLD, 10));
		roundScore.setBackground(Color.WHITE);
		bottomPanel.add(roundScore);
		roundScore.addActionListener(e -> {
			if (bottomPanel != null) {
				bottomPanel.showDialog(); //roundPanelがあるときは表示		
			} else {
				/***2025.6.13 東変更、追記 new RoundPanel(frame)→ RoundPanel roundDialogPanel = new RoundPanel(frame)に***/
				RoundPanel roundDialogPanel = new RoundPanel(frame); // 毎回新しく表示（RoundPanel 自体がJDialogを持ってるのでOK）
				roundDialogPanel.showDialog();
			}
		});
		/***ここまで***/

		// パネルがリサイズされたら再配置
		panel.addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {
				layout(); // サイズ変更で円形再配置
			}

		});

		// 初回レイアウト配置
		frame.setVisible(true);//設定してきた内容を画面に表示。
		layout(); // 最初に表示されたときも配置されるように
	
		//BGMの再生
		SoundPlayer.playBGMFromResource("BGM/ドキドキあそび広場.wav");
	}

	private void layout() {
		//nullチェック
		if (boxes == null || boxes.length < 12 || boxes[0] == null)
			return;

		int panelWidth = panel.getWidth();//パネルの幅サイズ			
		int panelHeight = panel.getHeight();

		// 各パネルの高さ設定	
		int topHeight = panelHeight / 5;
		int bottomHeight = topHeight + 30;
		int centerHeight = panelHeight - topHeight - bottomHeight;

		// パネル配置
		topPanel.setBounds(0, 0, panelWidth, topHeight);
		bottomPanel.setBounds(0, topHeight + centerHeight, panelWidth, bottomHeight);
		centerPanel.setBounds(0, topHeight, panelWidth, centerHeight);

		// 設定ボタン右上に配置
		int settingWidth = 45;
		int settingHeight = 30;
		int settingX = panelWidth - settingWidth - 10;
		int settingY = 5;
		setting.setBounds(settingX, settingY, settingWidth, settingHeight);//ボタン位置とサイズを決める

		//イラスト配置
		int illustWidth = 60;
		int illustHeight = topHeight - 20;
		int illustX = panelWidth - panelWidth + illustWidth;
		int illustY = 15;
		illust.setBounds(illustX, illustY, illustWidth, illustHeight);

		//フキダシ配置
		int actionResultWidth = 270;
		int actionResultHeight = topHeight - 20;
		int actionResultX = 150; //panelWidth  - panelWidth +actionResultWidth ; 
		int actionResultY = 15;
		actionResult.setBounds(actionResultX, actionResultY, actionResultWidth, actionResultHeight);

		//現在のプレイヤ-名表示　配置（上部中央）
		int TurnLabelWidth = panelWidth / 3;//パネルの幅の3分の1の幅サイズ
		int TurnLabelHeight = centerHeight / 3;//高さのサイズ
		int labelX = (panelWidth - TurnLabelWidth) / 2;
		int labelY = (centerHeight - TurnLabelHeight) / 2;
		playerTurnLabel.setBounds(labelX, labelY, TurnLabelWidth, TurnLabelHeight);//左、上、幅、高さのボタンをセットする。

		//箱を円の形の配置
		int boxesX = centerPanel.getWidth() / 2;
		int boxesY = centerPanel.getHeight() / 2;
		int radius = Math.min(centerPanel.getWidth(), centerPanel.getHeight()) / 2 - 50;//半径を求めている。Math.min＝a と b のうち小さい方の値を返す。

		for (int i = 0; i < 12; i++) {
			double angle = 2 * Math.PI * i / 12 - Math.PI / 2;
			;//等間隔でボタンを配置するため角度を求める。2 * Math.PI（3.14159）＝３６０度　
			int x = (int) (boxesX + radius * Math.cos(angle)) - 22;
			int y = (int) (boxesY + radius * Math.sin(angle)) - 22;
			boxes[i].setLocation(x, y);
		}

		int nowScorenWidth = 60;
		int nowScorenHeight = 60;
		int spacing = 85;
		int startX = 100;
		int y = 40;
		int nameWidth = 40;

		nowScoren[0].setBounds(startX, y, nowScorenWidth, nowScorenHeight);
		int x1 = startX + nowScorenWidth;
		nowScoren[1].setBounds(x1, y, nameWidth, nowScorenHeight);
		int x2 = x1 + nameWidth + spacing;
		nowScoren[2].setBounds(x2, y, nameWidth, nowScorenHeight);
		int x3 = x2 + nameWidth;
		nowScoren[3].setBounds(x3, y, nowScorenWidth, nowScorenHeight);

		int lifeLabelWidth = nowScorenWidth;
		int lifeLabelHeight = 30;
		int lifeLabelY = 5;
		lifeLabels[0].setBounds(startX, lifeLabelY, lifeLabelWidth, lifeLabelHeight);
		lifeLabels[1].setBounds(x3, lifeLabelY, lifeLabelWidth, lifeLabelHeight);

		int roundWidth = 45;
		int roundbtmHeight = 30;
		int roundX = (panelWidth - roundWidth) / 2;
		int roundY = bottomPanel.getHeight() - roundbtmHeight;
		roundScore.setBounds(roundX, roundY, roundWidth, roundbtmHeight);
	}

	//===========================6/19 変更上野=======================================
	private void showSelectionDialog(String message, Runnable onConfirm) {
	    JDialog dialog = new JDialog(frame, "選択確認", true); // **true にするとモーダル**
	    dialog.setSize(280, 140);
	    dialog.setLayout(new java.awt.FlowLayout());
	    dialog.setResizable(false);
	    dialog.getContentPane().setBackground(Color.decode("#ffffcc"));
		int x = frame.getX()+(frame.getWidth() - dialog.getWidth())/2;
		int y = frame.getY()+(frame.getHeight() - dialog.getHeight())/2;
		dialog.setLocation(x, y);
	    
		JLabel label = new JLabel(message);	
	    label.setPreferredSize(new Dimension(240, 50));
	    label.setFont(new Font("SansSerif", Font.BOLD, 13));
	    label.setHorizontalAlignment(JLabel.CENTER);
	    dialog.add(label);

	    JButton okButton = new JButton("はい");
	    
	   
	    okButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
	        	dialog.dispose();
	        	onConfirm.run(); // **OKを押すとターンが進行**
	        }
	    });

	    JButton cancelButton = new JButton("いいえ");
	    cancelButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
	        	dialog.dispose();
	        }
	    });
	    dialog.add(okButton);
	    dialog.add(cancelButton);
	    dialog.setVisible(true);
	}
	//==================================================================================			

	/*2025.6.12 東追加 */
	public void updateLifeDisplay() {
		Player[] players = { player1, player2 };
		for (int i = 0; i < lifeLabels.length; i++) {
			int life = players[i].getLife();
			StringBuilder lifes = new StringBuilder();
			for (int j = 0; j < life; j++) {
				lifes.append("❤");
			}
			lifeLabels[i].setText(lifes.toString());
		}
	}

	/*6_13東 追記部分***/ //6/15 上野調整 //6.17東変更、使用メソッドを変更（動作に変化なし）
	public void updateRoundScore(int round, int player1Score, int player2Score) {
		if (bottomPanel != null) {
			bottomPanel.updateTopScore(round, player1Score);
			bottomPanel.updateBottomScore(round, player2Score);
		}
	}
	/* ここまで  */

	//6/15 上野調整
	public void updateScoreDisplay() {
		nowScoren[0].setText(Integer.toString(player1.getScore()) + " 点");
		nowScoren[3].setText(Integer.toString(player2.getScore()) + " 点");
	}

	//表（player1）のスコアを更新
	public void updateRoundTop(int round, int player1Score) {
		if (bottomPanel != null) {
			bottomPanel.updateTopScore(round, player1Score);
		}
	}

	//裏（player2）のスコアを更新
	public void updateRoundBottom(int round, int player2Score) {
		if (bottomPanel != null) {
			bottomPanel.updateBottomScore(round, player2Score);
		}
	}

	//ラッパーメソッド、RoundPanelにGameLogicから罠の判定を中継する
	public void markTopTrapHit(int round) {
		if (bottomPanel != null) {
			bottomPanel.markTopTrapHit(round);
		}
	}

	public void markBottomTrapHit(int round) {
		if (bottomPanel != null) {
			bottomPanel.markBottomTrapHit(round);
		}
	}

	//6/8 ここから下 中山 実装
	public JFrame getFrame() {
		return this.frame;
	}

	public int[] getBoxNumbers() {
		int[] boxNumbers = new int[boxsnum.length];

		for (int i = 0; i < boxsnum.length; i++) {
			boxNumbers[i] = Integer.parseInt(boxsnum[i]);
		}
		return boxNumbers;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!isBoxSelectable) {
			return; //ボタン操作不能時に何の処理もしない様にする
		}
		SoundPlayer.playSEFromResource("SE/決定ボタンを押す1.wav");
		JButton clickedButton = (JButton) e.getSource();
		Box box = buttonToBox.get(clickedButton);

		if (box == null) {
			System.out.println("宝箱が未登録です");
			return;
		}

		GameState state = gameLogic.getGameState();

		if (state == GameState.DEFENSE) {
			showSelectionDialog("宝箱: " + box.getId() + " にバクダンをしかけますか？", new Runnable() {
				@Override
				public void run() {
					gameLogic.setSelectedAttackBox(box);
					System.out.println("防衛側が宝箱 : " + box.getId() + " を選択しました");
					gameLogic.processDefense();
					gameLogic.stepTurn();
				}
			});

		} else if (state == GameState.ATTACK) {
			showSelectionDialog("この宝箱: " + box.getId() + " を開けますか？", () -> {
				gameLogic.setSelectedDefenseBox(box);
				System.out.println("略奪側が宝箱: " + box.getId() + " を選択しました");
				gameLogic.processAttack();
				gameLogic.stepTurn();
			});
		}
	}
	public void setIsBoxSelectable(boolean selectable) {
		this.isBoxSelectable = selectable;
	}

	// 現在の状態を取得する getter
	public boolean isBoxSelectable() {
		return isBoxSelectable;
	}
}

/*6.23東変更点
*385,395,480行目、各種操作音追加
*272行目、BGM再生の処理追加*/
