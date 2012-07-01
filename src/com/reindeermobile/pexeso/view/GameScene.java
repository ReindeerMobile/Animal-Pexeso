
package com.reindeermobile.pexeso.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.util.Log;

public class GameScene extends Scene {
    private static final int CARD_NUMBERS = 29;
    private static final int START_Y = 100;
    private static final int START_X = 20;
    private static final int X = 150;
    private static final int Y = 150;
    private static final float SCALE = 1.1f;

    private static final String TAG = "Pexeso";

    private int lastIndex = -1;
    private List<TiledSprite> cardsSprite;
    private List<Integer> table = new ArrayList<Integer>();

    private BitmapTextureAtlas mBitmapTextureAtlas;
    private BitmapTextureAtlas mFontTexture;
    private Font mFont;
    private Text timeText;
    private Text clickText;
    private int clickNumber = 0;
    private boolean isInPlay = false;
    private int solved = 0;

    MemoryActivity activity;

    public GameScene() {
        activity = MemoryActivity.getSharedInstance();
    }

    public void loadResources() {
        mBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 512,
                TextureOptions.BILINEAR);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        activity.getTextureManager().loadTexture(mBitmapTextureAtlas);
        this.mFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        this.mFont = new Font(activity.getFontManager(), this.mFontTexture, Typeface.create(
                Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.BLACK);

        activity.getTextureManager().loadTexture(this.mFontTexture);
        activity.getFontManager().loadFont(this.mFont);
    }

    public void loadScenes() {
        this.setBackground(new Background(0.098f, 0.62f, 0.87f));

        Text playerName = new Text(10, 10, this.mFont, "Player1",
                activity.getVertexBufferObjectManager());
        this.attachChild(playerName);

        timeText = new Text(230, 10, this.mFont, "0123456789.",
                activity.getVertexBufferObjectManager());
        timeText.setText("0.0");
        this.attachChild(timeText);

        clickText = new Text(400, 10, this.mFont, "0123456789",
                activity.getVertexBufferObjectManager());
        clickText.setText("0");
        this.attachChild(clickText);

        this.registerUpdateHandler(new TimerHandler(0.1f, true,
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
                            }
                        }
                    }
                }));

        TiledTextureRegion cardsTexture = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mBitmapTextureAtlas, activity,
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
                int index = random.nextInt(randomCards.size());
                int card = randomCards.remove(index);
                table.add(card);
                createCard(cardsTexture.deepCopy(), x + j * X, y + i * Y, card,
                        cardsSprite.get(cardIndex), cardIndex);
                cardIndex++;
            }
        }

        this.setTouchAreaBindingOnActionDownEnabled(true);
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
        cardSprite = new TiledSprite(x, y, cardsTexture, activity.getVertexBufferObjectManager()) {

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

    }
}
