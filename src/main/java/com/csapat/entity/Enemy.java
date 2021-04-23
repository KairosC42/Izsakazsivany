package com.csapat.entity;


import java.awt.*;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

import static java.lang.Math.abs;

public class Enemy extends Sprite {
    private Directions direction = Directions.Left;
    private int speed = 1;
    private Directions lastMove = Directions.Left;
    private int healthPoints =100;
    private int attackRange;
    private int damage = 10;
    private String name;
    private int velx;
    private int vely;
    private Timer moveTimer;
    private java.util.Timer enemy_attacked;
    private int vision_range = 200;

    public Boolean changed_direction=false;
    private java.util.Timer change_dir;
    public Boolean isPlayerNearby = false;

    private Boolean gotAttacked=false;


    public Enemy(int x, int y, int width, int height, Image image, int damage) {
        super(x, y, width, height, image);
        this.damage = damage;
        moveTimer = new Timer();
        moveTimer.schedule(new collideTask(), 0, 1000);
    }


    //todo this functions
//todo this functions
    public void behaviour(int player_x, int player_y)
    {
        /*int x_dist = 0;
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
        */

        double x1 = x;
        double y1 = y;
        double x2 = player_x;
        double y2 = player_y;
        double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
        if (distance < vision_range && !changed_direction)
        {
            isPlayerNearby = true;
            changed_direction=true;
            change_dir=new Timer();
            change_dir.schedule(new changeDir(), 500);
            System.out.println("Közel van!!!!");
            int y_dist = abs(player_y - y);
            int x_dist = abs(player_x - x);
            System.out.println(x_dist+","+y_dist);
            if (y_dist >= x_dist)
            {
                if (y < player_y)
                {
                    direction = Directions.Down;
                } else if (y >player_y)
                {
                    direction = Directions.Up;
                }
                else
                {
                    direction = Directions.Still;
                }
            } else
            {
                if (x < player_x)
                {
                    direction = Directions.Right;
                } else if(x>player_x)
                {
                    direction = Directions.Left;
                }
                else
                {
                    direction = Directions.Still;
                }
            }
        }
        else
        {
            isPlayerNearby = false;
        }
    }

    public void move()
    {
        switch (direction) {
            case Up:
                y -= speed;
                break;
            case Down:
                y += speed;
                break;
            case Left:
                x -= speed;
                break;
            case Right:
                x += speed;
                break;
            case Still:
                break;
        }
        lastMove = direction;
    }


    public void randDirection() {
        Random rand = new Random();
        int randD = rand.nextInt(5);
        switch (randD) {
            case 0:
                direction = Directions.Up;
                break;
            case 1:
                direction = Directions.Down;
                break;
            case 2:
                direction = Directions.Left;
                break;
            case 3:
                direction = Directions.Right;
                break;
            case 4:
                direction = Directions.Still;
                break;
        }
    }


    public void moveBack() {
        switch (lastMove) {
            case Up:
                y += speed;
                break;
            case Down:
                y -= speed;;
                break;
            case Left:
                x += speed;
                break;
            case Right:
                x -= speed;
                break;
        }
    }

    public void attack() {
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


    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Directions getLastMove() {
        return lastMove;
    }

    public void setLastMove(Directions lastMove) {
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
            if(!isPlayerNearby)
            {
                randDirection();
            }
        }
    }

    class gotDamagedTask extends TimerTask
    {
        public void run()
        {
            gotAttacked = false;
        }
    }

    class changeDir extends TimerTask
    {
        public void run()
        {
            changed_direction=false;
        }
    }
}