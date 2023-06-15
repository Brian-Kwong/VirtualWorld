import java.util.List;
import java.util.function.Predicate;

public interface Movable extends Entity {
    default boolean moveToAdjacent(Entity target, EventScheduler scheduler, WorldModel worldModel){
        worldModel.removeEntity(scheduler, target);
        return true;
    }

    default boolean moveTo(Entity target,
                          EventScheduler scheduler, WorldModel worldModel) {
        if (Functions.adjacent(getPosition(), target.getPosition())) {
            return moveToAdjacent(target,scheduler,worldModel);
        } else {
            Point nextPos = nextPosition(worldModel,target.getPosition());
            if (!getPosition().equals(nextPos)) {
                worldModel.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    boolean doesContain(WorldModel world, Point newPos);
    default boolean doesNotContainProhibitedTiles(WorldModel world, Point newPos){
        return true;
    }

   default Point nextPosition(WorldModel world, Point destPos) {
       Predicate<Point> validPoint = point -> (world.withinBounds(point) && doesNotContainProhibitedTiles(world,point) && (!world.isOccupied(point)||(world.isOccupied(point) && doesContain(world, point))));
       PathingStrategy sp = new AStarPathingStrategy();
       List<Point> path = sp.computePath(getPosition(),destPos,validPoint,Functions::adjacent,PathingStrategy.CARDINAL_NEIGHBORS);
       return (path.size() <1)? getPosition():path.get(0);
   }
}


