package com.csapat.entity;

import java.awt.*;
import java.util.Vector;

public class Attack extends Sprite {

    private Sprite source;
    private Vector target;
    private int speed = 0;
    private boolean isEnded = false;
    private int range;
    private int starter_x;
    private int starter_y;
    private int renderPosX;
    private int renderPosY;
    private int damage;
    private int duration = 15;
    private Directions dir;
    //offset is a rough integer approximation of sqrt(((imagewidth/2)^2)/2) which is the length of an
    // equilateral triangle's shorter sides from py's theorem
    private final int OFFSET = 7;


    public Attack(int x, int y, int width, int height, Image image, Sprite source, Vector target, Directions dir, int range, int damage) {
        if (dir == Directions.Down) {
            this.x = source.x + ((source.width / 2) - (width / 2));
            this.y = source.y + source.height;
        }
        if (dir == Directions.Up) {
            this.x = source.x + ((source.width / 2) - (width / 2));
            this.y = source.y - height;
        }
        if (dir == Directions.Left) {
            this.x = source.x - height;
            this.y = source.y + ((source.height / 2) - (width / 2));
        }
        if (dir == Directions.Right) {
            this.x = source.x + source.width;
            this.y = source.y + ((source.height / 2) - (width / 2));
        }
        starter_x = this.x;
        starter_y = this.y;

        if (dir == Directions.Right || dir == Directions.Left) {
            this.height = width;
            this.width = height;
        } else {
            this.width = width;
            this.height = height;
        }
        this.image = image;
        this.source = source;
        this.target = target;
        this.dir = dir;
        this.range = range;
        this.damage = damage;
    }

    public Attack(int x, int y, int width, int height, Image image, Sprite source, Vector target, Directions primary, Directions secondary, int range, int damage) {
        int TOP_OFFSET = Math.round((float) Math.sqrt((range * range) / 2.f));
        switch (primary) {
            //todo try to fix hitboxes
            case Down:
                if (secondary == Directions.Left) {
                    this.x = source.x - TOP_OFFSET;
                    this.y = source.y + OFFSET + source.height;
                    renderPosX = source.x - OFFSET;
                    renderPosY = source.y + source.height - OFFSET;
                }
                //the else here is 100% unnecessary, but the compiler doesn't know that
                // so it throws an error as posX and posY are final
                else if (secondary == Directions.Right) {
                    this.x = source.x + source.width;
                    this.y = source.y + source.height;
                    renderPosX = source.x + source.width - OFFSET;
                    renderPosY = source.y + source.height + OFFSET;
                }
                break;
            case Up:
                if (secondary == Directions.Left) {
                    this.x = source.x - TOP_OFFSET;
                    this.y = source.y - OFFSET - TOP_OFFSET;
                    renderPosX = source.x - TOP_OFFSET - OFFSET;
                    renderPosY = source.y - TOP_OFFSET + OFFSET;
                } else if (secondary == Directions.Right) {
                    this.x = source.x + source.width + OFFSET;
                    this.y = source.y - TOP_OFFSET + OFFSET;
                    renderPosX = source.x + width + TOP_OFFSET - OFFSET;
                    renderPosY = source.y - TOP_OFFSET - OFFSET;
                }
                break;
            case Left:
                if (secondary == Directions.Up) {
                    this.x = source.x - TOP_OFFSET;
                    this.y = source.y - OFFSET - TOP_OFFSET;
                    renderPosX = source.x - TOP_OFFSET - OFFSET;
                    renderPosY = source.y - TOP_OFFSET + OFFSET;
                } else if (secondary == Directions.Down) {
                    this.x = source.x - TOP_OFFSET;
                    this.y = source.y + OFFSET + source.height;
                    renderPosX = source.x - OFFSET;
                    renderPosY = source.y + source.height - OFFSET;
                }
                break;
            case Right:
                if (secondary == Directions.Up) {
                    this.x = source.x + source.width + OFFSET;
                    this.y = source.y - TOP_OFFSET + OFFSET;
                    renderPosX = source.x + width + TOP_OFFSET - OFFSET;
                    renderPosY = source.y - TOP_OFFSET - OFFSET;
                } else if (secondary == Directions.Down) {
                    this.x = source.x + source.width;
                    this.y = source.y + source.height;
                    renderPosX = source.x + source.width - OFFSET;
                    renderPosY = source.y + source.height + OFFSET;
                }
                break;
        }

        starter_x = this.x;
        starter_y = this.y;

        if (primary == Directions.Right || primary == Directions.Left) {
            this.height = TOP_OFFSET + 3;
            this.width = TOP_OFFSET + 3;
        } else {
            this.width = TOP_OFFSET + 3;
            this.height = TOP_OFFSET + 3;
        }
        this.image = image;
        this.source = source;
        this.target = target;
        this.range = range;
        this.damage = damage;
    }

    public void decreaseDuration() {
        duration -= 1;
    }


    public void cast() {
        switch (dir) {
            case Up:
                this.y -= speed;
                break;
            case Down:
                this.y += speed;
                break;
            case Left:
                this.x -= speed;
                break;
            case Right:
                this.x += speed;
                break;
        }

        if (isOutOfRange()) {
            isEnded = true;
        }
    }

    public boolean isEnded() {
        return isEnded;
    }

    public boolean isOutOfRange() {
        int base_y = this.y + source.height / 2;
        int base_x = this.x + source.width / 2;
        //System.out.println((Math.sqrt((base_y - this.y) * (base_y - this.y) + (base_x - this.x) * (base_x - this.x))));
        //System.out.println((Math.sqrt(((base_y - this.y-this.height) * (base_y - this.y-this.height)) + ((base_x - this.x) * (base_x - this.x)))));

        switch (dir) {
            case Left:
            case Right:
                if (Math.abs(starter_x - this.x) > range - this.width) {
                    return true;
                }
                break;
            case Up:
            case Down:
                if (Math.abs(starter_y - this.y) > range - this.height) {
                    return true;
                }
                break;
            default:
                return false;

        }
        return false;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDuration() {
        return duration;
    }

    public Sprite getSource() {
        return source;
    }

    public int getDamage() {
        return damage;
    }

    public int getRenderPosX() {
        return renderPosX;
    }

    public int getRenderPosY() {
        return renderPosY;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


}
