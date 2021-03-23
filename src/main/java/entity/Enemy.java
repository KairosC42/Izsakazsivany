package entity;

import entity.Sprite;

import java.awt.*;
import java.util.Random;

public class Enemy extends Sprite
{
    private String direction ="left";
    private int speed = 2;
    private String lastMove="left";
    private int healthPoints;
    private int attackRange;
    private int damage;
    private String name;
    private int velx;
    private int vely;


    public Enemy(int x, int y, int width, int height, Image image)
    {
        super(x, y, width, height, image);
    }




    //todo this functions
    public void behaviour(){

    }

    public void move(){


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


    public void randDirection()
    {
        Random rand = new Random();
        int randD = rand.nextInt(4);
        switch(randD){
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


    public void moveBack()
    {
        switch(lastMove)
        {
            case "up":
                y+=speed;
                break;
            case "down":
                y-=speed;
                break;
            case "left":
                x+=speed;
                break;
            case "right":
                x-=speed;
                break;
        }
    }
    public void attack(){

    }



    //Getterek és szetterek az enemyhez


    public String getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public String getLastMove() {
        return lastMove;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }

    public int getVelx() {
        return velx;
    }

    public int getVely() {
        return vely;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setLastMove(String lastMove) {
        this.lastMove = lastMove;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVelx(int velx) {
        this.velx = velx;
    }

    public void setVely(int vely) {
        this.vely = vely;
    }
}
