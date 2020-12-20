package heart;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bootstrap extends Application {

  static final double FullAngle = 2 * Math.PI;
  static final double width = 1280;
  static final double height = 780;
  static final int quantity = 1000;
  static final double B = 0.98;
  static final List<Pixel> pixels = new ArrayList<>(quantity);
  double originX;
  double originY;
  double x, y, u, v;
  boolean w = false;

  public Bootstrap() {
    for (int i = 0; i < quantity; i++) {
      Pixel pixel = new Pixel();
      pixel.x = 0.5 * width;
      pixel.y = 0.5 * height;
      pixel.a = 61.8 * Math.cos(i) * Math.random();
      pixel.b = 61.8 * Math.sin(i) * Math.random();
      Point2D point2D = calcHeartPixel(i);
      pixel.hx = point2D.getX();
      pixel.hy = point2D.getY();
      pixels.add(pixel);
    }

    originX = u = 0.5 * width;
    originY = v = 0.5 * height;
  }

  private static Point2D calcHeartPixel(int step) {
    double theta = 2 * Math.PI + step * 0.02 * Math.PI;
    double tmp = theta / 2f;
    double x = Math.cos(tmp);
    double y = Math.sin(tmp) + Math.pow(Math.pow(x, 2), 1 / 3f);
    return new Point2D(x, y);
  }

  @Override
  public void start(Stage primaryStage) {
    Pane root = new Pane();
    Canvas canvas = new Canvas();
    canvas.heightProperty().bind(root.heightProperty());
    canvas.widthProperty().bind(root.widthProperty());
    GraphicsContext context2D = canvas.getGraphicsContext2D();

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        context2D.setGlobalBlendMode(BlendMode.SRC_OVER);
        context2D.setFill(Color.rgb(8,8,12, 0.65));
        context2D.fillRect(0, 0, width, height);
        context2D.setGlobalBlendMode(BlendMode.LIGHTEN);

        x = originX - u;
        y = originY - v;
        u = originX;
        v = originY;
        for (int i = 0; i < quantity; i++) {
          double m = 0.4 * width;
          double d = 0.86 * width;
          double l = 0.125 * width;
          Pixel pixel = pixels.get(i);
          double x1 = pixel.x;
          double y1 = pixel.y;
          double a = pixel.a;
          double b = pixel.b;
          double c = x1 - pixel.getOriginX(originX, m * 0.3);
          double k = y1 - pixel.getOriginY(originY, m * 0.2);
          double g = Math.sqrt(c * c + k * k);
          if (0 == g) {
            g = 0.001;
          }
          c = c / g;
          k = k / g;
          if (w && (g < m)) {
            double s = 14 * (1 - g / m);
            a = a + (c * s + 0.5 - Math.random());
            b = b + (k * s + 0.5 - Math.random());
          }
          if (g < d) {
            double s = 0.0014 * (1 - g / d) * width;
            a -= c * s;
            b -= k * s;
          }
          if (g < l) {
            c = 0.00026 * (1 - g / l) * width;
            a += x * c;
            b += y * c;
          }
          a *= B;
          b *= B;
          c = Math.abs(a);
          k = Math.abs(b);
          g = 0.5 * (c + k);
          if (0.1 > c) {
            a *= 3 * Math.random();
          }
          if (0.1 > k) {
            b *= 3 * Math.random();
          }
          c = 0.45 * g;
          c = Math.max(Math.min(c, 3.5), 0.8);
          x1 += a;
          y1 += b;
          if (x1 > width) {
            x1 = width;
            a *= -1;
          } else if (0 > x1){
            x1 = 0;
            a *= -1;
          }
          if (y1 > height) {
            y1 = height;
            b *= -1;
          } else if (0 > y1) {
            y1 = 0;
            b *= -1;
          }
          pixel.a = a;
          pixel.b = b;
          pixel.x = x1;
          pixel.y = y1;

          context2D.setFill(pixel.color);
          context2D.beginPath();
          context2D.arc(x1, y1, c, c, 0, 360);
          context2D.closePath();
          context2D.fill();
        }
      }
    }.start();

    root.setOnMousePressed(event -> {
      w = true;
    });
    root.setOnMouseReleased(event -> {
      w = false;
    });

    EventHandler<? super MouseEvent> mouseMoveHandler = event -> {
      originX = event.getX();
      originY = event.getY();
    };
    root.setOnMouseMoved(mouseMoveHandler);
    root.setOnMouseDragged(mouseMoveHandler);
    root.getChildren().add(canvas);
    Scene scene = new Scene(root, width, height);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}
