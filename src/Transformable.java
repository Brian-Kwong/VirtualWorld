public interface Transformable extends Executable{
    boolean transform(EventScheduler scheduler, ImageStore imageStore, WorldModel worldModel);

}
