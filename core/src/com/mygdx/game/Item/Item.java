package com.mygdx.game.Item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Entity.Player;
import com.mygdx.game.Game;
import com.mygdx.game.TUtility;

public class Item {
    private boolean manifested = false;
    private Body body;

    private String itemId;
    public Item(String itemId) {
        this.itemId = itemId;
    }

    public void manifest(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        Body body = Game.world.createBody(bodyDef);
        this.body = body;
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.25f, 0.25f);
        body.createFixture(box,0f);
        manifested = false;
    }



    public void renderEquip(SpriteBatch batch, Player player) {
        Sprite s = new Sprite(new Texture("Images/Items/"+itemId+".png"));
        TUtility.drawSprite(s,player.body.getPosition().x,player.body.getPosition().y,player.getDir()==0?90:270);
    }



    public void unequip() {

    }

    public void mouseDown() {

    }

    public void heldUpdate() {

    }

    public String getItemId() {
        return itemId;
    }

    public void hide() {
        Game.world.destroyBody(body);
        manifested = true;
    }

    public boolean isDropped() {
        return manifested;
    }
}
