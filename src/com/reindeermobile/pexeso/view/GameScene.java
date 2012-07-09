
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
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

public class GameScene extends Scene {
    private static final int CARD_NUMBERS = 29;
    private static final int START_Y = 40;
    private static final int START_X = -20;
    private static final int X = 77;
    private static final int Y = 77;
    private static final float SCALE = 0.6f;

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
    private int solved = 0;

    MemoryActivity activity;

    private TimerHandler pUpdateHandler = new TimerHandler(0.1f, true,
            new ITimerCallback() {
                float time = 0.0f;

                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    if (isInPlay) {
                        time += 0.1f;
                        timeText.setText(new DecimalFormat("#0.0")
                                .format(time) + "");
                    }
                }
            });

    public GameScene() {
        activity = MemoryActivity.getSharedInstance();
    }

    public void loadResources() {
        mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 512,
                TextureOptions.BILINEAR);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        activity.getTextureManager().loadTexture(mBitmapTextureAtlas);
        this.mFontTexture = new BitmapTextureAtlas(256, 256,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        this.mFont = new Font(this.mFontTexture, Typeface.create(
                Typeface.DEFAULT, Typeface.BOLD), 16, true, Color.BLACK);

        activity.getTextureManager().loadTexture(this.mFontTexture);
        activity.getFontManager().loadFont(this.mFont);
    }

    public void loadScenes() {
        this.setBackground(new ColorBackground(0.098f, 0.62f, 0.87f));

        Text playerName = new Text(10, 10, this.mFont, "Player1");
        this.attachChild(playerName);

        timeText = new ChangeableText(150, 10, this.mFont, "0123456789.");
        timeText.setText("0.0");
        this.attachChild(timeText);

        clickText = new ChangeableText(250, 10, this.mFont, "0123456789");
        clickText.setText("0");
        this.attachChild(clickText);

        this.registerUpdateHandler(pUpdateHandler);

        long time = System.currentTimeMillis();
        TiledTextureRegion cardsTexture = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mBitmapTextureAtlas, activity,
                        "cards.png", 0, 0, 8, 4);
        Log.d(TAG, "loading time: " + (System.currentTimeMillis() - time));

        List<Integer> randomCards = getRandomCards(CARD_NUMBERS, 10);

        cardsSprite = new ArrayList<TiledSprite>();
        for (int i = 0; i < CARD_NUMBERS; i++) {
            cardsSprite.add(null);
        }

        int x = START_X;
        int y = START_Y;

        int cardIndex = 0;
        for (int i = 4; i >= 0; i--) {
            for (int j = 3; j >= 0; j--) {
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
                    GameScene.this.isInPlay = true;
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
                            if (GameScene.this.solved > 5) {
                                GameScene.this.isInPlay = false;
                            }
                            GameScene.this.table.set(index, 100);
                            GameScene.this.table.set(
                                    GameScene.this.lastIndex, 100);
                        }
                    }
                    GameScene.this.lastIndex = index;
                }
                return true;
            }
        };

        cardSprite.setScale(SCALE);
        this.attachChild(cardSprite);
        this.registerTouchArea(cardSprite);

        GameScene.this.cardsSprite.set(index, cardSprite);
    }

    public void resetGame() {
        Log.d(TAG, "reset game");
        // this.unregisterUpdateHandler(pUpdateHandler);
    }
}
