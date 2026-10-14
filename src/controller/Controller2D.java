package controller;

import model.Point2D;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import view.Panel;
import model.Line;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller2D {
    private final Panel panel;

    // Rasterizers
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;

    private Polygon polygon;
    private final List<Line> lines=new ArrayList<>();
    private Point2D start;
    private boolean polygonMode=false;

    public Controller2D(Panel panel) {
        this.panel = panel;

        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        polygon = new Polygon();

        initListeners();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                start = new Point2D(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point2D end = new Point2D(e.getX(), e.getY());
                if (polygonMode) {
                    polygon.addPoint(end);
                } else {
                    lines.add(new Line(start, end));
                }
                drawScene(polygon);
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point2D cursor = new Point2D(e.getX(), e.getY());
                if (polygonMode) {
                    drawPolygonPreview(cursor);
                }
                else {
                    drawScene(polygon, new Line(start, cursor));
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_L){
                    polygonMode = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_P){
                    polygonMode = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    lines.clear();
                    polygon.clear();
                    drawScene(polygon);
                }
            }
        });
    }

    private void drawScene(Polygon polygonToDraw, Line... previews) {
        panel.getRaster().clear();

        for (Line line : lines) {
            lineRasterizer.rasterize(line);
        }

        polygonRasterizer.rasterize(polygonToDraw);

        for (Line preview : previews) {
            lineRasterizer.rasterize(preview);
        }

        panel.repaint();
    }

    private void drawPolygonPreview(Point2D cursor){
        if (polygon.getSize()==0){
            return;
        }

        if (polygon.getSize()==1){
            drawScene(polygon, new Line(polygon.getPoint(0), cursor));
        }
        else{
            drawScene(polygon.withPoint(cursor));
        }
    }
}
