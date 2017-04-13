import org.bytedeco.javacpp.opencv_core;
import org.datavec.image.data.ImageWritable;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.*;
import org.deeplearning4j.datasets.DataSets;
import org.deeplearning4j.datasets.iterator.ExistingDataSetIterator;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.util.Random;

/**
 * Created by Joachim Brehmer on 12.04.2017.
 */
public class DayNightClassifier {

    public static void main(String[] args) {
        DayNightClassifier classifier = new DayNightClassifier();
        
        classifier.run();
    }

    private void run() {
        ImageTransform resizeImageTransform = new ResizeImageTransform(320,240);
        ImageTransform flipTransform = new FlipHorizontalImageTransform();
        MultiImageTransform combinedTransform = new MultiImageTransform(resizeImageTransform);


    }


}
