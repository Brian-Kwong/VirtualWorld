import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
@FunctionalInterface
interface PathingStrategy
{
    /*
     * Returns a prefix of a path from the start point to a point within reach
     * of the end point.  This path is only valid ("clear") when returned, but
     * may be invalidated by movement of other entities.
     *
     * The prefix includes neither the start point nor the end point.
     */
    List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach,  Function<Point, Stream<Point>> potentialNeighbors);

    static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x, point.y - 1))
                            .add(new Point(point.x, point.y + 1))
                            .add(new Point(point.x - 1, point.y))
                            .add(new Point(point.x + 1, point.y))
                            .build();

    static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS_WITH_DIAGONAL = (p)->{
                List<Point> listOfNextPossiblePoints = new ArrayList<>();
                for(int x=p.x-1;x<p.x+2;x++){
                    for(int y=p.y-1;y<p.y+2;y++){
                        listOfNextPossiblePoints.add(new Point(x,y));
                    }
                }
                return listOfNextPossiblePoints.stream().filter(point->!point.equals(p));
            };
}