package entity;

import entity.Sprite;

public class Enemy extends Sprite
{


    public Enemy(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width=width;
        this.height= height;
      //  this.enemyImages = enemyImages;
    //    this.image = enemyImages[0];
    }


  private int healthPoints;
  private float moveSpeed;
  private int attackRange;
  private float damage;
  private String name;
  private int velx;
  private int vely;


    //todo this functions
  public void behaviour(){

  }

 public void move(){

  }
  public void attack(){

  }

  //Getterek az enemyhez

    public int getHealthPoints() {
        return healthPoints;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public float getDamage() {
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
}
