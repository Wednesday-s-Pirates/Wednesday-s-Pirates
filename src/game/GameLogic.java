/**攻防戦ありの椅子取りゲーム
*default8ラウンドの　3ライフ制　40点先取
*/
package game;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import gui.GameOverPanel;
import gui.GameWindow;
import gui.SwitchPanel;
import model.Box;
import model.Player;
import sound.SoundPlayer;

public class GameLogic {

	private List<Box> boxes = new ArrayList<>(); //Boxインスタンスを格納するリスト

	private GameWindow gameWindow;
	private int roundCount = 1; //ToDo ラウンドカウンター
	private static final int MAX_ROUNDS = 8; //ToDo 最大ラウンド default16
	private Player player1;
	private Player player2;
	private Player currentDefender;
	private Player currentRaider;
	private Box selectedAttackBox;
	private Box selectedDefenseBox;

	/*6.17東追加
	 * 野球の裏表の概念
	 * true = 表の回、player1が略奪側 false = 裏の回、player2が略奪側 */
	private boolean isTopOfRound = true;

	//コンストラクタ削除

	/*getter*/
	public Player getPlayer1() {
		return this.player1;
	}

	public Player getPlayer2() {
		return this.player2;
	}

	public List<Box> getBoxes() {
		return this.boxes;
	}

	public void setGameWindow(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}

	public void setRoundCount(int round) {
		this.roundCount = round;
	}

	public int getRoundCount() {
		return this.roundCount;
	}

	public enum GameState {
		TOP, START, END, ATTACK, DEFENSE
	}//今後の拡張のためにそのまま使用

	private GameState currentState = GameState.TOP;

	public GameState getGameState() {
		return currentState;
	}

	public void setGameState(GameState newState) {
		this.currentState = newState;
	}

	//ゲームを行うPlayerインスタンスを準備するメソッド
	public void setPlayers(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		this.currentDefender = player2;
		this.currentRaider = player1;
	}

	/*そのターンの状態を判断しゲームを続行するかどうか判別するメソッド*/
	public void updateState() {
		if (checkForGameEnd())
			return; // 終了したらそれ以上やらない

		// 通常ターン切り替え
		if (currentState == GameState.DEFENSE) {
			currentState = GameState.ATTACK;
		} else if (currentState == GameState.ATTACK) {
			currentState = GameState.DEFENSE;
		}
	}

	/*ゲームが終了条件を満たしているか判断するメソッド*/
	private boolean checkForGameEnd() {
		if (player1.getLife() <= 0 || player2.getLife() <= 0) {
			finishGame(player1.getLife() > 0 ? player1 : player2, "ライフ0");
			return true;
		}

		if (player1.getScore() >= 40 || player2.getScore() >= 40) {
			finishGame(player1.getScore() >= 40 ? player1 : player2, "ポイントmax");
			return true;
		}

		long visibleCount = gameWindow.getBoxMap().values().stream() //画面のBoxボタンのMapの値を取得、ストリーム処理
				.filter(JButton::isVisible) //表示中のボタンのみ抽出
				.count(); //フィルター後の表示中のボタンの数をカウントしてvisibleCountに代入

		if (visibleCount <= 1) {
			if (player1.getScore() != player2.getScore()) {
				finishGame(player1.getScore() > player2.getScore() ? player1 : player2, "残り1箱"); //スコアの高いほうを勝ちとする、理由は"残り1箱"
			} else {
				finishDrawJudge();
			}
			return true;
		}

		//画面上のBoxインスタンスが残り一個になっているか判定してゲーム終了するか判別する
		if (roundCount > MAX_ROUNDS) {
			if (player1.getScore() > player2.getScore()) {
				finishGame(player1, "最大ターン");
			} else if (player2.getScore() > player1.getScore()) {
				finishGame(player2, "最大ターン");
			}
			return true;
		}

		return false;
	}

	//ゲーム終了時のメソッド「ゲーム終了」と画面に表示し、GameOverPanelを開く
	private void finishGame(Player winner, String reason) {
		winner.setResult(true);
		currentState = GameState.END;
		gameWindow.actionResult.setText("<html>" + reason + "<br>ゲーム終了！</html>");
		btnStop();
		new GameOverPanel(gameWindow.getFrame(), player1, player2, this);
	}

	//引き分け時に動作するメソッド「引き分け、ゲーム終了」と画面に表示しGameOverPanelを開く
	private void finishDrawJudge() {
		currentState = GameState.END;
		if (player1.getLife() > player2.getLife()) {
			finishGame(player1, "Draw:残り体力");
		} else {
			finishGame(player2, "Draw:残り体力");
		}
		btnStop();
	}

	/*画面のBoxに触れなくなるメソッド*/
	public void btnStop() {
		for (int i = 0; i < gameWindow.boxes.length; i++) {
			gameWindow.boxes[i].setEnabled(false);
		}
	}

	/*Box内のTrapをすべて無効化し、ゲーム開始時の状態に戻すメソッド*/
	public void resetBoxes() {
		for (Box box : boxes) {
			box.resetTrap(); // 罠非活性などの状態初期化
		}
	}

	//略奪側の選択をセット**
	public void setSelectedAttackBox(Box box) {
		if (box != null)
			this.selectedAttackBox = getBoxById(box.getId());
	}

	//守備側の選択をセット**
	public void setSelectedDefenseBox(Box box) {
		if (box != null)
			this.selectedDefenseBox = getBoxById(box.getId());
	}

	//現在の略奪側ボックスを取得
	public Box getSelectedAttackBox() {
		return selectedAttackBox;
	}

	//現在の守備側ボックスを取得
	public Box getSelectedDefenseBox() {
		return selectedDefenseBox;
	}

	//Playerの攻防を交代するメソッド
	public void switchPlayers() {
		Player tmp = currentDefender;
		currentDefender = currentRaider;
		currentRaider = tmp;
	}

	//守備側の処理
	public void processDefense() {
		if (currentDefender == null) {
			System.out.println("守備側プレイヤーが未設定です！");
			return;
		}

		if (selectedAttackBox == null) {
			System.out.println("守備側の選択が完了していません。");
			return;
		}

		selectedAttackBox.activeTrap();
		System.out.println("罠を" + selectedAttackBox.getId() + " に設置しました！");
		gameWindow.playerTurnLabel.setText("バクダンをおきました！");

		//略奪ターンへ移行**
		setGameState(GameState.ATTACK);
	}

	// 略奪側の処理
	public void processAttack() {
		if (currentRaider == null) {
			System.out.println("略奪側プレイヤーが未設定です！");
			return;
		}

		if (selectedDefenseBox == null) {
			System.out.println("略奪側の選択が完了していません。");
			return;
		}

		//Trapが有効だったBoxを選んだの動作、プレイヤーはLifeを1失い、pointは0になる（処理内容はcaughtTrapメソッド）
		if (selectedDefenseBox.getTrap().isActive()) {

			gameWindow.setIsBoxSelectable(false);
			SoundPlayer.playSEFromResource("SE/宝箱を開ける.wav"); //宝箱を開けた音を鳴らす

			SoundPlayer.playSEFromResource("SE/爆発2.wav");
			new SwitchPanel(gameWindow.getFrame(), currentDefender, player1);

			System.out.println("バクダンが入ってた！ " + currentRaider.getName() + " のライフ減少");//デバックLog用
			gameWindow.playerTurnLabel
					.setText("<html>バクダンにかかった！？ " + "<br>" + currentRaider.getName() + "ライフ減少</html>");
			currentRaider.caughtTrap();

			// Trapにかかったことを示す「×」をRoundPanelに表示
			if (isTopOfRound) {
				gameWindow.markTopTrapHit(roundCount);
			} else {
				gameWindow.markBottomTrapHit(roundCount);
			}
			//GUIに反映(2025.6.12追加部分　東
			if (gameWindow != null) {
				gameWindow.updateLifeDisplay();
				gameWindow.updateScoreDisplay();
			}
			turnProgression();
			gameWindow.setIsBoxSelectable(true);
		} else {
			gameWindow.setIsBoxSelectable(false);
			SoundPlayer.playSEFromResource("SE/宝箱を開ける.wav");

			SoundPlayer.playSEFromResource("SE/歓声と拍手.wav");
			currentRaider.addScore(selectedDefenseBox.getId());

			System.out.println("安全な場所でした！入手ポイント：" + selectedDefenseBox.getId());//デバックLog用
			gameWindow.playerTurnLabel.setText("<html>宝箱をゲット！<br>入手ポイント：" + selectedDefenseBox.getId() + "</html>");

			//ToDo 箱の没収タイミング
			JButton safeBox = gameWindow.getBoxMap().get(selectedDefenseBox.getId());
			if (safeBox != null) {
				safeBox.setVisible(false);
			}
			//GUIに反映(2025.6.12追加部分　東
			if (gameWindow != null) {
				gameWindow.updateLifeDisplay();
				gameWindow.updateScoreDisplay();
			}
			turnProgression();
			gameWindow.setIsBoxSelectable(true);
		}

	}

	//ターンを進行させるメソッド
	public void turnProgression() {
		//ワナを無効化
		selectedAttackBox.resetTrap();

		//GUIに反映(2025.6.12追加部分　東
		if (gameWindow != null) {
			gameWindow.updateLifeDisplay();
			gameWindow.updateScoreDisplay();
		}

		//表、裏のどちらの回か判定する 6.17東追加
		if (isTopOfRound) {
			//裏に進む
			isTopOfRound = false;
			updateState();
			switchPlayers();
			setGameState(GameState.DEFENSE);
			gameWindow.updateRoundTop(roundCount, player1.getScore()); // ← 表だけ更新
			resetSelections();
		} else {
			gameWindow.updateRoundBottom(roundCount, player2.getScore()); // ← 裏だけ更新
			//表裏完了→ラウンド１加算
			setRoundCount(getRoundCount() + 1);
			isTopOfRound = true;
			// **次のラウンドへ向けてプレイヤー交代**
			updateState();
			switchPlayers();
			setGameState(GameState.DEFENSE);
			// **選択リセット**
			resetSelections();
		}
	}

	public void stepTurn() {
		if (getGameState() == GameState.DEFENSE) {
			System.out.println("ラウンド" + getRoundCount() + ":" + currentDefender.getName() + " の守備ターン選択を開始！");//デバックLog用
			gameWindow.actionResult.setText(
					"<html>ラウンド" + getRoundCount() + "<br>" + currentDefender.getName() + " 守備ターン<br>宝箱を選択してね！</html>");

		} else if (getGameState() == GameState.ATTACK) {
			System.out.println(currentRaider.getName() + " のどろぼうターン選択を開始！");//デバックLog用
			gameWindow.actionResult
					.setText("<html>ラウンド" + getRoundCount() + "<br>" + currentRaider.getName()
							+ " どろぼうターン<br>宝箱を選択してね！</html>");
		}
	}

	// 任意のBox IDからboxを取得
	public Box getBoxById(int id) {
		return boxes.stream()
				.filter(box -> box.getId() == id)
				.findFirst()
				.orElse(null);
	}

	/*GameWindowで生成されたBoxインスタンスを受け取り、攻防時に一貫して使用するための登録処理。
	  この方法により、罠の発動状態が正しく共有され、フェーズ間の不整合を防止する。*/
	public void registerBoxes(List<Box> sharedBoxes) {
		this.boxes = sharedBoxes;
	}

	/*Boxの選択状態を解除するメソッド,6.18東追加*/
	private void resetSelections() {
		selectedAttackBox = null;
		selectedDefenseBox = null;
	}
}

/*6.23変更点
* 236,240,266,269行目、各種効果音の実装
* 238,268行目、効果音のディレイの実装
*/
