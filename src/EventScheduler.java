import java.util.*;

/**
 * Keeps track of events that have been scheduled.
 */
public final class EventScheduler {
  private final PriorityQueue<Event> eventQueue;
  private final Map<Entity, List<Event>> pendingEvents;
  private double currentTime;

  public EventScheduler() {
    Comparator<Event> eventComparator =  (Event lft, Event rht)->(int)(1000 * (lft.getTime() - rht.getTime()));
    this.eventQueue = new PriorityQueue<>(eventComparator);
    this.pendingEvents = new HashMap<>();
    this.currentTime = 0;
  }

  public double getCurrentTime() { return currentTime; }


  public void scheduleEvent(Entity entity, Action action, double afterPeriod) {
    double time = currentTime + afterPeriod;
    Event event = new Event(action, time, entity);
    eventQueue.add(event);
    // update list of pending events for the given entity
    List<Event> pending =
        pendingEvents.getOrDefault(entity, new LinkedList<>());
    pending.add(event);
    pendingEvents.put(entity, pending);
  }

  // Removes all pending events and events scheduled for a given entity
  public void unscheduleAllEvents(Entity entity) {
    List<Event> pending = pendingEvents.remove(entity);
    if (pending != null) {
      for (Event event : pending) {
        eventQueue.remove(event);
      }
    }
  }

  private void removePendingEvent(Event event) {
    List<Event> pending = pendingEvents.get(event.getEntity());
    if (pending != null) {
      pending.remove(event);
    }
  }
  public void updateOnTime(double time) {
    double stopTime = currentTime + time;
    while (!eventQueue.isEmpty() &&
    eventQueue.peek().getTime() <= stopTime) {
      Event next = eventQueue.poll();
      removePendingEvent(next);
      currentTime = next.getTime();
      next.geAction().execute(this);
    }
    currentTime = stopTime;
  }

    // Removes a specific event
}
