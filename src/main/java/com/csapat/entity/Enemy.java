package com.csapat.entity;

import java.awt.*;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class Enemy extends Sprite {
    private String direction = "left";
    private float speed = 2;
    private String lastMove = "left";
    private int healthPoints;
    private int attackRange;
    private int damage = 10;
    private String name;
    private int velx;
    private int vely;
    private int visionRange;
    private int levelDepth;
    private Timer moveTimer;
    private java.util.Timer enemy_attacked;


    private Boolean gotAttacked=false;

    public Enemy(int x, int y, int width, int height, Image image, int damage, int visionRange, int attackRange, int healthPoints, float speed,int levelDepth) {
        super(x, y, width, height, image);
        this.damage = damage;
        this.visionRange=visionRange;
        this.attackRange=attackRange;
        this.healthPoints=healthPoints;
        this.speed=speed;
        this.levelDepth=levelDepth;
        moveTimer = new Timer();
        moveTimer.schedule(new collideTask(), 0, 2000);
    }

    public void takeDamage(int damage)
    {
        healthPoints-=damage;
        if(healthPoints<0)healthPoints=0;
    }

    public boolean isPlayerInVisionRange(int posX, int posY)
    {
        return visionRange>= Math.abs(x-posX)+ Math.abs(y-posY);
    }
    public void followPlayer(int posX, int posY)
    {
        int relX = x-posX;
        int relY = y-posY;
        if(Math.max(Math.abs(relX), Math.abs(relY))==Math.abs(relX))
        {
            if(relX>0)
            {
                direction="left";
            }
            else
            {
                direction="right";
            }
        }
        else
        {
            if(relY>0)
            {
                direction="up";
            }
            else
            {
                direction="down";
            }

        }

    }
    /*
    public void behaviour(int player_x, int player_y) {
        System.out.println("player_x: " + player_x + "Enemy_x: " + x);
        System.out.println("pl_x: " + (player_x - x));
        System.out.println("player: " + player_y + "Enemy: " + y);
        System.out.println("pl_y: " + (y - player_y));


        int x_dist = 0;
        int y_dist = 0;
        if (player_x > x) {
            x_dist = player_x - x;
        }
        if (player_x < x) {
            x_dist = x - player_x;
        }
        if (player_y > y) {
            y_dist = player_y - y;
        }
        if (player_y < y) {
            y_dist = y - player_y;
        }
        System.out.println("x dist: " + x_dist + " y_dist " + y_dist);
        if (y_dist < 200 && x_dist < 200) {
            System.out.println("Közel van!!!!");
            x = player_x-Math.round(x_dist/3);
            y = player_x-Math.round(y_dist/3);
            System.out.println("benne " + x + "és" + y);



        }
        System.out.println(x + "és" + y);
        System.out.println("--------------------");
    }
    */


    public void move() {

        //todo move:  if ( x<850 && y<800 ) {   }
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
                y -= speed;;
                break;
            case "left":
                x += speed;
                break;
            case "right":
                x -= speed;
                break;
        }
    }

    public void attack() {
    }

    public Item dropLoot(Player player)
    {
        Random rand = new Random();
        int experience=10 + 15*levelDepth;
        int money = 5 + 8*levelDepth;
        player.giveExperience(experience);
        player.giveMoney(money);

        if(rand.nextInt(10)==2) //the chance of it landing on any single value should be equal at 10%
        {
            ItemGenerator generator = new ItemGenerator();
            return generator.generateSomething(levelDepth,false);
        }
        return null;

    }


    public Boolean damaged(float dmg,float attack_speed)
    {
        this.healthPoints-=dmg;
        enemy_attacked = new java.util.Timer();
        enemy_attacked.schedule(new gotDamagedTask(),(int)(1000/attack_speed));
        if(this.healthPoints<=0)
        {
            //died
            this.healthPoints=0;
            return true;
        }
        else
        {
            return false;
        }
    }

    //Getterek és szetterek az enemyhez


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getLastMove() {
        return lastMove;
    }

    public void setLastMove(String lastMove) {
        this.lastMove = lastMove;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVelx() {
        return velx;
    }

    public void setVelx(int velx) {
        this.velx = velx;
    }

    public int getVely() {
        return vely;
    }

    public void setVely(int vely) {
        this.vely = vely;
    }

    public Boolean getGotAttacked()
    {
        return gotAttacked;
    }

    public void setGotAttacked(Boolean gotAttacked)
    {
        this.gotAttacked = gotAttacked;
    }

    class collideTask extends TimerTask
    {
        public void run()
        {
            System.out.println("Time's up!");
            randDirection();
        }
    }

    class gotDamagedTask extends TimerTask
    {
        public void run()
        {
            gotAttacked = false;
        }
    }
}