package com.mygdx.game.Entity;

import com.mygdx.game.TUtility;

public class Entity {
    private String entityId;
    private int health = 0;

    public Entity(String entityId) {
        String[] data = TUtility.getData("Entity.txt",entityId);
        this.entityId = entityId;
        this.health = Integer.parseInt(data[0]);
    }
}
