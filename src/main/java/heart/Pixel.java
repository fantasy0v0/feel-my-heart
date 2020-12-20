package heart;

import javafx.scene.paint.Color;

public class Pixel {

  public Color color;

  public double x = 0;

  public double y = 0;

  public double a = 0;

  public double b = 0;

  public double hx = 0;

  public double hy = 0;

  public Pixel() {
    int r = (int) Math.floor(255 * Math.random());
    int g = (int) Math.floor(255 * Math.random());
    int b = (int) Math.floor(255 * Math.random());
    this.color = Color.rgb(r, g, b);
  }

  public double getOriginX(double originX, double width) {
    double tmp = Math.abs(this.hx) * width;
    return this.hx >= 0 ? originX + tmp : originX - tmp;
  }

  public double getOriginY(double originY, double width) {
    double tmp = Math.abs(this.hy) * width;
    return this.hy >= 0 ? originY - tmp : originY + tmp;
  }


}
