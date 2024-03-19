package com.mygdx.game;

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

	public void drawSprite(Sprite sprite, double x, double y) {
		Vector2 playerPos = player.getPosition();
		float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
		float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
		double xSize = sprite.getWidth()/xScale;
		double ySize = sprite.getHeight()/yScale;
		double xPos = (x - playerPos.x) * (PPM/xScale) + Gdx.graphics.getWidth() / 2 - xSize / 2;
		double yPos = (y - playerPos.y) * (PPM/yScale) + Gdx.graphics.getHeight() / 2 - ySize / 2;
		System.out.println(xSize + " " + ySize);
		batch.draw(sprite, (float) xPos, (float) yPos, (float) xSize, (float) ySize);
	}

	@Override
	public void create () {
		img = new Texture("Images/player.png");
		playerSprite = new Sprite(img);
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -5), true);
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
		bodyDef.position.set(0, 5);

// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);
		player = body;
// Create a circle shape and set its radius to 6
		PolygonShape box = new PolygonShape();
		box.setAsBox((float) 0.5, 1);


// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f; // Make it bounce a little bit

// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
		box.dispose();
		BodyDef groundBodyDef = new BodyDef();
// Set its world position
		groundBodyDef.position.set(new Vector2(0, -1));

// Create a body from the definition and add it to the world
		Body groundBody = world.createBody(groundBodyDef);

// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();
// Set the polygon shape as a box which is twice the size of our view port and 20 high
// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(2, 0.2f);
// Create a fixture from our polygon shape and add it to our ground body
		groundBody.createFixture(groundBox, 0.0f);
// Clean up after ourselves
		groundBox.dispose();


		// creates blocks
		blockTextures = new HashMap<>();
		blockTextures.put("Grass", new Texture("Images/Blocks/grass.png"));
		new DefaultBlock("Grass", new Vector2(0, 0));
		new DefaultBlock("Grass", new Vector2(0, 1));
		new DefaultBlock("Grass", new Vector2(1, 0));
		new DefaultBlock("Grass", new Vector2(1, 1));
		new DefaultBlock("Grass", new Vector2(2, 0));
		new DefaultBlock("Grass", new Vector2(3, 0));
	}

	@Override
	public void render () {

		Vector2 vel = this.player.getLinearVelocity();
		Vector2 pos = this.player.getPosition();
		float MAX_VELOCITY = 2.3f;
		float MAX_JUMP_VEL = 4.5f;
// apply left impulse, but only if max velocity is not reached yet
		if (Gdx.input.isKeyPressed(Input.Keys.A) && vel.x > -MAX_VELOCITY) {
			this.player.applyLinearImpulse(-1.1f, 0, pos.x, pos.y, true);
		}

// apply right impulse, but only if max velocity is not reached yet
		if (Gdx.input.isKeyPressed(Input.Keys.D) && vel.x < MAX_VELOCITY) {
			this.player.applyLinearImpulse(1.1f, 0, pos.x, pos.y, true);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W) && vel.y < MAX_JUMP_VEL) {
			this.player.applyLinearImpulse(0, 2.2f, pos.x, pos.y, true);
		}
		ScreenUtils.clear(1, 0, 0, 1);
		world.step(1/60f, 6, 2);
		camera.position.x = player.getPosition().x;
		camera.position.y = player.getPosition().y;
		camera.position.z = 0;
		camera.update();
		//playerSprite.setPosition(player.getPosition().x, player.getPosition().y);

		float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
		float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
		batch.begin();
		// renders blocks
		Vector2 playerPos = player.getPosition();
		for (HashMap.Entry<Block, Vector2> entry : BlockTracker.getAllBlockPositions().entrySet()) {
			Block currentBlock = entry.getKey();
			Vector2 currentPos = entry.getValue();
			Texture currentTexture = blockTextures.get(currentBlock.getName());

			double xSize = currentTexture.getWidth()/xScale;
			double ySize = currentTexture.getHeight()/yScale;
			double xPos = (currentPos.x - playerPos.x) * (PPM/xScale) + Gdx.graphics.getWidth() / 2 - xSize / 2;
			double yPos = (currentPos.y - playerPos.y) * (PPM/yScale) + Gdx.graphics.getHeight() / 2 - ySize / 2;
			System.out.println(xSize + " " + ySize);
			batch.draw(currentTexture, (float) xPos, (float) yPos, (float) xSize, (float) ySize);
		}
		batch.end();
		// You know the rest...
		batch.begin();
		drawSprite(playerSprite,playerPos.x,playerPos.y);
		batch.end();
		debugRenderer.render(world, camera.combined);


	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
