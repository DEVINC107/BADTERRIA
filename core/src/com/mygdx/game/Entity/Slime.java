package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.TUtility;

public class Slime extends RenewableMob {
    public Slime(String entityId) {
        super(entityId);
        setBody(TUtility.createBox(0.5f,0.5f));
        getBody().setTransform(new Vector2(TUtility.getRandomInt(-10,10),50),0);
    }
}
