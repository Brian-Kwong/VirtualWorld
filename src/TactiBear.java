import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class TactiBear extends DudeNotFull{

   @Override
   public boolean doesNotContainProhibitedTiles(WorldModel world, Point newPos) {
      return true;
   }

    public TactiBear(String id, Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod) {
        super(id,position,images,resourceLimit,actionPeriod,animationPeriod);
    }


    @Override
    public boolean doesContain(WorldModel world, Point newPos) {
        return world.getOccupancyCell(newPos).getClass() == Stump.class;
    }


    @Override
    public void execute(WorldModel world,
                                           ImageStore imageStore,
                                           EventScheduler scheduler) {
        Class[] type = {BobaCat.class};
        Optional<Entity> bearTarget = world.findNearest(getPosition(), type);
        if (bearTarget.isPresent()){
            Point tgtPos = bearTarget.get().getPosition();
            moveTo(bearTarget.get(), scheduler, world);
            scheduler.scheduleEvent(
                    this, Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
        else{
                super.execute(world, imageStore, scheduler);
            }
    }

    @Override
    public boolean moveToAdjacent(Entity target, EventScheduler scheduler, WorldModel worldModel) {
        if (target instanceof Plants) {
            super.moveToAdjacent(target, scheduler, worldModel);
        } else {
            worldModel.removeEntity(scheduler, target);
        }
        return true;
    }

    @Override
    public Dude helperDudeTransform(WorldModel worldModel, ImageStore imageStore, EventScheduler scheduler){
        scheduler.unscheduleAllEvents(this);
        return Factory.createTactiBear(getID(), getPosition(),resourceLimit, getActionPeriod(), getAnimationPeriod(), getImages());
    }
}
