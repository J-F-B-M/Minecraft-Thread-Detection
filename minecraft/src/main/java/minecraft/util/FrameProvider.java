package minecraft.util;

import com.microsoft.msr.malmo.ByteVector;
import provider.Observable;

import java.util.Vector;

/**
 * Created by Joachim Brehmer on 28.03.2017.
 */
public class FrameProvider extends Observable<Vector<Byte>> {
    public void notifyObservers(ByteVector frame){
        Vector<Byte> byteVector = new Vector<>();
        for (int i = 0; i < frame.size(); i++) {
            short value = frame.get(i);

            if(value > Byte.MAX_VALUE || value < Byte.MIN_VALUE) throw new IllegalStateException("Input ByteVector contained OutOfBounds-Value. The value was "+value);

            byteVector.add((byte)value);
        }

        // super-call
        notifyObservers(byteVector);
    }
}
