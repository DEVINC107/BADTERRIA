package com.mygdx.game.Entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.TUtility;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class Entity {
    private String entityId;
    private int health = 0;
    public Body body;
    private static ArrayList<Entity> entities = new ArrayList<>();

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

    public static void updateEntities() {
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public int getHealth() {
        return health;
    }
    public void update() {
        TUtility.drawSprite(entityId,body.getPosition().x,body.getPosition().y);
    }
}
