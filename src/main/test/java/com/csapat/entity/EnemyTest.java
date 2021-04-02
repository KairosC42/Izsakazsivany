package entity;

import com.csapat.entity.Enemy;
import com.csapat.entity.Player;
import com.csapat.entity.Sprite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnemyTest {
    private Vector<Sprite> enemies = new Vector<>();
    private Player player;
    @Test

    public void EnemyTest(){

        assertTrue(enemies.size() == 0, "Enemyk szama nulla");
    }
}