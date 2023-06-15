import java.util.Optional;
import processing.core.PApplet;
import processing.core.PImage;

public final class WorldView {
  private PApplet screen;
  private WorldModel world;
  private int tileWidth;
  private int tileHeight;
  private Viewport viewport;

  public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
                   int tileWidth, int tileHeight) {
    this.screen = screen;
    this.world = world;
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
    this.viewport = new Viewport(numRows, numCols);
  }

  public WorldModel getWorldl() { return world; }

  public Viewport getViewPort() { return viewport; }

  public void drawViewport() {
    drawBackground();
    drawEntities();
  }

  private void drawEntities() {
    for (Entity entity : world.getEntities()) {
      Point pos = entity.getPosition();

      if (viewport.contains(pos)) {
        Point viewPoint = viewport.worldToViewport( pos.x, pos.y);
        screen.image(entity.getCurrentImage(), viewPoint.x * tileWidth,
                     viewPoint.y * tileHeight);
      }
    }
  }

  private void drawBackground() {
    for (int row = 0; row < viewport.getNumRows(); row++) {
      for (int col = 0; col < viewport.getNumCols(); col++) {
        Point worldPoint = viewport.viewportToWorld( col, row);
        Optional<PImage> image =
            Background.getBackgroundImage(world, worldPoint);
        if (image.isPresent()) {
          screen.image(image.get(), col * tileWidth, row * tileHeight);
        }
      }
    }
  }
}
