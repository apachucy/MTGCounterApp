package unii.mtg.life.counter.pojo;

public class LifeStart implements Item {

	private int mPlayerLife;

	public LifeStart(int life) {
		mPlayerLife = life;
	}

	@Override
	public String toString() {
		return mPlayerLife + "";
	}

	public int getPlayerLife() {
		return mPlayerLife;
	}

	public void setPlayerLife(int mPlayerLife) {
		this.mPlayerLife = mPlayerLife;
	}
}
