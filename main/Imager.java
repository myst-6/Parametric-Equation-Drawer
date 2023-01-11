package main;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import java.awt.image.BufferedImage;

public class Imager {
    private File file;
    private Dimension size;
    private double inset;
    private Map<RenderingHints.Key, Object> hints = new HashMap<>();

    public Imager(File file, Dimension size, double inset) {
        this.file = file;
        this.size = size;
        this.inset = inset;

        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
    }

    public BufferedImage draw(PointList points, float hue_offset, float hue_diff, boolean isRetina) {
        BufferedImage img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage retina = new BufferedImage(size.width / 2, size.height / 2, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();
        Graphics2D retina_g2d = retina.createGraphics();

        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, size.width, size.height);

        g2d.setRenderingHints(hints);
        g2d.setComposite(AlphaComposite.Src);

        double offsetX = points.minX();
        double offsetY = points.minY();

        double rangeX = points.maxX() - points.minX();
        double rangeY = points.maxY() - points.minY();

        double factorX = (size.width / rangeX) * (1d - inset / size.width);
        double factorY = (size.height / rangeY) * (1d - inset / size.height);

        double factor;

        System.out.println(offsetY);

        if (factorX < factorY) {
            factor = factorX;
            offsetY *= (factorY / factorX);
        } else {
            factor = factorY;
            offsetX *= (factorX / factorY);
        }

        // offsetX *= (factorX / factor);
        // offsetY *= (factorY / factor);

        float hue = hue_offset;
        float d_hue = hue_diff;
        int hue_stiffness = 1;
        int box = Math.min(size.width, size.height) / 50;

        for (Point point : points) {
            double x = ((point.x - offsetX) * factor) + inset / 2;
            double y = ((point.y - offsetY) * factor) + inset / 2;

            try {
                if (d_hue < hue_diff) {
                    d_hue = hue;
                    hue += hue_diff * (float) ++hue_stiffness;
                    d_hue = hue - d_hue;
                } else {
                    d_hue = hue;
                    hue += hue_diff;
                    d_hue = hue - d_hue;
                    hue_stiffness = 1;
                }

                int rgb = Color.HSBtoRGB(hue, 0.95f, 0.95f);

                g2d.setColor(new Color(rgb));
                g2d.fillRect((int) x - (box / 2), size.height - (int) y - 2, box, 5); // height 5 for ribbon effect
            } catch (ArrayIndexOutOfBoundsException ex) {
            }
        }

        g2d.dispose();

        if (!isRetina)
            return img;

        AffineTransform xform = AffineTransform.getScaleInstance(.5, .5);
        retina_g2d.setRenderingHints(hints);
        retina_g2d.drawImage(img, xform, null); // img being @2x (retina size)

        return retina;
    }

    public BufferedImage draw(PointList points, float hue_diff, boolean isRetina) {
        return draw(points, 0f, hue_diff, isRetina);
    }

    public void write(BufferedImage img) {
        String path = file.getPath();
        int extBegin = path.lastIndexOf(".") + 1;

        try {
            ImageIO.write(img, path.substring(extBegin), file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void remove() {
        file.delete();
    }
}
