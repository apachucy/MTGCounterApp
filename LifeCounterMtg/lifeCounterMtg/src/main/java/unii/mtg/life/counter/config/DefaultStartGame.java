package unii.mtg.life.counter.config;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import unii.mtg.life.counter.R;
import unii.mtg.life.counter.pojo.Player;

public class DefaultStartGame {

    private static List<String> sLifeList;
    private static List<String> sDiceValues;
    private static List<Player> sPlayerList;


    private DefaultStartGame() {
    }

    public static List<String> getLifeList() {
        if (sLifeList == null) {
            sLifeList = new ArrayList<>();
            sLifeList.add(BaseConfig.LIFE_20);
            sLifeList.add(BaseConfig.LIFE_30);
            sLifeList.add(BaseConfig.LIFE_40);
        }
        return sLifeList;
    }

    public static List<String> getDiceValueList() {
        if (sDiceValues == null) {
            sDiceValues = new ArrayList<>();
            sDiceValues.add(BaseConfig.DICE_2);
            sDiceValues.add(BaseConfig.DICE_6);
            sDiceValues.add(BaseConfig.DICE_10);
            sDiceValues.add(BaseConfig.DICE_20);
        }

        return sDiceValues;
    }

    public static List<Player> getDefaultPlayers(Context context) {
        // add default two players
        if (sPlayerList == null) {
            sPlayerList = new ArrayList<>();
            int playerNumber = 1;
            Player player1 = new Player(context.getString(R.string.item_player_name)
                    + playerNumber++);
            Player player2 = new Player(context.getString(R.string.item_player_name)
                    + playerNumber);
            sPlayerList.add(player1);
            sPlayerList.add(player2);
        }
        return sPlayerList;
    }
}
