package model;

/*罠クラス
  罠が有効化の時、プレイヤーの得点とライフを失わせる役割を持ちます */
public class Trap {
	private boolean active;
	
	//コンストラクタ作成（引数はboolean型)
	public Trap(boolean active) {
		this.active = active;
	}
	
	//トラップの状態を戻す
	public boolean isActive() {
		return active;
	}
	
	//トラップを有効にする
	public void active() {
		this.active = true;
	}
	
	//トラップを無効にする
	public void deactive() {
		this.active = false;
	}
	
}
