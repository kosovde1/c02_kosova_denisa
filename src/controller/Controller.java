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

    private final Canvas canvas;
    private final Polygon polygon;
    private final LineRasterizer rasterizer;
    private final PolygonRasterizer polygonRasterizer;

    private final List<Line> lines = new ArrayList<Line>();
    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
        this.polygon = new Polygon();
        this.polygonRasterizer = new PolygonOutlineRasterizer(rasterizer);


        lines.add(new Line(Color.WHITE.getRGB(), new Point(100, 100), new Point(300, 100)));
        lines.add(new Line(Color.WHITE.getRGB(), new Point(300, 100), new Point(350, 250)));
        lines.add(new Line(Color.WHITE.getRGB(), new Point(350, 250), new Point(200, 350)));
        lines.add(new Line(Color.WHITE.getRGB(), new Point(200, 350), new Point(100, 250)));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();
        polygonRasterizer.rasterize(new Polygon(lines));
        //obsluha vstupu mysi
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // TODO: Vzít aktualni pozici mysi -> pri stisknuti -> pridat novy bod do polygonu

                startPoint = getPoint(e);
                currentPoint = startPoint;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO: Vzít aktualni pozici mysi -> pri uvolneni leveho tlacitka -> dokoncit kresleni polygonu
                // pri pusteni leveho tlacitka zmizi preview cara
                lines.add(new Line(LINE_COLOR, startPoint, getPoint(e)));
                currentPoint = null;
                startPoint =  null;
                render();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO: Aktualizovat nahled preview usecky
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

    private void startLine(MouseEvent e) {
        // TODO: preview line
    }
    private void finishPolygon(){
        render();
    }
    private void render2(){
        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        canvas.repaint();

    }

    private Point getPoint(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }
}