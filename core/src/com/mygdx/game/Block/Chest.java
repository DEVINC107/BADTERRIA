package com.mygdx.game.Block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Game;
import com.mygdx.game.Item.Item;
import com.mygdx.game.TUtility;

public class Chest extends NoncollidableBlock {
    public Item[][] items = new Item[5][5];
    public Chest(Vector2 pos) {
        super("Chest", pos);
    }
    public static float chestSlotPixels = 20;
    float padding = 5;
    public void show() {
        float centerX = (float) Gdx.graphics.getWidth() /2;
        float centerY = (float) Gdx.graphics.getHeight() /2;
        for (int x = 0; x < items.length; x ++) {
            for (int y = 0; y < items.length; y ++) {
                Game.batch.draw(new Sprite(new Texture("Images/HotbarSlot.png")),centerX + x * chestSlotPixels,centerY + y * chestSlotPixels,20,20);
            }
        }
    }

    public Item takeItem(int x, int y) {
        Item i = items[x][y];
        items[x][y] = null;
        return i;
    }

    public void addItem(int x, int y, Item i) {
        items[x][y] = i;
    }

    public void onClicked() {
        System.out.println("CLICKED");
        Game.player.openChest = this;
    }
}
