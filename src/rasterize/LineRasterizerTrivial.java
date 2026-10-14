package rasterize;

import raster.Raster;

public class LineRasterizerTrivial extends LineRasterizer {

    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {

        int deltaX=x2-x1;
        int deltaY=y2-y1;

        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            if (x1 > x2) {
                int tmpX = x1; x1 = x2; x2 = tmpX;
                int tmpY = y1; y1 = y2; y2 = tmpY;
            }
            float k = (y2 - y1) / (float) (x2 - x1);
            float q = y1 - k * x1;

            for (int x = x1; x <= x2; x++) {
                int y = Math.round(k * x + q);
                raster.setPixel(x, y, 0xff0000);
            }
        } else {
            if (y1 > y2) {
                int tmpX = x1;
                x1 = x2;
                x2 = tmpX;
                int tmpY = y1;
                y1 = y2;
                y2 = tmpY;
            }
            float k = (x2 - x1) / (float) (y2 - y1);
            float q = x1 - k * y1;

            for (int y = y1; y <= y2; y++) {
                int x = Math.round(k * y + q);
                raster.setPixel(x, y, 0xff0000);
            }
        }
    }
}
