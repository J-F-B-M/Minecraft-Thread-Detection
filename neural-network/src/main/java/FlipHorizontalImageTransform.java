import org.datavec.image.data.ImageWritable;
import org.datavec.image.transform.FlipImageTransform;

import java.util.Random;

import static org.bytedeco.javacpp.opencv_core.*;

/**
 * Created by Joachim Brehmer on 12.04.2017.
 */
public class FlipHorizontalImageTransform extends FlipImageTransform {
    @Override
    public ImageWritable transform(ImageWritable image, Random random) {
        if (image == null) {
            return null;
        }
        Mat mat = converter.convert(image.getFrame());

        boolean shouldFlip = random != null ? random.nextBoolean() : false;

        Mat result = new Mat();
        if (shouldFlip) {
            flip(mat, result, 1);
        } else {
            // no flip
            mat.copyTo(result);
        }

        return new ImageWritable(converter.convert(result));
    }
}
