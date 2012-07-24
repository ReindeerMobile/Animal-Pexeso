
package com.reindeermobile.pexeso.util;

import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

public class TiledSpriteMenuItem extends TiledSprite implements IMenuItem {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final int mID;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TiledSpriteMenuItem(final int pID, final TiledTextureRegion pTiledTextureRegion) {
        super(0, 0, pTiledTextureRegion);

        this.mID = pID;
    }

    public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight,
            final TiledTextureRegion pTiledTextureRegion) {
        super(0, 0, pWidth, pHeight, pTiledTextureRegion);

        this.mID = pID;
    }

    public TiledSpriteMenuItem(final int pID, final float pX, final float pY,
            final TiledTextureRegion pTiledTextureRegion,
            final RectangleVertexBuffer pRectangleVertexBuffer) {
        super(pX, pY, pTiledTextureRegion, pRectangleVertexBuffer);

        this.mID = pID;
    }

    public TiledSpriteMenuItem(final int pID, final float pX, final float pY,
            final float pTileWidth,
            final float pTileHeight, final TiledTextureRegion pTiledTextureRegion,
            final RectangleVertexBuffer pRectangleVertexBuffer) {
        super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion, pRectangleVertexBuffer);

        this.mID = pID;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public int getID() {
        return this.mID;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onSelected() {
        setCurrentTileIndex(1);
    }

    @Override
    public void onUnselected() {
        setCurrentTileIndex(0);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
