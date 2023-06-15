import processing.core.PImage;

import java.util.List;

public final class Factory {
    // Create Functions
    public static BobaShop createBobaShop(String id, Point position, List<PImage> images, double animationPeriod) {
        return new BobaShop(id, position, images, animationPeriod);
    }

    public static DudeFull createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeFull(id, position, images, resourceLimit, actionPeriod, animationPeriod);
    }

    public static TactiBear createTactiBear(String id, Point position, int resourceLimit, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new TactiBear(id, position, images, resourceLimit,actionPeriod, animationPeriod);
    }

    // need resource count, though it always starts at 0
    public  static DudeNotFull createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeNotFull(id, position, images, resourceLimit, actionPeriod, animationPeriod);
    }

    public static  BobaCat createBobaCat(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new BobaCat(id, position, images,actionPeriod, animationPeriod);
    }

    public static  Fairy createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fairy(id, position, images,actionPeriod, animationPeriod);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static  Sapling createSapling(String id, Point position, List<PImage> images, int health) {
        return new Sapling(id, position,images);
    }

    public static  Stump  createStump(String id, Point position, List<PImage> images) {
        return new Stump(id, position, images);
    }

    public static  Tree createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id,position, images, actionPeriod, animationPeriod, health);
    }

    // Constructors of New "Custom" Entity Objects Private Methoods
    public  static House createHouse(String id, Point position, List<PImage> images) {
        return new House(id, position, images);
    }

    public  static Obstacle createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id,position, images,animationPeriod);
    }

    public static Animation createAnimationAction(Animatable entity, int repeatCount) {
        return new Animation(entity,repeatCount);
    }

    public static Activity createActivityAction(Executable entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }
}
