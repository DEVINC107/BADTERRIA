package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

public class Slime extends RenewableMob {
    public Slime(String entityId) {
        super(entityId);
        setBody(TUtility.createCircle(0.5f));
        getBody().setTransform(new Vector2(TUtility.getRandomInt(-10,10),50),0);
        for (Fixture f : getBody().getFixtureList()) {
            f.setDensity(1f);
            f.setRestitution(0.1f);
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
            getBody().applyLinearImpulse(x*5,10,getBody().getPosition().x,getBody().getPosition().y,true);
            lastTick = System.currentTimeMillis();
        }
    }
    Long lastHitTick = System.currentTimeMillis() - 1000;

    @Override
    public void collision(Entity other) {
        if (System.currentTimeMillis() - lastHitTick < 1000) {
            return;
        }
        if (other.entityId.equals("Player")) {
            Game.player.setHealth(Game.player.health - 10);
            Vector2 knockback = Game.player.body.getPosition().sub(body.getPosition()).nor();
            Vector2 playerPos = Game.player.body.getPosition();
            Game.player.body.applyLinearImpulse(-knockback.x * 7.5f, -knockback.y * 7.5f, playerPos.x, playerPos.y, true);
            lastHitTick = System.currentTimeMillis();
        }
    }

    public void die() {
        System.out.println("DEAD");
        super.die();
    }
}
