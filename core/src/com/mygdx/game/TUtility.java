package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Entity.Player;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

import static com.mygdx.game.Game.*;

public class TUtility {
    public TUtility() {

    }

    public static Body createBox(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 40);
        Body body = Game.world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(x, y);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        return body;
    }
    public static Body createCircle(float x) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 40);
        Body body = Game.world.createBody(bodyDef);
        CircleShape box = new CircleShape();
        box.setRadius(x);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        return body;
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

    public static Vector2 getCursor() {
        float f = (float) Gdx.input.getX();
        float fy = (float) Gdx.input.getY();
        Vector3 cam = Game.camera.position;
        return new Vector2(cam.x,cam.y).add(pixelToMeter(new Vector2(f, fy)));
    }

    public static Vector2 getRoundedVector2(Vector2 vecToRound) {
        return new Vector2(Math.round(vecToRound.x), Math.round(vecToRound.y));
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

    public static void drawSprite(String spriteId, double x, double y) {
        drawSprite(new Sprite(new Texture(getImage(spriteId).getPath())),x,y);
    }

    public static void drawSprite(String spriteId, double x, double y, double xSize, double ySize) {
        drawSprite(new Sprite(new Texture(getImage(spriteId).getPath())),x,y);
    }

    public static void drawSprite(Texture texture, double xSize, double ySize) {
        drawSprite(new Sprite(texture),xSize,ySize);
    }

    public static File getFile(String imageId, File root) {
        File f = new File(root.getPath().replace("\\","/")+"/"+imageId+".png");
        if (f.exists()) {
            return f;
        }
        for (File file : root.listFiles()) {;
            f = new File(file.getPath().replace("\\","/")+"/"+imageId+".png");
            if (f.exists()) {
                return f;
            }
            if (file.isDirectory()) {
                f = getFile(imageId, file);
                if (f!=null) {
                    return f;
                }
            }
        }
        return null;
    }

    public static File getImage(String imageId) {
        return getFile(imageId, new File("Images"));
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
        return new Vector2(x,-y);
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
    public static void wait(float seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException a) {

        }
    }

    public static double getMagnitude(Vector2 vec1, Vector2 vec2) {
        return Math.sqrt(Math.pow(vec1.x - vec2.x, 2) + Math.pow(vec1.y - vec2.y, 2));
    }
}