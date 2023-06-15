import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class BobaCat implements Executable, Movable{
    public static final String KEY = "bobaCat";
    public static final int NUM_PROPERTIES = 2;
    public static final double ACTION_PERIOD = 0.7;
    public static final double ANIMATION_PERIOD = 0.1;

    private final  String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex = 0;
    private double actionPeriod;
    private double animationPeriod;


    public BobaCat(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod){
        this.id = id;
        this.position = position;
        this.images = images;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    @Override
    public String getID() {
        return id;
    }
    @Override
    public Point getPosition(){
        return position;
    }
    @Override
    public void setPosition(Point position) { this.position = position; }
    @Override
    public List<PImage> getImages() {
        return images;
    }
    @Override
    public int getImageIndex(){
        return imageIndex;
    }
    @Override
    public void nextImage(){
        imageIndex++;
    }
    @Override
    public double getAnimationPeriod(){ return animationPeriod;}
    @Override
    public double getActionPeriod(){return actionPeriod;}
    @Override
    public void execute(WorldModel worldModel, ImageStore imageStore,
                        EventScheduler scheduler) {
        Class[] type = {TactiBear.class};
        Optional<Entity> catTarget = worldModel.findNearest(
                position, type);
        if (catTarget.isPresent()) {
            Point tgtPos = catTarget.get().getPosition();
            moveTo(catTarget.get(), scheduler, worldModel);
        }
        scheduler.scheduleEvent(
                this, Factory.createActivityAction(this, worldModel, imageStore),
                actionPeriod);
    }

    @Override
    public boolean doesContain(WorldModel world,Point newPos){
        return false;
    }
}
