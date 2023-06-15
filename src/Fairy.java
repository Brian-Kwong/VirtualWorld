import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Fairy implements Executable, Movable{
    public static final String KEY = "fairy";
    public static final int NUM_PROPERTIES = 2;
    public static final int ACTION_PERIOD = 1;
    public static final int ANIMATION_PERIOD = 0;

    private final  String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex = 0;
    private final double actionPeriod;
    private final double animationPeriod;


    public Fairy(String id, Point position,List<PImage> images,  double actionPeriod, double animationPeriod){
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
        Class[] type = {Stump.class};
        Optional<Entity> fairyTarget = worldModel.findNearest(
                position, type);
        Class [] houseType = {House.class};
        Optional<Entity> house = worldModel.findNearest(
                position, houseType);
        Point housePos = house.orElse(null).getPosition();
        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();
            if (moveTo(fairyTarget.get(), scheduler, worldModel)) {
                Sapling sapling = new Sapling((Sapling.KEY + "_" + fairyTarget.get().getID()), tgtPos, imageStore.getImageList(Sapling.KEY));
                worldModel.addEntity(sapling);
                sapling.schedule(scheduler, worldModel, imageStore);
            }
            if(!house.get().getPosition().equals(housePos) && worldModel.numberOfEntityTypeAlive(House.class) > 1 ){
                BobaShop boba = Factory.createBobaShop(BobaShop.KEY, housePos, imageStore.getImageList(BobaShop.KEY),BobaShop.ANIMATION_PERIOD);
                worldModel.addEntity(boba);
                boba.schedule(scheduler, worldModel, imageStore);
            }
        }
        scheduler.scheduleEvent(
                this, Factory.createActivityAction(this, worldModel, imageStore),
                actionPeriod);

    }

    @Override
    public boolean doesContain(WorldModel world,Point newPos){
        return world.getOccupancyCell(newPos).getClass() == House.class;
    }
}
