import processing.core.PImage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ImageStore {
  private final Map<String, List<PImage>> images;
  private final List<PImage> defaultImages;

  public ImageStore(PImage defaultImage) {
    this.images = new HashMap<>();
    defaultImages = new LinkedList<>();
    defaultImages.add(defaultImage);
  }

  public List<PImage> getImageList(String key) {
    return images.getOrDefault(key, defaultImages);
  }

  /*Getter */
  public Map<String, List<PImage>> getAllImages() { return images; }
}
