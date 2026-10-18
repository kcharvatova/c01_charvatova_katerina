package controller;

import model.Point2D;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import view.Panel;
import model.Line;

import javax.swing.*;
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

    private Point2D start; //misto kliknuti

    private boolean polygonMode=false; //true=kreslime polygon
    private boolean editMode=false; //true=editujeme

    private int insertIndex; //index, na ktery se v seznamu vlozi novy vrchol
    private int editedIndex=-1; //vrchol, ktery prave upravujeme (-1=zadny)

    private boolean editedLineStart;
    private int editedLine=-1;

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
                //pridani noveho vrcholu na konec seznamu
                if (polygonMode) {
                    insertIndex = polygon.getSize();
                }
                //mazani nejblizsiho bodu v editacnim modu
                if (editMode) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        findClosestPoint(start);
                        if (e.isShiftDown()) {
                            deleteEditedPoint();
                            drawScene(polygon);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point2D end = new Point2D(e.getX(), e.getY());

                if (editMode && SwingUtilities.isRightMouseButton(e)) {
                    //nova poloha upravovaneho bodu
                    if (editedIndex != -1) {
                        polygon.setPoint(editedIndex, end);
                    }
                    //nova poloha konce upravovane usecky
                    else if(editedLine!=-1){
                        moveEditedPoint(end);
                    }
                    editedIndex = -1;
                    editedLine=-1;
                }
                //novy vrchol polygonu
                else if (polygonMode) {
                    polygon.addPoint(insertIndex, end);
                }
                else {
                    lines.add(new Line(start, end));
                }
                drawScene(polygon);
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point2D cursor = new Point2D(e.getX(), e.getY());

                if (editMode && SwingUtilities.isRightMouseButton(e)) {
                    //polygon s presouvanym vrcholem na pozici kurzoru
                    if (editedIndex != -1) {
                        drawScene(polygon.withEditedPoint(editedIndex, cursor));
                    }
                    else if(editedLine!=-1){
                        moveEditedPoint(cursor);
                        drawScene(polygon);
                    }
                }
                else if (polygonMode) {
                    drawPolygonPreview(cursor);
                }
                else {
                    drawScene(polygon, new Line(start, cursor));
                }
            }
        });

        //meneni modu (L:usecky, P:polygon, E=editace, C=vse smazat)
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_L){
                    polygonMode = false;
                    editMode=false;
                }
                if (e.getKeyCode() == KeyEvent.VK_P){
                    polygonMode = true;
                    editMode=false;
                }
                if(e.getKeyCode()==KeyEvent.VK_E){
                    polygonMode=true;
                    editMode=true;
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
        //smaze platno
        panel.getRaster().clear();

        for (Line line : lines) {
            lineRasterizer.rasterize(line);
        }
        //vykresli polygon
        polygonRasterizer.rasterize(polygonToDraw);

        for (Line preview : previews) {
            lineRasterizer.rasterize(preview);
        }

        panel.repaint();
    }

    //nahled pri pridavani vrcholu do polygonu
    private void drawPolygonPreview(Point2D cursor){
        if (polygon.getSize()==0){
            return;
        }

        //kdyz ma polygon jen jeden bod, vykresli se pouze usecka od tohoto bodu ke kurzoru
        if (polygon.getSize()==1){
            drawScene(polygon, new Line(polygon.getPoint(0), cursor));
        }
        else{
            drawScene(polygon.withNewPoint(insertIndex, cursor));
        }
    }

    //odvozeno z pythagorovy vety
    private double distance(Point2D a, Point2D b) {
        return Math.hypot(a.getX() - b.getX(), a.getY() - b.getY());
    }

    private void findClosestPoint(Point2D target) {
        editedIndex = -1;
        editedLine = -1;
        double best = Double.MAX_VALUE; //dosud nejmensi nalezena vzddalenost

        //najde nejblizsi vrchol polygonu
        int polygonIndex = polygon.getClosestPointIndex(target);
        if (polygonIndex != -1) {
            best = distance(polygon.getPoint(polygonIndex), target);
            editedIndex = polygonIndex;
        }

        //projde vsechny vrcholy lines a porovna znovu jejich vzdalenost ke kurzoru
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);

            double distanceToStart = distance(line.getStart(), target);
            if (distanceToStart < best) {
                best = distanceToStart;
                editedIndex = -1; //polygon se neupravuje
                editedLine = i;
                editedLineStart = true; //upravuje se zacatek usecky
            }

            double distanceToEnd = distance(line.getEnd(), target);
            if (distanceToEnd < best) {
                best = distanceToEnd;
                editedIndex = -1; //polygon se neupravuje
                editedLine = i;
                editedLineStart = false; //upravuje se konec usecky
            }
        }
    }

    private void deleteEditedPoint() {
        if (editedIndex != -1) {
            polygon.removePoint(editedIndex);
        } else if (editedLine != -1) {
            lines.remove(editedLine);   // maze se cela
        }
        editedIndex = -1;
        editedLine = -1;
    }

    private void moveEditedPoint(Point2D newPosition) {
        if (editedLine != -1) {
            Line line = lines.get(editedLine);
            if (editedLineStart) {
                lines.set(editedLine, line.withStart(newPosition));
            } else {
                lines.set(editedLine, line.withEnd(newPosition));
            }
        }
    }
}
