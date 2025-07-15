import javax.swing.SwingUtilities;

import game.GameLogic;
import gui.TopMenu;
import model.Player;

//メインクラス、実行時に使用する
public class Main {
	
	public static void main(String[] args) {
		// プレイヤーのセットアップ
        new Player("Player1");
        new Player("Player2");

        // ゲームロジックのセットアップ
        new GameLogic();

        //GUIの安全な初期化**
        SwingUtilities.invokeLater(() -> new TopMenu());
    }
	
}