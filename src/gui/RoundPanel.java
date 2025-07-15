package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RoundPanel  extends JPanel {
	JPanel scorePanel;
	JFrame frame;
	JDialog roundDialog ;
	JLabel[] roundLabelsNum = new JLabel[9];
	JLabel[] roundLabelsTop = new JLabel[9];
    JLabel[] roundLabelsBottom = new JLabel[9];
    
	int roundwidth = 46;
	int roundHeight = 50;
	
	//6.17東追記
	private boolean[] topTrapHits = new boolean[9];
	private boolean[] bottomTrapHits = new boolean[9];
	
	public RoundPanel(JFrame frame) {
		 this.frame = frame;
		 
		//画面
		roundDialog = new JDialog(frame, "ラウンド", false);// モーダル設定(trueの場合は一時停止中状態）
		roundDialog.setResizable(false);
		roundDialog.setSize(500,200);			
		scorePanel = new JPanel(null);
		scorePanel.setBackground(Color.decode("#006400")); 
		
		//回数のラベル
        for (int i = 0; i < roundLabelsNum.length; i++) {
        	roundLabelsNum[i] = new JLabel (i +"R");
    		if (i == 0) {
    			roundLabelsNum[i].setText("");        			
       		}
    		commonLabel(roundLabelsNum[i]);       	        		
    		scorePanel.add(roundLabelsNum[i]);
        }
        
        //上のラベル
        for (int i = 0; i < roundLabelsTop.length; i++) {
        	roundLabelsTop[i] = new JLabel();
    		if (i == 0) {
    			roundLabelsTop[i].setText("1P");        			
    		} else {
    			roundLabelsTop[i].setText("");
    		}
    		commonLabel(roundLabelsTop[i]);
    		scorePanel.add(roundLabelsTop[i]);
        }
        
        //下のラベル
        for (int i = 0; i < roundLabelsBottom.length; i++) {
        	roundLabelsBottom[i] = new JLabel();
    	  	if (i == 0) {
				 roundLabelsBottom[i].setText("2P");						 	        		  
    	  	}
    	  	commonLabel(roundLabelsBottom[i]);		            
    	  	scorePanel.add(roundLabelsBottom[i]);
        }
        roundLayout() ;
        roundDialog.add(scorePanel);
	}
	
	// 共通ラベルメソッド
	private void commonLabel(JLabel label) {
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setOpaque(true);
		label.setBackground(Color.WHITE);
	}
	
	private void roundLayout() {
		int age = 40;
		//回数のラベル
		for (int i = 0; i < roundLabelsNum.length; i++) {				
	        int roundx =  roundwidth * i + age;
	        int roundy =  15;
	        
	        if (i == 0) {
	        	roundx=age;
	        	roundLabelsNum[i].setBounds(roundx, roundy,roundwidth + 20,roundHeight - 15); 
	        }else {
	        	roundx = age + (roundwidth + 20) + roundwidth * (i - 1);
	        	roundLabelsNum[i].setBounds(roundx, roundy,roundwidth,roundHeight - 15); 
        	}
        }
		
		//上のラベル配置
		for (int i = 0; i < roundLabelsTop.length; i++) {
			
	        int roundx =  roundwidth * i + age;
	        int roundy =  50;	
	        if (i == 0) {
	        	roundx=age;
	        	roundLabelsTop[i].setBounds(roundx, roundy,roundwidth +20,roundHeight); 
	        }else {
	        	roundx = age + (roundwidth + 20) + roundwidth * (i - 1);
	        roundLabelsTop[i].setBounds(roundx, roundy,roundwidth,roundHeight); 
	        }
        }
		
		//下のラベル配置
		for (int i = 0; i < roundLabelsBottom.length; i++) {
	        int roundx =  roundwidth * i + age;
	        int roundy =  100;
	        if (i == 0) {
	        	roundx = age;
	        	roundLabelsBottom[i].setBounds(roundx, roundy,roundwidth + 20,roundHeight); 
        	}else {
        	roundx = age + (roundwidth + 20) + roundwidth * (i - 1);
        	roundLabelsBottom[i].setBounds(roundx, roundy,roundwidth,roundHeight); 
        	}
        }

	}
	
	//RoundPanelを表示するメソッド
	public void showDialog() {
		roundDialog.setVisible(true);
		int x = frame.getX() + (frame.getWidth() - roundDialog.getWidth());
		int y = frame.getY()+ (frame.getHeight()- roundDialog.getHeight());
		roundDialog.setLocation(x, y);
	}
	
	// 表（player1）側のスコアが書かれたパネルを更新するメソッド
	public void updateTopScore(int round, int score) {
		if (round >= 1 && round < roundLabelsTop.length && !topTrapHits[round]) {
			roundLabelsTop[round].setText(String.valueOf(score));
			scorePanel.revalidate();
	        scorePanel.repaint();
		}
	}
	
	// 裏（player2）側のスコアが書かれたパネルを更新するメソッド
	public void updateBottomScore(int round, int score) {
	    if (round >= 1 && round < roundLabelsBottom.length && !bottomTrapHits[round]) {
	        roundLabelsBottom[round].setText(String.valueOf(score));
	        scorePanel.revalidate();
	        scorePanel.repaint();
	    }
	}
	
	// 表（player1）が罠にかかったときにパネル上にアイコンを表示するメソッド
	public void markTopTrapHit(int round) {
	    if (round >= 1 && round < roundLabelsTop.length) {
	    	topTrapHits[round] = true;
	        roundLabelsTop[round].setText("×");
	        scorePanel.revalidate();
	        scorePanel.repaint();
	    }
	}

	// 裏（player2）が罠にかかったときにパネル上にアイコンを表示するメソッド
	public void markBottomTrapHit(int round) {
	    if (round >= 1 && round < roundLabelsBottom.length) {
	    	bottomTrapHits[round] = true;
	        roundLabelsBottom[round].setText("×");
	        scorePanel.revalidate();
	        scorePanel.repaint();
	    }
	}
		
}
		


