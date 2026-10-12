package graphic;

import java.awt.*;

/**
 * Defines basic operations for working with a raster image.
 */
public interface Raster {
    int getWidth();

    int getHeight();

    void clear(int color);

    void present(Graphics graphics);

    void setPixel(int x, int y, int color);

}
