package unii.mtg.life.counter.pojo;

public class Player {

	private String mPlayerName;
	private int mLifeCounter;
	private int mPoisonCounter;

	public Player(String player) {
		this(player, 20, 0);
	}

	public Player(String player, int life, int poison) {
		mPlayerName = player;
		mLifeCounter = life;
		mPoisonCounter = poison;
	}

	public String getPlayerName() {
		return mPlayerName;
	}

	public void setPlayerName(String mPlayerName) {
		this.mPlayerName = mPlayerName;
	}

	public int getLifeCounter() {
		return mLifeCounter;
	}

	public void setLifeCounter(int mLifeCounter) {
		this.mLifeCounter = mLifeCounter;
	}

	public int getPoisonCounter() {
		return mPoisonCounter;
	}

	public void setPoisonCounter(int mPoisonCounter) {
		this.mPoisonCounter = mPoisonCounter;
	}

}
