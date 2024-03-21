package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Game;

public class Player extends Entity {
    public Body body;
    public Player() {
        super("Player");
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 10);
        Body body = Game.world.createBody(bodyDef);
        this.body = body;
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.75f, new Vector2(0,0.25f),0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        CircleShape cShape = new CircleShape();
        cShape.setRadius(0.5f);
        cShape.setPosition(new Vector2(0,-0.5f));
        FixtureDef circle = new FixtureDef();
        circle.shape = cShape;

        body.createFixture(circle);
        body.createFixture(fixtureDef);
    }
}
