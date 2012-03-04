/*
 * Find local maxima in an Image (or ROI) using the algorithm described in
 * Neubeck and Van Gool. Efficient non-maximum suppression. Pattern Recognition (2006) vol. 3 pp. 850-855
 *
 * Jonas Ries brought this to my attention and send me C code implementing one of the
 * described algorithms
 *
 *
 *
 */

package edu.valelab.GaussianFit;

import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;
import ij.plugin.filter.GaussianBlur;
import java.awt.Polygon;
import java.awt.Rectangle;


/**
 *
 * @author nico
 */
public class FindLocalMaxima {
   
   public enum FilterType {
      NONE,
      GAUSSIAN1_5
   }

   /**
    * Static utility function to find local maxima in an Image
    * 
    * 
    * @param iPlus - ImagePlus object in which to look for local maxima
    * @param n - size of blocks (in pixels) in which to divide up the image
    * @param threshold - value below which a maximum will be rejected
    * @return Polygon with maxima 
    */
   public static Polygon FindMax(ImagePlus iPlus, int n, int threshold, FilterType filterType) {
      Polygon maxima = new Polygon();

      ImageProcessor iProc = iPlus.getProcessor();
      Rectangle roi = iProc.getRoi();
      
      // Prefilter if needed
      switch (filterType) {
         case GAUSSIAN1_5 : 
            // TODO: if there is an ROI, we only need to filter in the ROI
            ImageProcessor iProcG1 = iProc.duplicate();
            ImageProcessor iProcG5 = iProc.duplicate();
            GaussianBlur filter = new GaussianBlur();
            filter.blur(iProcG1, 1);
            filter.blur(iProcG5, 5);
            ImagePlus p1 = new ImagePlus("G1", iProcG1);
            ImagePlus p5 = new ImagePlus("G5", iProcG5);
            ImageCalculator ic = new ImageCalculator();
            ic.run("subtract", p1, p5);
            iProc = p1.getProcessor();
             
          
            break;
      }


      // divide the image up in blocks of size n and find local maxima
      int n2 = 2*n + 1;
      // calculate borders once
      int xRealEnd = roi.x + roi.width;
      int xEnd = xRealEnd - n2;
      int yRealEnd = roi.y + roi.height;
      int yEnd = yRealEnd - n2;
      for (int i=roi.x; i <= xEnd; i+=n2) {
         for (int j=roi.y; j <= yEnd; j+=n2) {
            int mi = i;
            int mj = j;
            for (int i2=i; i2 < i + n2 && i2 < xRealEnd; i2++) {
               for (int j2=j; j2 < j + n2 && j2 < yRealEnd; j2++) {
                  // revert getPixel to get after debugging
                  if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj)) {
                     mi = i2;
                     mj = j2;
                  }
               }
            }
            // is the candidate really a local maximum?
            // check surroundings (except for the pixels that we already checked)
            boolean stop = false;
            // columns in block to the left
            if (mi - n < i && i>0) {
               for (int i2=mi-n; i2<i; i2++) {
                  for (int j2=mj-n; j2<=mj+n; j2++) {
                     if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj)) {
                        stop = true;
                     }
                  }
               }
            }
            // columns in block to the right
            if (!stop && mi + n >= i + n2 ) {
               for (int i2=i+n2; i2<=mi+n; i2++) {
                   for (int j2=mj-n; j2<=mj+n; j2++) {
                     if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj)) {
                        stop = true;
                     }
                  }
               }
            }
            // rows on top of the block
            if (!stop && mj - n < j && j > 0) {
               for (int j2 = mj - n; j2 < j; j2++) {
                  for (int i2 = mi - n; i2 <= mi + n; i2++) {
                     if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj))
                        stop = true;
                  }
               }
            }
            // rows below the block
            if (!stop && mj + n >= j + n2) {
               for (int j2 = j + n2; j2 <= mj + n; j2++) {
                  for (int i2 = mi - n; i2 <= mi + n; i2++) {
                     if (iProc.getPixel(i2, j2) > iProc.getPixel(mi, mj))
                        stop = true;
                  }
               }
            }
            if (!stop && (threshold == 0 || iProc.getPixel(mi, mj) > threshold))
               maxima.addPoint(mi, mj);
         }
      }


      return maxima;
   }


   // Filters local maxima list using the ImageJ findMaxima Threshold algorithm
   public static Polygon noiseFilter(ImageProcessor iProc, Polygon inputPoints, int threshold)
   {
      Polygon outputPoints = new Polygon();

      for (int i=0; i < inputPoints.npoints; i++) {
         int x = inputPoints.xpoints[i];
         int y = inputPoints.ypoints[i];
         int value = iProc.getPixel(x, y) - threshold;
         if (    value > iProc.getPixel(x-1, y-1) ||
                 value > iProc.getPixel(x-1, y)  ||
                 value > iProc.getPixel(x-1, y+1)||
                 value > iProc.getPixel(x, y-1) ||
                 value > iProc.getPixel(x, y+1) ||
                 value > iProc.getPixel(x+1, y-1) ||
                 value > iProc.getPixel(x+1, y) ||
                 value > iProc.getPixel(x+1, y+1)
               )
            outputPoints.addPoint(x, y);
      }

      return outputPoints;
   }

}
