package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

import java.util.ArrayList;
import java.util.HashMap;

public class TNT extends Block {
    private final int EXPLOSION_RADIUS = 5;

    public TNT(String blockName, Vector2 position) {
        super(blockName, position, "TNT");
        beginUpdating();
    }
    public void setCollision(Vector2 position, World world) {

    }

    public void updateBlock() {
        Vector2 pos = BlockTracker.getBlockPosition(this);

        this.destroyBlock(false);

        HashMap<Block, Vector2> blocks = BlockTracker.getAllBlockPositions();
        ArrayList<Block> toDestroy = new ArrayList<>();
        for (HashMap.Entry<Block, Vector2> entry : blocks.entrySet()) {
            Block currentBlock = entry.getKey();
            Vector2 currentPos = entry.getValue();
            if (TUtility.getMagnitude(currentPos, pos) < EXPLOSION_RADIUS) {
                toDestroy.add(currentBlock);
            }
        }
        for (int i = 0; i < toDestroy.size(); i++) {
            toDestroy.get(i).takeDamage(999);
        }
    }

    public void beginUpdating() {
        Game.addToBlockUpdates(this, 2);
    }
}
