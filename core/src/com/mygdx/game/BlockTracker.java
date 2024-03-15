package com.mygdx.game;

import Block.Block;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockTracker {
    private static HashMap<Block, Vector2> blockPositions = new HashMap<>();

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

    public static void addBlockToPosition(Block block, Vector2 position) {
        blockPositions.put(block, position);
    }
}