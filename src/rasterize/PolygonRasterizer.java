package rasterize;

import model.Point2D;
import model.Polygon;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
        //podminka je uz pro dva vrcholy, usecka se pak vykresli dvakrat pres sebe, ale vadilo mi, ze dokud nezadam treti bod, nemam predstavu, jak bude polygon vypadat
        if (polygon.getSize()<2) return;

        //cyklus pro vykresleni polygonu
        for(int i = 0; i < polygon.getSize(); i++) {
            int indexA = i;
            //pokud je to posledni bod, indexB bude 0 a posledni vrchol se propoji s bodem prvnim, zajistuje uzavrenost polygonu
            int indexB = (i + 1) % polygon.getSize();
            Point2D a = polygon.getPoint(indexA);
            Point2D b = polygon.getPoint(indexB);

            lineRasterizer.rasterize(a.getX(), a.getY(), b.getX(), b.getY());
        }
    }
}
