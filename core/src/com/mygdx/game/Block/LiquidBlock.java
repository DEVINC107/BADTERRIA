package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

public class LiquidBlock extends Block {
    public LiquidBlock(String blockName, Vector2 position) {
        super(blockName, position);
    }
    public void setCollision(Vector2 position, World world) {
        //empty so no collisions :)
    }

    public void updateBlock() {
        Block currentBlock = this;
        String sourceBlockName = currentBlock.getName().substring(0, currentBlock.getName().length() - 2);
        String nextBlockName = sourceBlockName + (Integer.parseInt(currentBlock.getName().substring(currentBlock.getName().length() - 1)) - 1);
        Vector2 currentPos = BlockTracker.getBlockPosition(currentBlock);
        Vector2 left = new Vector2(currentPos.x - 1, currentPos.y);
        Vector2 right = new Vector2(currentPos.x + 1, currentPos.y);
        Vector2 down = new Vector2(currentPos.x, currentPos.y - 1);
        if (BlockTracker.getBlocksAtPosition(left).size() == 0) {
            new CreateBlock(nextBlockName, left);
            BlockTracker.updateSurroundingBlocks(left);
        }
        if (BlockTracker.getBlocksAtPosition(right).size() == 0) {
            new CreateBlock(nextBlockName, right);
            BlockTracker.updateSurroundingBlocks(right);
        }
        if (BlockTracker.getBlocksAtPosition(down).size() == 0) {
            new CreateBlock(sourceBlockName, down);
            BlockTracker.updateSurroundingBlocks(down);
        }
    }
    public void beginUpdating() {
        Game.addToBlockUpdates(this, 0.4);
    }
}