import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;

/**
 * Created by Joachim on 03.04.2017.
 */
public class ImageNetProvider {

    private int seed;
    private final int width;
    private final int height;
    private final int channels;
    double learningRate = 1e-1;
    OptimizationAlgorithm optimizationAlgorithm;
    private final int iterations;
    private final WeightInit weightInit;
    private final Updater updater;
    private final double momentum;

    int batchSize = 50;
    int nEpochs = 30;

    int numInputs = 2;
    int numOutputs = 2;

    int numHiddenNodes = 20;

    MultiLayerConfiguration conf;
    MultiLayerNetwork net;

    public ImageNetProvider(int seed, int width, int height, int channels, int iterations) {
        this(seed, width, height, channels, 1e-1, OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT, iterations, WeightInit.)
    }

    public ImageNetProvider(int seed, int width, int height, int channels, double learningRate, OptimizationAlgorithm optimizationAlgorithm,
                            int iterations, WeightInit weightInit, Updater updater, double momentum) {

        this.seed = seed;
        this.width = width;
        this.height = height;
        this.channels = channels;
        this.learningRate = learningRate;
        this.optimizationAlgorithm = optimizationAlgorithm;
        this.iterations = iterations;
        this.weightInit = weightInit;
        this.updater = updater;
        this.momentum = momentum;
    }

    public void run(String[] args) {
        initializeNet();
    }

    private void initializeConfiguration() {
        if (conf != null) return;

        conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(1)
                .weightInit(WeightInit.XAVIER)
                .updater(Updater.NESTEROVS).momentum(0.9)
                .learningRate(learningRate)

                .list()

                .build();
    }

    private void initializeNet() {
        if (net != null) return;
        if (conf == null) initializeConfiguration();


    }
}
