package codename.cave.assets;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public final class Assets implements Disposable {
	// static members
	private static Assets instance = null;
	private static boolean assignedAssets = false;
	public static SpriteBatch SpriteBatcher = new SpriteBatch();
	public static BitmapFont Font = new BitmapFont();
	public static Skin skin = new Skin();
	
	// constants
	private static final String TEXTURES_LOCATION = "data/textures/";
	
	// fields
	private AssetManager manager;
	private Map<String, TextureAtlas> textureAtlanten;
	private Texture gameGround;
	
	private Assets() {
		manager = new AssetManager();
		textureAtlanten = new HashMap<String, TextureAtlas>();
		init();
	}
	
	public static Assets getInstance() {
		if(instance == null) {
			instance = new Assets();
		}
		return instance;
	}
	
	public <T> T getAsset(String path, Class<T> type) {
		if(manager.isLoaded(path)) {
			return manager.get(path, type);
		}
		return null;
	}
	
	public TextureRegion getAtlasRegion(String atlas, String region) {
		if(textureAtlanten.containsKey(atlas)) {
			return textureAtlanten.get(atlas).findRegion(region);
		}
		return null;
	}
	
	public TextureRegion getAtlasRegion(String atlas, String region, int index) {
		return textureAtlanten.get(atlas).findRegion(region, index);
	}
	
	private void init() {
		// we need stuff for the loading screen first
		manager.load("data/ui/loading.atlas", TextureAtlas.class);
		manager.finishLoading();
		// loading queue
		loadTextureAtlas("level");		//contains all level information
		loadTextureAtlas("characters");	//remove
		loadTextureAtlas("math");		//all math objects
		// let's see how we will treat the ground layer, I do not want to put it into the level atlas, but maybe something like "theme"
		manager.load(TEXTURES_LOCATION + "ground.png", Texture.class);
		Font.setColor(Color.WHITE);
		buildSkin();		
	}
	
	//Placeholder until we have a skin file and atlas for all the interface needs
	private void buildSkin() {
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		
		skin.add("white", new Texture(pixmap));
		skin.add("default", Font);

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
	}
	
	private void loadTextureAtlas(String name) {
		manager.load(TEXTURES_LOCATION + name + ".atlas", TextureAtlas.class);
		textureAtlanten.put(name, null); 
	}
	
	public boolean isLoadingDone() {
		if(manager != null && manager.update()) {
			assignAssets();
			return true;
		}
		return false;
	}
	
	private void assignAssets() {
		if(assignedAssets) {
			return;
		}
		
		for(String name : textureAtlanten.keySet()) {
			TextureAtlas atlas = getAsset(TEXTURES_LOCATION + name + ".atlas", TextureAtlas.class);
			if(atlas != null) {
				textureAtlanten.put(name, atlas);
				flipAtlas(atlas);
			}
			else {
				System.out.println("TextureAtlas '" + name + "' was not loaded.");
			}
		}
		gameGround = getAsset(TEXTURES_LOCATION + "ground.png", Texture.class);
		
		assignedAssets = true;
		System.out.println("Loading assets done.");
	}
	
	private void flipAtlas(TextureAtlas atlas) {
		Array<AtlasRegion> regions = atlas.getRegions();      
		for (int i = 0; i < regions.size; i++) {
			TextureRegion region = regions.get(i);
			if(!region.isFlipX()) {
				region.flip(false, true);
			}
		}
	}
	
	public Texture getGameGround() {
		return gameGround;
	}
	
	@Override
	public void dispose() {
		if(manager != null) manager.dispose();
		SpriteBatcher.dispose();
		skin.dispose();
		System.out.println("All assets were disposed!");
	}

}
