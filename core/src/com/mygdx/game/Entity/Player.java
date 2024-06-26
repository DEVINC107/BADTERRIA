package com.mygdx.game.Entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Block.Block;
import com.mygdx.game.Block.Chest;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.Item.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Game;
import com.mygdx.game.Item.Pickaxe;
import com.mygdx.game.Item.Sword;
import com.mygdx.game.TUtility;

import java.io.IOError;
import java.util.ArrayList;

public class Player extends Entity {
    int numSlots = 9;
    boolean reachedMaxJumpVel = false;
    double highestJumpVel = 0;
    Item[] inventory = new Item[numSlots];
    int equipped = 0;
    int health = 100;
    public Chest openChest;
    int maxHealth = 100;
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
        box.setAsBox(0.45f, 0.75f, new Vector2(0,0.25f),0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        CircleShape cShape = new CircleShape();
        cShape.setRadius(0.47f);
        cShape.setPosition(new Vector2(0,-0.5f));
        FixtureDef circle = new FixtureDef();
        circle.shape = cShape;
        circle.friction = 0f;
        setBody(body);
        inventory[0] = new Pickaxe("WoodPickaxe");
        inventory[1] = new Sword("WoodSword");
        body.createFixture(circle);
        body.createFixture(fixtureDef);
    }
    @Override
    public void setHealth(int newHealth) {
        this.health = newHealth;
        if (health <= 0) {
            health = 100;
            this.body.setTransform(new Vector2(0,50), 0);
        }
    }

    public String getSpriteId() {
        if (dir == 0) {
            return "player_left";
        } else {
            return "player_right";
        }
    }
    boolean addedItem = false;

    public void renderSlots() {
        float pps = (float) (Gdx.graphics.getWidth()/2)/numSlots;
        float offset = (float) (pps*0.2)/2;
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
                Game.batch.draw(new Texture("Images/Items/"+inventory[i].getItemId()+".png"),i*(pps+1) + offset,Gdx.graphics.getHeight()-pps + offset,pps*0.8f,pps*0.8f);
            }
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            System.out.println(pps-offset+"--------------");
            int i = (int) ((Gdx.input.getX() - offset)/(pps+1));
            if (Gdx.input.getY() <= pps-offset) {
                Item item = inventory[i];
                System.out.println(item);
                if (item != null && openChest != null && equipped != i) {
                    System.out.println("ADDING ITEM");
                    openChest.addItem(item);
                    inventory[i] = null;
                    addedItem = true;
                }
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

    long lastOpenedChest = 0;

    public void update() {
        super.update();
        //Game.batch.begin();
        if (inventory[equipped] != null) {
            inventory[equipped].heldUpdate();
        }
        if (openChest != null) {
            openChest.show();
        }
        renderSlots();
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && addedItem == false) {
            if (inventory[equipped] != null) {
                inventory[equipped].mouseDown();
            }
            if (System.currentTimeMillis() - lastOpenedChest > 100) {
                if (openChest != null) {
                    int x = (Gdx.input.getX()-Gdx.graphics.getWidth()/2)/(int) Chest.chestSlotPixels;
                    int y = (Gdx.input.getY()-Gdx.graphics.getHeight()/2)/(int) Chest.chestSlotPixels;
                    if (x <= 4 && x >= 0 && y >= 0 && y <= 4) {
                        Item i = openChest.takeItem(x, y);
                        if (i != null) {
                            for (int a = 0; a < inventory.length; a ++) {
                                if (inventory[a] == null) {
                                    inventory[a] = i;
                                    break;
                                }
                            }
                            System.out.println("TAKEN");
                        }
                    } else {
                        openChest = null;
                    }
                } else {
                    ArrayList<Block> blocks = BlockTracker.getBlocksAtPosition(TUtility.getCursor());
                    if (blocks.size() > 0) {
                        System.out.println(blocks.get(0).getClass());
                        blocks.get(0).onClicked();
                    }
                }
                lastOpenedChest = System.currentTimeMillis();
            }

        }
        if (addedItem == true) {
            addedItem = false;
            lastOpenedChest = System.currentTimeMillis();
        }

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
            TUtility.drawSprite(new Sprite(new Texture("Images/Player/player_left.png")),pos.x,pos.y);
        } else {
            TUtility.drawSprite(new Sprite(new Texture("Images/Player/player_right.png")),pos.x,pos.y);
        }
        renderEquipped();
        for (int i = 0; i < maxHealth/10; i ++) {
            if (health < i*10) {
                Game.batch.draw(new Sprite(new Texture("Images/MissingHeart.png")),Gdx.graphics.getWidth()-(25*i),Gdx.graphics.getHeight()-25,25,25);
            } else {
                Game.batch.draw(new Sprite(new Texture("Images/Heart.png")),Gdx.graphics.getWidth()-(25*i),Gdx.graphics.getHeight()-25,25,25);
            }
        }
        //Game.batch.end();
    }

}
