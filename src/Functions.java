import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions {
  public static final int COLOR_MASK = 0xffffff;
  public static final int KEYED_IMAGE_MIN = 5;
  private static final int KEYED_RED_IDX = 2;
  private static final int KEYED_GREEN_IDX = 3;
  private static final int KEYED_BLUE_IDX = 4;


  public static boolean adjacent(Point p1, Point p2) {
    return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
        (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
  }

  public static int getIntFromRange(int max, int min) {
    Random rand = new Random();
    return (max < 1)?0:min + rand.nextInt(max - min);
  }

  // Random number generator
  public static double getNumFromRange(double max, double min) {
    Random rand = new Random();
    return min + rand.nextDouble() * (max - min);
  }

  // Util function that gets a list of entities and determines the cloest one
  public static Optional<Entity> nearestEntity(List<Entity> entities,
                                               Point pos) {
    if (entities.isEmpty()) {
      return Optional.empty();
    } else {
      Entity nearest = entities.get(0);
      int nearestDistance = nearest.getPosition().distanceSquared(pos);

      for (Entity other : entities) {
        int otherDistance = other.getPosition().distanceSquared(pos);

        if (otherDistance < nearestDistance) {
          nearest = other;
          nearestDistance = otherDistance;
        }
      }
      return Optional.of(nearest);
    }
  }

  public static void processImageLine(Map<String, List<PImage>> images,
                                      String line, PApplet screen) {
    String[] attrs = line.split("\\s");
    if (attrs.length >= 2) {
      String key = attrs[0];
      PImage img = screen.loadImage(attrs[1]);
      if (img != null && img.width != -1) {
        List<PImage> imgs = getImages(images, key);
        imgs.add(img);

        if (attrs.length >= KEYED_IMAGE_MIN) {
          int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
          int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
          int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
          setAlpha(img, screen.color(r, g, b), 0);
        }
      }
    }
  }

  public static List<PImage> getImages(Map<String, List<PImage>> images,
                                       String key) {
    return images.computeIfAbsent(key, k -> new LinkedList<>());
  }

  /*
    Called with color for which alpha should be set and alpha value.
    setAlpha(img, color(255, 255, 255), 0));
  */
  public static void setAlpha(PImage img, int maskColor, int alpha) {
    int alphaValue = alpha << 24;
    int nonAlpha = maskColor & COLOR_MASK;
    img.format = PApplet.ARGB;
    img.loadPixels();
    for (int i = 0; i < img.pixels.length; i++) {
      if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
        img.pixels[i] = alphaValue | nonAlpha;
      }
    }
    img.updatePixels();
  }

  public static void loadImages(Scanner in, ImageStore imageStore,
                                PApplet screen) {
    int lineNumber = 0;
    while (in.hasNextLine()) {
      try {
        processImageLine(imageStore.getAllImages(), in.nextLine(), screen);
      } catch (NumberFormatException e) {
        System.out.printf("Image format error on line %d\n", lineNumber);
      }
      lineNumber++;
    }
  }

}
