package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Block.DefaultBlock;

public class TerrainGenerator {
    private final static int MAP_LENGTH = 50;
    private final static int MAP_HEIGHT = 10;
    private final static int DIRT_LENGTH = 3;

    public static void generateTerrain() {
        int targetHillHeight = (int) (Math.random() * 5);
        int currentHillHeight = 0;
        int flatLength = (int) (Math.random() * 4);
        int currentFlatLength = 0;
        for (int x = 0; x < MAP_LENGTH; x++) {
            int startingHeight = currentHillHeight;
            int currentHeight = startingHeight - 1;
            new DefaultBlock("Grass", new Vector2(x - MAP_LENGTH / 2, startingHeight));
            for (int y = currentHeight; y > currentHeight - DIRT_LENGTH; y--) {
                new DefaultBlock("Dirt", new Vector2(x - MAP_LENGTH / 2, y));
            }
            currentHeight = currentHeight - DIRT_LENGTH;
            for (int y = currentHeight; y > -MAP_HEIGHT; y--) {
                new DefaultBlock("Stone", new Vector2(x - MAP_LENGTH / 2, y));
            }
            if (currentHillHeight == targetHillHeight) {
                currentFlatLength += 1;
                if (currentFlatLength >= flatLength) {
                    targetHillHeight = (int) (Math.random() * 5);
                    flatLength = (int) (Math.random() * 5);
                    currentFlatLength = 0;
                }
            } else {
                if (currentHillHeight < targetHillHeight) {
                    currentHillHeight += 1;
                } else {
                    currentHillHeight -= 1;
                }
            }
        }
    }
}