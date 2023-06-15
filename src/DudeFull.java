import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude{

    public DudeFull(String id, Point position, List<PImage>  images, int resourceLimit, double actionPeriod, double animationPeriod){
        super(id,position,images,resourceLimit,actionPeriod,animationPeriod,true);
    }
@Override
    public void execute(WorldModel world, ImageStore imageStore,
                                        EventScheduler scheduler) {
        Class[] type = {House.class};
        Optional<Entity> fullTarget =
                world.findNearest(getPosition(),type);


        if (fullTarget.isPresent() &&
                moveTo(fullTarget.get(), scheduler, world)) {
            transform(scheduler, imageStore, world);
        } else {
            scheduler.scheduleEvent(
                    this, new Activity( this, world, imageStore),
                    getActionPeriod());
        }
    }

    public Dude helperDudeTransform(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler){
        return Factory.createDudeNotFull(getID(), getPosition(), getActionPeriod(), getAnimationPeriod(), resourceLimit, getImages());
    }

    @Override
    public boolean moveToAdjacent(Entity target, EventScheduler scheduler, WorldModel worldModel) {
        return true;
    }
}
