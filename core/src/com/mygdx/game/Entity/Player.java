package com.mygdx.game.Entity;

import com.badlogic.gdx.Input;
import com.mygdx.game.Item.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Game;
import com.mygdx.game.Item.Pickaxe;

public class Player extends Entity {
    public Body body;
    int numSlots = 9;
    Item[] inventory = new Item[numSlots];
    Item equipped;
    public Player() {
        super("Player");
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 40);
        Body body = Game.world.createBody(bodyDef);
        this.body = body;
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.48f, 0.75f, new Vector2(0,0.25f),0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        CircleShape cShape = new CircleShape();
        cShape.setRadius(0.49f);
        cShape.setPosition(new Vector2(0,-0.5f));
        FixtureDef circle = new FixtureDef();
        circle.shape = cShape;
        circle.friction = 0f;

        inventory[0] = new Pickaxe("WoodPickaxe");

        body.createFixture(circle);
        body.createFixture(fixtureDef);
    }

    public void renderSlots() {
        Game.batch.begin();
        float pps = (float) (Gdx.graphics.getWidth()/2)/numSlots;
        for (int i = 0; i < numSlots; i ++) {
            Game.batch.draw(new Texture("Images/HotbarSlot.png"),i*(pps+1),Gdx.graphics.getHeight()-pps,pps,pps);
            if (inventory[i] != null) {
                float offset = (float) (pps*0.2)/2;
                Game.batch.draw(new Texture("Images/Items/"+inventory[i].getItemId()+".png"),i*(pps+1) + offset,Gdx.graphics.getHeight()-pps + offset,pps*0.8f,pps*0.8f);
            }
        }
        Game.batch.end();
    }
    int[] hotkeys = {
        Input.Keys.NUM_1,
            Input.Keys.NUM_2,
            Input.Keys.NUM_3,
            Input.Keys.NUM_4,
            Input.Keys.NUM_5,
            Input.Keys.NUM_6,
            Input.Keys.NUM_7,
            Input.Keys.NUM_8,
            Input.Keys.NUM_9,
    };
    public void renderEquipped() {
        for (int i = 0; i < 9; i ++) {
            if (!Gdx.input.isKeyPressed(hotkeys[i])) {
                continue;
            }
            if (inventory[i] == null) {
                continue;
            }
            equipped = inventory[i];
        }
        if (equipped != null) {
            equipped.renderEquip(Game.batch,Game.player);
        }
    }
}
