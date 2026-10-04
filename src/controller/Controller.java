package controller;

import graphic.rasterizer.LineRasterizer;
import graphic.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Point;
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

    private final Canvas canvas;
    private final LineRasterizer rasterizer;

    private final List<Line> lines = new ArrayList<Line>();
    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = getPoint(e);
                currentPoint = startPoint;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                lines.add(new Line(LINE_COLOR, startPoint, getPoint(e)));
                currentPoint = null;
                startPoint =  null;
                render();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (startPoint == null) {
                    return;
                }
                currentPoint = getPoint(e);
                render();
            }
        });
        canvas.repaint();


    }
    private void render() {
        canvas.clear();
        for (Line line : lines) {
            rasterizer.rasterize(line);
        }
        if (startPoint != null) {
            rasterizer.rasterize(new Line(PREVIEW_COLOR, startPoint, currentPoint));
        }
        canvas.repaint();
    }

    private Point getPoint(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }
}