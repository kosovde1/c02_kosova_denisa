package graphic.rasterizer;

import model.Polygon;

/**
 * Defines an interface for polygon rasterization algorithms.
 */
public interface PolygonRasterizer {
    public void rasterize(Polygon polygon);
}
