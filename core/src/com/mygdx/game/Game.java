package com.mygdx.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.mygdx.game.Block.Block;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Block.DefaultBlock;
import com.mygdx.game.Entity.Player;

import java.util.HashMap;


public class Game extends ApplicationAdapter {
	public static World world;
	public static SpriteBatch batch;
	public static Box2DDebugRenderer debugRenderer;
	public static OrthographicCamera camera;
	public static Player player;
	HashMap<String, Texture> blockTextures;
	static double BLOCKS_HORIZONTAL_AXIS = 26;
	static double BLOCKS_VERTICAL_AXIS = 20;
	static double PPM = 100;
	boolean goingLeft = false;

	public static void drawSprite(Sprite sprite, double x, double y, int rot) {
		Vector2 playerPos = player.body.getPosition();
		float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
		float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
		double xSize = sprite.getWidth()/xScale;
		double ySize = sprite.getHeight()/yScale;
		double xPos = (x - playerPos.x) * (PPM/xScale) + Gdx.graphics.getWidth() / 2 - xSize / 2;
		double yPos = (y - playerPos.y) * (PPM/yScale) + Gdx.graphics.getHeight() / 2 - ySize / 2;
		sprite.setPosition((float) xPos,(float) yPos);
		sprite.setSize((float) xSize, (float) ySize);
		sprite.setOrigin((float)xSize/2,(float)ySize/2);
		sprite.setRotation(rot);
		sprite.draw(batch);
	}

	public static void drawSprite(Sprite sprite, double x, double y) {
		drawSprite(sprite,x,y,0);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -10), true);
		player = new Player();
		BlockTracker.setWorld(world);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (float) BLOCKS_HORIZONTAL_AXIS,(float) BLOCKS_VERTICAL_AXIS);
		System.out.println(camera.position);



		// add block textures
		blockTextures = new HashMap<>();
		blockTextures.put("Grass", new Texture("Images/Blocks/grass.png"));
		blockTextures.put("Dirt", new Texture("Images/Blocks/dirt.png"));
		blockTextures.put("Stone", new Texture("Images/Blocks/stone.png"));
		blockTextures.put("Leaves", new Texture("Images/Blocks/leaves.png"));
		blockTextures.put("Wood", new Texture("Images/Blocks/wood.png"));
		blockTextures.put("Water", new Texture("Images/Blocks/water.png"));

		//starts some stuff
		TerrainGenerator.setTreeData();

		// creates blocks
		TerrainGenerator.generateTerrain();
	}

	@Override
	public void render () {

		Vector2 vel = player.body.getLinearVelocity();
		Vector2 pos = player.body.getPosition();

		ScreenUtils.clear(1, 0, 0, 1);
		world.step(1/60f, 6, 2);
		camera.position.x = player.body.getPosition().x;
		camera.position.y = player.body.getPosition().y;
		camera.position.z = 0;
		camera.update();
		//playerSprite.setPosition(player.getPosition().x, player.getPosition().y);
		batch.begin();
		// renders blocks
		float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
		float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
		Vector2 playerPos = player.body.getPosition();
		for (HashMap.Entry<Block, Vector2> entry : BlockTracker.getAllBlockPositions().entrySet()) {
			Block currentBlock = entry.getKey();
			Vector2 currentPos = entry.getValue();
			Texture currentTexture = blockTextures.get(currentBlock.getName());

			double xSize = currentTexture.getWidth()/xScale;
			double ySize = currentTexture.getHeight()/yScale;
			double xPos = (currentPos.x - playerPos.x) * (PPM/xScale) + Gdx.graphics.getWidth() / 2 - xSize / 2;
			double yPos = (currentPos.y - playerPos.y) * (PPM/yScale) + Gdx.graphics.getHeight() / 2 - ySize / 2;
			batch.draw(currentTexture, (float) xPos, (float) yPos, (float) xSize, (float) ySize);
		}
		batch.end();

		player.update();
		debugRenderer.render(world, camera.combined);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
