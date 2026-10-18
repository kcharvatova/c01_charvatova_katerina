package model;

public class Line {
    private final Point2D start, end;

    public Line(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;
    }

    public Point2D getStart() {

        return start;
    }

    public Point2D getEnd() {

        return end;
    }

    //metoda pro vytvoreni nove usecky, pokud menime pri modu edit zacatek existujici, da ji novy zacatek a puvodni konec
    public Line withStart(Point2D newStart) {
        return new Line(newStart, end);
    }

    //metoda pro vytvoreni nove usecky, pokud menime pri modu edit konec existujici, da ji novy konec a puvodni zacatek
    public Line withEnd(Point2D newEnd) {
        return new Line(start, newEnd);
    }
}
