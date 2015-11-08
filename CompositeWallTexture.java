package codename.cave.assets;


import codename.cave.game.Constants;
import codename.cave.game.objects.tiles.TileBlockState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class CompositeWallTexture implements ICompositeTexture {

	private final static String ATLAS_NAME = "level";
	private TextureRegion[] outlines;
	private int[] surrounding;
	private Color color;

	
	public CompositeWallTexture(int[] surrounding) {
		this(surrounding, new Color(0,1,0,1));
	}
	
	public CompositeWallTexture(int[] surrounding, Color color) {
		this.surrounding = surrounding;
		outlines = new TextureRegion[] {
				getTextureRegion("wall_outline_top"),
				getTextureRegion("wall_outline_right"),
				getTextureRegion("wall_outline_bottom"),
				getTextureRegion("wall_outline_left")
		};
		this.color = color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	private TextureRegion getTextureRegion(String region) {
		return new Sprite(Assets.getInstance().getAtlasRegion(ATLAS_NAME, region));
	}

	@Override
	public void draw(SpriteBatch batcher, Vector2 offset) {
		draw(batcher, offset, Constants.LEVEL_GRID_SIZE, Constants.LEVEL_GRID_SIZE);
	}

	@Override
	public void draw(SpriteBatch batcher, Vector2 offset, int width, int height) {
		Color c = batcher.getColor();
		batcher.setColor(color);
		for(int t = 0; t < outlines.length; t++) {
			if(surrounding[t] == TileBlockState.NONE.getValue()) {
				batcher.draw(outlines[t], offset.x * width, offset.y * height, width, height);
			}
		}
		batcher.setColor(c);
	}

}
