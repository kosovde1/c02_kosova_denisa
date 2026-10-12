package graphic.rasterizer;

import model.Line;
import model.Point;
import model.Polygon;

import java.util.List;

/**
 * Rasterizes the polygon outline by connecting consecutive vertices
 * using a line rasterizer.
 */
public class PolygonOutlineRasterizer implements PolygonRasterizer {

    private final LineRasterizer lineRasterizer;
    private final int color;

    public PolygonOutlineRasterizer(LineRasterizer lineRasterizer, int color) {
        this.lineRasterizer = lineRasterizer;
        this.color = color;
    }

    @Override
    public void rasterize(Polygon polygon) {
        List<Point> points = polygon.getPoints();
        if (points.size() >= 2) {
            rasterizeLine(points);
        }

        // Close the completed polygon by connecting the last and first vertices
        if (points.size() > 2 && (polygon.isFinished())) {
            Point first = points.getFirst();
            Point last = points.getLast();
            lineRasterizer.rasterize(new Line(color, last, first));
        }
    }

    private void rasterizeLine(List<Point> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            Point first = points.get(i);
            Point second = points.get(i + 1);
            lineRasterizer.rasterize(new Line(color, first, second));
        }
    }
}
