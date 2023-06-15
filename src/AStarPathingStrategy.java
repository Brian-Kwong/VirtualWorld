import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class  AStarPathingStrategy
        implements PathingStrategy
{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<Point>();
        PriorityQueue<Node> toBeSearchedQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getDistanceTotal).thenComparingInt(Node::getDistanceTo));
        Map<Point, Node> toBeSearchedMap = new HashMap<>();
        Set<Point> searched = new HashSet<>();
        boolean found = false;
        Node startNode = new Node(start, 0, end, null);
        toBeSearchedQueue.add(startNode);
        toBeSearchedMap.put(start, startNode);
        Node n = null;
        while (!toBeSearchedQueue.isEmpty()) {
            n = toBeSearchedQueue.remove();
            toBeSearchedMap.remove(n.getP());
            if (withinReach.test(n.getP(), end)) {
                found = true;
                break;
            }
            for (Point p : potentialNeighbors.apply(n.getP()).filter(canPassThrough).filter(p -> !searched.contains(p)).toList()) {
                if (!toBeSearchedMap.containsKey(p)) {
                    Node nextNode = new Node(p, n.getDistanceFrom() + 1, end, n);
                    toBeSearchedQueue.add(nextNode);
                    toBeSearchedMap.put(p, nextNode);
                } else if ((n.getDistanceFrom() + 1) < toBeSearchedMap.get(p).getDistanceFrom()) {
                    toBeSearchedMap.get(p).updateDistanceFrom(n.getDistanceFrom() + 1, n);
                }
            }
            searched.add(n.getP());
        }
        while (found && n.getP() != start) {
            path.add(0, n.getP());
            n = n.getPrev();
        }
        return path;
    }




    private static class Node{
        private final Point p;
        private Integer distanceFrom;
        private final Integer distanceTo;
        private Integer distanceTotal = -1;
        private Node prev;

        public Node(Point p, int distanceFrom, Point goal,Node prev){
            this.p = p;
            this.distanceFrom = distanceFrom;
            distanceTo=calculateDistance(p,goal);
            this.prev=prev;
            updateTotal();
        }
        private void updateTotal(){
            distanceTotal=distanceFrom+distanceTo;
        }

        public void updateDistanceFrom(Integer newDistance,Node from){
            distanceFrom=newDistance;
            prev=from;
            updateTotal();
        }

        public Integer getDistanceTotal() {
            return distanceTotal;
        }

        public Point getP() {
            return p;
        }

        public Integer getDistanceFrom() {
            return distanceFrom;
        }


        public static Integer calculateDistance(Point p1, Point p2){
            return Math.abs(p1.x- p2.x)+Math.abs(p1.y-p2.y);
        }

        public Node getPrev() {
            return prev;
        }
        public Integer getDistanceTo(){return distanceTo;}

        @Override
        public boolean equals(Object o) {
            if(o!= null && o.getClass() == this.getClass()){
                Node n = (Node) o;
                return this.p.equals(n.getP());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(p);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "p=" + p +
                    ", distanceFrom=" + distanceFrom +
                    ", distanceTo=" + distanceTo +
                    ", distanceTotal=" + distanceTotal +
                    '}';
        }
    }
}
