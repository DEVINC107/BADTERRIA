package com.mygdx.game.Item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Block.Block;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

import java.util.ArrayList;

public class Pickaxe extends Item {
    float miningSpeed; // blocks/second
    public Pickaxe(String itemId) {
        super(itemId);
        String[] data = TUtility.getData("Item.txt",itemId);
        this.miningSpeed = Float.parseFloat(data[0]);
    }
    float lastSmartCursorX;
    float lastSmartCursorY;
    double precision = 1;
    double angle = 45;
    int maxRange = 5;
    long lastTick = 0;
    Vector2 blockPos;
    public void mouseDown() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (blockPos != null) {
                ArrayList<Block> blocksAtPos = BlockTracker.getBlocksAtPosition(blockPos);
                if (blocksAtPos.size() > 0) {
                    blocksAtPos.get(0).takeDamage(10);
                }
            }
        }
    }
    public void heldUpdate() {
        if (blockPos != null) {
            TUtility.drawSprite(new Sprite(new Texture("Images/SmartCursorSelect.png")), blockPos.x, blockPos.y);
        }
        if (System.currentTimeMillis() - lastTick < 200) {
            return;
        }
        lastTick = System.currentTimeMillis();
        Vector2 cursor = TUtility.getCursor();
        Vector2 pos = Game.player.body.getPosition();
        float xDiff = cursor.x - pos.x;
        float yDiff = cursor.y - pos.y;
        float radius = (float) Math.sqrt(Math.pow(cursor.x-pos.x,2) + Math.pow(cursor.y-pos.y,2));

        if (radius > maxRange) {
            float cSquared = (float) Math.pow(radius,2);
            float xSign = Math.signum(xDiff);
            float ySign = Math.signum(yDiff);
            xDiff = xSign * (float) (Math.pow(xDiff,2)/cSquared)*maxRange;
            yDiff = ySign * (float) (Math.pow(yDiff,2)/cSquared)*maxRange;
            radius = maxRange;
        }

        float closestDistance = Integer.MAX_VALUE;
        Block closestBlock = null;
        float degOffset = (float) Math.toDegrees(Math.acos(Math.abs(xDiff)/radius));
        if (xDiff < 0 && yDiff > 0) {
            degOffset = 180 - degOffset;
        } else if (xDiff <= 0 && yDiff <= 0) {
            degOffset = 180 + degOffset;
        } else if (xDiff >= 0 && yDiff <= 0) {
            degOffset = -degOffset;
        }
        for (float deg = (float) -angle/2; deg <= angle/2; deg += angle/precision) {
            float rad = (float) Math.toRadians(deg + degOffset);
            float x = pos.x + (float) Math.cos(rad) * radius;
            float y = pos.y + (float) Math.sin(rad) * radius;
            Block result = BlockTracker.raycast(pos,new Vector2(x, y));
            if (result == null) {
                continue;
            }
            Vector2 blockPos = BlockTracker.getBlockPosition(result);
            float distance = (float) Math.sqrt(Math.pow(pos.x-blockPos.x,2) + Math.pow(pos.y-blockPos.y,2));
            if (distance < closestDistance) {
                closestDistance = distance;
                closestBlock = result;
            }
        }
        if (closestBlock == null) {
            return;
        }
        lastSmartCursorX = cursor.x;
        lastSmartCursorY = cursor.y;
        blockPos = BlockTracker.getBlockPosition(closestBlock);
    }
}
