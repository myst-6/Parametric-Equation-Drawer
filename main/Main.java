package main;

import java.io.File;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public final class Main {
  public static Equation circle = new Equation(-Math.PI, Math.PI) {
    @Override
    public Point point(double t) {
      return new Point(Math.sin(t), Math.cos(t));
    }
  };

  public static Equation butterfly = new Equation(0, 12d * Math.PI) {
    @Override
    public Point point(double t) {
      double coeff = Math.exp(Math.cos(t)) - 2 * Math.cos(4 * t) - Math.pow(Math.sin(t / 2), 5);
      return circle.point(t).scale(coeff);
    }
  };

  public static final double sqrt5 = Math.sqrt(5);
  public static final double phi = (1 + sqrt5) / 2;
  public static final double psi = 1 - phi;
  public static final Complex cpsi = new Complex(psi);
  public static Equation fibonacci = new Equation(0f, 8f) {
    @Override
    public Point point(double t) {
      // get fibonacci number as complex number in the form a+bi
      Complex phiN = new Complex(Math.pow(phi, t));
      Complex psiN = cpsi.pow(t);
      Complex pt = psiN.neg().add(phiN).divide(sqrt5);
      return new Point(pt.real * 20, pt.imag * 500); // argand
    }
  };

  public static void equation(String name, Dimension size, int inset, Equation equation, double EPS, float FPS,
      float TIME, float hue_diff, boolean isRetina, boolean drawGif) {
    File png = new File("./out/" + name + ".png");
    File gif = new File("./out/" + name + ".gif");

    PointList points = equation.points(EPS);
    Imager imager = new Imager(png, size, inset);
    imager.remove();
    imager.write(imager.draw(points, hue_diff, isRetina));
    if (drawGif)
      Gifer.gif(imager, points, gif, FPS, TIME, hue_diff, isRetina);
  }

  public static void main(String[] args) {
    // Font arial =
    // GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0];

    // Equation eq = Equation.combine(Bezier.fromString("test", arial, new
    // Point(0, 0), 20));

    // System.out.println(fibonacci.point(1.5));

    equation("fibonacci_sd", Dimension.SD, 64, fibonacci, 1e-5, 24f, 1.5f, 1e-5f,
        false, true);
  }
}