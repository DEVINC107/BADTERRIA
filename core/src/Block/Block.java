package Block;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BlockTracker;
import com.mygdx.game.TUtility;

public class Block {
    private String name;
    private int health;

    public Block(String name, Vector2 position) {
        this.name = name;
        this.health = TUtility.getInitialBlockHealth(name);
        BlockTracker.addBlockToPosition(this, position);
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }
}