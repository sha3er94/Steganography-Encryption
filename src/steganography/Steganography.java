/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Mohamed El-Shaer
 */
public class Steganography {

    /**
     * @param args the command line arguments
     */
    public static boolean[][] checkColour(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        boolean[][] secret = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = image.getRGB(i, j);
                int red = (pixel >> 16) & 0xff;
                if (red == 0) {
                    secret[i][j] = false;
                } else {
                    secret[i][j] = true;
                }
            }
        }
        return secret;
    }

    public static BufferedImage hideMessage(boolean[][] message, BufferedImage source) {
        int width = source.getWidth();
        int height = source.getHeight();
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (message[i][j] == true) {         //White
                    int pixel = source.getRGB(i, j);
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;

                    if (red % 2 == 1) {
                        red--;
                    }
                    Color rgb = new Color(red, green, blue);
                    resultImage.setRGB(i, j, rgb.getRGB());
                } else {                              //Black
                    int pixel = source.getRGB(i, j);
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;

                    if (red % 2 == 0) {
                        red++;
                    }
                    Color rgb = new Color(red, green, blue);
                    resultImage.setRGB(i, j, rgb.getRGB());
                }
            }
        }
        return resultImage;
    }

    public static boolean[][] findMessage(BufferedImage resultImage) {
        int width = resultImage.getWidth();
        int height = resultImage.getHeight();
        boolean[][] pixels = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = resultImage.getRGB(i, j);
                int red = (pixel >> 16) & 0xff;
                if(red % 2 == 0)
                    pixels[i][j] = true;
                else
                    pixels[i][j] = false;
            }
        }

        return pixels;
    }
    
    public static void showMessage(boolean[][] checkedPixel, BufferedImage result) throws IOException{
        int width = result.getWidth();
        int height = result.getHeight();
        BufferedImage resultMessage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
            if(checkedPixel[i][j] == true){     //White
                Color rgb = new Color(255, 255, 255);
                resultMessage.setRGB(i, j, rgb.getRGB());
            }
            else{
                Color rgb = new Color(0, 0, 0);
                resultMessage.setRGB(i, j, rgb.getRGB());
            }
                
            }
        }
        File outputfile = new File("resultMessage.png");
        ImageIO.write(resultMessage, "png", outputfile);
        System.out.println("WEL FIND KAMAN !");
    }

    public static void main(String[] args) throws IOException {
        BufferedImage secretImage = ImageIO.read(new File("m.png"));
        BufferedImage sourceImage = ImageIO.read(new File("o.png"));
        boolean[][] checkMessage = checkColour(secretImage);
        BufferedImage resultImage = hideMessage(checkMessage, sourceImage);
        File outputfile = new File("resultImage.png");
        ImageIO.write(resultImage, "png", outputfile);
        System.out.println("5ALAAAAST !");
        BufferedImage result = ImageIO.read(new File("resultImage.png"));
        boolean[][] checkPixel = findMessage(result);
        showMessage(checkPixel, result);
        
    }

}
