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

    //todo this functions
  public void behaviour(){

  }

 public void move(){

  }
  public void attack(){

  }


}
