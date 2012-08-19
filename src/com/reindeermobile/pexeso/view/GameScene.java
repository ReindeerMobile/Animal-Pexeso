
package com.reindeermobile.pexeso.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Color;
import android.util.Log;

public class GameScene extends Scene {
    private static final int PAIRS = 6;
    private static final int CARD_NUMBERS = 31;
    private static final int START_Y = 70;
    private static final int START_X = 20;
    private static final float SCALE = 1.15f;
    private static final int X = (int) (135 * SCALE);
    private static final int Y = (int) (135 * SCALE);

    private static final String TAG = "Pexeso";

    private int lastIndex = -1;
    private List<TiledSprite> cardsSprite;
    private List<Integer> table = new ArrayList<Integer>();

    private BitmapTextureAtlas mBitmapTextureAtlas;
    private BitmapTextureAtlas mFontTexture;
    private Font mFont;
    private ChangeableText timeText;
    private ChangeableText clickText;
    private int clickNumber = 0;
    private boolean isInPlay = false;
    private boolean firstTouch = true;
    private int solved = 0;
    private float time = 0.0f;

    MemoryActivity activity;

    private TimerHandler pUpdateHandler = new TimerHandler(0.1f, true,
            new ITimerCallback() {

                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    if (isInPlay) {
                        time += 0.1f;
                        timeText.setText(new DecimalFormat("#0.0")
                                .format(time) + "");
                    }
                }
            });
    
    private TiledTextureRegion cardsTexture;

    public GameScene() {
        activity = MemoryActivity.getSharedInstance();
    }

    public void loadResources() {
        mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 512,
                TextureOptions.BILINEAR);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        activity.getTextureManager().loadTexture(mBitmapTextureAtlas);

        FontFactory.setAssetBasePath("font/");
        this.mFontTexture = new BitmapTextureAtlas(256, 256,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        this.mFont = FontFactory.createFromAsset(this.mFontTexture, activity,
                "DroidSans-Bold.ttf",
                32, true, Color.WHITE);

        activity.getTextureManager().loadTexture(this.mFontTexture);
        activity.getFontManager().loadFont(this.mFont);
    }

    public void loadScenes() {
        this.setBackground(new ColorBackground(0.098f, 0.62f, 0.87f));

        Text timeLabel = new Text(10, 10, this.mFont, "Time:");
        this.attachChild(timeLabel);

        timeText = new ChangeableText(120, 10, this.mFont, "0123456789.");
        timeText.setText("0.0");
        this.attachChild(timeText);

        Text clickLabel = new Text(200, 10, this.mFont, "Clicks:");
        this.attachChild(clickLabel);

        clickText = new ChangeableText(350, 10, this.mFont, "0123456789");
        clickText.setText("0");
        this.attachChild(clickText);

        cardsTexture = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mBitmapTextureAtlas, activity,
                        "cards.png", 0, 0, 8, 4);
        Log.d(TAG, "loading time: " + (System.currentTimeMillis() - time));
    }

    public void resetGame() {
        Log.d(TAG, "reset game");
        this.isInPlay = false;
        this.firstTouch = true;
        this.solved = 0;
        this.lastIndex = -1;
        this.clickNumber = 0;
        clickText.setText("0");
        this.time = 0.0f;
        timeText.setText("0.0");
        if (this.cardsSprite != null) {
            for (TiledSprite sprite : this.cardsSprite) {
                this.unregisterTouchArea(sprite);
                this.detachChild(sprite);
            }
        }
        table = new ArrayList<Integer>();
        this.unregisterUpdateHandler(pUpdateHandler);

        List<Integer> randomCards = getRandomCards(CARD_NUMBERS, PAIRS);

        this.cardsSprite = new ArrayList<TiledSprite>();
        for (int i = 0; i < CARD_NUMBERS; i++) {
            this.cardsSprite.add(null);
        }

        int x = START_X;
        int y = START_Y;

        int cardIndex = 0;
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 2; j++) {
                Random random = new Random();
                int index = random.nextInt(randomCards.size());
                int card = randomCards.remove(index);
                table.add(card);
                createCard(cardsTexture.deepCopy(), x + j * X, y + i * Y, card,
                        cardsSprite.get(cardIndex), cardIndex);
                cardIndex++;
            }
        }

        this.setTouchAreaBindingEnabled(true);

        this.registerUpdateHandler(pUpdateHandler);
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
        cardSprite = new TiledSprite(x, y, cardsTexture) {

            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
                    final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    if (GameScene.this.firstTouch) {
                        GameScene.this.isInPlay = true;
                        GameScene.this.firstTouch = false;
                    }
                    if (GameScene.this.isInPlay) {
                        GameScene.this.clickNumber++;
                        GameScene.this.clickText
                                .setText(GameScene.this.clickNumber + "");
                        this.setCurrentTileIndex(card);
                        if (GameScene.this.lastIndex >= 0) {
                            if (GameScene.this.lastIndex != index
                                    && GameScene.this.table.get(index) != GameScene.this.table
                                            .get(GameScene.this.lastIndex)
                                    && GameScene.this.table
                                            .get(GameScene.this.lastIndex) != 100
                                    && GameScene.this.table.get(index) != 100) {
                                GameScene.this.cardsSprite.get(
                                        GameScene.this.lastIndex)
                                        .setCurrentTileIndex(0);
                            }
                            if (GameScene.this.table.get(index) == GameScene.this.table
                                    .get(GameScene.this.lastIndex)
                                    && index != GameScene.this.lastIndex) {
                                GameScene.this.solved++;
                                if (GameScene.this.solved >= PAIRS) {
                                    GameScene.this.isInPlay = false;
                                }
                                GameScene.this.table.set(index, 100);
                                GameScene.this.table.set(
                                        GameScene.this.lastIndex, 100);
                            }
                        }
                        GameScene.this.lastIndex = index;
                    }
                }
                return true;
            }
        };

        cardSprite.setScale(SCALE);
        this.attachChild(cardSprite);
        this.registerTouchArea(cardSprite);

        GameScene.this.cardsSprite.set(index, cardSprite);
    }
}
