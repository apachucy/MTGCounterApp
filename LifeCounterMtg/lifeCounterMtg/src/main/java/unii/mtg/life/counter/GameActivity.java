package unii.mtg.life.counter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
import unii.mtg.life.counter.config.BaseConfig;
import unii.mtg.life.counter.config.DefaultStartGame;
import unii.mtg.life.counter.config.DisplayAdditionalCounters;
import unii.mtg.life.counter.helper.MenuHelper;
import unii.mtg.life.counter.pojo.GameSettings;
import unii.mtg.life.counter.pojo.Player;
import unii.mtg.life.counter.sharedpreferences.SettingsPreferencesFactory;
import unii.mtg.life.counter.view.OnElementClick;
import unii.mtg.life.counter.view.adapter.GridPlayersRecyclerAdapter;
import unii.mtg.life.counter.view.timer.CounterClass;


/**
 * @author Arkadiusz Pachucy
 */
public class GameActivity extends BaseActivity {

    public static final int MIN_GAME_LENGTH = 15;
    private static final int DEFAULT_ELEMENT_CLICKED = -1;
    private static GameSettings sRunSettings;
    private Unbinder mUnbinder;

    private List<Player> mPlayerList;

    private GridPlayersRecyclerAdapter mPlayerRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // dialog
    private ArrayList<String> mLifeList;
    private ArrayList<String> mDiceValues;

    private CounterClass mTimer;
    private DisplayAdditionalCounters mDisplayAdditionalCounters;
    // help library
    private TourGuide mTutorialHandler = null;
    private int mElementClicked = DEFAULT_ELEMENT_CLICKED;
    @BindView(R.id.activity_game_timerTextView)
    TextView mTimerTextView;

    @BindView(R.id.activity_game_timerButton)
    Button mTimerStartButton;
    @BindView(R.id.activity_game_playersRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mUnbinder = ButterKnife.bind(this);
        initData();
        initView();
        initActionBar();

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
                showInputDigitsDialog(this, getString(R.string.dialog_fragment_timerTitle),
                        getString(R.string.dialog_fragment_timerMessage), getString(R.string.dialog_fragment_timerHint),
                        sRunSettings.getGameTimeInMin() + "", mTimerSetAction
                );

                break;
            case R.id.settings_info:
                showInfoDialog(this, getString(R.string.dialog_fragment_info_title),
                        getString(R.string.dialog_fragment_info_message),
                        getString(R.string.help_button_action_end));
                break;
            case R.id.settings_set_counters:
                showMultipleChooserDialog(this, getString(R.string.dialog_set_counters_title),
                        mDisplayAdditionalCounters.countersToString(), mDisplayAdditionalCounters.getSelectedCounters(),
                        getString(R.string.dialog_button_set), mListCallbackMultiChoice);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @OnClick(R.id.activity_game_timerTextView)
    public void onTimerClicked() {
        mTimerStartButton.setVisibility(View.VISIBLE);
        if (mTimer != null) {
            mTimer.cancel();
        }

    }

    private void initData() {
        mPlayerList = new ArrayList<>();
        sRunSettings = new GameSettings();

        Intent openIntent = getIntent();
        if (isOpenFromOtherApplication(openIntent)) {
            setDataFromOtherApplication(openIntent);
        } else {
            mPlayerList.addAll(DefaultStartGame.getDefaultPlayers(this));
        }
        mLifeList = new ArrayList<>();
        mLifeList.addAll(DefaultStartGame.getLifeList());

        mDiceValues = new ArrayList<>();
        mDiceValues.addAll(DefaultStartGame.getDiceValueList());
        mDisplayAdditionalCounters = new DisplayAdditionalCounters(this);

    }

    private void initView() {
        mTimerStartButton.setOnClickListener(mOnTimerStart);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPlayerRecyclerAdapter = new GridPlayersRecyclerAdapter(mPlayerList, mDisplayAdditionalCounters);
        mPlayerRecyclerAdapter.setPlayerNameListener(mOnElementClick);
        mRecyclerView.setAdapter(mPlayerRecyclerAdapter);
        mTimerTextView.setText(populateTimerAtStart((long) (sRunSettings
                .getGameTimeInMin() * BaseConfig.DEFAULT_TIME_MINUT)));

    }

    private void initActionBar() {
        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
    }

    private boolean isOpenFromOtherApplication(Intent openIntent) {
        return openIntent != null && openIntent.getExtras() != null && openIntent.getExtras().containsKey(BaseConfig.BUNDLE_KEY_PLAYERS_NAMES);
    }

    private void setDataFromOtherApplication(Intent openIntent) {
        String[] draftPlayers = openIntent.getExtras().getStringArray(BaseConfig.BUNDLE_KEY_PLAYERS_NAMES);
        for (String draftPlayer : draftPlayers) {
            mPlayerList.add(new Player(draftPlayer));
        }

        if (openIntent.getExtras().containsKey(BaseConfig.BUNDLE_KEY_ROUND_TIME)) {
            sRunSettings.setGameTimeInMin(openIntent.getExtras().getInt(BaseConfig.BUNDLE_KEY_ROUND_TIME));
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
            Sequence sequence = new Sequence.SequenceBuilder().add(bindTourGuideButton(getString(R.string.help_life), lifeButton), bindTourGuideButton(getString(R.string.help_dice), diceButton), bindTourGuideButton(getString(R.string.help_person), addPlayer))
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
                showRadioButtonListDialog(GameActivity.this, getString(R.string.dialog_fragment_lifeTitle), mLifeList,
                        getString(R.string.dialog_button_set), getString(R.string.dialog_button_cancel), 0, mSetLifeAtStartAction);
            }
        });
        diceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showRadioButtonListDialog(GameActivity.this, getString(R.string.dialog_fragment_rollTitle), mDiceValues,
                        getString(R.string.dialog_button_roll), getString(R.string.dialog_button_cancel), 0, mRollDiceAction);
            }
        });
        addPlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputTextDialog(GameActivity.this, getString(R.string.dialog_fragment_playerTitle),
                        getString(R.string.dialog_fragment_playerMessage),
                        getString(R.string.dialog_fragment_playerHint), "", mAddPlayerAction);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

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
            mElementClicked = position;
            showInputTextDialog(GameActivity.this, getString(R.string.dialog_fragment_playerTitle),
                    getString(R.string.dialog_fragment_playerMessage),
                    getString(R.string.dialog_fragment_playerHint), mPlayerList.get(position).getPlayerName(), mChangePlayerAction);
        }
    };

    private static int rollDice(int diceValue) {
        Random random = new Random();
        // do not return value from range: 0 to diceValue-1 return value from 1
        // to diceValue
        return random.nextInt(diceValue) + 1;
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
                .setDescription(bodyText).setBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setGravity(Gravity.START | Gravity.BOTTOM);

        return TourGuide.init(this)
                .setToolTip(toolTip).playLater(imageView);
    }

    private MaterialDialog.InputCallback mTimerSetAction = new MaterialDialog.InputCallback() {
        @Override
        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            String newTimeValue = input.toString();
            try {
                int roundTime = Integer.parseInt(newTimeValue);
                if (roundTime < MIN_GAME_LENGTH) {
                    return;
                }
                sRunSettings.setGameTimeInMin(roundTime);
                mTimerStartButton.setVisibility(View.VISIBLE);
                if (mTimer != null) {
                    mTimer.cancel();
                }
                mTimerTextView.setText(populateTimerAtStart(sRunSettings.getGameTimeInMin() * BaseConfig.DEFAULT_TIME_MINUT));
            } catch (NumberFormatException e) {
                Toast.makeText(GameActivity.this, getString(R.string.warning_only_input_digits), Toast.LENGTH_LONG).show();
            }
        }
    };

    private MaterialDialog.ListCallbackSingleChoice mSetLifeAtStartAction = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            int startLife = Integer.parseInt(text.toString());
            for (Player p : mPlayerList) {
                p.setLifeCounter(startLife);
            }
            mPlayerRecyclerAdapter.notifyDataSetChanged();
            sRunSettings.setStartingLife(startLife);
            return true;
        }
    };

    private MaterialDialog.ListCallbackSingleChoice mRollDiceAction = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            int diceValue = Integer.parseInt(text.toString());

            showInfoDialog(GameActivity.this, getString(R.string.dialog_fragment_rollTitle),
                    getString(R.string.dialog_fragment_rolledValue) + " "
                            + rollDice(diceValue), getString(R.string.help_button_action_end));
            return true;
        }
    };

    private MaterialDialog.InputCallback mAddPlayerAction = new MaterialDialog.InputCallback() {
        @Override
        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            String playerName = input.toString();
            if (playerName.trim().equals("")) {
                Toast.makeText(GameActivity.this, getString(R.string.dialog_fragment_error_name_empty), Toast.LENGTH_SHORT).show();
            } else if (!validatePlayerName(playerName)) {
                Toast.makeText(GameActivity.this, getString(R.string.dialog_fragment_error_name_used), Toast.LENGTH_SHORT).show();
            } else {
                mPlayerList.add(new Player(playerName, sRunSettings.getStartingLife(), 0, 0));
                mPlayerRecyclerAdapter.notifyDataSetChanged();

            }
        }

    };


    private MaterialDialog.InputCallback mChangePlayerAction = new MaterialDialog.InputCallback() {
        @Override
        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            String newPlayerName = input.toString();
            if (newPlayerName.trim().equals("")) {
                Toast.makeText(GameActivity.this, getString(R.string.dialog_fragment_error_name_empty), Toast.LENGTH_SHORT).show();
            } else if (!validatePlayerName(newPlayerName)) {
                Toast.makeText(GameActivity.this, getString(R.string.dialog_fragment_error_name_used), Toast.LENGTH_SHORT).show();
            } else {
                if (mElementClicked == DEFAULT_ELEMENT_CLICKED || mElementClicked >= mPlayerList.size()) {
                    return;
                }
                mPlayerList.get(mElementClicked).setPlayerName(
                        newPlayerName);
                mElementClicked = DEFAULT_ELEMENT_CLICKED;
                mPlayerRecyclerAdapter.notifyDataSetChanged();

            }
        }
    };

    private MaterialDialog.ListCallbackMultiChoice mListCallbackMultiChoice = new MaterialDialog.ListCallbackMultiChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
            mDisplayAdditionalCounters.setVisibleEnergyCounter(false);
            mDisplayAdditionalCounters.setVisiblePoisonCounter(false);
            for (Integer aWhich : which) {
                switch (aWhich) {
                    case 0:
                        mDisplayAdditionalCounters.setVisiblePoisonCounter(true);
                        break;
                    case 1:
                        mDisplayAdditionalCounters.setVisibleEnergyCounter(true);
                        break;

                    default:
                        break;
                }
            }
            mPlayerRecyclerAdapter.notifyDataSetChanged();
            return true;
        }
    };
}
