
package com.reindeermobile.pexeso.view;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.atlas.buildable.builder.ITextureBuilder.TextureAtlasSourcePackingException;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;

import com.Leadbolt.AdController;
import com.Leadbolt.AdLog;

public class MemoryActivity extends BaseGameActivity {

    private static final int SPLASH_HEIGHT = 423;
    private static final int SPLASH_WIDTH = 300;
    private static float scale = 1;
    public static int CAMERA_WIDTH = 480;
    public static int CAMERA_HEIGHT = 720;

    private Camera camera;
    private Scene splashScene;
    private GameScene gameScene;
    private MainMenuScene mainMenuScene;
    private NewGameMenuScence newGameMenuScene;

    private TextureRegion splashTextureRegion;
    private Sprite splash;

    private SceneType currentScene = SceneType.SPLASH;

    public static MemoryActivity instance;

    public static MemoryActivity getSharedInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public Engine onLoadEngine() {
        AdController myController = new AdController(getApplicationContext(), "215327244");
        myController.setAsynchTask(true);
        myController.loadIcon();

        instance = this;

        scale = (float) CAMERA_HEIGHT / SPLASH_HEIGHT * 0.9f;
        Log.d("pexeso", scale + "");

        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 100,
                getResources().getDisplayMetrics());
        Log.d("pexeso", "100 dip is in pixel: " + value);

        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera));
        return engine;
    }

    @Override
    public void onLoadResources() {
        SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
                1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        splashTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
                pBuildableBitmapTextureAtlas,
                this, "splash.svg", Math.round(SPLASH_WIDTH * scale),
                Math.round(SPLASH_HEIGHT * scale));
        try {
            pBuildableBitmapTextureAtlas
                    .build(new BlackPawnTextureBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
                            1));
        } catch (TextureAtlasSourcePackingException e) {
            e.printStackTrace();
        }

        this.mEngine.getTextureManager().loadTexture(pBuildableBitmapTextureAtlas);
    }

    @Override
    public Scene onLoadScene() {
        initSplashScene();
        return splashScene;
    }

    @Override
    public void onLoadComplete() {
        mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                mainMenuScene = new MainMenuScene();

                gameScene = new GameScene();
                gameScene.loadResources();
                gameScene.loadScenes();

                newGameMenuScene = new NewGameMenuScence();

                splash.detachSelf();
                setCurrentScene(mainMenuScene);
            }
        }));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (currentScene) {
                case SPLASH:
                    break;
                case MAIN_MENU:
                    finish();
                    break;
                case NEW_GAME:
                    setCurrentScene(mainMenuScene);
                    break;
                case CLASSIC_GAME:
                    setCurrentScene(newGameMenuScene);
                    break;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initSplashScene() {
        splashScene = new Scene();
        splash = new Sprite(0, 0, splashTextureRegion);

        splash.setPosition((CAMERA_WIDTH - splash.getWidth()) * 0.5f,
                (CAMERA_HEIGHT - splash.getHeight()) * 0.5f);
        splashScene.attachChild(splash);
        splashScene.setBackground(new ColorBackground(1f, 1f, 1f));
    }

    public void setCurrentScene(Scene scene) {
        if (scene instanceof GameScene) {
            currentScene = SceneType.CLASSIC_GAME;
        } else if (scene instanceof MainMenuScene) {
            currentScene = SceneType.MAIN_MENU;
        } else if (scene instanceof NewGameMenuScence) {
            currentScene = SceneType.NEW_GAME;
        }
        mEngine.setScene(scene);
    }

    public Camera getCamera() {
        return camera;
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public MainMenuScene getMainMenuScene() {
        return mainMenuScene;
    }

    public NewGameMenuScence getNewGameMenuScene() {
        return newGameMenuScene;
    }
}
