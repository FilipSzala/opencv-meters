package org.example;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class WaterMeters {
    public void focusOnRedElements (String inputPath, String outputPath){
        Mat result = new Mat();
        Mat image = Imgcodecs.imread(inputPath);

        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

        Scalar lowerRed = new Scalar(0, 120, 60);
        Scalar upperRed = new Scalar(5, 200, 255);

        Mat redMask = new Mat();
        Core.inRange(hsvImage, lowerRed, upperRed, redMask);


        MatOfPoint points = new MatOfPoint();
        Core.findNonZero(redMask, points);
        Rect boundingRect = Imgproc.boundingRect(points);
        double changedWidth = 3.5;
        double changedHeight = 1.7;

        int newWidthOfPicture = (int) (boundingRect.width * changedWidth);
        int newHeightOfPicture = (int) (boundingRect.height * changedHeight);
        int newX = boundingRect.y - (newHeightOfPicture - boundingRect.height);
        int newY = boundingRect.x - (newWidthOfPicture - boundingRect.width);

            //by boundingRect.x I set coordinates of right edge of picture.
            boundingRect.x = newX+50;
            boundingRect.width = newWidthOfPicture;
            boundingRect.y = newY+60;
            boundingRect.height = newHeightOfPicture;

            result = new Mat(image, boundingRect);

        Imgcodecs.imwrite(outputPath,result);
    }
   }