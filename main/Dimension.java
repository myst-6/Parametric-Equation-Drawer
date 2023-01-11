package main;

public class Dimension {
    public static Dimension SD = new Dimension(720, 480); // 480p
    public static Dimension HD_1 = new Dimension(1280, 720); // 720p
    public static Dimension HD_2 = new Dimension(1920, 1080); // 1080p
    public static Dimension UHD_1 = new Dimension(3840, 2160); // 2160p / 4K
    public static Dimension UHD_2 = new Dimension(5120, 3880); // 5K
    public static Dimension UHD_3 = new Dimension(7680, 4320); // 8K
    public static Dimension UHD_4 = new Dimension(10240, 7760); // 10K
    public static Dimension UHD_5 = new Dimension(15360, 8640); // 16K

    public int width, height;

    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
