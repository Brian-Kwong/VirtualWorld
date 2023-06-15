/**
 * An action that can be taken by an entity
 */
public interface Action {
  public void execute(EventScheduler scheduler);
}
