public interface Animatable extends Entity{
    double getAnimationPeriod();
    void nextImage();
    default void schedule(EventScheduler eventScheduler, WorldModel world, ImageStore imageStore){
            eventScheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0),
                    getAnimationPeriod());
    }

}
