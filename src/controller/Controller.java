package controller;

import graphic.rasterizer.LineRasterizer;
import graphic.rasterizer.PolygonOutlineRasterizer;
import graphic.rasterizer.PolygonRasterizer;
import graphic.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Point;
import model.Polygon;
import view.Canvas;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles user input and controls the application flow related to the {@link Canvas}.
 * The controller coordinates input events, canvas operations, drawing modes, and rendering updates.
 *
 * @author PGRF FIM UHK
 * @version 2026
 */
public class Controller {
    private Point startPoint;
    private Point currentPoint;
    private Line previewLine;

    private final Canvas canvas;
    private final Polygon polygon;
    private final LineRasterizer rasterizer;
    private final PolygonRasterizer polygonRasterizer;

    private final List<Point> points = new ArrayList<Point>();
    private final List<Line> lines = new ArrayList<Line>();
    private boolean isPolygonMode;
    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
        this.polygon = new Polygon(points);
        this.polygonRasterizer = new PolygonOutlineRasterizer(rasterizer, LINE_COLOR);
        this.isPolygonMode = false;

    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        // Handle mouse press and release events.
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isPolygonMode) {
                    startLine(e);
                } else {
                    startPoint = getPoint(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isPolygonMode) {
                    if (previewLine != null) {
                        currentPoint = getPoint(e);
                        points.add(currentPoint);
                        previewLine = null;
                        render();
                    }
                } else {
                    if (startPoint != null) {
                        currentPoint = getPoint(e);
                        lines.add(new Line(LINE_COLOR, startPoint, currentPoint));
                        previewLine = null;
                        startPoint = null;
                        render();
                    }
                }
            }
        });

        // Update the line preview while dragging the mouse.
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if ((isPolygonMode && previewLine == null) || startPoint == null) {
                    return;
                }
                currentPoint = getPoint(e);
                previewLine = new Line(PREVIEW_COLOR, startPoint, currentPoint);
                render();
            }
        });

        // Handle keyboard input for drawing modes and canvas operations.
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    isPolygonMode = true;
                    previewLine = null;
                    startPoint = null;
                    render();
                }
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    isPolygonMode = false;
                    previewLine = null;
                    startPoint = null;
                    render();
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER && isPolygonMode) {
                    finishPolygon();
                }
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    points.clear();
                    lines.clear();
                    previewLine = null;
                    polygon.setFinished(false);
                    startPoint = null;
                    currentPoint = null;
                    render();
                }
            }
        });
        render();
    }

    /**
     * Redraws all line segments, the polygon, and temporary preview lines.
     * The canvas is cleared before each rendering to prevent drawing trails.
     */
    private void render() {
        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        for (Line line : lines) {
            rasterizer.rasterize(line);
        }
        if (previewLine != null) {
            rasterizer.rasterize(previewLine);
            // Preview the connection between the first vertex and the cursor.
            if (isPolygonMode && !points.isEmpty()) {
                Point firstPoint = points.getFirst();
                currentPoint = previewLine.getPoint2();
                rasterizer.rasterize(new Line(PREVIEW_COLOR, firstPoint, currentPoint));
            }
        }
        canvas.repaint();

    }

    private void startLine(MouseEvent e) {
        if (polygon.isFinished()) {
            return;
        }
        currentPoint = getPoint(e);
        if (!polygon.getPoints().isEmpty()) {
            startPoint = polygon.getPoints().getLast();
        } else {
            startPoint = currentPoint;
            points.add(startPoint);
        }
        previewLine = new Line(PREVIEW_COLOR, startPoint, currentPoint);
        render();
    }

    private void finishPolygon() {
        if (polygon.getPoints().size() > 2) {
            polygon.setFinished(true);
        }
        render();
    }

    private Point getPoint(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }
}