package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

import java.util.ArrayList;

public class LiquidBlock extends Block {
    public LiquidBlock(String blockName, Vector2 position) {
        super(blockName, position, "Liquid");
    }
    public void setCollision(Vector2 position, World world) {
        //empty so no collisions :)
    }

    public static void flowToPosition(String sourceBlockName, int level, Vector2 pos) {
        ArrayList<Block> blocksAtPos = BlockTracker.getBlocksAtPosition(pos);
        if (blocksAtPos.size() == 0) {
            new CreateBlock(sourceBlockName + level, pos);
            BlockTracker.updateSurroundingBlocks(pos);
        } else {
            for (int i = 0; i < blocksAtPos.size(); i++) {
                Block currentBlock = blocksAtPos.get(i);
                if (currentBlock.getBlockType().equals("Liquid") && currentBlock.getName().substring(0, sourceBlockName.length()).equals(sourceBlockName) && Integer.parseInt(currentBlock.getName().substring(sourceBlockName.length())) < level) {
                    currentBlock.destroyBlock(false);
                    new CreateBlock(sourceBlockName + level, pos);
                    BlockTracker.updateSurroundingBlocks(pos);
                }
            }
        }
    }

    public void updateBlock() {
        Block currentBlock = this;
        String sourceBlockName = currentBlock.getName().substring(0, currentBlock.getName().length() - 1);
        int nextFlowNumber = Integer.parseInt(currentBlock.getName().substring(currentBlock.getName().length() - 1)) - 1;
        Vector2 currentPos = BlockTracker.getBlockPosition(currentBlock);
        Vector2 left = new Vector2(currentPos.x - 1, currentPos.y);
        Vector2 right = new Vector2(currentPos.x + 1, currentPos.y);
        Vector2 down = new Vector2(currentPos.x, currentPos.y - 1);
        if (nextFlowNumber > 0) {
            flowToPosition(sourceBlockName, nextFlowNumber, left);
            flowToPosition(sourceBlockName, nextFlowNumber, right);
        }
        flowToPosition(sourceBlockName, 6, down);
    }
    public void beginUpdating() {
        Game.addToBlockUpdates(this, 0.4);
    }
}