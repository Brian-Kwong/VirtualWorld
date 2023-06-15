import processing.core.PImage;

import java.util.List;

public class Sapling implements Plants, Transformable {
    public static final String KEY = "sapling";
    public static final int NUM_PROPERTIES = 1;
    public static final int HEALTH = 0;
    public static final double ACTION_ANIMATION_PERIOD = 1.000;
    public static final int HEALTH_LIMIT = 5;

    public final  String  id;
    private int health;
    public Point position;
    private final List<PImage> images;
    private int imageIndex = 0;
    private final double animationPeriod = ACTION_ANIMATION_PERIOD;
    private final double actionPeriod = ACTION_ANIMATION_PERIOD;
    private final int healthLimit = HEALTH_LIMIT;


    public Sapling(String id, Point position,  List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
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
    public void decreaseHealth(){
        health--;
    }
    @Override
    public int getHealth() {
        return health;
    }
    @Override
    public void nextImage(){
        imageIndex++;
    }
    @Override
    public double getAnimationPeriod(){ return animationPeriod;}
    @Override
    public double getActionPeriod() {
        return actionPeriod;
    }
    @Override
    public void execute(WorldModel world, ImageStore imageStore,
                        EventScheduler scheduler) {
        health++;
        if (!transform(scheduler, imageStore, world)) {
            scheduler.scheduleEvent(
                    this, new Activity( this, world, imageStore),
                    actionPeriod);
        }
    }
    @Override
    public boolean transform(EventScheduler scheduler,
                             ImageStore imageStore, WorldModel worldModel) {
        if (Plants.super.transform(scheduler,imageStore,worldModel)) {
            return true;
        } else if (health >= healthLimit) {
            Tree tree =
                    new Tree((Tree.KEY + "_" + id),
                            position,
                            imageStore.getImageList(Tree.KEY),
                            Functions.getNumFromRange(Tree.ACTION_MAX,Tree.ACTION_MIN),
                            Functions.getNumFromRange(Tree.ANIMATION_MAX, Tree.ANIMATION_MIN),
                            Functions.getIntFromRange(Tree.HEALTH_MAX, Tree.HEALTH_MIN));
            worldModel.removeEntity(scheduler, this);

            worldModel.addEntity(tree);
            tree.schedule(scheduler, worldModel, imageStore);

            return true;
        }

        return false;
    }
}
