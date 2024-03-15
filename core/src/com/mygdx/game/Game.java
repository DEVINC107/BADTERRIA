package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sun.org.apache.bcel.internal.classfile.Utility;


public class Game extends ApplicationAdapter {
	World world;
	SpriteBatch batch;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;
	Body player;
	Sprite playerSprite;
	Texture img;


	
	@Override
	public void create () {
		img = new Texture("player.png");
		playerSprite = new Sprite(img);
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -2), true);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10,10);
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
		box.setAsBox(playerSprite.getWidth()/2,playerSprite.getHeight()/2);


// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

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
		groundBox.setAsBox(100, 0.2f);
// Create a fixture from our polygon shape and add it to our ground body
		groundBody.createFixture(groundBox, 0.0f);
// Clean up after ourselves
		groundBox.dispose();
	}

	@Override
	public void render () {

		Vector2 vel = this.player.getLinearVelocity();
		Vector2 pos = this.player.getPosition();
		int MAX_VELOCITY = 1;
// apply left impulse, but only if max velocity is not reached yet
		if (Gdx.input.isKeyPressed(Input.Keys.A) && vel.x > -MAX_VELOCITY) {
			this.player.applyLinearImpulse(-0.8f, 0, pos.x, pos.y, true);
		}

// apply right impulse, but only if max velocity is not reached yet
		if (Gdx.input.isKeyPressed(Input.Keys.D) && vel.x < MAX_VELOCITY) {
			this.player.applyLinearImpulse(0.80f, 0, pos.x, pos.y, true);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && vel.y < MAX_VELOCITY) {
			this.player.applyLinearImpulse(0, 0.8f, pos.x, pos.y, true);
		}
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.end();
		world.step(1/60f, 6, 2);
		camera.position.x = player.getPosition().x;
		camera.position.y = player.getPosition().y;
		camera.position.z = 0;
		camera.update();
		playerSprite.setPosition(player.getPosition().x, player.getPosition().y);

		// You know the rest...
		batch.begin();
		batch.draw(playerSprite, playerSprite.getX(), playerSprite.getY());
		batch.end();
		debugRenderer.render(world, camera.combined);


	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
