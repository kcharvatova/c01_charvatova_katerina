package rasterize;

import model.Point2D;
import model.Polygon;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
        if (polygon.getSize()<2) return;

        for(int i = 0; i < polygon.getSize(); i++) {
            int indexA = i;
            // Pozor, abych nesahal mimo seznam a zráoveˇmn chci spojit poslední point s nultým
            int indexB = (i + 1) % polygon.getSize();
            Point2D a = polygon.getPoint(indexA);
            Point2D b = polygon.getPoint(indexB);

            lineRasterizer.rasterize(a.getX(), a.getY(), b.getX(), b.getY());
        }
    }
}
