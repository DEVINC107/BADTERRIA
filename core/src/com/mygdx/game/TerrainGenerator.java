package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Block.Block;
import com.mygdx.game.Block.DefaultBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TerrainGenerator {
    private static ArrayList<HashMap<Vector2, String>> treeData;
    public static void setTreeData() {
        treeData = new ArrayList<>();

        HashMap<Vector2, String> tree1 = new HashMap<>();
        tree1.put(new Vector2(0, 0), "Wood");
        tree1.put(new Vector2(0, 1), "Wood");
        tree1.put(new Vector2(0, 2), "Wood");
        tree1.put(new Vector2(-2, 3), "Leaves");
        tree1.put(new Vector2(-1, 3), "Leaves");
        tree1.put(new Vector2(0, 3), "Leaves");
        tree1.put(new Vector2(1, 3), "Leaves");
        tree1.put(new Vector2(2, 3), "Leaves");
        tree1.put(new Vector2(-2, 4), "Leaves");
        tree1.put(new Vector2(-1, 4), "Leaves");
        tree1.put(new Vector2(0, 4), "Leaves");
        tree1.put(new Vector2(1, 4), "Leaves");
        tree1.put(new Vector2(2, 4), "Leaves");
        tree1.put(new Vector2(-1, 5), "Leaves");
        tree1.put(new Vector2(0, 5), "Leaves");
        tree1.put(new Vector2(1, 5), "Leaves");
        tree1.put(new Vector2(-1, 6), "Leaves");
        tree1.put(new Vector2(0, 6), "Leaves");
        tree1.put(new Vector2(1, 6), "Leaves");
        treeData.add(tree1);

        HashMap<Vector2, String> tree2 = new HashMap<>();
        tree2.put(new Vector2(0, 0), "Wood");
        tree2.put(new Vector2(0, 1), "Wood");
        tree2.put(new Vector2(0, 2), "Wood");
        tree2.put(new Vector2(-1, 3), "Leaves");
        tree2.put(new Vector2(0, 3), "Leaves");
        tree2.put(new Vector2(1, 3), "Leaves");
        tree2.put(new Vector2(-1, 4), "Leaves");
        tree2.put(new Vector2(0, 4), "Leaves");
        tree2.put(new Vector2(1, 4), "Leaves");
        treeData.add(tree2);
    }
    public static void generateTree(Vector2 pos) {
        HashMap<Vector2, String> chosenTree = treeData.get(TUtility.getRandomInt(1, treeData.size()) - 1);

        for (HashMap.Entry<Vector2, String> entry : chosenTree.entrySet()) {
            Vector2 blockPos = entry.getKey();
            String blockName = entry.getValue();
            if (!BlockTracker.hasBlockAtPosition(blockPos.add(pos))) {
                new DefaultBlock(blockName, blockPos.add(pos));
            }
        }
    }


    private static final int MAP_LENGTH = 80;
    private static final int MAP_HEIGHT = 10;
    private static final int DIRT_LENGTH = 3;
    private static final int MAX_HILL_HEIGHT = 12;
    private static final int MAX_FLAT_LENGTH = 5;
    public static void generateTerrain() {

        boolean targetReached = false;
        int targetHillHeight = TUtility.getRandomInt(0, MAX_HILL_HEIGHT);
        double currentHillHeight = 0;
        double heightIncrementIncrement = TUtility.getRandomDouble(0.3, 0.4);
        double heightIncrement = heightIncrementIncrement;
        int targetFlatLength = (int) (Math.random() * MAX_FLAT_LENGTH + 1);
        int currentFlatLength = 0;

        for (int x = -MAP_LENGTH / 2; x < MAP_LENGTH / 2; x++) {
            int startingHeight = Math.round((float) currentHillHeight);
            int currentHeight = startingHeight - 1;
            if (TUtility.getRandomInt(1, 5) == 1) {
                generateTree(new Vector2(x, startingHeight + currentHeight));
            }
            new DefaultBlock("Grass", new Vector2(x, startingHeight));
            for (int y = currentHeight; y > currentHeight - DIRT_LENGTH; y--) {
                new DefaultBlock("Dirt", new Vector2(x, y));
            }
            currentHeight = currentHeight - DIRT_LENGTH;
            for (int y = currentHeight; y > -MAP_HEIGHT; y--) {
                new DefaultBlock("Stone", new Vector2(x, y));
            }

            // changes current Hill Height
            if (targetReached) {
                currentFlatLength += 1;
                if (currentFlatLength >= targetFlatLength) {
                    targetHillHeight = TUtility.getRandomInt(0, MAX_HILL_HEIGHT);
                    targetFlatLength = (int) (Math.random() * MAX_FLAT_LENGTH + 1);
                    currentFlatLength = 0;
                    heightIncrementIncrement = TUtility.getRandomDouble(0.3, 0.4);
                    heightIncrement = heightIncrementIncrement;
                    targetReached = false;
                }
            } else {
                heightIncrement += heightIncrementIncrement;
                if (currentHillHeight < targetHillHeight) {
                    currentHillHeight += heightIncrement;
                    if (currentHillHeight >= targetHillHeight) {
                        targetReached = true;
                    }
                } else {
                    currentHillHeight -= heightIncrement;
                    if (currentHillHeight <= targetHillHeight) {
                        targetReached = true;
                    }
                }

                if (targetReached) {
                    currentHillHeight = targetHillHeight;
                }
            }
        }
    }
}