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

import java.util.HashMap;


public class Game extends ApplicationAdapter {
	World world;
	SpriteBatch batch;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;
	Body player;
	Sprite playerSprite;
	Texture img;
	HashMap<String, Texture> blockTextures;
	double BLOCKS_HORIZONTAL_AXIS = 26;
	double BLOCKS_VERTICAL_AXIS = 20;
	double PPM = 100;
	boolean reachedMaxJumpVel = false;
	boolean goingLeft = false;

	public void drawSprite(Sprite sprite, double x, double y) {
		Vector2 playerPos = player.getPosition();
		float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
		float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
		double xSize = sprite.getWidth()/xScale;
		double ySize = sprite.getHeight()/yScale;
		double xPos = (x - playerPos.x) * (PPM/xScale) + Gdx.graphics.getWidth() / 2 - xSize / 2;
		double yPos = (y - playerPos.y) * (PPM/yScale) + Gdx.graphics.getHeight() / 2 - ySize / 2;
		batch.draw(sprite, (float) xPos, (float) yPos, (float) xSize, (float) ySize);
	}

	@Override
	public void create () {
		for (String a : TUtility.getData("Entity.txt","player")) {
			System.out.println(a);
		}
		System.out.println(TUtility.getData("Entity.txt","player"));
		img = new Texture("Images/Player/player_left.png");
		playerSprite = new Sprite(img);
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -10), true);
		BlockTracker.setWorld(world);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (float) BLOCKS_HORIZONTAL_AXIS,(float) BLOCKS_VERTICAL_AXIS);
		System.out.println(camera.position);
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyDef.BodyType.DynamicBody;
// Set our body's starting position in the world
		bodyDef.position.set(0, 10);

// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);
		player = body;
		PolygonShape box = new PolygonShape();
		box.setAsBox(0.5f, 0.75f, new Vector2(0,0.25f),0);


// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f; // Make it bounce a little bit

		CircleShape cShape = new CircleShape();
		cShape.setRadius(0.5f);
		cShape.setPosition(new Vector2(0,-0.5f));
		FixtureDef circle = new FixtureDef();
		circle.shape = cShape;
		body.createFixture(circle);

// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
		box.dispose();



		// add block textures
		blockTextures = new HashMap<>();
		blockTextures.put("Grass", new Texture("Images/Blocks/grass.png"));
		blockTextures.put("Dirt", new Texture("Images/Blocks/dirt.png"));
		blockTextures.put("Stone", new Texture("Images/Blocks/stone.png"));
		blockTextures.put("Leaves", new Texture("Images/Blocks/dirt.png"));
		blockTextures.put("Wood", new Texture("Images/Blocks/wood.png"));

		//starts some stuff
		TerrainGenerator.setTreeData();

		// creates blocks
		TerrainGenerator.generateTerrain();
	}

	@Override
	public void render () {

		Vector2 vel = this.player.getLinearVelocity();
		Vector2 pos = this.player.getPosition();
		float MAX_VELOCITY = 3.5f;
		float MAX_JUMP_VEL = 5f;
// apply left impulse, but only if max velocity is not reached yet
		if (Gdx.input.isKeyPressed(Input.Keys.A) ) {
			goingLeft = true;
			if (vel.x > -MAX_VELOCITY) {
				this.player.applyLinearImpulse(-1.1f, 0, pos.x, pos.y, true);
			}
		}
// apply right impulse, but only if max velocity is not reached yet
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			goingLeft = false;
			if (vel.x < MAX_VELOCITY) {
				this.player.applyLinearImpulse(1.1f, 0, pos.x, pos.y, true);
			}
		}
		if (Math.abs(player.getLinearVelocity().x) > 0) {
			player.applyLinearImpulse(-player.getLinearVelocity().x/10,0f,pos.x,pos.y,true);
		}
		if (reachedMaxJumpVel) {
			if (player.getLinearVelocity().y == 0) {
				reachedMaxJumpVel = false;
			}
		}
		if (player.getLinearVelocity().y >= MAX_JUMP_VEL) {
			reachedMaxJumpVel = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (!reachedMaxJumpVel && player.getLinearVelocity().y >= 0) {
				this.player.applyLinearImpulse(0, 1.1f, pos.x, pos.y, true);
			}
		}
		ScreenUtils.clear(1, 0, 0, 1);
		world.step(1/60f, 6, 2);
		camera.position.x = player.getPosition().x;
		camera.position.y = player.getPosition().y;
		camera.position.z = 0;
		camera.update();
		//playerSprite.setPosition(player.getPosition().x, player.getPosition().y);
		batch.begin();
		// renders blocks
		float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
		float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
		Vector2 playerPos = player.getPosition();
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
		// You know the rest...
		batch.begin();
		if (goingLeft) {
			drawSprite(playerSprite,playerPos.x,playerPos.y);
		} else {
			drawSprite(new Sprite(new Texture("Images/Player/player_right.png")),playerPos.x,playerPos.y);
		}
		batch.end();
		debugRenderer.render(world, camera.combined);


	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
