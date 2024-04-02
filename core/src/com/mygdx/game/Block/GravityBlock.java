package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

import java.util.ArrayList;

public class GravityBlock extends Block {
    public GravityBlock(String blockName, Vector2 position) {
        super(blockName, position, "Gravity");
        beginUpdating();
    }

    public void updateBlock() {
        Vector2 currentPos = BlockTracker.getBlockPosition(this);
        Vector2 underPos = new Vector2(currentPos.x, Math.round(currentPos.y - 0.51));
        Vector2 upperPos = new Vector2(underPos.x, underPos.y + 1);
        ArrayList<Block> blocksBeneath = BlockTracker.getBlocksAtPosition(underPos);
        boolean canFall = true;
        ArrayList<Block> blocksToRemove = new ArrayList<>();
        for (int i = 0; i < blocksBeneath.size(); i++) {
            Block currentBlock = blocksBeneath.get(i);
            if (currentBlock.getBlockType().equals("Liquid")) {
                blocksToRemove.add(currentBlock);
            } else if (currentBlock != this) {
                canFall = false;
            }
        }
        if (canFall) {
            for (int i = 0; i < blocksToRemove.size(); i++) {
                blocksToRemove.get(i).destroyBlock();
            }
            BlockTracker.setPosition(this, new Vector2(currentPos.x, (float) Math.round((currentPos.y - 0.1) * 10) / 10));
            beginUpdating();
            BlockTracker.updateSurroundingBlocks(upperPos);
        }
    }
    public void beginUpdating() {
        Game.addToBlockUpdates(this, 0.1);
    }
}