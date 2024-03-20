package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
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
        // creates collisions somehow idk
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(position);
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox((float) 0.5, (float) 0.5);
        groundBody.createFixture(groundBox,0);
        groundBox.dispose();
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }
}
