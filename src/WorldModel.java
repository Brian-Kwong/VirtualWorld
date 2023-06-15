import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of  the background image for each
 * location in the and the entities that populate the
 */
public final class WorldModel {
  public static final int ENTITY_NUM_PROPERTIES = 4;
  public static final int PROPERTY_ROW = 3;
  public static final int PROPERTY_COL = 2;
  public static final int PROPERTY_ID = 1;
  public static final int PROPERTY_KEY = 0;

  private int numRows;
  private int numCols;
  private Background[][] background;
  private Entity[][] occupancy;
  private Set<Entity> entities;

  public WorldModel() {}

  /**
   * Helper method for testing. Don't move or modify this method.
   */
  public List<String> log() {
    List<String> list = new ArrayList<>();
    for (Entity entity : entities) {
      String log = entity.log();
      if (log != null)
        list.add(log);
    }
    return list;
  }

  /* Getters and Setters */
  public int getNumRows() { return numRows; }

  public int getNumCols() { return numCols; }

  public Set<Entity> getEntities() { return entities; }

  public Entity getOccupancyCell(Point pos) { return occupancy[pos.y][pos.x]; }

  public Background getBackgroundCell(Point pos) {
    return this.background[pos.y][pos.x];
  }

  public Background setBackgroundCell(Background background, Point pos) {
    return this.background[pos.y][pos.x] = background;
  }


  public void setBackground(Background[][] background) {
    this.background = background;
  }

  public void setOccupancy(Entity[][] occupancy) { this.occupancy = occupancy; }

  public void setEntities(Set<Entity> entities) { this.entities = entities; }

  public void setNumRows(int numRows) { this.numRows = numRows; }

  public void setNumCols(int numCols) { this.numCols = numCols; }

  public Background[][] getBackground() { return background; }

  private void setOccupancyCell(Point pos, Entity entity) {
    occupancy[pos.y][pos.x] = entity;
  }

  /* Functions to ensure proper location */
  public boolean withinBounds(Point pos) {
    return pos.y >= 0 && pos.y < numRows && pos.x >= 0 && pos.x < numCols;
  }

  public boolean isOccupied(Point pos) {
    return withinBounds(pos) && getOccupancyCell(pos) != null;
  }

  /*
Adds and removes entities
  */

  public void addEntity(Entity entity) {
    if (withinBounds(entity.getPosition())) {
      setOccupancyCell(entity.getPosition(), entity);
      entities.add(entity);
    }
  }

  public void tryAddEntity(Entity entity) {
    if (isOccupied(entity.getPosition())) {
      // arguably the wrong type of exception, but we are not
      // defining our own exceptions yet
      throw new IllegalArgumentException("position occupied");
    }

    addEntity(entity);
  }

  public void removeEntityAt(Point pos) {
    if (withinBounds(pos) && getOccupancyCell(pos) != null) {
      Entity entity = getOccupancyCell(pos);

      /* This moves the entity just outside of the grid for
       * debugging purposes. */
      entity.setPosition(new Point(-1, -1));
      entities.remove(entity);
      setOccupancyCell(pos, null);
    }
  }

  public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
    Point oldPos = entity.getPosition();
    if (withinBounds(pos) && !pos.equals(oldPos)) {
      setOccupancyCell(oldPos, null);
      Optional<Entity> occupant = getOccupant(pos);
      occupant.ifPresent(target -> removeEntity(scheduler, target));
      setOccupancyCell(pos, entity);
      entity.setPosition(pos);
    }
  }

  public void removeEntity(EventScheduler scheduler, Entity entity) {
    scheduler.unscheduleAllEvents(entity);
    removeEntityAt(entity.getPosition());
  }

  private Optional<Entity> getOccupant(Point pos) {
    if (isOccupied(pos)) {
      return Optional.of(getOccupancyCell(pos));
    } else {
      return Optional.empty();
    }
  }

  /* Finds nearest  */
  public Optional<Entity> findNearest(Point pos, Class[] types) {
    List<Entity> ofType = new LinkedList<>();
    for (Entity entity : entities) {
      for(Class c : types ) {
        if (entity.getClass() == c) {
          ofType.add(entity);
        }
      }
    }
    return Functions.nearestEntity(ofType, pos);
  }

  public int numberOfEntityTypeAlive(Class c) {
    int count = 0;
    for (Entity entity : entities) {
      if(entity.getClass() == c && !entity.getPosition().equals(new Point(-1,-1))) {
          count++;
      }
    }
    return count;
  }

  public <T extends Entity> Optional<Entity> findNearest(Point pos, Class<T> types) {
    List<Entity> ofType = new LinkedList<>();
    for (Entity entity : entities) {
        if (entity.getClass() == types){
          ofType.add(entity);
        }
    }
    return Functions.nearestEntity(ofType, pos);
  }

  public void createCrater(Point pressed, ImageStore imageStore, EventScheduler scheduler) {
    Function<Point, List<Point>> createCircle = point -> {
      return Stream.of(new Point(point.x, point.y - 2),
              new Point(point.x - 1, point.y - 1),
              new Point(point.x, point.y - 1),
              new Point(point.x + 1, point.y - 1),
              new Point(point.x - 2, point.y),
              new Point(point.x - 1, point.y),
              new Point(point.x, point.y),
              new Point(point.x + 1, point.y),
              new Point(point.x + 2, point.y),
              new Point(point.x - 1, point.y + 1),
              new Point(point.x, point.y + 1),
              new Point(point.x + 1, point.y + 1),
              new Point(point.x, point.y + 2)).filter(this::withinBounds).toList();
    };
    if (findNearest(pressed, BobaShop.class).isPresent()) {
      List<Point> validPoints = PathingStrategy.CARDINAL_NEIGHBORS_WITH_DIAGONAL.apply(findNearest(pressed, BobaShop.class).get().getPosition()).filter(point -> withinBounds(point) && !isOccupied(point) || getOccupancyCell(point).getClass() == BobaCat.class).toList();
      BobaCat bc = Factory.createBobaCat("bobaCat", validPoints.get(Functions.getIntFromRange(validPoints.size(),0)), BobaCat.ACTION_PERIOD, BobaCat.ANIMATION_PERIOD, imageStore.getImageList("bobacat"));
      addEntity(bc);
      bc.schedule(scheduler, this, imageStore);
    }
    for (Point p : createCircle.apply(pressed)) {
      setBackgroundCell(new Background("stone", imageStore.getImageList("stone")), p);
      getOccupant(p).ifPresent(entity ->{if(entity instanceof Plants || entity instanceof Stump){ removeEntity(scheduler, entity);} else if (entity instanceof Dude) {
        ((Dude)entity).transform(scheduler,imageStore,this);
    }
    });
    }
  }


  public void spawnDudes(ImageStore imageStore, EventScheduler scheduler) {
    if (findNearest(new Point(0, 0), House.class).isPresent()) {
      Dude dude = Factory.createDudeNotFull("", findNearest(new Point(0, 0), House.class).get().getPosition(), 0.787, 0.180, 4, imageStore.getImageList("dude"));
      addEntity(dude);
      dude.schedule(scheduler, this, imageStore);
      if (!dude.doesNotContainProhibitedTiles(this, dude.getPosition())) {
        dude.transform(scheduler, imageStore, this);
      }
    }
  }

  public void load(Scanner saveFile, ImageStore imageStore,
                   Background defaultBackground) {
    Parser.parseSaveFile(this, saveFile, imageStore, defaultBackground);
    if (background == null) {
      background = new Background[numRows][numCols];
      for (Background[] row : background)
        Arrays.fill(row, defaultBackground);
    }
    if (occupancy == null) {
      occupancy = new Entity[numRows][numCols];
      entities = new HashSet<>();
    }
  }

}
