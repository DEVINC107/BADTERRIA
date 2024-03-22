package com.mygdx.game.Item;

import com.mygdx.game.TUtility;

public class Pickaxe extends Item {
    float miningSpeed; // blocks/second
    public Pickaxe(String itemId) {
        super(itemId);
        String[] data = TUtility.getData("Item.txt",itemId);
        this.miningSpeed = Float.parseFloat(data[0]);
    }
}
