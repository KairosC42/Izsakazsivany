package entity;

import java.awt.*;
import java.util.Random;

public class Enemy extends Sprite {
    private final float speed = 2;
    private int healthPoints;
    private int attackRange;
    private float damage;
    private int velx;
    private int vely;
    private String direction = "left";
    private String lastMove = "left";


    public Enemy(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    public void behaviour() {

    }

    public void move() {
        switch (direction) {
            case "up":
                y -= speed;
                break;
            case "down":
                y += speed;
                break;
            case "left":
                x -= speed;
                break;
            case "right":
                x += speed;
                break;
        }
        lastMove = direction;
    }

    public void randDirection() {
        Random rand = new Random();
        int randD = rand.nextInt(4);
        switch (randD) {
            case 0:
                direction = "up";
                break;
            case 1:
                direction = "down";
                break;
            case 2:
                direction = "left";
                break;
            case 3:
                direction = "right";
                break;

        }

    }

    public void moveBack() {
        switch (lastMove) {
            case "up":
                y += speed;
                break;
            case "down":
                y -= speed;
                break;
            case "left":
                x += speed;
                break;
            case "right":
                x -= speed;
                break;
        }
    }
    //Getterek az enemyhez


    public float getSpeed() {
        return speed;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public float getDamage() {
        return damage;
    }

    public int getVelx() {
        return velx;
    }

    public int getVely() {
        return vely;
    }

    public String getDirection() {
        return direction;
    }

    public String getLastMove() {
        return lastMove;
    }

}
