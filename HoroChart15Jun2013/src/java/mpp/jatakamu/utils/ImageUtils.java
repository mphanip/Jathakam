/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpp.jatakamu.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Phani Pramod M
 */
public class ImageUtils
{

    public static BufferedImage getImage(String text)
    {
        AffineTransform at = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(at, true, true);
        Font font = getFont();
        TextLayout textLayout = new TextLayout(text, font, frc);
        float descent = textLayout.getDescent();
        Shape shape = textLayout.getOutline(at);
        Rectangle bounds = shape.getBounds();
        float width = bounds.width + (text.length() * 2);
        float height = bounds.height + 2;
        BufferedImage image = new BufferedImage(Math.round(width),
            Math.round(height), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setFont(font);
        g.setColor(Color.red);
        textLayout.draw(g, 0, height - descent);

        return image;
    }

    public static BufferedImage getVerticalImage(String text)
    {
        AffineTransform at = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(at, true, true);
        Font font = getFont();
        TextLayout textLayout = new TextLayout(text, font, frc);
        float descent = textLayout.getDescent();
        Shape shape = textLayout.getOutline(at);
        Rectangle bounds = shape.getBounds();
//        float width = bounds.width;
        float height = bounds.height;
        int imgHeight = Math.round(height * textLayout.getCharacterCount()); // rotating the image to 90degs
        int imgWidth = Math.round(height); // rotating the image to 90degs
        // Using TYPE_INT_ARGB for transparent background
        BufferedImage image = new BufferedImage(imgWidth, imgHeight,
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.red);
        float curht = height - descent;
        int len = text.length();
        for (int i = 0; i < len; i++)
        {
            TextLayout txtLayout = new TextLayout(text.substring(i, i + 1), font, frc);
            Rectangle2D bds = txtLayout.getBounds();
            double w = (imgWidth - bds.getWidth()) / 2.0 - 2.0;
            txtLayout.draw(g, (float) w, curht);
            curht += bds.getHeight() + txtLayout.getDescent();
        }

        return image;
    }

    public static DefaultStreamedContent getDynVerticalImage(String strValue)
    {
        BufferedImage bi = getVerticalImage(strValue);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            ImageIO.write(bi, "png", os);
            DefaultStreamedContent graphicText = new DefaultStreamedContent(
                new ByteArrayInputStream(os.toByteArray()), "image/png");
            return graphicText;
        }
        catch (IOException ex)
        {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Font getFont()
    {
        Font font = null;
        font = new Font("Arial Unicode MS", Font.TRUETYPE_FONT, 24);
        font = font.deriveFont(Font.BOLD, 16f);

//        if (font == null)
//        {
//            try
//            {
////                InputStream fontis = Graphic2dEx1.class.getResourceAsStream("ARIALUNI.TTF"); // Currently not working. will debug later.
//                font = Font.createFont(Font.TRUETYPE_FONT, new File("D:\\devel\\mine\\prj\\astro\\ARIALUNI.TTF"));
////                System.out.println("fontis = " + fontis);
////                font = Font.createFont(Font.TRUETYPE_FONT, fontis);
//                System.out.println("font = " + font);
//                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
//                font = font.deriveFont(Font.BOLD, 24f);
//            }
//            catch (Exception ex)
//            {
//                ex.printStackTrace();
//            }
//        }

        return font;
    }
}
