package minecraft.util;

import com.microsoft.msr.malmo.TimestampedString;
import org.json.JSONObject;
import provider.Observable;

/**
 * Created by Joachim on 01.04.2017.
 */
public class ObservationProvider extends Observable<JSONObject> {
    public void notifyObservers(TimestampedString observation) {
        JSONObject obs = new JSONObject(observation.getText());
        obs.put("observation_date", observation.getTimestamp());

        super.notifyObservers(obs);
    }
}
