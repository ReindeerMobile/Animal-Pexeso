
package com.reindeermobile.pexeso.view;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.widget.Toast;

import com.reindeermobile.pexeso.R;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {
    MemoryActivity activity;

    final int MENU_NEW_GAME = 0;
    final int MENU_TOPLIST = 1;
    final int MENU_ABOUT = 2;

    public MainMenuScene() {
        super(MemoryActivity.getSharedInstance().getCamera());
        activity = MemoryActivity.getSharedInstance();

        setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

        Font mFont = FontFactory.create(activity.getFontManager(),
                activity.getTextureManager(), 256, 256,
                Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 50);
        mFont.load();

        IMenuItem newGameButton = new TextMenuItem(MENU_NEW_GAME, mFont,
                activity.getString(R.string.title_new_game),
                activity.getVertexBufferObjectManager());
        newGameButton.setPosition(mCamera.getWidth() / 2 - newGameButton.getWidth()
                / 2, (mCamera.getHeight() / 2 - newGameButton.getHeight() / 2) * 3 / 4);
        addMenuItem(newGameButton);

        IMenuItem toplistButton = new TextMenuItem(MENU_TOPLIST, mFont,
                activity.getString(R.string.title_toplist),
                activity.getVertexBufferObjectManager());
        toplistButton.setPosition(mCamera.getWidth() / 2 - toplistButton.getWidth()
                / 2, (mCamera.getHeight() / 2 - toplistButton.getHeight() / 2));
        addMenuItem(toplistButton);

        IMenuItem aboutButton = new TextMenuItem(MENU_TOPLIST, mFont,
                activity.getString(R.string.title_about),
                activity.getVertexBufferObjectManager());
        aboutButton.setPosition(mCamera.getWidth() / 2 - aboutButton.getWidth()
                / 2, (mCamera.getHeight() / 2 - aboutButton.getHeight() / 2) * 5 / 4);
        addMenuItem(aboutButton);

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
