package model;

public class Player {
	private String name; /*プレイヤー名*/
	private int score; /*プレイヤーのスコア*/
	private int Life; /*プレイヤのヘルス,3以下を設定*/
	private boolean isTrapped; /*トラップにかかったかの設定、かかった際にtrue*/
	private boolean isResult = false;
	
	//コンストラクタ作成（引数はString型を設定）
	public Player(String name) {
		this.name = name;
		this.score = 0;
		this.Life = Clamp.clamp(3);
		this.isTrapped = false;
	}
	
	//トラップを踏んだ際の動作、
	public void caughtTrap() {
		this.isTrapped = true;
		this.score = 0;
		this.Life --;
	}
	
	//トラップを踏んだ後の動作、トラップにかかった状態を解除する
	public void reset() {
		this.isTrapped = false;
	}
	
	//以下、getter,setter
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getLife() {
		return Life;
	}
	public void setLife(int Life) {
		this.Life = Life;
	}
	public boolean isTrapped() {
		return isTrapped;
	}
	public void setTrapped(boolean isTrapped) {
		this.isTrapped = isTrapped;
	}
	public boolean getResult() {
		return isResult;
	}
	public void setResult(boolean isResult) {
		this.isResult = isResult;
	}
	
	//スコア加算
    public void addScore(int points) {
        this.score += points;
    }

    //スコアリセット（罠にかかった時）
    public void resetScore() {
        this.score = 0;
    }
	
}

//Lifeの最大値を3、最小値を0にするクラス
class Clamp {
	public static final int LIFE_MAX = 3;
	public static final int LIFE_MIN = 0;

	// クランプ処理：指定範囲に収める
	public static int clamp(int value) {
		return Math.max(LIFE_MIN, Math.min(LIFE_MAX, value));
	}
}
