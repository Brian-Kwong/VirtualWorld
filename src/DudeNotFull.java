import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude{
    private int resourceCount;


    public DudeNotFull(String id,Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod) {
        super(id,position,images,resourceLimit,actionPeriod,animationPeriod,false);
    }

    @Override
    public boolean transform(EventScheduler scheduler,
                                     ImageStore imageStore, WorldModel worldModel) {
        if (resourceCount >= resourceLimit || worldModel.getBackgroundCell(getPosition()).getType().equals("stone")) {
            super.transform(scheduler,imageStore,worldModel);
            return true;
        }
        return false;
    }

    @Override
    public void execute(WorldModel world,
                                           ImageStore imageStore,
                                           EventScheduler scheduler) {
        Class[] type = {Sapling.class,Tree.class};
        Optional<Entity> target = world.findNearest(getPosition(), type);
        if (target.isEmpty() ||
                !moveTo(target.get(), scheduler, world) ||
                !transform(scheduler, imageStore, world)) {
            scheduler.scheduleEvent(
                    this, new Activity(this, world, imageStore),
                    getActionPeriod());
        }
    }

    public Dude helperDudeTransform(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler){
        scheduler.unscheduleAllEvents(this);
        return Factory.createDudeFull(getID(), getPosition(), getActionPeriod(), getAnimationPeriod(), resourceLimit, getImages());
    }

    @Override
    public boolean moveToAdjacent(Entity target, EventScheduler scheduler, WorldModel worldModel) {
        Plants plant = (Plants) target;
        resourceCount += 1;
        plant.decreaseHealth();
        return true;
    }
}
