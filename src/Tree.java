import processing.core.PImage;

import java.util.List;

public class Tree implements Transformable, Plants{
    /* Constants */
    public static final String KEY = "tree";
    public static final int HEALTH_MIN = 1;
    public static final int HEALTH_MAX = 3;
    public static final double ACTION_MIN = 1.000;
    public static final double ACTION_MAX = 1.400;
    public static final double ANIMATION_MIN = 0.050;
    public static final double ANIMATION_MAX = 0.600;
    public static final int NUM_PROPERTIES = 3;
    public static final int HEALTH = 2;
    public static final int ACTION_PERIOD = 1;
    public static final int ANIMATION_PERIOD = 0;

    public final String id;
    private int health;
    public Point position;
    private final double actionPeriod;
    private final double animationPeriod;
    private final List<PImage> images;
    private int imageIndex;

    public Tree(String id, Point position, List<PImage>images,double actionPeriod, double animationPeriod, int health){
        this.id = id;
        this.position  = position;
        this.images = images;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
    }
    @Override
    public String getID() {
        return id;
    }
    @Override
    public Point getPosition() {
        return position;
    }
    @Override
    public void setPosition(Point position) { this.position = position; }
    @Override
    public int getImageIndex() {
        return imageIndex;
    }
    @Override
    public List<PImage> getImages() {
        return images;
    }
    @Override
    public void nextImage(){
        imageIndex++;
    }
    @Override
    public void decreaseHealth(){
        health--;
    }
    @Override
    public int getHealth() {
        return health;
    }
    @Override
    public double getActionPeriod() {
        return actionPeriod;
    }
    @Override
    public double getAnimationPeriod(){ return animationPeriod;}
    @Override
    public void execute(WorldModel world, ImageStore imageStore,
                        EventScheduler scheduler) {

        if (!transform(scheduler, imageStore, world)) {

            scheduler.scheduleEvent(
                    this, new Activity(this, world, imageStore),
                    actionPeriod);
        }
    }
}
