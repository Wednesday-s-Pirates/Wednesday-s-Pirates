package model;

/*Boxクラス
  電気イスゲームのイスにあたるクラスです
  プレイヤーが選択する対象のクラスです
  各Boxは得点を数字として持っています、またすべてのBoxがTrapクラスを持っています*/
public class Box {
	private int id;
	private Trap trap;
	
	//コンストラクタ
	public Box(int id, boolean hasTrapped) {
		this.id = id;
		this.trap = new Trap(hasTrapped);
	}
	
	public int getId() {
		return id;
	}
	public Trap getTrap() {
		return trap;
	}
	
	//Boxを選んだ際の動作
	public boolean sit(Player player) {
		/*トラップにかかった時、playerが罠にかかった時の動作をする*/
		if(trap.isActive()) {
			player.caughtTrap();
			return true;
		}
		return false;
	}
	
	/*Box内のトラップを有効にする動作*/
	public void activeTrap() {
		trap.active();
	}
	 
	/*Box内のトラップを無効にする動作*/
	public void resetTrap() {
		trap.deactive();
	}
	
}
