package main;

public abstract class Equation {
  protected static final int MAX_ITER = 1 << 26; // 67108864 - about 50-60s

  private double tMin, tMax;

  public Equation(double tMin, double tMax) {
    this.tMin = tMin;
    this.tMax = tMax;
  }

  public abstract Point point(double t);

  public PointList points(double eps) {
    PointList points = new PointList();

    int iter = 0;

    for (double t = tMin; t < tMax && iter++ < MAX_ITER; t += eps) {
      points.add(point(t));
    }

    return points;
  }

  public static Equation combine(Equation... equations) {
    return new Equation(0, equations.length) {

      @Override
      public Point point(double t) {
        int i = (int) t;
        Equation equation = equations[i];
        double min = equation.tMin;
        double max = equation.tMax;
        double progress = t % 1d;

        return equation.point(min + progress * (max - min));
      }

    };
  }

  public static void main(String[] args) {
    System.out.println(123.61d % 1d);
  }
}
