import org.datavec.api.records.reader.RecordReader;
import org.datavec.image.recordreader.ImageRecordReader;

/**
 * Created by Joachim on 03.04.2017.
 */
public class DayNightClassifier {

    private int seed;
    double learningRate = 0.01;
    int batchSize = 50;
    int nEpochs = 30;

    int numInputs = 2;
    int numOutputs = 2;

    int numHiddenNodes = 20;


    public void run(String[] args) {
        RecordReader rr = new ImageRecordReader();
    }
}
