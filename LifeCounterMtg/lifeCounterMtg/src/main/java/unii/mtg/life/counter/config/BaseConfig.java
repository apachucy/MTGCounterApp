package unii.mtg.life.counter.config;


public class BaseConfig {

    // starting life
    public static final int PLAYER_START_LIFE = 20;

    public static final String LIFE_20 = "20";
    public static final String LIFE_30 = "30";
    public static final String LIFE_40 = "40";

    public static final boolean DEFAULT_FIRST_RUN = true;
    // Times
    /**
     * In minutes
     */
    public static final int GAME_DEFAULT_TIME = 45;

    public static final long DEFAULT_TIME_SECOND = 1000;
    public static final long DEFAULT_TIME_MINUT = DEFAULT_TIME_SECOND * 60;// 1m

    public static final long DEFAULT_COUNTER_INTERVAL = DEFAULT_TIME_SECOND;// 1s

    // dices
    public static final String DICE_2 = "2";
    public static final String DICE_6 = "6";
    public static final String DICE_10 = "10";
    public static final String DICE_20 = "20";


    public static final int ONE_POINT = 1;
    public static final int FIVE_POINTS = 5;
    public static final int GAME_OVER = 0;

    /**
     * Used to obtain data from different application
     */
    public static final String BUNDLE_KEY_PLAYERS_NAMES = "BUNDLE_KEY_PLAYERS_NAMES";
    public static final String BUNDLE_KEY_ROUND_TIME = "BUNDLE_KEY_ROUND_TIME";
}
