import processing.core.PImage;

import java.util.List;

public class Stump implements Entity{
    public static final String KEY = "stump";
    public static final int STUMP_NUM_PROPERTIES = 0;

    private final String id;
    private Point position;
    private final List<PImage> images;

    public  Stump(String id, Point position, List<PImage>images){
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
    @Override
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
