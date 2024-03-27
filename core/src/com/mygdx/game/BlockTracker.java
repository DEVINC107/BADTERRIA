package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Block.Block;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockTracker {
    public static World world;
    private static HashMap<Block, Vector2> blockPositions = new HashMap<>();
    public static void setWorld(World newWorld) {
        world = newWorld;
    }

    public static void setPosition(Block block, Vector2 position) {
        blockPositions.put(block, position);
        block.setCollision(position, world);
    }

    public static void removeBlock(Block block) {
        blockPositions.remove(block);
    }

    public static Vector2 getBlockPosition(Block block) {
        return blockPositions.get(block);
    }

    public static ArrayList<Block> getBlocksAtPosition(Vector2 pos) {
        ArrayList<Block> blocksAtPos = new ArrayList<>();

        for (HashMap.Entry<Block, Vector2> entry : blockPositions.entrySet()) {
            Block currentBlock = entry.getKey();
            Vector2 currentPos = entry.getValue();
            if (currentPos.equals(pos)) {
                blocksAtPos.add(currentBlock);
            }
        }

        return blocksAtPos;
    }

    public static Block raycast(Vector2 origin, Vector2 pos) {
        origin = TUtility.roundVec2(origin);
        pos = TUtility.roundVec2(pos);
        float deltaY = (pos.y-origin.y);
        float deltaX = (pos.x-origin.x);
        double hyp = Math.round(Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
        for (int i = 0; i <= hyp; i ++) {
            int x = (int) Math.round(origin.x + (deltaX/hyp) * i);
            int y = (int) Math.round(origin.y + (deltaY/hyp) * i);
            if (BlockTracker.hasBlockAtPosition(new Vector2(x,y))) {
                return BlockTracker.getBlocksAtPosition(new Vector2(x, y)).get(0);
            }
        }
        return null;
    }

    public static HashMap<Block, Vector2> getAllBlockPositions() {
        return blockPositions;
    }
    public static boolean hasBlockAtPosition(Vector2 pos) {
        return getBlocksAtPosition(pos).size() > 0;
    }
    public static void updateSurroundingBlocks(Vector2 pos) {
        Vector2 up = new Vector2(pos.x, pos.y + 1);
        Vector2 down = new Vector2(pos.x, pos.y - 1);
        Vector2 right = new Vector2(pos.x + 1, pos.y);
        Vector2 left = new Vector2(pos.x - 1, pos.y);

        ArrayList<Block> blocksToUpdate = new ArrayList<>();

        for (HashMap.Entry<Block, Vector2> entry : blockPositions.entrySet()) {
            Block currentBlock = entry.getKey();
            Vector2 currentPos = entry.getValue();
            if (currentPos.equals(up) || currentPos.equals(down) || currentPos.equals(left) || currentPos.equals(right) || currentPos.equals(pos)) {
                blocksToUpdate.add(currentBlock);
            }
        }

        for (int i = 0; i < blocksToUpdate.size(); i++) {
            blocksToUpdate.get(i).beginUpdating();
        }
    }
}
