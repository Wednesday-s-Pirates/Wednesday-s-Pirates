package game;

import java.util.ArrayList;
import java.util.List;

import model.Box;
import model.Player;

//ゲーム上で動くオブジェクトを生成するクラス 東作成
public class GameInitializer {

    public static GameLogic initializeGame(String player1Name, String player2Name) {
        Player p1 = new Player(player1Name);
        Player p2 = new Player(player2Name);

        GameLogic gameLogic = new GameLogic();
        gameLogic.setPlayers(p1, p2);

        // Boxもここで登録しておく
        List<Box> boxList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            boxList.add(new Box(i,false));
        }
        gameLogic.registerBoxes(boxList);

        return gameLogic;
    }
    
}
