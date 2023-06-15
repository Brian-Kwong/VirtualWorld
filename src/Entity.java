import processing.core.PImage;

import java.util.List;

//*
// * An entity that exists in the world. See EntityKind for the
// * different kinds of entities that exist.
public interface
Entity {
  String getID();
  Point getPosition();
  void setPosition(Point position);
  int getImageIndex();

  List<PImage> getImages();

  default PImage getCurrentImage() {
        List<PImage> images = getImages();
        return images.get(getImageIndex() % images.size());
    }

   default String  log() {
    return getID().isEmpty()
            ? null
            : String.format("%s %d %d %d", getID(), getPosition().x,
            getPosition().y, getImageIndex());
  }

}
































