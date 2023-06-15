import processing.core.PImage;

import java.util.List;

public class House implements Entity{
    public static final String KEY = "house";
    public static final int NUM_PROPERTIES = 0;
    private final String id;
    private Point position;
    private final List<PImage> images;

    public  House(String id, Point position, List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) { this.position = position; }

    @Override
    public int getImageIndex() {
        return 0;
    }

    @Override
    public List<PImage> getImages() {
        return images;
    }


}
