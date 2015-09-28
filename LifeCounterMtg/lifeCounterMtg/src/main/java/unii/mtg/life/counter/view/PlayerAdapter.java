package unii.mtg.life.counter.view;

import java.util.List;

import unii.mtg.life.counter.R;
import unii.mtg.life.counter.pojo.Player;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerAdapter extends BaseAdapter {

	private List<Player> mPlayerList;
	private Context mContext;

	private OnElementClick mPlayerNameListener;

	public PlayerAdapter(Context context, List<Player> playerList) {
		mContext = context;
		mPlayerList = playerList;
	}

	@Override
	public int getCount() {
		return mPlayerList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mPlayerList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void setPlayerNameListener(OnElementClick onClickListener) {
		mPlayerNameListener = onClickListener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.item_player, null);

			viewHolder = new ViewHolder();
			viewHolder.playerNameTextView = (TextView) convertView
					.findViewById(R.id.item_player_playerNameTextView);

			viewHolder.playerLifeTextView = (TextView) convertView
					.findViewById(R.id.item_player_playerLifeTextView);

			viewHolder.playerPoisonTextView = (TextView) convertView
					.findViewById(R.id.item_player_playerPoisonTextView);
			viewHolder.addLifeButton = (ImageView) convertView
					.findViewById(R.id.item_player_playerLifeAddButton);
			viewHolder.removeLifeButton = (ImageView) convertView
					.findViewById(R.id.item_player_playerLifeRemoveButton);
			viewHolder.addPoisonButton = (ImageView) convertView
					.findViewById(R.id.item_player_playerPoisonAddButton);
			viewHolder.removePoisonButton = (ImageView) convertView
					.findViewById(R.id.item_player_playerPoisonRemoveButton);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PlayerAdapterOnClickListener onClickListener = new PlayerAdapterOnClickListener(
				viewHolder.playerLifeTextView, viewHolder.playerPoisonTextView,
				mPlayerList.get(position));
		// remove listeners
		viewHolder.addLifeButton.setOnClickListener(null);
		viewHolder.removeLifeButton.setOnClickListener(null);
		viewHolder.addPoisonButton.setOnClickListener(null);
		viewHolder.removePoisonButton.setOnClickListener(null);
		viewHolder.playerNameTextView.setOnClickListener(null);
		
		viewHolder.addLifeButton.setOnLongClickListener(null);
		viewHolder.removeLifeButton.setOnLongClickListener(null);
		viewHolder.addPoisonButton.setOnLongClickListener(null);
		viewHolder.removePoisonButton.setOnLongClickListener(null);
		
		// setting
		viewHolder.playerNameTextView.setText(mPlayerList.get(position)
				.getPlayerName());
		viewHolder.playerLifeTextView.setText(mPlayerList.get(position)
				.getLifeCounter() + "");
		viewHolder.playerPoisonTextView.setText(mPlayerList.get(position)
				.getPoisonCounter() + "");
		// add listeners

		viewHolder.addLifeButton.setOnClickListener(onClickListener);
		viewHolder.removeLifeButton.setOnClickListener(onClickListener);
		viewHolder.addPoisonButton.setOnClickListener(onClickListener);
		viewHolder.removePoisonButton.setOnClickListener(onClickListener);
		viewHolder.playerNameTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayerNameListener.elementClicked(position);

			}
		});
		viewHolder.addLifeButton.setOnLongClickListener(onClickListener);
		viewHolder.removeLifeButton.setOnLongClickListener(onClickListener);
		viewHolder.addPoisonButton.setOnLongClickListener(onClickListener);
		viewHolder.removePoisonButton.setOnLongClickListener(onClickListener);
		return convertView;
	}

	static class ViewHolder {
		TextView playerNameTextView;
		TextView playerLifeTextView;
		TextView playerPoisonTextView;
		ImageView addLifeButton;
		ImageView removeLifeButton;
		ImageView addPoisonButton;
		ImageView removePoisonButton;
	}

}
