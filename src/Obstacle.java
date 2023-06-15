import processing.core.PImage;

import java.util.List;

public class Obstacle implements Animatable{
    public static final String KEY = "obstacle";
    public static final int NUM_PROPERTIES = 1;
    public static final int ANIMATION_PERIOD = 0;
    private final String id;
    private final double animationPeriod;
    private Point position;
    private final List<PImage> images;
    private int imageIndex = 0;


    public Obstacle(String id, Point position, List<PImage>images,double animationPeriod){
        this.id = id;
        this.position = position;
        this.images = images;
        this.animationPeriod = animationPeriod;
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
        return imageIndex;
    }
    @Override
    public List<PImage> getImages() {
        return images;
    }
    @Override
    public void nextImage(){
        imageIndex++;
    }
    @Override
    public double getAnimationPeriod(){ return animationPeriod;}
}
