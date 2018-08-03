package unii.mtg.life.counter.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import unii.mtg.life.counter.R;
import unii.mtg.life.counter.config.BaseConfig;
import unii.mtg.life.counter.config.DisplayAdditionalCounters;
import unii.mtg.life.counter.pojo.Player;
import unii.mtg.life.counter.view.OnElementClick;


public class GridPlayersRecyclerAdapter extends RecyclerView.Adapter<GridPlayersRecyclerAdapter.ViewHolder> {
    private List<Player> mPlayerList;
    private DisplayAdditionalCounters mDisplayAdditionalCounters;

    private OnElementClick mPlayerNameListener;

    public GridPlayersRecyclerAdapter(List<Player> playerList, DisplayAdditionalCounters displayAdditionalCounters) {
        mDisplayAdditionalCounters = displayAdditionalCounters;
        mPlayerList = playerList;
    }

    public void setPlayerNameListener(OnElementClick onClickListener) {
        mPlayerNameListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // setting
        viewHolder.playerNameTextView.setText(mPlayerList.get(position)
                .getPlayerName());

        TextView playerLife = (TextView) viewHolder.playerLifeView.findViewById(R.id.item_player_playerPointsTextView);
        playerLife.setText(String.valueOf(mPlayerList.get(position)
                .getLifeCounter()));

        if (mDisplayAdditionalCounters.isVisiblePoisonCounter()) {
            viewHolder.playerPoisonView.setVisibility(View.VISIBLE);
            TextView playerPoison = (TextView) viewHolder.playerPoisonView.findViewById(R.id.item_player_playerPointsTextView);
            playerPoison.setText(String.valueOf(mPlayerList.get(position)
                    .getPoisonCounter()));
        } else {
            viewHolder.playerPoisonView.setVisibility(View.GONE);

        }

        if (mDisplayAdditionalCounters.isVisibleEnergyCounter()) {
            viewHolder.playerEnergyView.setVisibility(View.VISIBLE);
            TextView playerEnergy = (TextView) viewHolder.playerEnergyView.findViewById(R.id.item_player_playerPointsTextView);
            playerEnergy.setText(String.valueOf(mPlayerList.get(position)
                    .getEnergyCounter()));
        } else {
            viewHolder.playerEnergyView.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_player_playerNameTextView)
        TextView playerNameTextView;

        @BindView(R.id.player_life)
        View playerLifeView;

        @BindView(R.id.player_poison)
        View playerPoisonView;
        @BindView(R.id.player_energy)
        View playerEnergyView;

        private void setData(View view, int drawableIcon) {
            ImageView add = (ImageView) view.findViewById(R.id.item_player_playerAddButton);
            final TextView counters = (TextView) view.findViewById(R.id.item_player_playerPointsTextView);
            ImageView remove = (ImageView) view.findViewById(R.id.item_player_playerRemoveButton);

            //remove Listeners
            add.setOnClickListener(null);
            add.setOnLongClickListener(null);
            remove.setOnClickListener(null);
            remove.setOnLongClickListener(null);
            //set icon
            counters.setCompoundDrawablesWithIntrinsicBounds(drawableIcon, 0, 0, 0);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int points = Integer.parseInt(counters.getText().toString());
                    points += BaseConfig.ONE_POINT;
                    mPlayerList.get(getPosition()).setLifeCounter(points);
                    counters.setText(String.valueOf(mPlayerList.get(getPosition()).getLifeCounter()));
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int points = Integer.parseInt(counters.getText().toString());
                    if (BaseConfig.GAME_OVER < points) {
                        points -= BaseConfig.ONE_POINT;
                        mPlayerList.get(getPosition()).setLifeCounter(points);
                        counters.setText((String.valueOf(mPlayerList.get(getPosition()).getLifeCounter())));
                    }
                }
            });
            add.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int points = Integer.parseInt(counters.getText().toString());
                    points += BaseConfig.FIVE_POINTS;
                    mPlayerList.get(getPosition()).setLifeCounter(points);
                    counters.setText((String.valueOf(mPlayerList.get(getPosition()).getLifeCounter())));
                    return true;
                }
            });

            remove.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int points = Integer.parseInt(counters.getText().toString());
                    if (BaseConfig.GAME_OVER < points) {
                        points -= BaseConfig.FIVE_POINTS;
                        mPlayerList.get(getPosition()).setLifeCounter(points);
                        counters.setText((String.valueOf(mPlayerList.get(getPosition()).getLifeCounter())));
                    }
                    return true;
                }
            });
        }

        @OnClick(R.id.item_player_playerNameTextView)
        void onChangePlayerName() {
            mPlayerNameListener.elementClicked(getPosition());
        }


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setData(playerLifeView, R.drawable.ic_life_small);
            setData(playerPoisonView, R.drawable.ic_poison_small);
            setData(playerEnergyView, R.drawable.ic_energy_small);

        }
    }
}
