package main;

public class Complex {
  public double real, imag, abs, arg;

  private void init(double real, double imag) {
    this.real = real;
    this.imag = imag;
    this.abs = Math.sqrt(Math.pow(real, 2) + Math.pow(imag, 2));
  }

  public Complex(double real, double imag) {
    init(real, imag);
    this.arg = 2 * Math.atan(imag / (abs + real));
  }

  public Complex(double a) {
    init(a, 0);
    this.arg = Math.PI;
  }

  public Complex() {
    init(0, 0);
    this.arg = 0; // undefined
  }

  public double abs() {
    return Math.sqrt(real * real + imag * imag);
  }

  public Complex add(Complex other) {
    return new Complex(real + other.real, imag + other.imag);
  }

  public Complex multiply(double n) {
    return new Complex(real * n, imag * n);
  }

  public Complex divide(double n) {
    return new Complex(real / n, imag / n);
  }

  public Complex neg() {
    return multiply(-1);
  }

  public Complex pow(double n) {
    return new Complex(Math.cos(n * arg), Math.sin(n * arg)).multiply(Math.pow(abs, n));
  }

  public String toString(int precision) {
    return String.format("%." + precision + "f + %." + precision + "f", real, imag) + "i";
  }

  public String toString() {
    return toString(10);
  }

  // tester
  public static void main(String[] args) {
    Complex a = new Complex(-1, 1);

    double abs = a.abs;
    double arg = a.arg;

    System.out.println(String.format("Abs %f", abs));
    System.out.println(String.format("Arg %f", arg));

    Complex b = new Complex(-4, -3);
    double c = 6;

    Complex added = a.add(b);
    Complex multiplied = a.multiply(c);
    Complex divided = a.divide(c);
    Complex neg = a.neg();
    Complex pow = a.pow(c);

    System.out.println(String.format("Added %s", added.toString()));
    System.out.println(String.format("Multiplied %s", multiplied.toString()));
    System.out.println(String.format("Divided %s", divided.toString()));
    System.out.println(String.format("Neg %s", neg.toString()));
    System.out.println(String.format("Pow %s", pow.toString()));

  }
}
