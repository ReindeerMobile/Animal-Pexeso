
package com.reindeermobile.pexeso.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.reindeermobile.pexeso.R;

public class NewGameActivity extends Activity implements
        OnCheckedChangeListener, OnClickListener, OnFocusChangeListener {
    public static final String TAG = "NewGameActivity";

    public static final String SELECTED_PLAYER_NAME = "PLAYER_NAME";
    public static final String SELECTED_GAME_MODE = "GAME_MODE";
    public static final String SELECTED_GAME_LEVEL = "SELECTED_GAME_LEVEL";

    private EditText playerNameEditText;

    private ToggleButton classicModeToggleButton;
    private ToggleButton otherModeToggleButton;
    private ToggleButton easyToggleButton;
    private ToggleButton mediumToggleButton;
    private ToggleButton hardToggleButton;

    private GameMode actualGameMode = GameMode.CLASSIC;
    private GameLevel actualGameLevel = GameLevel.EASY;
    private Button playButton;
    private String actualPlayerName = "Player1";

    private enum GameMode {
        CLASSIC(0), OTHER(1);
        private int value;

        GameMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private enum GameLevel {
        EASY(0), MEDIUM(1), HARD(2);
        private int value;

        GameLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView != null) {
            int view = buttonView.getId();
            if (isChecked) {
                switch (view) {
                    case R.id.toggleButtonClassic:
                        this.updateGameModeStatus(GameMode.CLASSIC);
                        break;
                    case R.id.toggleButtonOther:
                        this.updateGameModeStatus(GameMode.OTHER);
                        break;
                    case R.id.toggleButtonEasy:
                        this.updateGameLevelStatus(GameLevel.EASY);
                        break;
                    case R.id.toggleButtonMedium:
                        this.updateGameLevelStatus(GameLevel.MEDIUM);
                        break;
                    case R.id.toggleButtonHard:
                        this.updateGameLevelStatus(GameLevel.HARD);
                        break;
                    default:
                        break;
                }
            } else {
                if (view == R.id.toggleButtonClassic
                        || view == R.id.toggleButtonOther) {
                    this.actualGameMode = null;
                } else {
                    this.actualGameLevel = null;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonPlay:
                    this.parsePlayerName();
                    if (this.checkStatus()) {
                        this.callGameActivity();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "onFocusChange - START");
        if (v != null && v.getId() == R.id.editTextPlayerName) {
            if (hasFocus) {
                Log.d(TAG, "onFocusChange - HIDE");
                // this.hideOthers();
            } else {
                Log.d(TAG, "onFocusChange - SHOW");
                // this.showOthers();
            }
        }
    }

    private void parsePlayerName() {
        String playerName = this.playerNameEditText.getEditableText()
                .toString().trim();
        if (playerName.length() > 0) {
            this.actualPlayerName = playerName;
        } else {
            this.actualPlayerName = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_new_game);

        this.initLayout();
    }

    private void initLayout() {
        this.classicModeToggleButton = (ToggleButton) findViewById(R.id.toggleButtonClassic);
        this.otherModeToggleButton = (ToggleButton) findViewById(R.id.toggleButtonOther);
        this.classicModeToggleButton.setOnCheckedChangeListener(this);
        this.otherModeToggleButton.setOnCheckedChangeListener(this);
        this.classicModeToggleButton.setChecked(true);

        this.easyToggleButton = (ToggleButton) findViewById(R.id.toggleButtonEasy);
        this.mediumToggleButton = (ToggleButton) findViewById(R.id.toggleButtonMedium);
        this.hardToggleButton = (ToggleButton) findViewById(R.id.toggleButtonHard);
        this.easyToggleButton.setOnCheckedChangeListener(this);
        this.mediumToggleButton.setOnCheckedChangeListener(this);
        this.hardToggleButton.setOnCheckedChangeListener(this);
        this.easyToggleButton.setChecked(true);

        this.playerNameEditText = (EditText) findViewById(R.id.editTextPlayerName);
        this.playerNameEditText.setOnFocusChangeListener(this);
        this.playerNameEditText.setText("Player1");
        
        this.playButton = (Button) findViewById(R.id.buttonPlay);
        this.playButton.setOnClickListener(this);
    }

    private void updateGameModeStatus(GameMode gameMode) {
        if (GameMode.CLASSIC == gameMode) {
            this.actualGameMode = GameMode.CLASSIC;
            this.classicModeToggleButton.setChecked(true);
        } else {
            this.classicModeToggleButton.setChecked(false);
        }
        if (GameMode.OTHER == gameMode) {
            this.otherModeToggleButton.setChecked(true);
            this.actualGameMode = GameMode.OTHER;
        } else {
            this.otherModeToggleButton.setChecked(false);
        }
    }

    private void updateGameLevelStatus(GameLevel gameLevel) {
        if (GameLevel.EASY == gameLevel) {
            this.actualGameLevel = GameLevel.EASY;
            this.easyToggleButton.setChecked(true);
        } else {
            this.easyToggleButton.setChecked(false);
        }
        if (GameLevel.MEDIUM == gameLevel) {
            this.actualGameLevel = GameLevel.MEDIUM;
            this.mediumToggleButton.setChecked(true);
        } else {
            this.mediumToggleButton.setChecked(false);
        }
        if (GameLevel.HARD == gameLevel) {
            this.actualGameLevel = GameLevel.HARD;
            this.hardToggleButton.setChecked(true);
        } else {
            this.hardToggleButton.setChecked(false);
        }
    }

    private boolean checkStatus() {
        Log.d(TAG, "checkStatus - START");
        if (this.actualGameLevel == null) {
            return false;
        }
        if (this.actualGameMode == null) {
            return false;
        }
        if (this.actualPlayerName == null) {
            return false;
        }
        return true;
    }

    private void callGameActivity() {
//        Log.d(TAG, "callGameActivity - START");
//        Intent gameIntent = new Intent(this, GameActivity.class);
//        gameIntent.putExtra(SELECTED_GAME_LEVEL,
//                this.actualGameLevel.getValue());
//        gameIntent.putExtra(SELECTED_GAME_MODE, this.actualGameMode.getValue());
//        gameIntent.putExtra(SELECTED_PLAYER_NAME, this.actualPlayerName);
//        this.startActivity(gameIntent);
//        this.finish();
    }
}
