public interface Executable extends Animatable{

    double getActionPeriod();

    void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    default void schedule(EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());
        Animatable.super.schedule(eventScheduler,world,imageStore);
    }
}


