
package com.reindeermobile.pexeso.main;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends BaseGameActivity {

    private static final int X = 95;
    private static final int Y = 117;
    private static final float SCALE = 0.65f;

    private static final String TAG = "Pexeso";

    private Camera mCamera;
    private Scene mMainScene;
    private BitmapTextureAtlas mBitmapTextureAtlas;

    private int lastIndex = -1;
    private List<TiledSprite> cardsSprite;
    private List<Integer> table = new ArrayList<Integer>();

    private static final int CAMERA_WIDTH = 480;
    private static final int CAMERA_HEIGHT = 720;

    @Override
    public Engine onLoadEngine() {
        mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
    }

    @Override
    public void onLoadResources() {
        mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 512,
                TextureOptions.BILINEAR);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
    }

    @Override
    public Scene onLoadScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        mMainScene = new Scene();
        mMainScene.setBackground(new ColorBackground(0.098f, 0.62f, 0.87f));

        TiledTextureRegion cardsTexture = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(
                        this.mBitmapTextureAtlas, this, "cards.png", 0, 0, 8, 2);

        List<Integer> cards = new ArrayList<Integer>();
        for (int i = 1; i < 16; i++) {
            cards.add(i);
            cards.add(i);
        }

        cardsSprite = new ArrayList<TiledSprite>();
        for (int i = 0; i < 30; i++) {
            cardsSprite.add(null);
        }

        int x = -15;
        int y = -30;

        int cardIndex = 0;
        for (int i = 5; i >= 0; i--) {
            for (int j = 4; j >= 0; j--) {
                Random random = new Random();
                int index = random.nextInt(cards.size());
                int card = cards.remove(index);
                table.add(card);
                createCard(cardsTexture.clone(), x + j * X, y + i * Y, card,
                        cardsSprite.get(cardIndex), cardIndex);
                cardIndex++;
            }
        }

        for (Integer index : table) {
            Log.d(TAG, "" + index);
        }

        mMainScene.setTouchAreaBindingEnabled(true);
        return mMainScene;
    }

    private void createCard(TiledTextureRegion cardsTexture, int x, int y, final int card,
            TiledSprite cardSprite, final int index) {
        cardSprite = new TiledSprite(x, y, cardsTexture) {

            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
                    final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    this.setCurrentTileIndex(card);
                    if (Main.this.lastIndex >= 0) {
                        Log.d(TAG, "lastIndex: " + Main.this.lastIndex);
                        Log.d(TAG, "index: " + index);
                        Log.d(TAG,
                                "Main.this.table.last: " + Main.this.table.get(Main.this.lastIndex));
                        Log.d(TAG, "Main.this.table.actual: " + Main.this.table.get(index));
                        if (Main.this.lastIndex != index
                                && Main.this.table.get(index) != Main.this.table
                                        .get(Main.this.lastIndex) && Main.this.table
                                        .get(Main.this.lastIndex) != 100
                                && Main.this.table.get(index) != 100) {
                            Main.this.cardsSprite.get(Main.this.lastIndex).setCurrentTileIndex(0);
                        }
                        if (Main.this.table.get(index) == Main.this.table
                                .get(Main.this.lastIndex) && index != Main.this.lastIndex) {
                            Main.this.table.set(index, 100);
                            Main.this.table.set(Main.this.lastIndex, 100);
                        }
                    }
                    Main.this.lastIndex = index;
                }
                return true;
            }
        };

        cardSprite.setScale(SCALE);
        mMainScene.attachChild(cardSprite);
        mMainScene.registerTouchArea(cardSprite);

        Main.this.cardsSprite.set(index, cardSprite);
    }

    @Override
    public void onLoadComplete() {
    }

}
