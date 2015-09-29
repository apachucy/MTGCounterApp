package unii.mtg.life.counter.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import unii.mtg.life.counter.R;
import unii.mtg.life.counter.config.BaseConfig;
import unii.mtg.life.counter.pojo.Player;
import unii.mtg.life.counter.view.OnElementClick;

/**
 * Created by apachucy on 2015-09-29.
 */
public class GridPlayersRecyclerAdapter extends RecyclerView.Adapter<GridPlayersRecyclerAdapter.ViewHolder> {
    private List<Player> mPlayerList;
    private Context mContext;

    private OnElementClick mPlayerNameListener;

    public GridPlayersRecyclerAdapter(Context context, List<Player> playerList) {
        mContext = context;
        mPlayerList = playerList;
    }

    public void setPlayerNameListener(OnElementClick onClickListener) {
        mPlayerNameListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // setting
        viewHolder.playerNameTextView.setText(mPlayerList.get(position)
                .getPlayerName());
        viewHolder.playerLifeTextView.setText(mPlayerList.get(position)
                .getLifeCounter() + "");
        viewHolder.playerPoisonTextView.setText(mPlayerList.get(position)
                .getPoisonCounter() + "");
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_player_playerNameTextView)
        TextView playerNameTextView;
        @Bind(R.id.item_player_playerLifeTextView)
        TextView playerLifeTextView;
        @Bind(R.id.item_player_playerPoisonTextView)
        TextView playerPoisonTextView;
        @Bind(R.id.item_player_playerLifeAddButton)
        ImageView addLifeButton;
        @Bind(R.id.item_player_playerLifeRemoveButton)
        ImageView removeLifeButton;
        @Bind(R.id.item_player_playerPoisonAddButton)
        ImageView addPoisonButton;
        @Bind(R.id.item_player_playerPoisonRemoveButton)
        ImageView removePoisonButton;

        @OnClick(R.id.item_player_playerLifeAddButton)
        void onPlusLifeButtonClicked(View view) {
            int points = Integer.parseInt(playerLifeTextView.getText().toString());
            points += BaseConfig.ONE_POINT;
            mPlayerList.get(getPosition()).setLifeCounter(points);
            playerLifeTextView.setText(mPlayerList.get(getPosition()).getLifeCounter() + "");
        }

        @OnClick(R.id.item_player_playerLifeRemoveButton)
        void onMinusLifeButtonClicked(View view) {
            int points = Integer.parseInt(playerLifeTextView.getText().toString());
            if (BaseConfig.GAME_OVER < points) {
                points -= BaseConfig.ONE_POINT;
                mPlayerList.get(getPosition()).setLifeCounter(points);
                playerLifeTextView.setText(mPlayerList.get(getPosition()).getLifeCounter() + "");
            }
        }

        @OnClick(R.id.item_player_playerPoisonAddButton)
        void onPlusPoisonButtonClicked(View view) {
            int points = Integer.parseInt(playerPoisonTextView.getText()
                    .toString());
            points += BaseConfig.ONE_POINT;
            mPlayerList.get(getPosition()).setPoisonCounter(points);
            playerPoisonTextView.setText(mPlayerList.get(getPosition()).getPoisonCounter() + "");
        }

        @OnClick(R.id.item_player_playerPoisonRemoveButton)
        void onMinusPoisonButtonClicked(View view) {
            int points = Integer.parseInt(playerPoisonTextView.getText()
                    .toString());
            if (BaseConfig.GAME_OVER < points) {
                points -= BaseConfig.ONE_POINT;
                mPlayerList.get(getPosition()).setPoisonCounter(points);
                playerPoisonTextView.setText(mPlayerList.get(getPosition()).getPoisonCounter() + "");
            }
        }

        @OnClick(R.id.item_player_playerNameTextView)
        void onChangePlayerName(View view) {
            mPlayerNameListener.elementClicked(getPosition());
        }

        @OnLongClick(R.id.item_player_playerLifeAddButton)
        boolean onLongPlusLifeButtonClicked(View view) {
            int points = Integer.parseInt(playerLifeTextView.getText().toString());
            points += BaseConfig.FIVE_POINTS;
            mPlayerList.get(getPosition()).setLifeCounter(points);
            playerLifeTextView.setText(mPlayerList.get(getPosition()).getLifeCounter() + "");
            return true;
        }

        @OnLongClick(R.id.item_player_playerLifeRemoveButton)
        boolean onLongMinusLifeButtonClicked(View view) {
            int points = Integer.parseInt(playerLifeTextView.getText().toString());
            if (BaseConfig.GAME_OVER < points) {
                points -= BaseConfig.FIVE_POINTS;
                mPlayerList.get(getPosition()).setLifeCounter(points);
                playerLifeTextView.setText(mPlayerList.get(getPosition()).getLifeCounter() + "");
            }
            return true;
        }

        @OnLongClick(R.id.item_player_playerPoisonAddButton)
        boolean onLongPlusPoisonButtonClicked(View view) {
            int points = Integer.parseInt(playerPoisonTextView.getText()
                    .toString());
            points += BaseConfig.FIVE_POINTS;
            mPlayerList.get(getPosition()).setPoisonCounter(points);
            playerPoisonTextView.setText(mPlayerList.get(getPosition()).getPoisonCounter() + "");
            return true;
        }

        @OnLongClick(R.id.item_player_playerPoisonRemoveButton)
        boolean onLongMinusPoisonButtonClicked(View view) {
            int points = Integer.parseInt(playerPoisonTextView.getText()
                    .toString());
            if (BaseConfig.GAME_OVER < points) {
                points -= BaseConfig.FIVE_POINTS;
                mPlayerList.get(getPosition()).setPoisonCounter(points);
                playerPoisonTextView.setText(mPlayerList.get(getPosition()).getPoisonCounter() + "");
            }
            return true;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
