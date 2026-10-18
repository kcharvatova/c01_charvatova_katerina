package rasterize;

import raster.Raster;

public class LineRasterizerTrivial extends LineRasterizer {

    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {

        //zmena mezi souradnicemi zacatecniho a koncoveho bodu
        int deltaX=x2-x1;
        int deltaY=y2-y1;

        //podle toho, ktera zmena je vetsi, se vybira ridici usecka
        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            //kontrola a pripadne prohozeni, ktere zajistuje, ze v promenne x1 se nachazi mensi cislo (a s nim i prohozeni y)
            if (x1 > x2) {
                int tmpX = x1;
                x1 = x2;
                x2 = tmpX;

                int tmpY = y1;
                y1 = y2;
                y2 = tmpY;
            }
            //promenne potrebne pro trivialni algoritmus
            float k = (y2 - y1) / (float) (x2 - x1);
            float q = y1 - k * x1;

            //trivialni algoritmus
            for (int x = x1; x <= x2; x++) {
                int y = Math.round(k * x + q);
                raster.setPixel(x, y, 0xff0000);
            }
        } else {
            //kontrola a pripadne prohozeni, ktere zajistuje, ze v promenne y1 se nachazi mensi cislo (a s nim i prohozeni x)
            if (y1 > y2) {
                int tmpX = x1;
                x1 = x2;
                x2 = tmpX;

                int tmpY = y1;
                y1 = y2;
                y2 = tmpY;
            }

            //promenne potrebne pro trivialni algoritmus
            float k = (x2 - x1) / (float) (y2 - y1);
            float q = x1 - k * y1;

            //trivialni algoritmus
            for (int y = y1; y <= y2; y++) {
                int x = Math.round(k * y + q);
                raster.setPixel(x, y, 0xff0000);
            }
        }
    }
}
