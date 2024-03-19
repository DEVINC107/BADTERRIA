package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.TUtility;

public class Block {
    private String name;
    private int health;

    public Block(String name, Vector2 position) {
        this.name = name;
        this.health = TUtility.getInitialBlockHealth(name);
        BlockTracker.setPosition(this, position);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }
    public void setCollision(Vector2 position, World world) {
        BodyDef groundBodyDef = new BodyDef();
// Set its world position
        groundBodyDef.position.set(position);

// Create a body from the definition and add it to the world
        Body groundBody = world.createBody(groundBodyDef);

// Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
// Set the polygon shape as a box which is twice the size of our view port and 20 high
// (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox((float) 0.5, (float) 0.5);
// Create a fixture from our polygon shape and add it to our ground body
        groundBody.createFixture(groundBox, 0);
// Clean up after ourselves
        groundBox.dispose();
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }
}
