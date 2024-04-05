package com.mygdx.game;

import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.PerformanceCounter;
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
import com.mygdx.game.Block.CreateBlock;
import com.mygdx.game.Block.DefaultBlock;
import com.mygdx.game.Entity.Entity;
import com.mygdx.game.Entity.Player;
import com.mygdx.game.Entity.Slime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.Vector;


public class Game extends ApplicationAdapter {
	public static World world;
	public static SpriteBatch batch;
	public static Box2DDebugRenderer debugRenderer;
	public static OrthographicCamera camera;
	public static Player player;
	private static HashMap<String, Texture> blockTextures;
	public static double BLOCKS_HORIZONTAL_AXIS = 26;
	public static double BLOCKS_VERTICAL_AXIS = 20;
	public static double PPM = 100;
	boolean goingLeft = false;
	public static HashMap<Block, Long> blockUpdateTimes = new HashMap<>();
	public static PerformanceCounter counter;

	public static FPSLogger logger = new FPSLogger();

	@Override
	public void create () {
		counter = new PerformanceCounter("Game");
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -10), true);
		player = new Player();
		BlockTracker.setWorld(world);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (float) BLOCKS_HORIZONTAL_AXIS,(float) BLOCKS_VERTICAL_AXIS);
		new Slime("Pig");


		// add block textures
		blockTextures = new HashMap<>();
		blockTextures.put("Grass", new Texture("Images/Blocks/grass.png"));
		blockTextures.put("Dirt", new Texture("Images/Blocks/dirt.png"));
		blockTextures.put("Stone", new Texture("Images/Blocks/stone.png"));
		blockTextures.put("Leaves", new Texture("Images/Blocks/leaves.png"));
		blockTextures.put("Wood", new Texture("Images/Blocks/wood.png"));
		blockTextures.put("Flower", new Texture("Images/Blocks/flower.png"));
		blockTextures.put("Water1", new Texture("Images/Blocks/water1.png"));
		blockTextures.put("Water2", new Texture("Images/Blocks/water2.png"));
		blockTextures.put("Water3", new Texture("Images/Blocks/water3.png"));
		blockTextures.put("Water4", new Texture("Images/Blocks/water4.png"));
		blockTextures.put("Water5", new Texture("Images/Blocks/water5.png"));
		blockTextures.put("Water6", new Texture("Images/Blocks/water6.png"));
		blockTextures.put("TNT", new Texture("Images/Blocks/tnt.png"));
		blockTextures.put("Sand", new Texture("Images/Blocks/sand.png"));
		blockTextures.put("Iron", new Texture("Images/Blocks/iron.png"));

		//starts some stuff
		TerrainGenerator.setTreeData();

		// creates blocks
		TerrainGenerator.generateTerrain();
	}

	@Override
	public void render () {
		//counter.start();
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
		batch.draw(new Sprite(new Texture("Images/Sky.png")),0f,0f,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
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
			if (yPos > -100 && yPos < Gdx.graphics.getHeight() + 100 && xPos > -100 && xPos < Gdx.graphics.getWidth() + 100) {
				batch.draw(currentTexture, (float) xPos, (float) yPos, (float) xSize, (float) ySize);
			}
		}
		Entity.updateEntities();
		for (Contact contact : world.getContactList()) {
			Entity e1 = Entity.getInstance(contact.getFixtureA().getBody());
			Entity e2 = Entity.getInstance(contact.getFixtureB().getBody());
			if (e1 != null && e2 != null) {
				e1.collision(e2);
				e2.collision(e1);
			}
		}
		batch.end();

		debugRenderer.render(world, camera.combined);

		//block building
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			Vector2 mousePos = TUtility.getCursor();
			Vector2 roundedMousePos = TUtility.getRoundedVector2(mousePos);
			ArrayList<Block> blocksAtPos = BlockTracker.getBlocksAtPosition(roundedMousePos);
			boolean canPlace = true;
			ArrayList<Block> blocksToRemove = new ArrayList<>();
			for (int i = 0; i < blocksAtPos.size(); i++) {
				Block currentBlock = blocksAtPos.get(i);
				if (currentBlock.getBlockType().equals("Liquid")) {
					blocksToRemove.add(currentBlock);
				} else {
					canPlace = false;
				}
			}
			boolean hasSurroundingBlock = false;
			Vector2[] vecsToCheck = {new Vector2(1, 0), new Vector2(0, 1), new Vector2(-1, 0), new Vector2(0, -1)};
			for (int i = 0; i < vecsToCheck.length; i++) {
				ArrayList<Block> blocks = BlockTracker.getBlocksAtPosition(vecsToCheck[i].add(roundedMousePos));
				for (int x = 0; x < blocks.size(); x++) {
					Block currentBlock = blocks.get(x);
					if (!currentBlock.getBlockType().equals("Liquid")) {
						hasSurroundingBlock = true;
						break;
					}
				}

				if (hasSurroundingBlock) {
					break;
				}
			}
			if (canPlace && hasSurroundingBlock) {
				for (int i = 0; i < blocksToRemove.size(); i++) {
					blocksToRemove.get(i).destroyBlock(false);
				}
				new CreateBlock("Sand", TUtility.getRoundedVector2(mousePos));
			}
		}

		long currentTimeMillis = System.currentTimeMillis();
		ArrayList<Block> readyToUpdate = new ArrayList<>();

		for (HashMap.Entry<Block, Long> entry : blockUpdateTimes.entrySet()) {
			Block currentBlock = entry.getKey();
			long updateTime = entry.getValue();
			if (currentTimeMillis >= updateTime) {
				readyToUpdate.add(currentBlock);
			}
		}

		for (int i = readyToUpdate.size() - 1; i >= 0; i--) {
			Block currentBlock = readyToUpdate.get(i);
			blockUpdateTimes.remove(currentBlock);
			if (!currentBlock.isDestroyed()) {
				currentBlock.updateBlock();
			}
		}

		DroppedItemTracker.pickUpSurroundingDroppedItems();
		batch.begin();
		DroppedItemTracker.renderDroppedItems();
		batch.end();

		counter.stop();
		//System.out.println(counter.time);
		logger.log();
	}

	public static void addToBlockUpdates(Block block, double timeBeforeUpdate) {
		if (!blockUpdateTimes.containsKey(block)) {
			blockUpdateTimes.put(block, Math.round(System.currentTimeMillis() + timeBeforeUpdate * 1000));
		}
	}

	public static Texture getBlockTexture(String name) {
		return blockTextures.get(name);
	}

	public static World getWorld() {
		return world;
	}

	public static double getPPM() {
		return PPM;
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
