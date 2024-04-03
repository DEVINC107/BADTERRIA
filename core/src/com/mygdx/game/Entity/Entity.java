package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class Entity {
    public String entityId;
    public int health = 100;
    public Body body;
    public static ArrayList<Entity> entities = new ArrayList<>();

    public Entity(String entityId) {
        String[] data = TUtility.getData("Entity.txt",entityId);
        this.entityId = entityId;
        this.health = Integer.parseInt(data[0]);
        entities.add(this);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
    public void setHealth(int newHealth) {
        this.health = newHealth;
        if (this.health <= 0) {
            this.die();
        }
    }
    public void die() {

    }
    public void applyKnockback(Entity other, float strength) {
        Vector2 knockback = this.body.getPosition().sub(other.body.getPosition()).nor();
        Vector2 playerPos = this.body.getPosition();
        Game.player.body.applyLinearImpulse(-knockback.x * 7.5f, -knockback.y * 7.5f, playerPos.x, playerPos.y, true);
    }
    public static void updateEntities() {
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public int getHealth() {
        return health;
    }
    public void update() {
        if (Math.abs(body.getLinearVelocity().x) > 0 && body.getLinearVelocity().y <= 0) {
            body.applyLinearImpulse(-body.getLinearVelocity().x/20,0f,getBody().getPosition().x,getBody().getPosition().y,true);
        }
        TUtility.drawSprite(entityId,body.getPosition().x,body.getPosition().y);
    }

    public static Entity getInstance(Body body) {
        for (Entity entity : entities) {
            if (entity.getBody() == body) {
                return entity;
            }
        }
        return null;
    }

    public void collision(Entity other) {

    }
}
