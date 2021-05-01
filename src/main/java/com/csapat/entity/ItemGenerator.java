package com.csapat.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 * ItemGenerator class
 * used for creating items either dropped by enemies or for shops or item com.csapat.rooms
 * for shops call with hasPrice=true to calculate a price, otherwise with false to have price set to 0
 * <p>
 * i wanted this class to be static so it doesn't require instantiation, but random cannot be used in a static environment
 * value inside are subject to change
 * <p>
 * use method generateAnything() to generate either a statItem, weapon or potion
 * <p>
 * for now textures aren't randomly chosen, this will change
 * <p>
 * levelDepth is just freshly implemented, and is also subject to change just like the rest of the statistics are.
 * for now the per-level scaling isn't very high, that's because enemies can drop items too
 */

public class ItemGenerator {
    Random rand;
    Image redPotion;
    Image bluePotion;
    Image glasses;
    Image sword;
    Image bow;
    Image rubyPendant;
    Image emeraldPendant;
    Image muscle;
    Image boots;
    Image fast;
    private final float oneHundredF = 100.f;
    private final float twoF = 2.f;
    private final int basePrice = 50;
    private final float priceDepthScale = 0.4f;

    private final int imageSize = 50;
    private final int imagePos = 200;

    public ItemGenerator() {
        this.rand = new Random();
        try {
            redPotion = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("red_potion.png")));
            bluePotion = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("blue_potion.png")));
            glasses = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("glasses.png")));
            sword = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("sword.png")));
            bow = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("bow.png")));
            rubyPendant = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ruby_pendant.png")));
            emeraldPendant = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("emerald_pendant.png")));
            muscle = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("muscle.png")));
            boots = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("boots.png")));
            fast = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("fast.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a statItem with random stats that scale with levelDepth
     * If hasPrice is true a price will be generated based on how good the stats are compared to the average
     * otherwise the price will be 0
     * The image for the statItem is it's most dominant stat (compared to its average)
     *
     * @param levelDepth gives extra stats the higher this is
     * @param hasPrice   decides if a price should be generated
     * @return a statItem
     */
    public Item generateStatItem(int levelDepth, boolean hasPrice) {


        final int healthRandMax = 26;
        final int healthBase = 10;
        final int healthDepthScale = 15;
        final float averageHealthValue = (healthBase + healthBase + healthRandMax - 1) / twoF;
        int healthModifier = (rand.nextInt(healthRandMax) + healthBase) + (levelDepth - 1) * healthDepthScale;

        final int rangeRandMax = 4;
        final int rangeBase = 3;
        final int rangeDepthScale = 3;
        final float averageRangeValue = (rangeBase + rangeBase + rangeRandMax - 1) / twoF;
        float rangeModifier = ((rand.nextInt(rangeRandMax) + rangeBase) + (levelDepth - 1) * rangeDepthScale) / oneHundredF;

        final int attackSpeedRandMax = 5;
        final int attackSpeedBase = 5;
        final int attackSpeedDepthScale = 3;
        final float averageAttackSpeedValue = (attackSpeedBase + attackSpeedBase + attackSpeedRandMax - 1) / twoF;
        float attackSpeedModifier = ((rand.nextInt(attackSpeedRandMax) + attackSpeedBase) + (levelDepth - 1) * attackSpeedDepthScale) / oneHundredF;


        final int damageRandMax = 6;
        final int damageBase = 2;
        final int damageDepthScale = 2;
        final float averageDamageValue = (damageBase + damageBase + damageRandMax - 1) / twoF;
        float damageModifier = ((rand.nextInt(damageRandMax) + damageBase) + (levelDepth - 1) * damageDepthScale) / oneHundredF;

        final int speedRandMax = 4;
        final int speedBase = 2;
        final int speedDepthScale = 2;
        final float averageSpeedValue = (rangeBase + rangeBase + rangeRandMax - 1) / twoF;
        float speedModifier = ((rand.nextInt(speedRandMax) + speedBase) + (levelDepth - 1) * speedDepthScale) / oneHundredF;


        //formula for ValueRatio: actual value generated divided by average
        float rangeValueRatio = (rangeModifier * oneHundredF / averageRangeValue);
        float rangeValueRatioMax = (((rangeModifier*oneHundredF)-rangeBase - ((levelDepth-1)*rangeDepthScale) )/(float) (rangeRandMax-1));
        float attackSpeedValueRatio = (attackSpeedModifier * oneHundredF / averageAttackSpeedValue);
        float attackSpeedValueRatioMax = (((attackSpeedModifier*oneHundredF) - attackSpeedBase - ((levelDepth-1)*attackSpeedDepthScale) ) /(float) (attackSpeedRandMax-1));
        float damageValueRatio = (damageModifier * oneHundredF / averageDamageValue);
        float damageValueRatioMax = (((damageModifier*100f) -damageBase - ((levelDepth-1)*damageDepthScale) )/(float) (damageRandMax-1));
        float speedValueRatio = (speedModifier * oneHundredF / averageSpeedValue);
        float speedValueRatioMax = (((speedModifier*100.f) - speedBase  - ((levelDepth-1)*speedDepthScale) )/(float) ( speedRandMax-1));
        float healthValueRatio = (healthModifier / averageHealthValue);
        float healthValueRatioMax = ((healthModifier-healthBase - ((levelDepth-1)*healthDepthScale) ) /(float)(healthRandMax-1));

        //calculating which image to use
        float[] valueRatios = new float[5];
        valueRatios[0] = rangeValueRatioMax;
        valueRatios[1] = attackSpeedValueRatioMax;
        valueRatios[2] = damageValueRatioMax;
        valueRatios[3] = speedValueRatioMax;
        valueRatios[4] = healthValueRatioMax;

        float maxValueRatio = valueRatios[0];
        int maxIndex = 0;
        for (int i = 1; i < 5; ++i) {
            if (maxValueRatio < valueRatios[i]) {
                maxValueRatio = valueRatios[i];
                maxIndex = i;
            }
        }
        Image statItemImage = null;

        //it bothers me how unreadable this came out to be,
        // but this is the only way to do this without importing libraries
        switch (maxIndex) {
            case 0:
                statItemImage = glasses;
                break;
            case 1:
                statItemImage = emeraldPendant;
                break;
            case 2:
                statItemImage = muscle;
                break;
            case 3:
                statItemImage = boots;
                break;
            case 4:
                statItemImage = rubyPendant;
                break;
            default:
                System.out.println("Something went horribly wrong!");
                break;
        }


        final int statCount = 5;
        float priceMultiplier = (rangeValueRatio + damageValueRatio + attackSpeedValueRatio + speedValueRatio + healthValueRatio) / statCount;
        float price = (basePrice + (basePrice * (levelDepth - 1) * priceDepthScale)) * priceMultiplier;


        int realPrice = 0;
        if (hasPrice) {
            realPrice = Math.round(price);
        }

        return new StatItem(imagePos, imagePos, imageSize, imageSize, statItemImage, realPrice, "statItem", healthModifier, rangeModifier, attackSpeedModifier, damageModifier, speedModifier);
    }

    /**
     * Generates a Weapon with random stats, if hasPrice is false, the price is 0, otherwise it's calculated based on stats
     * since a player can only wield one weapon at a time Weapons have higher stats than individual statItems
     * The base price is the same as a statItem.
     * The image for the Weapon is it's most dominant stat (compared to its average)
     *
     * @param levelDepth gives extra stats the higher this is
     * @param hasPrice   decides if a price should be generated
     * @return a Weapon
     */
    public Item generateWeapon(int levelDepth, boolean hasPrice) {

        final int rangeBase = 30;
        final int rangeRandMax = 25;
        final int rangeDepthScale = 15;
        final float averageRangeValue = (rangeBase * 2 + rangeRandMax - 1) / twoF;
        int weaponRangeModifier = rand.nextInt(rangeRandMax) + rangeBase + (levelDepth - 1) * rangeDepthScale;
        float weaponRangeValueRatioMax = ((weaponRangeModifier-rangeBase -((levelDepth-1)*rangeDepthScale)  )/ (float)(rangeRandMax-1));

        final int damageBase = 15;
        final int damageRandMax = 11;
        final int damageDepthScale = 8;
        final float averageDamageValue = (damageBase * 2 + damageRandMax - 1) / twoF;
        int weaponDamageModifier = rand.nextInt(damageRandMax) + damageBase + (levelDepth - 1) * damageDepthScale;
        float weaponDamageValueRatioMax = (((weaponDamageModifier-damageBase - ((levelDepth-1)*damageDepthScale))/ (float)(damageRandMax-1)));

        final int attackSpeedBase = 6;
        final int attackSpeedRandMax = 7;
        final int attackSpeedDepthScale = 3;
        final float averageAttackSpeedValue = (attackSpeedBase * 2 + attackSpeedRandMax - 1) / twoF;
        float attackSpeedMultiplier = ((rand.nextInt(attackSpeedRandMax) + attackSpeedBase) + (levelDepth - 1) * attackSpeedDepthScale) / oneHundredF;
        float weaponAttackSpeedValueRatioMax = (((attackSpeedMultiplier*100.f)-attackSpeedBase - ((levelDepth-1)*attackSpeedDepthScale)  )/ (float)(attackSpeedRandMax-1));

        float weaponDamageValueRatio = (weaponRangeModifier / averageRangeValue);
        float weaponRangeValueRatio = (weaponDamageModifier / averageDamageValue);
        float attackSpeedValueRatio = (attackSpeedMultiplier * oneHundredF / averageAttackSpeedValue);


        float priceMultiplier = (weaponDamageValueRatio + weaponRangeValueRatio + attackSpeedValueRatio) / 3;
        float price = ((basePrice + (basePrice * (levelDepth - 1) * priceDepthScale)) * priceMultiplier);

        float[] valueRatios = new float[3];
        valueRatios[0] = weaponRangeValueRatioMax;
        valueRatios[1] = weaponAttackSpeedValueRatioMax;
        valueRatios[2] = weaponDamageValueRatioMax;


        float maxValueRatio = valueRatios[0];
        int maxIndex = 0;
        for (int i = 1; i < 3; ++i) {
            if (maxValueRatio < valueRatios[i]) {
                maxValueRatio = valueRatios[i];
                maxIndex = i;
            }
        }
        Image weaponImage = null;

        //it bothers me how unreadable this came out to be,
        // but this is the only way to do this without importing libraries
        switch (maxIndex) {
            case 0:
                weaponImage = bow;
                break;
            case 1:
                weaponImage = fast;
                break;
            case 2:
                weaponImage = sword;
                break;
            default:
                System.out.println("Something went horribly wrong!");
                break;
        }


        int realPrice = 0;
        if (hasPrice) {
            realPrice = Math.round(price);
        }
        return new Weapon(imagePos, imagePos, imageSize, imageSize, weaponImage, realPrice, "weapon", weaponRangeModifier, weaponDamageModifier, attackSpeedMultiplier);
    }

    /**
     * Generates a potion that either gives experience or restores health
     * price is 0 if hasPrice is false, otherwise a price will be generated
     *
     * @param levelDepth influences the healing or experience
     * @param hasPrice   decides if a price will be generated
     * @return a potion
     */
    public Item generatePotion(int levelDepth, boolean hasPrice) {

        int healthRestore = 0;
        int grantExp = 0;

        final int healthBase = 20;
        final int healthRandMax = 31;
        final int healthDepthScale = 15;
        final float healthAverageValue = (2 * healthBase + healthRandMax - 1) / twoF;

        final int experienceBase = 30;
        final int experienceRandMax = 51;
        final int experienceDepthScale = 20;
        final float experienceAverageValue = (2 * experienceBase + experienceRandMax - 1) / twoF;


        float price = basePrice + (basePrice / twoF * (levelDepth - 1) * priceDepthScale);
        Image potionImage;
        if (rand.nextBoolean()) {

            healthRestore = rand.nextInt(healthRandMax) + healthBase + ((levelDepth - 1) * healthDepthScale);
            price *= healthRestore / healthAverageValue;
            potionImage = redPotion;
        } else {
            grantExp = rand.nextInt(experienceRandMax) + experienceBase + ((levelDepth - 1) * experienceDepthScale);
            price *= grantExp / experienceAverageValue;
            potionImage = bluePotion;

        }
        int realPrice = 0;
        if (hasPrice) {
            realPrice = Math.round(price);
        }
        return new Potion(imagePos, imagePos, imageSize, imageSize, potionImage, realPrice, "potion", healthRestore, grantExp);
    }

    /**
     * generates a random value between 0 and 2 and generates either a statItem, a Weapon or a Potion based on that
     *
     * @param levelDepth to be used by the selected method
     * @param hasPrice   to be used by the selected method
     * @return an Item
     */
    public Item generateSomething(int levelDepth, boolean hasPrice) {
        int what = rand.nextInt(3);
        Item ret = null;
        switch (what) {
            case 0:
                ret = generateStatItem(levelDepth, hasPrice);
                break;
            case 1:
                ret = generateWeapon(levelDepth, hasPrice);
                break;
            case 2:
                ret = generatePotion(levelDepth, hasPrice);
                break;
        }
        return ret;
    }

}
