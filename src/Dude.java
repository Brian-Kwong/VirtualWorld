import processing.core.PImage;

import java.util.List;

public abstract class Dude implements Transformable, Movable {
    public static final String KEY = "dude";
    public static final int NUM_PROPERTIES = 3;
    public static final int LIMIT = 2;
    public static final int ANIMATION_PERIOD = 1;
    public static final int ACTION_PERIOD = 0;
    private final String id;
    private Point position;
    private final double actionPeriod;
    private final double animationPeriod;
    protected final int resourceLimit;
    private final List<PImage> images;
    private int imageIndex = 0;
    private final boolean full;


    public Dude(String id, Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod, boolean full) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.full = full;
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
    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public List<PImage> getImages() {
        return images;
    }

    public void nextImage() {
        imageIndex++;
    }

    @Override
    public double getAnimationPeriod() {
        return animationPeriod;
    }

    @Override
    public double getActionPeriod() {
        return actionPeriod;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public boolean doesContain(WorldModel world, Point newPos) {
        return world.getOccupancyCell(newPos).getClass() == Stump.class;
    }

   public  boolean doesNotContainProhibitedTiles(WorldModel world, Point newPos){
        return !world.getBackgroundCell(newPos).getType().equals("stone");
    }

    public boolean transform(EventScheduler scheduler,
                             ImageStore imageStore, WorldModel worldModel) {
        Dude dude;
        if (worldModel.getBackgroundCell(position).getType().equals("stone") && this.getClass() != TactiBear.class){
            dude = Factory.createTactiBear("tactiBear", position, resourceLimit*10, actionPeriod*0.6, animationPeriod, imageStore.getImageList("tactibear"));
            scheduler.unscheduleAllEvents(this);
        }
        else {
            dude = helperDudeTransform(worldModel, imageStore, scheduler);
        }
        worldModel.removeEntity(scheduler, this);
        worldModel.addEntity(dude);
        dude.schedule(scheduler, worldModel, imageStore);
        return true;
    }

    protected abstract Dude helperDudeTransform(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler);

}