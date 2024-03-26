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
    private Body groundBody;
    private PolygonShape groundBox;
    private Fixture fixture;

    public Block(String name, Vector2 position) {
        this.name = name;
        int blockHealth = -1;
        if (TUtility.getData("BlockHealth.txt", name) != null) {
            blockHealth = Integer.parseInt(TUtility.getData("BlockHealth.txt", name)[0]);
        }
        if (blockHealth != -1) {
            this.health = blockHealth;
            destroyable = true;
        } else {
            this.health = -1;
            destroyable = false;
        }
        groundBody = null;
        groundBox = null;
        fixture = null;
        BlockTracker.setPosition(this, position);
    }

    public void destroyBlock() {
        if (groundBody != null && fixture != null) {
            groundBody.destroyFixture(fixture);
        }
        BlockTracker.removeBlock(this);
    }

    //returns true if damage is successfully dealt
    public boolean takeDamage(int damage) {
        if (destroyable) {
            health -= damage;
            if (health <= 0) {
                destroyBlock();
            }
            return true;
        }
        return false;
    }
    public void setCollision(Vector2 position, World world) {
        // creates collisions somehow idk
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(position);
        groundBody = world.createBody(groundBodyDef);
        groundBox = new PolygonShape();
        groundBox.setAsBox(xSize, ySize);
        fixture = groundBody.createFixture(groundBox,0);
        groundBox.dispose();
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }
}
