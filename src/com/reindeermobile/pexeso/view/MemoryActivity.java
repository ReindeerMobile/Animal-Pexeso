
package com.reindeermobile.pexeso.view;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

public class MemoryActivity extends BaseGameActivity {

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 720;

    private Camera camera;
    private Scene splashScene;
    private GameScene gameScene;
    private MainMenuScene mainMenuScene;

    private BitmapTextureAtlas splashTextureAtlas;
    private ITextureRegion splashTextureRegion;
    private Sprite splash;

    private SceneType currentScene = SceneType.SPLASH;

    public static MemoryActivity instance;

    public static MemoryActivity getSharedInstance() {
        return instance;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        instance = this;
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

                mainMenuScene = new MainMenuScene();

                gameScene = new GameScene();
                gameScene.loadResources();
                gameScene.loadScenes();

                splash.detachSelf();
                setCurrentScene(mainMenuScene);
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
                case MAIN_MENU:
                    System.exit(0);
                    break;
                case CLASSIC_GAME:
                    setCurrentScene(mainMenuScene);
                    break;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if (clickNumber > 0) {
        // Record record = new Record();
        // record.setClicks(clickNumber);
        // record.setLevel(1);
        // record.setTime(playTime);
        //
        // Bundle messageBundle = new Bundle();
        // messageBundle.putParcelable(DatabaseController.SAVE_RECORD, record);
        // Presenter.getInst().sendModelMessage(
        // DatabaseController.SAVE_RECORD, messageBundle);
        // // Presenter.getInst().sendModelMessage(
        // // DatabaseController.SAVE_RECORD, new MessageObject(record));
        // this.finish();
        // }
    }

    private void initSplashScene() {
        splashScene = new Scene();
        splash = new Sprite(0, 0, splashTextureRegion, this.getVertexBufferObjectManager()) {
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

    public void setCurrentScene(Scene scene) {
        if (scene instanceof GameScene) {
            currentScene = SceneType.CLASSIC_GAME;
        } else if (scene instanceof MainMenuScene) {
            currentScene = SceneType.MAIN_MENU;
        }
        mEngine.setScene(scene);
    }

    public Camera getCamera() {
        return camera;
    }

    public GameScene getGameScene() {
        return gameScene;
    }
}
