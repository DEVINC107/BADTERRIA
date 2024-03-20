package com.mygdx.game.Entity;

import com.mygdx.game.TUtility;

public class Entity {
    String entityId;

    public Entity(String entityId) {
        TUtility.readFile(entityId);
    }
}
