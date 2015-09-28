package unii.mtg.life.counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import unii.mtg.life.counter.config.BaseConfig;
import unii.mtg.life.counter.pojo.GameSettings;
import unii.mtg.life.counter.pojo.Player;
import unii.mtg.life.counter.view.EditTextDialogFragmentActionListener;
import unii.mtg.life.counter.view.OnElementClick;
import unii.mtg.life.counter.view.OnSetValueListener;
import unii.mtg.life.counter.view.PlayerAdapter;
import unii.mtg.life.counter.view.fragments.CustomDialogEditTextFragment;
import unii.mtg.life.counter.view.fragments.CustomDialogFragment;
import unii.mtg.life.counter.view.fragments.CustomDialogSpinnerFragment;
import unii.mtg.life.counter.view.timer.CounterClass;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
 * 
 * @author Arkadiusz Pachucy
 * 
 */
public class GameActivity extends Activity {

	private GridView mPlayerGridView;
	private TextView mTimerTextView;
	private Button mTimerStartButton;

	private List<Player> mPlayerList;
	private PlayerAdapter mAdapter;

	// dialog
	private ArrayList<String> mLifeList;
	private ArrayList<String> mDiceValues;

	public static final String TAG_FRAGMENT_START_LIFE = GameActivity.class
			.getName() + "FRAGMENT_START_LIFE";
	public static final String TAG_FRAGMENT_ROLL_DICE = GameActivity.class
			.getName() + "TAG_FRAGMENT_ROLL_DICE";

	public static final String TAG_FRAGMENT_NEW_PLAYER = GameActivity.class
			.getName() + "TAG_FRAGMENT_NEW_PLAYER";

	public static final String TAG_FRAGMENT_TIMER = GameActivity.class
			.getName() + "TAG_FRAGMENT_TIMER";
	public static final String TAG_FRAGMENT_INFO = GameActivity.class.getName()
			+ "TAG_FRAGMENT_INFO";

	private CustomDialogSpinnerFragment mCustomSpinnerDialogFragment;
	private CustomDialogEditTextFragment mCustomEditTextDialogFragment;
	private CustomDialogFragment mCustomDialogFragment;

	private OnSetValueListener mOnSetValueListener;
	private EditTextDialogFragmentActionListener mOnSetEditTextListener;

	private static GameSettings sRunSettings;
	private CounterClass mTimer;

	// help library
	//private ShowcaseView mShowcaseView;
	//private int mShowCaseID = 144;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		mPlayerGridView = (GridView) findViewById(R.id.activity_game_playerGridLayout);
		mTimerTextView = (TextView) findViewById(R.id.activity_game_timerTextView);
		mTimerStartButton = (Button) findViewById(R.id.activity_game_timerButton);
		// add default two players
		int playerNumber = 1;
		Player player1 = new Player(getString(R.string.item_player_name)
				+ playerNumber++);
		Player player2 = new Player(getString(R.string.item_player_name)
				+ playerNumber);
		mPlayerList = new ArrayList<Player>();
		mPlayerList.add(player1);
		mPlayerList.add(player2);

		mLifeList = new ArrayList<String>();
		mLifeList.add(BaseConfig.LIFE_20);
		mLifeList.add(BaseConfig.LIFE_30);
		mLifeList.add(BaseConfig.LIFE_40);

		mDiceValues = new ArrayList<String>();
		mDiceValues.add(BaseConfig.DICE_2);
		mDiceValues.add(BaseConfig.DICE_6);
		mDiceValues.add(BaseConfig.DICE_10);
		mDiceValues.add(BaseConfig.DICE_20);

		sRunSettings = new GameSettings();
		mTimerStartButton.setOnClickListener(mOnTimerStart);
		mAdapter = new PlayerAdapter(this, mPlayerList);
		mAdapter.setPlayerNameListener(mOnElementClick);

		mPlayerGridView.setAdapter(mAdapter);

		// hints for users
	/*	ViewTarget target = new ViewTarget(R.id.activity_game_timerButton, this);
		mShowcaseView = new ShowcaseView.Builder(this, true).setTarget(target)
				.setContentTitle(getString(R.string.help_button_title))
				.setStyle(R.style.showcaseThem)
				.setContentText(getString(R.string.help_button_message))
				.setOnClickListener(mOnHelpClickListener)
				.singleShot(mShowCaseID).build();
		mShowcaseView
				.setButtonText(getString(R.string.help_button_action_next));
*/
		mTimerTextView.setText(populateTimerAtStart((long) (sRunSettings
				.getGameTimeInMin() * BaseConfig.DEFAULT_TIME_MINUT)));
	}

	private String populateTimerAtStart(long millis) {
		return String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings_startLife:
			initSpinnerDialog(TAG_FRAGMENT_START_LIFE,
					getString(R.string.dialog_fragment_lifeTitle),
					getString(R.string.dialog_fragment_lifeMessage), mLifeList,
					getString(R.string.dialog_button_set),
					getString(R.string.dialog_button_cancel),
					mOnPossitiveButtonLifeClick, mNegativeClick);
			mCustomSpinnerDialogFragment.show(getFragmentManager(),
					TAG_FRAGMENT_START_LIFE);
			break;

		case R.id.settings_rollDice:
			initSpinnerDialog(TAG_FRAGMENT_ROLL_DICE,
					getString(R.string.dialog_fragment_rollTitle),
					getString(R.string.dialog_fragment_rollMessage),
					mDiceValues, getString(R.string.dialog_button_roll),
					getString(R.string.dialog_button_cancel),
					mOnPossitiveButtonRollClick, mNegativeClick);
			mCustomSpinnerDialogFragment.show(getFragmentManager(),
					TAG_FRAGMENT_ROLL_DICE);
			break;

		case R.id.settings_gameTime:
			Log.v("TAG", "Game Time: " + sRunSettings.getGameTimeInMin());
			initEditTextDialog(TAG_FRAGMENT_TIMER,
					getString(R.string.dialog_fragment_timerTitle),
					getString(R.string.dialog_fragment_timerMessage),
					sRunSettings.getGameTimeInMin() + "",
					getString(R.string.dialog_button_set),
					getString(R.string.dialog_button_cancel),
					mOnPossitiveSetTimeClick, mNegativeClick);

			mCustomEditTextDialogFragment.show(getFragmentManager(),
					TAG_FRAGMENT_TIMER);
			break;

		case R.id.settings_add_player:
			initEditTextDialog(TAG_FRAGMENT_NEW_PLAYER,
					getString(R.string.dialog_fragment_playerTitle),
					getString(R.string.dialog_fragment_playerMessage), "",
					getString(R.string.dialog_button_add),
					getString(R.string.dialog_button_cancel),
					mOnPossitiveButtonAddPlayerClick, mNegativeClick);
			mCustomEditTextDialogFragment.show(getFragmentManager(),
					TAG_FRAGMENT_NEW_PLAYER);
			break;
		case R.id.settings_info:
			if (mCustomDialogFragment == null) {
				mCustomDialogFragment = CustomDialogFragment.newInstance(
						getString(R.string.dialog_fragment_info_title),
						getString(R.string.dialog_fragment_info_message));
			}
			mCustomDialogFragment.show(getFragmentManager(), TAG_FRAGMENT_INFO);

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initSpinnerDialog(String tag, String title, String message,
			ArrayList<String> simpleList, String leftButtonName,
			String rightButtonName, OnClickListener possitive,
			OnClickListener negative) {
		if (mCustomSpinnerDialogFragment == null) {
			mCustomSpinnerDialogFragment = CustomDialogSpinnerFragment
					.newInstance(title, message, simpleList, leftButtonName,
							rightButtonName, possitive, negative);
		} else {
			if (getFragmentManager().findFragmentByTag(tag) == null) {
				mCustomSpinnerDialogFragment.setDialogData(title, message,
						simpleList, leftButtonName, rightButtonName, possitive,
						negative);
			}
		}
	}

	private void initEditTextDialog(String tag, String title, String message,
			String textEditText, String leftButtonName, String rightButtonName,
			OnClickListener possitive, OnClickListener negative) {
		mCustomEditTextDialogFragment = CustomDialogEditTextFragment
				.newInstance(title, message, textEditText, leftButtonName,
						rightButtonName, possitive, negative);

	}

	private OnClickListener mOnPossitiveSetTimeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mOnSetEditTextListener = (EditTextDialogFragmentActionListener) mCustomEditTextDialogFragment;
			// check if only digist are entered
			String regexOnlyDigitsPattern = "[0-9]+";
			if (mOnSetEditTextListener.getTextInEditText().matches(
					regexOnlyDigitsPattern)) {
				sRunSettings.setGameTimeInMin(Integer
						.parseInt(mOnSetEditTextListener.getTextInEditText()));
				mOnSetEditTextListener.clearEditText();
				mCustomEditTextDialogFragment.dismiss();

				mTimerStartButton.setVisibility(View.VISIBLE);
				if (mTimer != null) {
					mTimer.cancel();
				}
			} else {
				Toast.makeText(GameActivity.this,
						getString(R.string.warning_only_input_digits),
						Toast.LENGTH_SHORT).show();

			}
		}
	};

	private OnClickListener mOnPossitiveButtonAddPlayerClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mOnSetEditTextListener = (EditTextDialogFragmentActionListener) mCustomEditTextDialogFragment;
			mPlayerList.add(new Player(mOnSetEditTextListener
					.getTextInEditText(), sRunSettings.getStartingLife(), 0));
			mAdapter.notifyDataSetChanged();
			mOnSetEditTextListener.clearEditText();
			mCustomEditTextDialogFragment.dismiss();
		}
	};

	private OnClickListener mOnPossitiveButtonLifeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mOnSetValueListener = (OnSetValueListener) mCustomSpinnerDialogFragment;
			int startLife = Integer.parseInt(mOnSetValueListener
					.getSpinnerValue());
			for (Player p : mPlayerList) {
				p.setLifeCounter(startLife);
			}
			mAdapter.notifyDataSetChanged();
			mCustomSpinnerDialogFragment.dismiss();
			sRunSettings.setStartingLife(startLife);
		}
	};

	private OnClickListener mOnPossitiveButtonRollClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mOnSetValueListener = (OnSetValueListener) mCustomSpinnerDialogFragment;
			int diceValue = Integer.parseInt(mOnSetValueListener
					.getSpinnerValue());
			mOnSetValueListener.getRollTextView().setText(
					getString(R.string.dialog_fragment_rolledValue)
							+ rollDice(diceValue));

		}
	};

	private OnClickListener mNegativeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mCustomEditTextDialogFragment != null) {
				mCustomEditTextDialogFragment.dismiss();
			}
			if (mCustomSpinnerDialogFragment != null) {
				mCustomSpinnerDialogFragment.dismiss();
			}
		}
	};

	private OnClickListener mOnTimerStart = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v("TAG", "Button triggered");
			if (mTimer != null) {
				mTimer.cancel();
			}
			mTimer = new CounterClass(
					(long) (sRunSettings.getGameTimeInMin() * BaseConfig.DEFAULT_TIME_MINUT),
					BaseConfig.DEFAULT_COUNTER_INTERVAL, mTimerTextView);
			mTimer.start();
			mTimerStartButton.setVisibility(View.INVISIBLE);
		}
	};

	private OnElementClick mOnElementClick = new OnElementClick() {

		@Override
		public void elementClicked(final int position) {
			initEditTextDialog(TAG_FRAGMENT_NEW_PLAYER,
					getString(R.string.dialog_fragment_changePlayerTitle),
					getString(R.string.dialog_fragment_playerMessage),
					mPlayerList.get(position).getPlayerName(),
					getString(R.string.dialog_button_change),
					getString(R.string.dialog_button_cancel),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							mOnSetEditTextListener = (EditTextDialogFragmentActionListener) mCustomEditTextDialogFragment;
							mPlayerList.get(position).setPlayerName(
									mOnSetEditTextListener.getTextInEditText());
							mAdapter.notifyDataSetChanged();
							mOnSetEditTextListener.clearEditText();
							mCustomEditTextDialogFragment.dismiss();
						}
					}, mNegativeClick);
			mCustomEditTextDialogFragment.show(getFragmentManager(),
					TAG_FRAGMENT_NEW_PLAYER);
		}
	};

	private static int rollDice(int diceValue) {
		Random random = new Random();
		// do not return value from range: 0 to diceValue-1 return value from 1
		// to diceValue
		return random.nextInt(diceValue) + 1;
	}
/*
	private OnClickListener mOnHelpClickListener = new OnClickListener() {
		private int mCounter = 0;

		@Override
		public void onClick(View v) {
			switch (mCounter) {
			case 0:
				mShowcaseView
						.setShowcase(new ViewTarget(mPlayerGridView), true);

				mShowcaseView
						.setContentTitle(getString(R.string.help_list_title));
				mShowcaseView
						.setContentText(getString(R.string.help_list_message));
				break;

			case 1:
				mShowcaseView.setTarget(Target.NONE);
				mShowcaseView
						.setContentTitle(getString(R.string.help_menu_title));
				mShowcaseView
						.setContentText(getString(R.string.help_menu_message));
				mShowcaseView
						.setButtonText(getString(R.string.help_button_action_end));
				break;

			case 2:
				mShowcaseView.hide();
				break;
			}
			mCounter++;
		}
	};*/
}
