
package com.reindeermobile.pexeso.view;

import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.atlas.buildable.builder.ITextureBuilder.TextureAtlasSourcePackingException;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.reideermobile.pexeso.util.TiledSpriteMenuItem;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {

    MemoryActivity activity;

    private static final int MENU_NEW_GAME = 0;
    private static final int MENU_TOPLIST = 1;
    private static final int MENU_ABOUT = 2;
    private static final int MENU_FACEBOOK = 3;

    public MainMenuScene() {
        super(MemoryActivity.getSharedInstance().getCamera());
        activity = MemoryActivity.getSharedInstance();

        setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

        BitmapTextureAtlas mFontTexture = new BitmapTextureAtlas(256, 256,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Font mFont = new Font(mFontTexture, Typeface.create(
                Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.BLACK);
        activity.getEngine().getTextureManager().loadTexture(mFontTexture);
        activity.getEngine().getFontManager().loadFont(mFont);

        SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
                1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        TiledTextureRegion pTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(
                        pBuildableBitmapTextureAtlas,
                        activity, "play_button.svg", 256, 128, 1, 2);

        TiledTextureRegion pTextureRegion2 = SVGBitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(
                        pBuildableBitmapTextureAtlas,
                        activity, "toplist_button.svg", 256, 128, 1, 2);

        TiledTextureRegion pTextureRegion3 = SVGBitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(
                        pBuildableBitmapTextureAtlas,
                        activity, "about_button.svg", 256, 128, 1, 2);

        TextureRegion titleTexture = SVGBitmapTextureAtlasTextureRegionFactory
                .createFromAsset(pBuildableBitmapTextureAtlas, activity, "title.svg", 300, 150);
        Sprite title = new Sprite(7, 7, titleTexture);
        this.attachChild(title);

        TiledTextureRegion facebookTexture = SVGBitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(pBuildableBitmapTextureAtlas, activity, "facebook.svg", 50,
                        100, 1, 2);

        TextureRegion twitterTexture = SVGBitmapTextureAtlasTextureRegionFactory
                .createFromAsset(pBuildableBitmapTextureAtlas, activity, "twitter.svg", 50, 50);
        Sprite twitter = new Sprite(140, 400, twitterTexture);
        this.attachChild(twitter);

        TextureRegion googleTexture = SVGBitmapTextureAtlasTextureRegionFactory
                .createFromAsset(pBuildableBitmapTextureAtlas, activity, "google.svg", 50, 50);
        Sprite google = new Sprite(200, 400, googleTexture);
        this.attachChild(google);

        try {
            pBuildableBitmapTextureAtlas
                    .build(new BlackPawnTextureBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
                            1));
        } catch (final TextureAtlasSourcePackingException e) {
            Log.e("pexeso", e.getMessage());
        }
        activity.getEngine().getTextureManager().loadTexture(pBuildableBitmapTextureAtlas);

        IMenuItem newGameButton = new TiledSpriteMenuItem(MENU_NEW_GAME, pTextureRegion);
        newGameButton.setPosition(mCamera.getWidth() / 2 - newGameButton.getWidth()
                / 2,
                (int) ((mCamera.getHeight() / 2 - newGameButton.getHeight() / 2) - newGameButton
                        .getHeight() * 0.75));
        addMenuItem(newGameButton);

        IMenuItem toplistButton = new TiledSpriteMenuItem(MENU_TOPLIST, pTextureRegion2);
        toplistButton.setPosition(
                mCamera.getWidth() / 2 - toplistButton.getWidth()
                        / 2, (int)
                ((mCamera.getHeight() / 2 - toplistButton.getHeight() / 2)
                + toplistButton.getHeight() * 0.5));
        addMenuItem(toplistButton);

        IMenuItem aboutButton = new TiledSpriteMenuItem(MENU_ABOUT, pTextureRegion3);
        aboutButton.setPosition(mCamera.getWidth() / 2 - aboutButton.getWidth()
                / 2, (int) ((mCamera.getHeight() / 2 - aboutButton.getHeight() / 2) + aboutButton
                .getHeight() * 1.75));
        addMenuItem(aboutButton);

        IMenuItem facebookButton = new TiledSpriteMenuItem(MENU_FACEBOOK, facebookTexture);
        facebookButton.setPosition(80, 400);
        addMenuItem(facebookButton);

        setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
            float arg2, float arg3) {
        switch (arg1.getID()) {
            case MENU_NEW_GAME:
                activity.getGameScene().resetGame();
                activity.setCurrentScene(activity.getGameScene());
                return true;
            case MENU_TOPLIST:
                break;
            case MENU_ABOUT:
                createAboutBox();
                break;
            default:
                break;
        }
        return false;
    }

    private void createAboutBox() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
                alertbox.setMessage("Reindeer Mobile");
                alertbox.setPositiveButton("OK", null);
                alertbox.show();
            }
        });
    }
}
