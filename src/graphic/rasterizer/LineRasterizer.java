package graphic.rasterizer;

import model.Line;

/**
 * Provides a base for line rasterization algorithms.
 */
public interface LineRasterizer {
    public void rasterize(Line line);
}
