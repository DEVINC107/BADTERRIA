package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.TUtility;

public class Block {
    private String name;
    private int health;
    private boolean destroyable;
    float xSize = 0.5f;
    float ySize = 0.5f;

    public Block(String name, Vector2 position) {
        this.name = name;
        int blockHealth = TUtility.getInitialBlockHealth(name);
        if (blockHealth != -1) {
            this.health = blockHealth;
            destroyable = true;
        } else {
            this.health = -1;
            destroyable = false;
        }
        BlockTracker.setPosition(this, position);
    }

    //returns true if damage is successfully dealt
    public boolean takeDamage(int damage) {
        if (destroyable) {
            health -= damage;
            return true;
        }
        return false;
    }
    public void setCollision(Vector2 position, World world) {
        // creates collisions somehow idk
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(position);
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(xSize, ySize);
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
