package graphic.rasterizer;

import graphic.Raster;
import graphic.RasterImage;
import model.Line;
import model.Point;

public class TrivialLineRasterizer implements LineRasterizer{

    private Raster raster;

    public TrivialLineRasterizer(Raster raster){
        this.raster = raster;
    }

    @Override
    public void rasterize (Line line) {
        rasterize(
                line.getPoint1().getX(),
                line.getPoint1().getY(),
                line.getPoint2().getX(),
                line.getPoint2().getY(),
                line.getColor()
        );
    }
   private void rasterize (int x1, int y1, int x2, int y2, int color) {
        // trivialni algoritmus pro rasterizaci usecky
       int deltaX = Math.abs(x2 - x1);
       int deltaY = Math.abs(y2 - y1);
       boolean dominantX = deltaX >= deltaY;
       boolean dominantY = deltaY > deltaX;
       // vykresleni vertikalni usecky
       if (x1 == x2) {
           if (y1 > y2){
               rasterize (x2, y2, x1, y1, color);
               return;
           }
           else{
               for (int y = y1; y <= y2; y++) {
                   raster.setPixel(x1, y, color);
               }
           }
       }
       else {
           float k = (y2 - y1) / (float) (x2 - x1);
           float q = y1 - k * x1;
           if (dominantX) {
               if (x1 > x2) {
                   rasterize (x2, y2, x1, y1, color);
                   return;
               }
               else  {
                   for (int x = x1; x <= x2; x++) {
                       float y = k * x + q;
                       raster.setPixel(x, Math.round(y), color);
                   }
               }
           }
           else {
               if (y1 > y2) {
                   rasterize (x2, y2, x1, y1, color);
                   return;
               }
               else  {
                   for (int y = y1; y <= y2; y++) {
                       float x = (y - q) / k;
                       raster.setPixel(Math.round(x), y, color);
                   }
               }
           }
       }
    }
}

