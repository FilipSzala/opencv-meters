package org.example;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.findContours;

public class ElectricMeters {
    public ElectricMeters() {
    }

    public void removeUselessElements (String inputPath, String outputPath, PictureType quality){
        int width;
        int height;
        Mat image = Imgcodecs.imread(inputPath);

        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

        Scalar lowerBlue = new Scalar(90, 90, 90 );
        Scalar upperBlue = new Scalar(150, 255, 255);

        Mat blueMask = new Mat();
        Core.inRange(hsvImage, lowerBlue, upperBlue, blueMask);

        MatOfPoint nonZeroPoints = new MatOfPoint();
        Core.findNonZero(blueMask, nonZeroPoints);
        Rect boundingRect = Imgproc.boundingRect(nonZeroPoints);

        Mat result = new Mat(image, boundingRect);

        if (quality == PictureType.LOWQUALITY) {
            width = (int) (result.width() * 0.2);
            height = (int) (result.height() * 0.2);
        }
        else {
           width = (int) (result.width() * 0.35);
           height = (int) (result.height() * 0.35);
        }
        Mat resizedResult = new Mat();
        Imgproc.resize(result, resizedResult, new Size(width, height));

        Size currentSize = resizedResult.size();
        width = (int) (currentSize.width * 0.93);
        height = (int) (currentSize.height * 0.60);

        int xOffset = (int) (currentSize.width * 0.05);
        int yOffset = (int) (currentSize.height * 0.17);

        Rect croppedRect = new Rect(xOffset, yOffset, width, height);
        Mat finalCroppedResult = new Mat(resizedResult, croppedRect);

        Imgcodecs.imwrite(outputPath, finalCroppedResult);
    }

    public void convertPhotoToBinary(String inputPath, String outputPath,PictureType quality) {
        int width;
        int height;
        Mat image = Imgcodecs.imread(inputPath);
        Mat grayImage = new Mat();
        Mat thresholdedImage = new Mat();
        Mat result = new Mat();
        Mat resizedResult = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(grayImage, thresholdedImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 7);
        Imgproc.GaussianBlur(thresholdedImage, result, new Size(7, 7), 0);
        Imgproc.threshold(result, result, 200, 255, Imgproc.THRESH_BINARY);
        if (quality==PictureType.LOWQUALITY) {
            width = (int) (result.width() * 5);
            height = (int) (result.height() * 5);
        }
        else {
           width = (int) (result.width() * 4);
           height = (int) (result.height() * 4);
        }

        Imgproc.resize(result, resizedResult, new Size(width, height));

        Imgcodecs.imwrite(outputPath, resizedResult);
    }}
