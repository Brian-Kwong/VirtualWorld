import processing.core.PImage;

import java.util.List;
import java.util.Optional;

/**
 * Represents a background for the 2D world.
 */
public final class Background {
  //private String id;
  private final List<PImage> images;
  private final String type;

  public Background(String type,List<PImage> images) {
    //this.id = id;
    this.images = images;
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public static Optional<PImage> getBackgroundImage(WorldModel world,
                                                    Point pos) {
    if (world.withinBounds(pos)) {
      return Optional.of(
              world.getBackgroundCell(pos).images.get(0));
    } else {
      return Optional.empty();
    }
  }

}

