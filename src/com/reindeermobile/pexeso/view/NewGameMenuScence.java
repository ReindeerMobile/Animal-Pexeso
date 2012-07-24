
package com.reindeermobile.pexeso.view;

import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.atlas.buildable.builder.ITextureBuilder.TextureAtlasSourcePackingException;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.util.Log;

import com.reindeermobile.pexeso.util.TiledSpriteMenuItem;

public class NewGameMenuScence extends MenuScene implements IOnMenuItemClickListener {

    MemoryActivity activity;

    private static final int MENU_PLAY = 0;

    public NewGameMenuScence() {
        super(MemoryActivity.getSharedInstance().getCamera());
        activity = MemoryActivity.getSharedInstance();

        setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

        SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
                1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        TiledTextureRegion pTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(
                        pBuildableBitmapTextureAtlas,
                        activity, "play_button.svg", 256, 128, 1, 2);

        try {
            pBuildableBitmapTextureAtlas
                    .build(new BlackPawnTextureBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
                            1));
        } catch (final TextureAtlasSourcePackingException e) {
            Log.e("pexeso", e.getMessage());
        }
        activity.getEngine().getTextureManager().loadTexture(pBuildableBitmapTextureAtlas);

        IMenuItem newGameButton = new TiledSpriteMenuItem(MENU_PLAY, pTextureRegion);
        newGameButton.setPosition(mCamera.getWidth() / 2 - newGameButton.getWidth()
                / 2,
                (int) ((mCamera.getHeight() / 2 - newGameButton.getHeight() / 2) - newGameButton
                        .getHeight() * 0.75));
        addMenuItem(newGameButton);

        setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
            float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case MENU_PLAY:
                activity.getGameScene().resetGame();
                activity.setCurrentScene(activity.getGameScene());
                return true;
            default:
                break;
        }
        return false;
    }

}
