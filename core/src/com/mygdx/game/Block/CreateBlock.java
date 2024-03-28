package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TUtility;

public class CreateBlock {
    public CreateBlock(String blockName, Vector2 position) {
        String blockType = TUtility.getData("BlockType.txt", blockName)[0];
        if (blockType.equals("Default")) {
            new DefaultBlock(blockName, position);
        } else if (blockType.equals("Noncollidable")) {
            new NoncollidableBlock(blockName, position);
        } else if (blockType.equals("Liquid")) {
            new LiquidBlock(blockName, position);
        } else if (blockType.equals("TNT")) {
            new TNT(blockName, position);
        }
    }
}