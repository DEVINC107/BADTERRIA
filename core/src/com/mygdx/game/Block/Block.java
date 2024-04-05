package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.DroppedItem;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

public class Block {
    private String name;
    private int health;
    private boolean destroyable;
    float xSize = 0.5f;
    float ySize = 0.5f;
    private Body groundBody;
    private Fixture fixture;
    private Boolean isDestroyed;
    public String blockType;

    public Block(String name, Vector2 position, String blockType) {
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
        this.blockType = blockType;
        groundBody = null;
        fixture = null;
        isDestroyed = false;
        BlockTracker.setPosition(this, position);
    }

    public void onClicked() {

    }
    public void removeCollision() {
        if (groundBody != null && fixture != null) {
            groundBody.destroyFixture(fixture);
            groundBody = null;
            fixture = null;
        }
    }

    public void destroyBlock(boolean shouldDropItem) {
        isDestroyed = true;
        removeCollision();
        Vector2 blockPos = BlockTracker.getBlockPosition(this);
        BlockTracker.removeBlock(this);
        BlockTracker.updateSurroundingBlocks(blockPos);
        if (shouldDropItem) {
            new DroppedItem(name, blockPos);
        }
    }

    //returns true if damage is successfully dealt
    public boolean takeDamage(int damage) {
        if (destroyable) {
            health -= damage;
            if (health <= 0) {
                destroyBlock(true);
            }
            return true;
        }
        return false;
    }
    public void setCollision(Vector2 position, World world) {
        removeCollision();
        // creates collisions somehow idk
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(position);
        groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
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
    public Boolean isDestroyed() {
        return isDestroyed;
    }

    public void updateBlock() {

    }
    public void beginUpdating() {
        updateBlock();
    }
    public String getBlockType() {
        return blockType;
    }
}
