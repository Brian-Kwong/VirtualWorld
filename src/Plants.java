public interface Plants extends Transformable {
    //Health --
    void decreaseHealth();
    int getHealth();

    @Override
    default boolean transform(EventScheduler scheduler,
                             ImageStore imageStore, WorldModel worldModel) {
        if (getHealth()<=0){
            Entity stump = new Stump(Stump.KEY + "_" + getID(), getPosition(), imageStore.getImageList(Stump.KEY));
            worldModel.removeEntity(scheduler, this);
            worldModel.addEntity(stump);
            return true;
        }
        return false;
    }
}
