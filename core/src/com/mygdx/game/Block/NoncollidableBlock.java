package com.mygdx.game.Block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class NoncollidableBlock extends Block {
    public NoncollidableBlock(String blockName, Vector2 position) {
        super(blockName, position);
    }
    public void setCollision(Vector2 position, World world) {
        //empty so no collisions :)
    }
}