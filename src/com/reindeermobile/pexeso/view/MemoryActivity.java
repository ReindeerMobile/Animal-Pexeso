
package com.reindeermobile.pexeso.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.reindeermobile.pexeso.controller.DatabaseController;
import com.reindeermobile.pexeso.entity.Record;
import com.reindeermobile.reindeerutils.mvp.Presenter;

public class MemoryActivity extends BaseGameActivity {

    private final int CAMERA_WIDTH = 480;
    private final int CAMERA_HEIGHT = 720;

    private Camera camera;
    private Scene splashScene;
    private Scene mainScene;

    private BitmapTextureAtlas splashTextureAtlas;
    private ITextureRegion splashTextureRegion;
    private Sprite splash;

    private static final int CARD_NUMBERS = 29;
    private static final int START_Y = 100;
    private static final int START_X = 20;
    private static final int X = 150;
    private static final int Y = 150;
    private static final float SCALE = 1.1f;

    private static final String TAG = "Pexeso";

    private BitmapTextureAtlas mBitmapTextureAtlas;

    private int lastIndex = -1;
    private List<TiledSprite> cardsSprite;
    private List<Integer> table = new ArrayList<Integer>();

    private BitmapTextureAtlas mFontTexture;
    private Font mFont;
    private Text timeText;
    private Text clickText;
    private int clickNumber = 0;
    private boolean isInPlay = false;
    private int solved = 0;
    protected float playTime = 0f;

    private enum SceneType {
        SPLASH,
        MAIN
    }

    private SceneType currentScene = SceneType.SPLASH;

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
            throws Exception {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,
                TextureOptions.DEFAULT);
        splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                splashTextureAtlas, this, "splash.png", 0, 0);
        splashTextureAtlas.load();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        initSplashScene();
        pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback)
            throws Exception {
        mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                loadResources();
                loadScenes();
                splash.detachSelf();
                mEngine.setScene(mainScene);
                currentScene = SceneType.MAIN;
            }
        }));

        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (currentScene) {
                case SPLASH:
                    break;
                case MAIN:
                    System.exit(0);
                    break;
            }
        }
        return false;
    }

    public void loadResources() {
        // Load your game resources here!
        mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1024, 512,
                TextureOptions.BILINEAR);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
        this.mFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        this.mFont = new Font(this.getFontManager(), this.mFontTexture, Typeface.create(
                Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.BLACK);

        this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
        this.mEngine.getFontManager().loadFont(this.mFont);
    }

    private void loadScenes() {
        // load your game here, you scenes
        mEngine.registerUpdateHandler(new FPSLogger());

        mainScene = new Scene();
        mainScene.setBackground(new Background(0.098f, 0.62f, 0.87f));

        Text playerName = new Text(10, 10, this.mFont, "Player1",
                this.getVertexBufferObjectManager());
        mainScene.attachChild(playerName);

        timeText = new Text(230, 10, this.mFont, "0123456789.", this.getVertexBufferObjectManager());
        timeText.setText("0.0");
        mainScene.attachChild(timeText);

        clickText = new Text(400, 10, this.mFont, "0123456789", this.getVertexBufferObjectManager());
        clickText.setText("0");
        mainScene.attachChild(clickText);

        this.mainScene.registerUpdateHandler(new TimerHandler(0.1f, true,
                new ITimerCallback() {
                    float time = 0.0f;

                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        if (isInPlay) {
                            time += 0.1f;
                            timeText.setText(new DecimalFormat("#0.0")
                                    .format(time) + "");
                        } else {
                            if (time > 0.0) {
                                Log.d(TAG, "onLoadScene - end");
                                MemoryActivity.this.playTime = time;
                            }
                        }
                    }
                }));

        TiledTextureRegion cardsTexture = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mBitmapTextureAtlas, this,
                        "cards.png", 0, 0, 8, 4);

        List<Integer> randomCards = getRandomCards(CARD_NUMBERS, 6);

        cardsSprite = new ArrayList<TiledSprite>();
        for (int i = 0; i < CARD_NUMBERS; i++) {
            cardsSprite.add(null);
        }

        int x = START_X;
        int y = START_Y;

        int cardIndex = 0;
        for (int i = 3; i >= 0; i--) {
            for (int j = 2; j >= 0; j--) {
                Random random = new Random();
                Log.d("debug", randomCards.size() + "");
                int index = random.nextInt(randomCards.size());
                int card = randomCards.remove(index);
                table.add(card);
                createCard(cardsTexture.deepCopy(), x + j * X, y + i * Y, card,
                        cardsSprite.get(cardIndex), cardIndex);
                cardIndex++;
            }
        }

        mainScene.setTouchAreaBindingOnActionDownEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clickNumber > 0) {
            Record record = new Record();
            record.setClicks(clickNumber);
            record.setLevel(1);
            record.setTime(playTime);

            Bundle messageBundle = new Bundle();
            messageBundle.putParcelable(DatabaseController.SAVE_RECORD, record);
            Presenter.getInst().sendModelMessage(
                    DatabaseController.SAVE_RECORD, messageBundle);
            // Presenter.getInst().sendModelMessage(
            // DatabaseController.SAVE_RECORD, new MessageObject(record));
            this.finish();
        }
    }

    // ===========================================================
    // INITIALIZIE
    // ===========================================================

    private void initSplashScene() {
        splashScene = new Scene();
        splash = new Sprite(0, 0, splashTextureRegion, mEngine.getVertexBufferObjectManager()) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };

        splash.setScale(1.5f);
        splash.setPosition((CAMERA_WIDTH - splash.getWidth()) * 0.5f,
                (CAMERA_HEIGHT - splash.getHeight()) * 0.5f);
        splashScene.attachChild(splash);
        splashScene.setBackground(new Background(1f, 1f, 1f));
    }

    private List<Integer> getRandomCards(int cardNumbers, int pairs) {
        List<Integer> cards = new ArrayList<Integer>();
        for (int i = 1; i <= cardNumbers; i++) {
            cards.add(i);
        }
        List<Integer> randomCards = new ArrayList<Integer>();
        for (int j = 0; j < pairs; j++) {
            Random random = new Random();
            int selectedCardIndex = random.nextInt(cards.size() - 1) + 1;
            int selectedCard = cards.get(selectedCardIndex);
            cards.remove(selectedCardIndex);
            randomCards.add(selectedCard);
            randomCards.add(selectedCard);
        }
        return randomCards;
    }

    private void createCard(TiledTextureRegion cardsTexture, int x, int y,
            final int card, TiledSprite cardSprite, final int index) {
        cardSprite = new TiledSprite(x, y, cardsTexture, this.getVertexBufferObjectManager()) {

            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
                    final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    MemoryActivity.this.isInPlay = true;
                    MemoryActivity.this.clickNumber++;
                    MemoryActivity.this.clickText
                            .setText(MemoryActivity.this.clickNumber + "");
                    this.setCurrentTileIndex(card);
                    if (MemoryActivity.this.lastIndex >= 0) {
                        if (MemoryActivity.this.lastIndex != index
                                && MemoryActivity.this.table.get(index) != MemoryActivity.this.table
                                        .get(MemoryActivity.this.lastIndex)
                                && MemoryActivity.this.table
                                        .get(MemoryActivity.this.lastIndex) != 100
                                && MemoryActivity.this.table.get(index) != 100) {
                            MemoryActivity.this.cardsSprite.get(
                                    MemoryActivity.this.lastIndex)
                                    .setCurrentTileIndex(0);
                        }
                        if (MemoryActivity.this.table.get(index) == MemoryActivity.this.table
                                .get(MemoryActivity.this.lastIndex)
                                && index != MemoryActivity.this.lastIndex) {
                            MemoryActivity.this.solved++;
                            if (MemoryActivity.this.solved > 5) {
                                MemoryActivity.this.isInPlay = false;
                            }
                            MemoryActivity.this.table.set(index, 100);
                            MemoryActivity.this.table.set(
                                    MemoryActivity.this.lastIndex, 100);
                        }
                    }
                    MemoryActivity.this.lastIndex = index;
                }
                return true;
            }
        };

        cardSprite.setScale(SCALE);
        mainScene.attachChild(cardSprite);
        mainScene.registerTouchArea(cardSprite);

        MemoryActivity.this.cardsSprite.set(index, cardSprite);
    }
}
