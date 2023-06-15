import java.util.HashSet;
import java.util.Scanner;

public class Parser {
    // Parsing Functions
    public static void parseSapling(WorldModel world, String[] properties,
                                    Point pt, String id, ImageStore imageStore) {
      if (properties.length == Sapling.NUM_PROPERTIES) {
        int health = Integer.parseInt(properties[Sapling.HEALTH]);
        Entity sapling = Factory.createSapling(
            id, pt, imageStore.getImageList(Sapling.KEY), health);
        world.tryAddEntity(sapling);
      } else {
        throw new IllegalArgumentException(
            String.format("%s requires %d properties when parsing",
                          Sapling.KEY, Sapling.NUM_PROPERTIES));
      }
    }


    public static void parseDude(WorldModel world, String[] properties, Point pt,
                                   String id, ImageStore imageStore) {
      if (properties.length == DudeFull.NUM_PROPERTIES) {
        Entity dude = Factory.createDudeNotFull(
            id, pt, Double.parseDouble(properties[DudeFull.ACTION_PERIOD]),
            Double.parseDouble(properties[DudeFull.ANIMATION_PERIOD]),
            Integer.parseInt(properties[DudeFull.LIMIT]),
            imageStore.getImageList(DudeNotFull.KEY));
        world.tryAddEntity(dude);
      } else {
        throw new IllegalArgumentException(
            String.format("%s requires %d properties when parsing",
                          DudeFull.KEY, DudeFull.NUM_PROPERTIES));
      }
    }

    public static void parseObstacle(WorldModel world, String[] properties,
                                       Point pt, String id, ImageStore imageStore) {
      if (properties.length == Obstacle.NUM_PROPERTIES) {
        Entity obstacle = Factory.createObstacle(
            id, pt,
            Double.parseDouble(properties[Obstacle.ANIMATION_PERIOD]),
            imageStore.getImageList(Obstacle.KEY));
        world.tryAddEntity(obstacle);
      } else {
        throw new IllegalArgumentException(String.format(
            "%s requires %d properties when parsing", Obstacle.KEY,
            Obstacle.NUM_PROPERTIES));
      }
    }

    public static void parseFairy(WorldModel world, String[] properties, Point pt,
                                    String id, ImageStore imageStore) {
      if (properties.length == Fairy.NUM_PROPERTIES) {
        Entity fairy = Factory.createFairy(
            id, pt,
            Double.parseDouble(properties[Fairy.ACTION_PERIOD]),
            Double.parseDouble(properties[Fairy.ANIMATION_PERIOD]),
            imageStore.getImageList(Fairy.KEY));
        world.tryAddEntity(fairy);
      } else {
        throw new IllegalArgumentException(
            String.format("%s requires %d properties when parsing",
                          Fairy.KEY, Fairy.NUM_PROPERTIES));
      }
    }

    public static void parseTree(WorldModel world, String[] properties, Point pt,
                                   String id, ImageStore imageStore) {
      if (properties.length == Tree.NUM_PROPERTIES) {
        Entity tree = Factory.createTree(
            id, pt, Double.parseDouble(properties[Tree.ACTION_PERIOD]),
            Double.parseDouble(properties[Tree.ANIMATION_PERIOD]),
            Integer.parseInt(properties[Tree.HEALTH]),
            imageStore.getImageList(Tree.KEY));
        world.tryAddEntity(tree);
      } else {
        throw new IllegalArgumentException(
            String.format("%s requires %d properties when parsing",
                          Tree.KEY, Tree.NUM_PROPERTIES));
      }
    }

    public static void parseHouse(WorldModel world, String[] properties, Point pt,
                                    String id, ImageStore imageStore) {
      if (properties.length == House.NUM_PROPERTIES) {
        Entity entity = Factory.createHouse(
            id, pt, imageStore.getImageList(House.KEY));
        world.tryAddEntity(entity);
      } else {
        throw new IllegalArgumentException(
            String.format("%s requires %d properties when parsing",
                          House.KEY, House.NUM_PROPERTIES));
      }
    }

    public static void parseStump(WorldModel world, String[] properties, Point pt,
                                    String id, ImageStore imageStore) {
      if (properties.length == Stump.STUMP_NUM_PROPERTIES) {
        Entity stump = Factory.createStump(
            id, pt, imageStore.getImageList(Stump.KEY));
        world.tryAddEntity(stump);
      } else {
        throw new IllegalArgumentException(
            String.format("%s requires %d properties when parsing",
                          Stump.KEY, Stump.STUMP_NUM_PROPERTIES));
      }
    }

    public static void parseEntity(WorldModel world, String line,
                                     ImageStore imageStore) {
      String[] properties = line.split(" ", WorldModel.ENTITY_NUM_PROPERTIES + 1);
      if (properties.length >= WorldModel.ENTITY_NUM_PROPERTIES) {
        String key = properties[WorldModel.PROPERTY_KEY];
        String id = properties[WorldModel.PROPERTY_ID];
        Point pt =
            new Point(Integer.parseInt(properties[WorldModel.PROPERTY_COL]),
                      Integer.parseInt(properties[WorldModel.PROPERTY_ROW]));

        properties =
            properties.length == WorldModel.ENTITY_NUM_PROPERTIES
                ? new String[0]
                : properties[WorldModel.ENTITY_NUM_PROPERTIES].split(" ");

        switch (key) {
            case Obstacle.KEY -> parseObstacle(world,properties, pt, id, imageStore);
            case DudeNotFull.KEY -> parseDude(world, properties, pt, id, imageStore);
            case Fairy.KEY -> parseFairy(world,properties, pt, id, imageStore);
            case House.KEY -> parseHouse(world,properties, pt, id, imageStore);
            case Tree.KEY -> parseTree(world,properties, pt, id, imageStore);
            case Sapling.KEY -> parseSapling(world,properties, pt, id, imageStore);
            case Stump.KEY -> parseStump(world,properties, pt, id, imageStore);
                  default -> throw new IllegalArgumentException("Entity key is unknown");
              }
          }else{
              throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
          }
      }

    public static void parseSaveFile(WorldModel world, Scanner saveFile, ImageStore imageStore, Background defaultBackground){
          String lastHeader = "";
          int headerLine = 0;
          int lineCounter = 0;
          while(saveFile.hasNextLine()){
              lineCounter++;
              String line = saveFile.nextLine().strip();
              if(line.endsWith(":")){
                  headerLine = lineCounter;
                  lastHeader = line;
                  switch (line){
                      case "Backgrounds:" ->world.setBackground(new Background[world.getNumRows()][world.getNumCols()]);
                      case "Entities:" -> {
                          world.setOccupancy(new Entity[world.getNumRows()][world.getNumCols()]);
                          world.setEntities(new HashSet<>());
                      }
                  }
              }else{
                  switch (lastHeader){
                      case "Rows:" -> world.setNumRows( Integer.parseInt(line));
                      case "Cols:" -> world.setNumCols( Integer.parseInt(line));
                      case "Backgrounds:" -> parseBackgroundRow(world, line, lineCounter-headerLine-1, imageStore);
                      case "Entities:" ->  parseEntity(world,line, imageStore);
                  }
              }
          }
      }

    public static void parseBackgroundRow(WorldModel world, String line, int row, ImageStore imageStore) {
          String[] cells = line.split(" ");
          if(row < world.getNumRows()){
              int rows = Math.min(cells.length, world.getNumCols());
              for (int col = 0; col < rows; col++){
                  //world.getBackground()[row][col] = new Background(cells[col], imageStore.getImageList(cells[col]));
                  world.getBackground()[row][col] = new Background(cells[col],imageStore.getImageList(cells[col]));
              }
          }
      }
}
