package minecraft.util;

import com.microsoft.msr.malmo.ByteVector;
import provider.Observable;

import java.util.Vector;

/**
 * Created by Joachim Brehmer on 28.03.2017.
 */
public class FrameProvider extends Observable<Vector<Short>> {
    public void notifyObservers(ByteVector frame){
        Vector<Short> shortVector = new Vector<>();
        for (int i = 0; i < frame.size(); i++) {
            shortVector.add(frame.get(i));
        }

        // super-call
        notifyObservers(shortVector);
    }
}
