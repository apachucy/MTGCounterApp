package unii.mtg.life.counter.pojo;

import unii.mtg.life.counter.config.BaseConfig;

public class GameSettings {

	private LifeStart mPlayersLife;
	private int mGameTimeInMin;

	public GameSettings() {
		mPlayersLife = new LifeStart(BaseConfig.PLAYER_START_LIFE);
		mGameTimeInMin = BaseConfig.GAME_DEFAULT_TIME;
	}

	public int getGameTimeInMin() {
		return mGameTimeInMin;
	}

	public void setGameTimeInMin(int mGameTimeInMin) {
		this.mGameTimeInMin = mGameTimeInMin;
	}

	public int getStartingLife() {
		return mPlayersLife.getPlayerLife();
	}

	public void setStartingLife(int startLife) {
		mPlayersLife.setPlayerLife(startLife);
	}

}
