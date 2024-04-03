package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Block.Block;

import java.util.ArrayList;
import java.util.HashMap;

public class DroppedItemTracker {
    private static ArrayList<DroppedItem> droppedItems = new ArrayList<>();

    public DroppedItemTracker() {

    }

    public static void pickUpSurroundingDroppedItems() {

    }

    public static void renderDroppedItems() {
        double PPM = Game.getPPM();
        float xScale = (float)(Game.BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
        float yScale = (float)(Game.BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));

        for (int i = droppedItems.size() - 1; i >= 0; i--) {
            DroppedItem item = droppedItems.get(i);
            Texture currentTexture = Game.getBlockTexture(item.getName());
            double xSize = currentTexture.getWidth()/xScale;
            double ySize = currentTexture.getHeight()/yScale;
            double xPos = (item.getPos().x - Game.player.body.getPosition().x) * (PPM/xScale) + Gdx.graphics.getWidth() / 2 - xSize / 2;
            double yPos = (item.getPos().y - Game.player.body.getPosition().y) * (PPM/yScale) + Gdx.graphics.getHeight() / 2 - ySize / 2;
            if (yPos > -100 && yPos < Gdx.graphics.getHeight() + 100 && xPos > -100 && xPos < Gdx.graphics.getWidth() + 100) {
                Game.batch.draw(currentTexture, (float) xPos, (float) yPos, (float) xSize, (float) ySize);
            }
        }
    }
}