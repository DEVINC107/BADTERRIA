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

    private static final int MAP_LENGTH = 200;
    private static final int MAP_HEIGHT = 10;
    private static final int DIRT_LENGTH = 3;

    public static double currentElevation = 0;

    public static void generatePlainsTerrain() {
        final int TREE_GENERATE_FREQUENCY = 10; // 0 to 100

        int generateLength = 15;
        if (MAP_LENGTH/2 - currentX < generateLength) {
            generateLength = MAP_LENGTH/2 - currentX;
        }
        int startX = currentX;
        currentX += generateLength;
        int endX = currentX - 1;

        for (int x = startX; x <= endX; x++) {
            int currentHeight = Math.round((float) currentElevation);
            if (TUtility.getRandomInt(1, 100) <= TREE_GENERATE_FREQUENCY) {
                generateTree(new Vector2(x, currentHeight + 1));
            }
            new CreateBlock("Grass", new Vector2(x, currentHeight));
            currentHeight -= 1;
            for (int y = currentHeight; y > currentHeight - DIRT_LENGTH; y--) {
                new CreateBlock("Dirt", new Vector2(x, y));
            }
            currentHeight -= DIRT_LENGTH;
            for (int y = currentHeight; y > -MAP_HEIGHT; y--) {
                new CreateBlock("Stone", new Vector2(x, y));
            }
        }
    }

    public static void generateLakeTerrain() {
        final int MAX_WATER_DEPTH = 4;

        int generateLength = 16;
        if (MAP_LENGTH/2 - currentX < generateLength) {
            generateLength = MAP_LENGTH/2 - currentX;
        }
        int startX = currentX;
        currentX += generateLength;
        int endX = currentX - 1;

        for (int x = startX; x <= endX; x++) {
            int currentHeight = Math.round((float) currentElevation);
            int currentWaterDepth = x - startX;
            if (endX - x < currentWaterDepth) {
                currentWaterDepth = endX - x;
            }
            if (currentWaterDepth > MAX_WATER_DEPTH) {
                currentWaterDepth = MAX_WATER_DEPTH;
            }
            for (int y = currentHeight; y > currentHeight - currentWaterDepth; y--) {
                new CreateBlock("Water", new Vector2(x, y));
            }
            currentHeight -= currentWaterDepth;
            for (int y = currentHeight; y > currentHeight - DIRT_LENGTH; y--) {
                new CreateBlock("Dirt", new Vector2(x, y));
            }
            currentHeight -= DIRT_LENGTH;
            for (int y = currentHeight; y > -MAP_HEIGHT; y--) {
                new CreateBlock("Stone", new Vector2(x, y));
            }
        }
    }

    public static void generateMountainsTerrain() {
        final int MAX_MOUNTAIN_HEIGHT = 30;
        final int MIN_MOUNTAIN_HEIGHT = 20;
        final int MAX_FLAT_LENGTH = 8;

        boolean targetReached = false;
        int targetHillHeight = TUtility.getRandomInt(MIN_MOUNTAIN_HEIGHT, MAX_MOUNTAIN_HEIGHT);
        double heightIncrementIncrement = TUtility.getRandomDouble(0.5,0.8);
        double heightIncrement = heightIncrementIncrement;
        int targetFlatLength = (int) (Math.random() * MAX_FLAT_LENGTH + 1);
        int currentFlatLength = 0;

        int generateLength = 50;
        if (MAP_LENGTH/2 - currentX < generateLength) {
            generateLength = MAP_LENGTH/2 - currentX;
        }
        int startX = currentX;
        currentX += generateLength;
        int endX = currentX - 1;

        for (int x = startX; x <= endX; x++) {
            int currentHeight = Math.round((float) currentElevation);
            for (int y = currentHeight; y > -MAP_HEIGHT; y--) {
                new CreateBlock("Stone", new Vector2(x, y));
            }

            // changes current Hill Height
            if (targetReached) {
                currentFlatLength += 1;
                if (currentFlatLength >= targetFlatLength) {
                    targetHillHeight = TUtility.getRandomInt(0, MAX_MOUNTAIN_HEIGHT);
                    targetFlatLength = (int) (Math.random() * MAX_FLAT_LENGTH + 1);
                    currentFlatLength = 0;
                    heightIncrementIncrement = TUtility.getRandomDouble(0.5, 0.8);
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

    public static void generateHillsTerrain() {
        final int MAX_HILL_HEIGHT = 12;
        final int MAX_FLAT_LENGTH = 5;
        final int TREE_GENERATE_FREQUENCY = 4; // 0 to 100

        boolean targetReached = false;
        int targetHillHeight = TUtility.getRandomInt(0, MAX_HILL_HEIGHT);
        double heightIncrementIncrement = TUtility.getRandomDouble(0.2, 0.3);
        double heightIncrement = heightIncrementIncrement;
        int targetFlatLength = (int) (Math.random() * MAX_FLAT_LENGTH + 1);
        int currentFlatLength = 0;

        int generateLength = 20;
        if (MAP_LENGTH/2 - currentX < generateLength) {
            generateLength = MAP_LENGTH/2 - currentX;
        }
        int startX = currentX;
        currentX += generateLength;
        int endX = currentX - 1;

        for (int x = startX; x <= endX; x++) {
            int currentHeight = Math.round((float) currentElevation);
            if (TUtility.getRandomInt(1, 100) <= TREE_GENERATE_FREQUENCY) {
                generateTree(new Vector2(x, currentHeight + 1));
            }
            new CreateBlock("Grass", new Vector2(x, currentHeight));
            currentHeight -= 1;
            for (int y = currentHeight; y > currentHeight - DIRT_LENGTH; y--) {
                new CreateBlock("Dirt", new Vector2(x, y));
            }
            currentHeight -= DIRT_LENGTH;
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
    private static int currentX;
    public static void generateTerrain() {
        currentX = -MAP_LENGTH/2;
        while (currentX < MAP_LENGTH/2) {
            int random = TUtility.getRandomInt(1, 1000);
            if (random < 200) {
                generateHillsTerrain();
            } else if (random < 400) {
                generatePlainsTerrain();
            } else if (random <= 800) {
                generateMountainsTerrain();
            } else if (random <= 1000) {
                generateLakeTerrain();
            }
        }
    }
}