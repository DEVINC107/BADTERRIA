package com.mygdx.game.Item;

import com.mygdx.game.Entity.Entity;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

public class Sword extends Item {
    float damage; // blocks/second
    public Sword(String itemId) {
        super(itemId);
        String[] data = TUtility.getData("Item.txt",itemId);
        this.damage = Float.parseFloat(data[0]);
    }
    long lastTickUpdate = System.currentTimeMillis() - 500;
    public void mouseDown() {
        if (System.currentTimeMillis() - lastTickUpdate < 500) {
            return;
        }
        for (Entity e : Entity.entities) {
            if (e == Game.player) {
                continue;
            }
            System.out.println(e.entityId);
            if (e.body.getPosition().sub(Game.player.body.getPosition()).len() < 2) {
                e.setHealth(e.health - 10);
                e.applyKnockback(e, 5);
            }
        }
        lastTickUpdate = System.currentTimeMillis();
    }
}
