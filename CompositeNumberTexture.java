package codename.cave.assets;

import java.text.DecimalFormat;

import codename.cave.game.Constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Saves all sprites a number consists of and draws them depending on their position
 * 
 * @author Franz
 *
 */
public class CompositeNumberTexture implements ICompositeTexture {
	
	private final static String ATLAS_NAME = "math";
	private DecimalFormat format;
	private Sprite[] numberSprites;
	private Color color;
	
	
	public CompositeNumberTexture(double number) {
		this(number, new Color(1,1,1,1));
	}
	
	public CompositeNumberTexture(double number, Color color) {
		this.color = color;
		this.format = new DecimalFormat();
		this.format.setDecimalSeparatorAlwaysShown(false);
		this.numberSprites = generateSprites(number);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setNumber(double baseValue) {
		numberSprites = generateSprites(baseValue);
	}

	/**
	 * Returns an array of all used sprites by the given number
	 * @param number that will be used to generate a sprite array
	 * @return Array of used Sprites
	 */
	private Sprite[] generateSprites(double number) {
		
		char[] nums = format.format(number).toCharArray();
		Sprite[] sprites = new Sprite[nums.length];
		String region = "";
		
		for(int pos = 0; pos < nums.length; pos++) {
			
			switch(nums[pos]){
				case '0': region = "0"; break;
				case '1': region = "1"; break;
				case '2': region = "2"; break;
				case '3': region = "3"; break;
				case '4': region = "4"; break;
				case '5': region = "5"; break;
				case '6': region = "6"; break;
				case '7': region = "7"; break;
				case '8': region = "8"; break;
				case '9': region = "9"; break;
				case '+': region = "plus"; break;
				case '-': region = "minus"; break;
				case '.': region = "comma"; break;
				default: break;
			}
			
			sprites[pos] = getSprite(region);
		}
		
		return sprites;
	}
	
	/**
	 * Returns a Sprite representing a given number
	 * @param region name of the region: 1-9.+-/*
	 * @return TexureRegion inside the math atlas
	 */
	private Sprite getSprite(String region) {
		return new Sprite(Assets.getInstance().getAtlasRegion(ATLAS_NAME, region));
	}
	
	/**
	 * Draws all single values a number contains
	 * @param batcher 
	 * @param offset position of the drawn number
	 */
	public void draw(SpriteBatch batcher, Vector2 offset) {
		draw(batcher, offset, Constants.LEVEL_GRID_SIZE, Constants.LEVEL_GRID_SIZE);
	}
	
	public void draw(SpriteBatch batcher, Vector2 offset, int width, int height) {
		int pad = 2;
		int count = numberSprites.length;
		Color c = batcher.getColor();
		batcher.setColor(color);
		
		for(int i = 0; i < count; i++) {
			batcher.draw(numberSprites[i], 
					offset.x * width + i * width/count + pad,  
					offset.y * height + pad,
					width/count - pad, 
					height - pad);
		}
		batcher.setColor(c);
	}
	
}
