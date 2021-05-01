package com.csapat.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Vector;

public class Enemy extends Sprite {
    private Directions moveDirection;
    private Directions attackDirection;
    private float speed;
    private Directions lastMove;
    private int healthPoints;
    private int attackRange;
    private int damage = 10;
    private String name;
    private int velx;
    private int vely;
    private final int visionRange;
    private final int levelDepth;
    private final Timer moveTimer;
    private final Timer attackTimer;
    private Image attackImage;
    private java.util.Timer enemyAttacked;
    private boolean changeDirection;
    private boolean canAttack;
    private final int ATTACK_SPEED=1;


    private Boolean gotAttacked=false;

    public Enemy(int x, int y, int width, int height, Image image, int damage, int visionRange, int attackRange, int healthPoints, float speed,int levelDepth) {
        super(x, y, width, height, image);
        this.damage = damage;
        this.visionRange=visionRange;
        this.attackRange=attackRange;
        this.healthPoints=healthPoints;
        this.speed=speed;
        this.levelDepth=levelDepth;
        changeDirection=true;
        canAttack = true;
        moveTimer = new Timer();
        attackTimer = new Timer();
        moveTimer.schedule(new collideTask(), 0, 2000);
        try
        {
            attackImage = ImageIO.read(this.getClass().getClassLoader().getResource("attack.png"));
        }
        catch (Exception e)
        {
            System.out.println("Missing enemy attack");
        }

    }

    public void takeDamage(int damage)
    {
        healthPoints-=damage;
        if(healthPoints<0)healthPoints=0;
    }

    public Attack attack(Player player)
    {
        Vector<Player> target = new Vector<>();
        target.add(player);
        return new Attack(x, y, 25, attackRange,attackImage, this,target, lastMove, attackRange, damage);
    }

    public boolean isPlayerInAttackRange(int posX, int posY)
    {
        return attackRange>= Math.abs(x-posX)+ Math.abs(y-posY);
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
                moveDirection =Directions.Left;
            }
            else
            {
                moveDirection =Directions.Right;
            }
        }
        else
        {
            if(relY>0)
            {
                moveDirection =Directions.Up;
            }
            else
            {
                moveDirection =Directions.Down;
            }

        }

    }

    public Attack behaviour(Player player)
    {
        if(isPlayerInVisionRange(player.getX(), player.getY()))
        {
            followPlayer(player.getX(), player.getY());
            if(canAttack&&isPlayerInAttackRange(player.getX(), player.getY()))
            {
                canAttack=false;
                attackTimer.schedule(new attackedRecently(), 1000/ATTACK_SPEED);
                return attack(player);
            }
        }
        else
        {
            if(changeDirection) {
                changeDirection=false;
                moveTimer.schedule(new changeDir(), 250);
                randDirection();
            }
        }
        move();
        return null;
    }

    public void move() {

        switch (moveDirection) {
            case Up:
                y -= (int)speed;
                break;
            case Down:
                y += (int)speed;
                break;
            case Left:
                x -= (int)speed;
                break;
            case Right:
                x += (int)speed;
                break;
            case Still:
                break;
        }
        lastMove = moveDirection;
    }


    public void randDirection() {
        Random rand = new Random();
        int randD = rand.nextInt(5);
        switch (randD) {
            case 0:
                moveDirection = Directions.Up;
                break;
            case 1:
                moveDirection = Directions.Down;
                break;
            case 2:
                moveDirection = Directions.Left;
                break;
            case 3:
                moveDirection = Directions.Right;
                break;
            case 4:
                moveDirection = Directions.Still;
                break;
        }
    }


    public void moveBack() {
        switch (lastMove) {
            case Up:
                y += (int)speed;
                break;
            case Down:
                y -= (int)speed;;
                break;
            case Left:
                x += (int)speed;
                break;
            case Right:
                x -= (int)speed;
                break;
        }
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
        enemyAttacked = new java.util.Timer();
        enemyAttacked.schedule(new gotDamagedTask(),(int)(1000/attack_speed));
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
        return moveDirection;
    }

    public void setDirection(Directions direction) {
        this.moveDirection = direction;
    }

    public float getSpeed() {
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

    class changeDir extends TimerTask
    {
        public void run()
        {
            changeDirection = true;
        }
    }
    class attackedRecently extends TimerTask
    {
        public void run()
        {
            canAttack = true;
        }
    }

}