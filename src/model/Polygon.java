package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    //seznam bodu v poradi v jakem byly pridany
    private final List<Point2D> points;

    public Polygon() {

        points = new ArrayList<>();
    }

    //prida bod na konec seznamu
    public void addPoint(Point2D point){
        points.add(point);
    }

    //prida bod na urceny index, pouziva se pro pridavani bodu na nejblizsi hranu v modu E
    public void addPoint(int index, Point2D point) {
        points.add(index, point);
    }

    //nahradi bod na konkretnim indexu jinym bodem,pouziva se pro upravovani polohy jednotlivych vrcholu
    public void setPoint(int index, Point2D point){
        points.set(index, point);
    }

    public Point2D getPoint(int i) {
        return points.get(i);
    }

    public int getSize() {
        return points.size();
    }

    //metoda pouzivana pri pridavani noveho bodu do polygonu, vrati kopii polygonu puvodniho+novy vkladany, ale neprepise polygon puvodni, pouziva se, abychom videli, jak bude polygon po pridani bodu vypadat
    public Polygon withNewPoint(int index, Point2D point) {
        Polygon copy = new Polygon();
        for (Point2D p : points) {
            copy.addPoint(p);
        }
        copy.addPoint(index, point);
        return copy;
    }

    //metoda pouzivana pri editaci polohy bodu v polygonu, aby uzivatel videl, jak bude polygon po uprave vypadat, vraci kopii polygonu puvodniho a nakonec nahradi upravovany bod bodem novym
    public Polygon withEditedPoint(int index, Point2D point){
        Polygon copy=new Polygon();
        for (Point2D p:points){
            copy.addPoint(p);
        }
        copy.setPoint(index, point);
        return copy;
    }

    //hleda nejblizsi bod od kliknuti a vraci index tohoto bodu
    public int getClosestPointIndex(Point2D target) {

        //pokud je polygon prazdny, vraci se -1
        int best = -1;
        //nejvyssi hodnota, aby ho hned nahradil prvni bod v cyklu
        double bestDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            //odvozeno z pythagorovy veta
            double distance = Math.hypot(p.getX() - target.getX(), p.getY() - target.getY());
            if (distance < bestDistance) {
                bestDistance = distance;
                best = i;
            }
        }
        return best;
    }




    //metoda pro mazani vrcholu
    public void removePoint(int index){
        points.remove(index);
    }

    //smaze body ze seznamu
    public void clear(){
        points.clear();
    }
}
