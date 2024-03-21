package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Block.Block;
import com.mygdx.game.Block.CreateBlock;
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
        tree2.put(new Vector2(-2, 2), "Leaves");
        tree2.put(new Vector2(-1, 2), "Leaves");
        tree2.put(new Vector2(0, 2), "Leaves");
        tree2.put(new Vector2(1, 2), "Leaves");
        tree2.put(new Vector2(2, 2), "Leaves");
        tree2.put(new Vector2(-2, 3), "Leaves");
        tree2.put(new Vector2(-1, 3), "Leaves");
        tree2.put(new Vector2(0, 3), "Leaves");
        tree2.put(new Vector2(1, 3), "Leaves");
        tree2.put(new Vector2(2, 3), "Leaves");
        tree2.put(new Vector2(-1, 4), "Leaves");
        tree2.put(new Vector2(0, 4), "Leaves");
        tree2.put(new Vector2(1, 4), "Leaves");
        tree2.put(new Vector2(-1, 5), "Leaves");
        tree2.put(new Vector2(0, 5), "Leaves");
        tree2.put(new Vector2(1, 5), "Leaves");
        treeData.add(tree2);
    }
    public static void generateTree(Vector2 pos) {
        HashMap<Vector2, String> chosenTree = treeData.get(TUtility.getRandomInt(1, treeData.size()) - 1);

        for (HashMap.Entry<Vector2, String> entry : chosenTree.entrySet()) {
            Vector2 blockPos = entry.getKey();
            String blockName = entry.getValue();
            Vector2 placeAt = new Vector2(0, 0);
            placeAt.add(pos);
            placeAt.add(blockPos);
            if (!BlockTracker.hasBlockAtPosition(placeAt)) {
                new CreateBlock(blockName, placeAt);
            }
        }
    }


    private static final int DIRT_LENGTH = 3;
    private static final int MAX_HILL_HEIGHT = 12;
    private static final int MAX_FLAT_LENGTH = 5;
    private static final int TREE_GENERATE_FREQUENCY = 10; // 0 to 100
    private static final int MAP_LENGTH = 80;
    private static final int MAP_HEIGHT = 10;

    public static void generateHillsTerrain(int startX, int endX) {

        boolean targetReached = false;
        int targetHillHeight = TUtility.getRandomInt(0, MAX_HILL_HEIGHT);
        double heightIncrementIncrement = TUtility.getRandomDouble(0.2, 0.3);
        double heightIncrement = heightIncrementIncrement;
        int targetFlatLength = (int) (Math.random() * MAX_FLAT_LENGTH + 1);
        int currentFlatLength = 0;

        for (int x = startX / 2; x < endX / 2; x++) {
            int startingHeight = Math.round((float) currentElevation);
            int currentHeight = startingHeight - 1;
            if (TUtility.getRandomInt(1, 100) <= TREE_GENERATE_FREQUENCY) {
                generateTree(new Vector2(x, currentHeight + 1));
            }
            new CreateBlock("Grass", new Vector2(x, currentHeight));
            for (int y = currentHeight - 1; y > currentHeight - DIRT_LENGTH; y--) {
                new CreateBlock("Dirt", new Vector2(x, y));
            }
            currentHeight = currentHeight - DIRT_LENGTH;
            for (int y = currentHeight; y > -MAP_HEIGHT; y--) {
                new CreateBlock("Stone", new Vector2(x, y));
            }

            // changes current Hill Height
            if (targetReached) {
                currentFlatLength += 1;
                if (currentFlatLength >= targetFlatLength) {
                    targetHillHeight = TUtility.getRandomInt(0, MAX_HILL_HEIGHT);
                    targetFlatLength = (int) (Math.random() * MAX_FLAT_LENGTH + 1);
                    currentFlatLength = 0;
                    heightIncrementIncrement = TUtility.getRandomDouble(0.2, 0.3);
                    heightIncrement = heightIncrementIncrement;
                    targetReached = false;
                }
            } else {
                heightIncrement += heightIncrementIncrement;
                if (currentElevation < targetHillHeight) {
                    currentElevation += heightIncrement;
                    if (currentElevation >= targetHillHeight) {
                        targetReached = true;
                    }
                } else {
                    currentElevation -= heightIncrement;
                    if (currentElevation <= targetHillHeight) {
                        targetReached = true;
                    }
                }

                if (targetReached) {
                    currentElevation = targetHillHeight;
                }
            }
        }
    }

    public static double currentElevation = 0;
    public static void generateTerrain() {
        int currentX = -MAP_LENGTH/2;
        while (currentX <= MAP_LENGTH/2) {
            int generateLength = 10;
            generateHillsTerrain(currentX, currentX + generateLength);
            currentX += generateLength;
        }
    }
}