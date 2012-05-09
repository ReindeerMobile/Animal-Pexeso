package com.reindeermobile.pexeso.view;

import com.reindeermobile.pexeso.controller.DatabaseController;
import com.reindeermobile.pexeso.entity.Record;
import com.reindeermobile.reindeerutils.mvp.MessageObject;
import com.reindeermobile.reindeerutils.mvp.Presenter;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends BaseGameActivity {

	private static final int CARD_NUMBERS = 25;
	private static final int START_Y = 100;
	private static final int START_X = 20;
	private static final int X = 150;
	private static final int Y = 150;
	private static final float SCALE = 1.1f;

	private static final String TAG = "Pexeso";

	private Camera mCamera;
	private Scene mMainScene;
	private BitmapTextureAtlas mBitmapTextureAtlas;

	private int lastIndex = -1;
	private List<TiledSprite> cardsSprite;
	private List<Integer> table = new ArrayList<Integer>();

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 720;

	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private ChangeableText timeText;
	private ChangeableText clickText;
	private int clickNumber = 0;
	private boolean isInPlay = false;
	private int solved = 0;
	protected float playTime = 0f;

	@Override
	public Engine onLoadEngine() {
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new Engine(
				new EngineOptions(true, ScreenOrientation.PORTRAIT,
						new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
						mCamera));
	}

	@Override
	public void onLoadResources() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 512,
				TextureOptions.BILINEAR);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		this.mFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mFont = new Font(this.mFontTexture, Typeface.create(
				Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.BLACK);

		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);
	}

	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());

		mMainScene = new Scene();
		mMainScene.setBackground(new ColorBackground(0.098f, 0.62f, 0.87f));

		Text playerName = new Text(10, 10, this.mFont, "Player1");
		mMainScene.attachChild(playerName);

		timeText = new ChangeableText(230, 10, this.mFont, "0.0",
				HorizontalAlign.CENTER, 7);
		mMainScene.attachChild(timeText);

		clickText = new ChangeableText(400, 10, this.mFont, "0",
				HorizontalAlign.RIGHT, 7);
		mMainScene.attachChild(clickText);

		this.mMainScene.registerUpdateHandler(new TimerHandler(0.1f, true,
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
								GameActivity.this.playTime = time;
							}
						}
					}
				}));

		TiledTextureRegion cardsTexture = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"cards.png", 0, 0, 8, 4);

		List<Integer> randomCards = getRandomCards(24, 6);

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
				Log.d("debug", randomCards.size() + "");
				int index = random.nextInt(randomCards.size());
				int card = randomCards.remove(index);
				table.add(card);
				createCard(cardsTexture.clone(), x + j * X, y + i * Y, card,
						cardsSprite.get(cardIndex), cardIndex);
				cardIndex++;
			}
		}

		mMainScene.setTouchAreaBindingEnabled(true);

		return mMainScene;
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
					GameActivity.this.isInPlay = true;
					GameActivity.this.clickNumber++;
					GameActivity.this.clickText.setText(GameActivity.this.clickNumber + "");
					this.setCurrentTileIndex(card);
					if (GameActivity.this.lastIndex >= 0) {
						if (GameActivity.this.lastIndex != index
								&& GameActivity.this.table.get(index) != GameActivity.this.table
										.get(GameActivity.this.lastIndex)
								&& GameActivity.this.table.get(GameActivity.this.lastIndex) != 100
								&& GameActivity.this.table.get(index) != 100) {
							GameActivity.this.cardsSprite.get(GameActivity.this.lastIndex)
									.setCurrentTileIndex(0);
						}
						if (GameActivity.this.table.get(index) == GameActivity.this.table
								.get(GameActivity.this.lastIndex)
								&& index != GameActivity.this.lastIndex) {
							GameActivity.this.solved++;
							if (GameActivity.this.solved > 5) {
								GameActivity.this.isInPlay = false;
							}
							GameActivity.this.table.set(index, 100);
							GameActivity.this.table.set(GameActivity.this.lastIndex, 100);
						}
					}
					GameActivity.this.lastIndex = index;
				}
				return true;
			}
		};

		cardSprite.setScale(SCALE);
		mMainScene.attachChild(cardSprite);
		mMainScene.registerTouchArea(cardSprite);

		GameActivity.this.cardsSprite.set(index, cardSprite);
	}

	@Override
	public void onLoadComplete() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (clickNumber > 0) {
			Record record = new Record();
			record.setClicks(clickNumber);
			record.setLevel(1);
			record.setTime(playTime);

			Presenter.getInst().sendModelMessage(
					DatabaseController.SAVE_RECORD, new MessageObject(record));
			this.finish();
		}
	}

}
