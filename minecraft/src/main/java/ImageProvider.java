import com.microsoft.msr.malmo.ByteVector;
import provider.IObservable;
import provider.IObserver;
import provider.Observable;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Joachim Brehmer on 28.03.2017.
 */
public class ImageProvider extends Observable<byte[]> {
    public void notifyObservers(ByteVector frame){
        //ArrayList list = new ArrayList(frame.size());
        for (int i = 0; i < frame.size(); i++) {

        }
    }
}
