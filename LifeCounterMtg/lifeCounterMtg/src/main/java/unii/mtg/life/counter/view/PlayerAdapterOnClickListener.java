package unii.mtg.life.counter.view;

import unii.mtg.life.counter.R;
import unii.mtg.life.counter.pojo.Player;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class PlayerAdapterOnClickListener implements OnClickListener, OnLongClickListener {

	private TextView mPlayerLifeTextView;
	private TextView mPlayerPoisonTextView;
	private Player mPlayer;
	private static final int ONE_POINT = 1;
	private static final int FIVE_POINTS = 5;
	private static final int GAME_OVER = 0;

	public PlayerAdapterOnClickListener(TextView playerLife,
			TextView playerPoison, Player player) {
		mPlayerLifeTextView = playerLife;
		mPlayerPoisonTextView = playerPoison;
		mPlayer = player;
	}

	@Override
	public void onClick(View v) {
		int points;
		switch (v.getId()) {
		case R.id.item_player_playerLifeAddButton:
			points = Integer.parseInt(mPlayerLifeTextView.getText().toString());
			points += ONE_POINT;
			mPlayer.setLifeCounter(points);
			mPlayerLifeTextView.setText(mPlayer.getLifeCounter() + "");
			break;
		case R.id.item_player_playerLifeRemoveButton:
			points = Integer.parseInt(mPlayerLifeTextView.getText().toString());
			if (GAME_OVER < points) {
				points -= ONE_POINT;
				mPlayer.setLifeCounter(points);
				mPlayerLifeTextView.setText(mPlayer.getLifeCounter() + "");
			}
			break;
		case R.id.item_player_playerPoisonAddButton:
			points = Integer.parseInt(mPlayerPoisonTextView.getText()
					.toString());
			points += ONE_POINT;
			mPlayer.setPoisonCounter(points);
			mPlayerPoisonTextView.setText(mPlayer.getPoisonCounter() + "");
			break;
		case R.id.item_player_playerPoisonRemoveButton:
			points = Integer.parseInt(mPlayerPoisonTextView.getText()
					.toString());
			if (GAME_OVER < points) {
				points -= ONE_POINT;
				mPlayer.setPoisonCounter(points);
				mPlayerPoisonTextView.setText(mPlayer.getPoisonCounter() + "");
			}
			break;
		}

	}

	@Override
	public boolean onLongClick(View v) {
		int points;
		switch (v.getId()) {
		case R.id.item_player_playerLifeAddButton:
			points = Integer.parseInt(mPlayerLifeTextView.getText().toString());
			points += FIVE_POINTS;
			mPlayer.setLifeCounter(points);
			mPlayerLifeTextView.setText(mPlayer.getLifeCounter() + "");
			break;
		case R.id.item_player_playerLifeRemoveButton:
			points = Integer.parseInt(mPlayerLifeTextView.getText().toString());
			if (GAME_OVER < points) {
				points -= FIVE_POINTS;
				mPlayer.setLifeCounter(points);
				mPlayerLifeTextView.setText(mPlayer.getLifeCounter() + "");
			}
			break;
		case R.id.item_player_playerPoisonAddButton:
			points = Integer.parseInt(mPlayerPoisonTextView.getText()
					.toString());
			points += FIVE_POINTS;
			mPlayer.setPoisonCounter(points);
			mPlayerPoisonTextView.setText(mPlayer.getPoisonCounter() + "");
			break;
		case R.id.item_player_playerPoisonRemoveButton:
			points = Integer.parseInt(mPlayerPoisonTextView.getText()
					.toString());
			if (GAME_OVER < points) {
				points -= FIVE_POINTS;
				mPlayer.setPoisonCounter(points);
				mPlayerPoisonTextView.setText(mPlayer.getPoisonCounter() + "");
			}
			break;
		}
		return true;
	}

}
