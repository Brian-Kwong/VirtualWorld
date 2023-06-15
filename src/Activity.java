public class Activity implements Action{
    private final Executable entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    public Activity(Executable entity,WorldModel world,ImageStore imageStore){
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    public void execute(EventScheduler scheduler) {
            entity.execute(world,imageStore,scheduler);
        }
    }
