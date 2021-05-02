package com.csapat.entity;

import com.csapat.sound.Sfx;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class Player extends Sprite {


    /**
     *
     * -healthPointsMax is a new data,
     * -check the usage of healthPoints and replace it with healthPointsMax if necessary
     * -revision needed for starting stats
     * -potions as a quippedItem? or in a separate Vector?
     * <p>
     * <p>
     * <p>
     * <p>
     * -módosító külön
     */

    private int healthPointsMax = 100;
    private int healthPoints = 100;
    private float range = 100;
    private float attackSpeed = 2f;
    private float damage = 20;
    private int moveSpeed = 2;
    private int experience = 0;
    private int nextLevelThreshold;
    private int playerLevel = 1;
    private Directions direction = Directions.Down;
    private Timer invincibilityTimer;
    private boolean canTakeDamage;
    //private Inventory inventory;
    private int velx;
    private int vely;
    private int money = 100;
    Vector<StatItem> equippedItems = new Vector<>();
    Vector<Potion> potions = new Vector<>();
    Weapon equippedWeapon;
    int walkingTime;
    Image playerImages[];
    private int frameHeight;
    private int frameWidth;

    private int healthPointsMaxModifier = 0;

    private float rangeModifier = 1;
    private float attackSpeedModifier = 1;
    private float damageModifier = 1;
    private float moveSpeedModifier = 1;
    private int killcount;
    private Sfx sfx;

    private boolean lastMove; //false, y-n, true- x-en

    public Player(int x, int y, int width, int height, Image playerImages[], int frameHeight, int frameWidth) {
        sfx=new Sfx();
        killcount=0;
        Image weaponImage = null;
        try {
            weaponImage = ImageIO.read(this.getClass().getClassLoader().getResource("sword.png"));
        } catch (Exception e) {
            System.out.println("Missing texture!");
        }
        equipItem(new Weapon(0, 0, 0, 0, weaponImage, 0, "starting weapon", 0, 0, 0));
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.playerImages = playerImages;
        this.image = playerImages[0];
        invincibilityTimer= new Timer();
        canTakeDamage = true;
        nextLevelThreshold = 200;
    }

    public void update(float deltaTime) {
        walkingTime += deltaTime;
    }


    public boolean isDead() {return healthPoints>0;}

    public void takeDamage(int damage)
    {
        if(canTakeDamage)
        {
            canTakeDamage=false;
            int INVINCIBILITY_TIME = 500;
            invincibilityTimer.schedule(new canTakeDamageTask(), INVINCIBILITY_TIME);
            healthPoints-=damage;
        }
        if(healthPoints<0)healthPoints=0;

    }

    public void buyItem(Item item) {
        this.money -= item.getPrice();
        this.equipItem(item);
    }

    public void stepBack() {
        if (lastMove) {
            x -= velx;
            //System.out.println("x-en visszalép");
        } else if (!lastMove) {
            y -= vely;
            //System.out.println("y-on visszalép");
        }
    }

    public void moveX() {
        if (velx > 0) {
            //jobbra
            this.image = playerImages[12];
            direction = Directions.Right;
            lastMove = true;
        } else if (velx < 0) {
            //balra
            this.image = playerImages[9];
            direction = Directions.Left;
            lastMove = true;
        }
        x += velx;
    }

    public void giveMoney(int money) {
        this.money += money;
    }

    public void giveExperience(int experience) {
        this.experience += experience;
        if (this.experience >= nextLevelThreshold) {
            raisePlayerLevel(this.experience - nextLevelThreshold);
        }
    }

    public void raisePlayerLevel(int experienceOverFlow) {
        ++playerLevel;
        experience = experienceOverFlow;
        nextLevelThreshold = (int) Math.round(Math.pow(nextLevelThreshold, 1.2));
        healthPointsMaxModifier += 20;
        healthPoints = getHealthPointsMax();
        sfx.levelUp();
    }

    public void moveY() {
        if (vely > 0) {
            //előre
            this.image = this.image = playerImages[0];
            direction = Directions.Down;
            lastMove = false;
        } else if (vely < 0) {
            //hátra
            this.image = this.image = playerImages[4];
            direction = Directions.Up;
            lastMove = false;
        }
        y += vely;


    }

    public int getHealth() {
        return this.healthPoints;
    }

    public int getVelX() {
        return this.velx;
    }

    public int getVelY() {
        return this.vely;
    }

    public void setHealth(int health) {
        this.healthPoints = health;
    }

    public void setVelX(int velx) {
        this.velx = velx;
    }

    public void setVelY(int vely) {
        this.vely = vely;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    /**
     * @param money Should always be positive!
     */
    public void setMoney(int money) {
        this.money += money;
    }

    public int getMoveSpeed() {
        return (int) (moveSpeed * moveSpeedModifier);
    }


    public Directions getDirection() {
        return direction;
    }

    public int getHealthPointsMax() {
        return healthPointsMax + healthPointsMaxModifier;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public float getAttackSpeed() {
        return attackSpeed * attackSpeedModifier;
    }

    public float getDamage() {
        return damage * damageModifier;
    }

    public int getExperience() {
        return experience;
    }

    public int getMoney() {
        return money;
    }


    public void equipWeapon(Weapon weapon) {
        rangeModifier -= equippedWeapon.rangeModifier;
        attackSpeedModifier -= equippedWeapon.attackSpeedModifier;
        attackSpeedModifier -= equippedWeapon.damageModifier;

        rangeModifier += weapon.rangeModifier;
        attackSpeedModifier += weapon.attackSpeedModifier;
        attackSpeedModifier += weapon.damageModifier;
        equippedWeapon = weapon;


    }

    public void equipItem(Item item) {
        if (item instanceof StatItem) {
            StatItem statItem = (StatItem) item;
            healthPointsMaxModifier += statItem.getHealthModifier();
            healthPoints += statItem.getHealthModifier();

            rangeModifier += statItem.getRangeModifier();
            attackSpeedModifier += statItem.getAttackSpeedModifier();
            damageModifier += statItem.getDamageModifier();
            moveSpeedModifier += statItem.getSpeedModifier();
            equippedItems.add(statItem);
            sfx.itemPickUp();
        } else if (item instanceof Potion) {
            if (((Potion) item).getHealthRestore() == 0) {
                giveExperience(((Potion) item).grantExp);
            }
            else potions.add((Potion) item);
            sfx.itemPickUp();
        } else if (item instanceof Weapon) {
            Weapon weapon = (Weapon) item;
            if (equippedWeapon != null) {

                range -= equippedWeapon.rangeModifier;
                attackSpeedModifier -= equippedWeapon.attackSpeedModifier;
                damage -= equippedWeapon.damageModifier;
            }
            range += weapon.rangeModifier;
            attackSpeedModifier += weapon.attackSpeedModifier;
            damage += weapon.damageModifier;
            equippedWeapon = weapon;

        }
    }

    public Item dropCurrentWeapon() {
        return equippedWeapon;
    }

    public void useHealthPotion() {
        if (potions.size()>0)
        {
            Potion pot=potions.get(0);
            healthPoints += pot.getHealthRestore();
            if (healthPoints > getHealthPointsMax()) {
                healthPoints = getHealthPointsMax();
            }
            potions.remove(pot);
        }
    }


    public int getRange() {

        return (int) (range * rangeModifier);
    }

    public void stepBackAfterLeveltransition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    class canTakeDamageTask extends TimerTask
    {
        public void run()
        {
            canTakeDamage=true;
        }
    }

    public Vector<StatItem> getEquippedItems()
    {
        return equippedItems;
    }

    public Weapon getEquippedWeapon()
    {
        return equippedWeapon;
    }

    public int getNextLevelThreshold()
    {
        return nextLevelThreshold;
    }

    public int getPlayerLevel()
    {
        return playerLevel;
    }

    public Vector<Potion> getPotions()
    {
        return potions;
    }

    public int getKillcount()
    {
        return killcount;
    }

    public void incrementKillCount()
    {
        killcount++;
        sfx.enemyDeath();
    }
}
