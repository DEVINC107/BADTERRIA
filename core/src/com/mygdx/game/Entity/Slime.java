package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

public class Slime extends RenewableMob {
    public Slime(String entityId) {
        super(entityId);
        setBody(TUtility.createBox(0.5f,0.5f));
        getBody().setTransform(new Vector2(TUtility.getRandomInt(-10,10),50),0);
        for (Fixture f : getBody().getFixtureList()) {
            f.setDensity(1f);
            f.setRestitution(0.3f);
        }
    }
    Long lastTick = null;
    public void update() {
        super.update();
        if (getBody().getLinearVelocity().y != 0) {
            return;
        }
        if (lastTick == null) {
            lastTick = System.currentTimeMillis() - 8000;
        }
        if (System.currentTimeMillis() - lastTick >= 5000) {
            Vector2 playerPos = Game.player.getBody().getPosition();
            float x = Math.signum(playerPos.x - getBody().getPosition().x);
            getBody().applyLinearImpulse(x*10,10,getBody().getPosition().x,getBody().getPosition().y,true);
            lastTick = System.currentTimeMillis();
        }
    }

    @Override
    public void collision(Entity other) {
        if (other.entityId.equals("Player")) {
            System.out.println("COLLIDED WITH HUMAN");
        }
    }
}
