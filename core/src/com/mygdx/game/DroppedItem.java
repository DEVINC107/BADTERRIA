package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class DroppedItem {
    private Body groundBody;
    private Fixture fixture;
    private String name;
    public DroppedItem(String item, Vector2 position) {
        name = item;
        DroppedItemTracker.addToDroppedItems(this);

        World world = Game.getWorld();
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.DynamicBody;
        groundBodyDef.position.set(new Vector2(position.x + (float) TUtility.getRandomDouble(-0.2, 0.2), position.y));
        groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox((float) 0.1, (float) 0.1);
        fixture = groundBody.createFixture(groundBox,0);
        groundBox.dispose();
    }

    public void destroyItem() {
        if (groundBody != null && fixture != null) {
            groundBody.destroyFixture(fixture);
            groundBody = null;
            fixture = null;
        }

        DroppedItemTracker.removeFromDroppedItems(this);
    }

    public String getName() {
        return name;
    }

    public Vector2 getPos() {
        return groundBody.getPosition();
    }
}