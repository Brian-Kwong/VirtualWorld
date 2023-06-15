public class Animation implements Action{
    private final Animatable entity;
    private final int repeatCount;
    public Animation(Animatable entity, int repeatCount){
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void execute(EventScheduler scheduler) {
        entity.nextImage();
        if (repeatCount != 1) {
            scheduler.scheduleEvent(
                    entity,
                    Factory.createAnimationAction(entity, Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }
}
