package com.mygdx.game.Entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    boolean reachedMaxJumpVel = false;
    double highestJumpVel = 0;
    Item[] inventory = new Item[numSlots];
    int equipped = 0;
    private int dir = 0; // 0 = left, 1 = right
    public int getDir() {
        return dir;
    }
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
        float pps = (float) (Gdx.graphics.getWidth()/2)/numSlots;
        for (int i = 0; i < 9; i ++) {
            if (!Gdx.input.isKeyPressed(hotkeys[i])) {
                continue;
            }
            if (inventory[i] == null) {
                continue;
            }
            equipped = i;
        }
        for (int i = 0; i < numSlots; i ++) {
            Game.batch.draw(new Texture((i != equipped) ? "Images/HotbarSlot.png" : "Images/HotbarSlotEquipped.png"),i*(pps+1),Gdx.graphics.getHeight()-pps,pps,pps);
            if (inventory[i] != null) {
                float offset = (float) (pps*0.2)/2;
                Game.batch.draw(new Texture("Images/Items/"+inventory[i].getItemId()+".png"),i*(pps+1) + offset,Gdx.graphics.getHeight()-pps + offset,pps*0.8f,pps*0.8f);
            }
        }
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
        if (inventory[equipped] != null) {
            inventory[equipped].renderEquip(Game.batch,Game.player);
        }
    }

    public void update() {
        Game.batch.begin();
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        float MAX_VELOCITY = 3.5f;
        float MAX_JUMP_VEL = 5f;
// apply left impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.A) ) {
            dir = 0;

            if (vel.x > -MAX_VELOCITY) {
                body.applyLinearImpulse(-1.1f, 0, pos.x, pos.y, true);
            }
        }
// apply right impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dir = 1;
            if (vel.x < MAX_VELOCITY) {
                body.applyLinearImpulse(1.1f, 0, pos.x, pos.y, true);
            }
        }
        if (Math.abs(body.getLinearVelocity().x) > 0) {
            body.applyLinearImpulse(-body.getLinearVelocity().x/10,0f,pos.x,pos.y,true);
        }
        if (reachedMaxJumpVel) {
            if (body.getLinearVelocity().y == 0) {
                reachedMaxJumpVel = false;
                highestJumpVel = 0;
            }
        }
        if (body.getLinearVelocity().y >= MAX_JUMP_VEL) {
            reachedMaxJumpVel = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (!reachedMaxJumpVel && body.getLinearVelocity().y >= 0) {
                body.applyLinearImpulse(0, 1.1f, pos.x, pos.y, true);
            }
        }
        if (body.getLinearVelocity().y < highestJumpVel) {
            reachedMaxJumpVel = true;
        }
        if (body.getLinearVelocity().y > highestJumpVel) {
            highestJumpVel = body.getLinearVelocity().y;
        }
        if (dir == 0) {
            Game.drawSprite(new Sprite(new Texture("Images/Player/player_left.png")),pos.x,pos.y);
        } else {
            Game.drawSprite(new Sprite(new Texture("Images/Player/player_right.png")),pos.x,pos.y);
        }
        renderSlots();
        renderEquipped();
        Game.batch.end();
    }
}
