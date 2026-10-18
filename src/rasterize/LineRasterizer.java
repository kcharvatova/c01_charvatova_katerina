package rasterize;

import raster.Raster;
import model.Line;

public abstract class LineRasterizer {
    protected Raster raster;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public void rasterize(int x1, int y1, int x2, int y2) {
    }

    public void rasterize(Line line){
        rasterize(line.getStart().getX(), line.getStart().getY(), line.getEnd().getX(), line.getEnd().getY());
    }

}
