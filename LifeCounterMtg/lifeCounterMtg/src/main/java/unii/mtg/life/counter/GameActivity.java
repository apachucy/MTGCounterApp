package unii.mtg.life.counter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
import unii.mtg.life.counter.config.BaseConfig;
import unii.mtg.life.counter.helper.MenuHelper;
import unii.mtg.life.counter.pojo.GameSettings;
import unii.mtg.life.counter.pojo.Player;
import unii.mtg.life.counter.sharedpreferences.SettingsPreferencesFactory;
import unii.mtg.life.counter.view.EditTextDialogFragmentActionListener;
import unii.mtg.life.counter.view.OnElementClick;
import unii.mtg.life.counter.view.OnSetValueListener;
import unii.mtg.life.counter.view.adapter.GridPlayersRecyclerAdapter;
import unii.mtg.life.counter.view.fragments.CustomDialogEditTextFragment;
import unii.mtg.life.counter.view.fragments.CustomDialogFragment;
import unii.mtg.life.counter.view.fragments.CustomDialogSpinnerFragment;
import unii.mtg.life.counter.view.timer.CounterClass;


/**
 * @author Arkadiusz Pachucy
 */
public class GameActivity extends ActionBarActivity {


    @Bind(R.id.activity_game_timerTextView)
    TextView mTimerTextView;
    @Bind(R.id.activity_game_timerButton)
    Button mTimerStartButton;
    @Bind(R.id.activity_game_playersRecyclerView)
    RecyclerView mRecyclerView;


    private List<Player> mPlayerList;


    private GridPlayersRecyclerAdapter mPlayerRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
    private TourGuide mTutorialHandler = null;

    @Bind(R.id.toolbar)
    Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        mPlayerList = new ArrayList<Player>();
        sRunSettings = new GameSettings();

        Intent openIntent = getIntent();
        if (openIntent != null && openIntent.getExtras() != null && openIntent.getExtras().containsKey(BaseConfig.BUNDLE_KEY_PLAYERS_NAMES)) {
            String[] draftPlayers = openIntent.getExtras().getStringArray(BaseConfig.BUNDLE_KEY_PLAYERS_NAMES);
            for (int i = 0; i < draftPlayers.length; i++) {
                mPlayerList.add(new Player(draftPlayers[i]));
            }

            if(openIntent.getExtras().containsKey(BaseConfig.BUNDLE_KEY_ROUND_TIME)){
                sRunSettings.setGameTimeInMin(openIntent.getExtras().getInt(BaseConfig.BUNDLE_KEY_ROUND_TIME));
            }
        } else {
            // add default two players
            int playerNumber = 1;
            Player player1 = new Player(getString(R.string.item_player_name)
                    + playerNumber++);
            Player player2 = new Player(getString(R.string.item_player_name)
                    + playerNumber);
            mPlayerList.add(player1);
            mPlayerList.add(player2);
        }
        mLifeList = new ArrayList<String>();
        mLifeList.add(BaseConfig.LIFE_20);
        mLifeList.add(BaseConfig.LIFE_30);
        mLifeList.add(BaseConfig.LIFE_40);

        mDiceValues = new ArrayList<String>();
        mDiceValues.add(BaseConfig.DICE_2);
        mDiceValues.add(BaseConfig.DICE_6);
        mDiceValues.add(BaseConfig.DICE_10);
        mDiceValues.add(BaseConfig.DICE_20);

        mTimerStartButton.setOnClickListener(mOnTimerStart);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPlayerRecyclerAdapter = new GridPlayersRecyclerAdapter(mPlayerList);
        mPlayerRecyclerAdapter.setPlayerNameListener(mOnElementClick);


        mRecyclerView.setAdapter(mPlayerRecyclerAdapter);

        mTimerTextView.setText(populateTimerAtStart((long) (sRunSettings
                .getGameTimeInMin() * BaseConfig.DEFAULT_TIME_MINUT)));

        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        setMenuActions((ImageView) menu.getItem(0).getActionView(), (ImageView) menu.getItem(1).getActionView(), (ImageView) menu.getItem(2).getActionView());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

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


    private void setMenuActions(ImageView lifeButton, ImageView diceButton, ImageView addPlayer) {
        // just adding some padding to look better
        int padding = MenuHelper.getHelperMenuPadding(getResources().getDisplayMetrics().density);

        lifeButton.setPadding(padding, padding, padding, padding);
        diceButton.setPadding(padding, padding, padding, padding);
        addPlayer.setPadding(padding, padding, padding, padding);

        // set an image
        lifeButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_life));
        diceButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_dice));
        addPlayer.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_person_add));
        if (SettingsPreferencesFactory.getInstance().isFirstRun()) {
            Sequence sequence = null;
            sequence = new Sequence.SequenceBuilder().add(bindTourGuideButton(getString(R.string.help_life), lifeButton), bindTourGuideButton(getString(R.string.help_dice), diceButton), bindTourGuideButton(getString(R.string.help_person), addPlayer))
                    .setDefaultOverlay(new Overlay().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mTutorialHandler.next();
                        }
                    })).setContinueMethod(Sequence.ContinueMethod.OverlayListener).setDefaultPointer(new Pointer()).build();


            mTutorialHandler = TourGuide.init(this).playInSequence(sequence);
            SettingsPreferencesFactory.getInstance().setFirstRun(false);
        }
        lifeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initSpinnerDialog(TAG_FRAGMENT_START_LIFE,
                        getString(R.string.dialog_fragment_lifeTitle),
                        getString(R.string.dialog_fragment_lifeMessage), mLifeList,
                        getString(R.string.dialog_button_set),
                        getString(R.string.dialog_button_cancel),
                        mOnPossitiveButtonLifeClick, mNegativeClick);
                mCustomSpinnerDialogFragment.show(getFragmentManager(),
                        TAG_FRAGMENT_START_LIFE);
            }
        });
        diceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initSpinnerDialog(TAG_FRAGMENT_ROLL_DICE,
                        getString(R.string.dialog_fragment_rollTitle),
                        getString(R.string.dialog_fragment_rollMessage),
                        mDiceValues, getString(R.string.dialog_button_roll),
                        getString(R.string.dialog_button_cancel),
                        mOnPossitiveButtonRollClick, mNegativeClick);
                mCustomSpinnerDialogFragment.show(getFragmentManager(),
                        TAG_FRAGMENT_ROLL_DICE);
            }
        });
        addPlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initEditTextDialog(TAG_FRAGMENT_NEW_PLAYER,
                        getString(R.string.dialog_fragment_playerTitle),
                        getString(R.string.dialog_fragment_playerMessage), "",
                        getString(R.string.dialog_button_add),
                        getString(R.string.dialog_button_cancel),
                        mOnPossitiveButtonAddPlayerClick, mNegativeClick);
                mCustomEditTextDialogFragment.show(getFragmentManager(),
                        TAG_FRAGMENT_NEW_PLAYER);
            }
        });
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
            // check if only digits are entered
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
                mTimerTextView.setText(populateTimerAtStart(sRunSettings.getGameTimeInMin() * BaseConfig.DEFAULT_TIME_MINUT));
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
            String playerName = mOnSetEditTextListener
                    .getTextInEditText();
            if (playerName.trim().equals("")) {
                Toast.makeText(GameActivity.this, getString(R.string.dialog_fragment_error_name_empty), Toast.LENGTH_SHORT).show();
            } else if (!validatePlayerName(playerName)) {
                Toast.makeText(GameActivity.this, getString(R.string.dialog_fragment_error_name_used), Toast.LENGTH_SHORT).show();
            } else {
                mPlayerList.add(new Player(playerName, sRunSettings.getStartingLife(), 0));
                mPlayerRecyclerAdapter.notifyDataSetChanged();
                mOnSetEditTextListener.clearEditText();
                mCustomEditTextDialogFragment.dismiss();
            }

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
            mPlayerRecyclerAdapter.notifyDataSetChanged();
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
                    getString(R.string.dialog_fragment_rolledValue) + " "
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
                            String newPlayerName = mOnSetEditTextListener.getTextInEditText();
                            if (newPlayerName.trim().equals("")) {
                                Toast.makeText(GameActivity.this, getString(R.string.dialog_fragment_error_name_empty), Toast.LENGTH_SHORT).show();
                            } else if (!validatePlayerName(newPlayerName)) {
                                Toast.makeText(GameActivity.this, getString(R.string.dialog_fragment_error_name_used), Toast.LENGTH_SHORT).show();
                            } else {
                                mPlayerList.get(position).setPlayerName(
                                        newPlayerName);
                                mPlayerRecyclerAdapter.notifyDataSetChanged();
                                mOnSetEditTextListener.clearEditText();
                                mCustomEditTextDialogFragment.dismiss();
                            }
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

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    private boolean validatePlayerName(String playerName) {
        boolean isUnique = true;
        if (playerName.trim().equals("")) {
            isUnique = false;
        } else {
            for (Player addedPlayers : mPlayerList) {
                if (playerName.equals(addedPlayers.getPlayerName())) {
                    isUnique = false;
                }
            }
        }
        return isUnique;
    }

    protected TourGuide bindTourGuideButton(String bodyText, ImageView imageView) {
        ToolTip toolTip = new ToolTip()
                .setTitle(getString(R.string.help_title))
                .setDescription(bodyText).setBackgroundColor(getResources().getColor(R.color.accent))
                .setGravity(Gravity.LEFT | Gravity.BOTTOM);

        return TourGuide.init(this)
                .setToolTip(toolTip).playLater(imageView);
    }
}
