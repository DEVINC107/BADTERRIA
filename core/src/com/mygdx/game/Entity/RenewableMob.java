package com.mygdx.game.Entity;

import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

import java.util.ArrayList;

public class RenewableMob extends Entity{
    private Float spawnsEvery;
    private Long lastDied;
    private boolean respawn;
    public RenewableMob (String entityId) {
        super(entityId);
        String[] data = TUtility.getData("Entity.txt",entityId);
        this.spawnsEvery = Float.parseFloat(data[1]);
    }

    public float getSpawnsEvery() {
        return spawnsEvery;
    }

    public boolean doRespawn() {
        return respawn;
    }

    public void respawn() {

    }

    public void update() {
        super.update();
    }

    @Override
    public void die() {
        super.die();
        if (getHealth() <= 0) {
            respawn();
        }

    }
}
