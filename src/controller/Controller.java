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
 * The controller coordinates input events, canvas operations, and rendering updates.
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

    private final List<Line> lines = new ArrayList<Line>();
    private final List<Point> points = new ArrayList<Point>();
    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
        this.polygon = new Polygon(points);
        this.polygonRasterizer = new PolygonOutlineRasterizer(rasterizer, LINE_COLOR);

    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        // Obsluha vstupu z myši
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startLine(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (previewLine == null){
                    return;
                }
                if (polygon.isFinished()){
                    previewLine = null;
                    render();
                } else {
                    Point endPoint = getPoint(e);
                    points.add(endPoint);
                    previewLine = null;
                    render();
                }

            }
        });

        // Obsluha vstupu z klávesnice
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (startPoint == null) {
                    return;
                }
                currentPoint = getPoint(e);
                if (previewLine == null){
                    return;
                }
                else {
                    previewLine = new Line(PREVIEW_COLOR, startPoint, currentPoint);
                }
                render();
            }
        });

        // Obsluha vstupu z klávesnice
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    finishPolygon(e);
                }
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    points.clear();
                    previewLine = null;
                    polygon.setFinished(false);
                    render();
                }
            }
        });
        canvas.repaint();
    }
    private void render() {
        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        if (previewLine != null){
            rasterizer.rasterize(previewLine);
        }
        canvas.repaint();

    }
    private void startLine(MouseEvent e) {
        if (polygon.isFinished()){
            return;
        }
        currentPoint = getPoint(e);
        if (!polygon.getPoints().isEmpty()){
            startPoint = polygon.getPoints().getLast();
        }
        else {
            startPoint = currentPoint;
            points.add(startPoint);
        }
        previewLine = new Line(PREVIEW_COLOR, startPoint, currentPoint);
        render();
    }

    private void finishPolygon(KeyEvent e) {
        if (polygon.getPoints().size() > 2) {
            polygon.setFinished(true);
        }
        render();
    }


    private Point getPoint(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }
}