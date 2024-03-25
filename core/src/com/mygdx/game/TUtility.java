package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

import static com.mygdx.game.Game.*;

public class TUtility {
    public TUtility() {

    }

    public static String[] getData(String file, String token) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            File myFile = Gdx.files.internal(file).file();
            Scanner fileScanner = new Scanner(myFile);
            while (fileScanner.hasNext()) {
                String data = fileScanner.nextLine();
                lines.add(data);
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        for (String line : lines) {
            if (line.indexOf(token) == 0) {
                line = line.substring(token.length() + 1);
                return line.split(",");
            }
        }
        return null;
    }

    public Vector2 getCursor() {
        float f = (float) Gdx.input.getX();
        float fy = (float) Gdx.input.getY();
        Vector3 cam = Game.camera.position;
        return pixelToMeter(new Vector2(cam.x + f,cam.y + fy));
    }

    public static void drawSprite(Sprite sprite, double x, double y) {
        drawSprite(sprite,x,y,0);
    }
    public static void drawSprite(Sprite sprite, double x, double y, int rot) {
        Player player = Game.player;
        Vector2 playerPos = player.body.getPosition();
        float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
        float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
        double xSize = sprite.getWidth()/xScale;
        double ySize = sprite.getHeight()/yScale;
        double xPos = (x - playerPos.x) * (PPM/xScale) + (double) Gdx.graphics.getWidth() / 2 - xSize / 2;
        double yPos = (y - playerPos.y) * (PPM/yScale) + (double) Gdx.graphics.getHeight() / 2 - ySize / 2;
        sprite.setPosition((float) xPos,(float) yPos);
        sprite.setSize((float) xSize, (float) ySize);
        sprite.setOrigin((float)xSize/2,(float)ySize/2);
        sprite.setRotation(rot);
        sprite.draw(batch);
    }


    public static int getInitialBlockHealth(String blockName) {
        try {
            File myFile = new File("src\\students.txt");
            Scanner fileScanner = new Scanner(myFile);
            while (fileScanner.hasNext()) {
                String data = fileScanner.nextLine();
                String[] splitData = data.split(",");
                String currentName = splitData[0];
                if (currentName.equals(blockName)) {
                    return Integer.parseInt(splitData[1]);
                }
            }
        } catch (IOException ignored) {

        }
        return 69420;
    }

    public static Vector2 meterToPixel(Vector2 meters) {
        float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
        float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
        return new Vector2((float) (meters.x * (PPM/xScale) + Gdx.graphics.getWidth() / 2),(float) (meters.y *(PPM/yScale) + Gdx.graphics.getHeight() / 2));
    }

    public static Vector2 pixelToMeter(Vector2 pixels) {
        float xScale = (float)(BLOCKS_HORIZONTAL_AXIS/(Gdx.graphics.getWidth()/PPM));
        float yScale = (float)(BLOCKS_VERTICAL_AXIS/(Gdx.graphics.getHeight()/PPM));
        float x = (float) ((pixels.x - Gdx.graphics.getWidth()/2) * (xScale/PPM));
        float y = (float) ((pixels.y - Gdx.graphics.getHeight()/2) * (yScale/PPM));
        return new Vector2(x,y);
    }

    public static Vector2 roundVec2(Vector2 vec) {
        return new Vector2(Math.round(vec.x),Math.round(vec.y));
    }


    public static double getRandomDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}