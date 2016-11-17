package unii.mtg.life.counter.pojo;

public class Player {

    private String mPlayerName;
    private int mLifeCounter;
    private int mPoisonCounter;
    private int mEnergyCounter;

    public Player(String player) {
        this(player, 20, 0, 0);
    }

    public Player(String player, int life, int poison, int energy) {
        mPlayerName = player;
        mLifeCounter = life;
        mPoisonCounter = poison;
        mEnergyCounter = energy;
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

    public int getEnergyCounter() {
        return mEnergyCounter;
    }

    public void setEnergyCounter(int mEnergyCounter) {
        this.mEnergyCounter = mEnergyCounter;
    }
}
