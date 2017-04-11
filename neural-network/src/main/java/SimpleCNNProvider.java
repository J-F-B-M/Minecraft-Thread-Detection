import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Created by Joachim on 03.04.2017.
 */
public class SimpleCNNProvider {

    private int seed;
    private final int width;
    private final int height;
    private final int nChannels;
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

    public SimpleCNNProvider(int seed, int width, int height, int nChannels, int nLabel, int iterations) {
        this(seed, width, height, nChannels, nLabel, 1e-1, OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT, iterations, WeightInit.XAVIER, Updater.NESTEROVS, 0.9);
    }

    public SimpleCNNProvider(int seed, int width, int height, int nChannels, int nLabel, double learningRate, OptimizationAlgorithm optimizationAlgorithm,
                             int iterations, WeightInit weightInit, Updater updater, double momentum) {

        this.seed = seed;
        this.width = width;
        this.height = height;
        this.nChannels = nChannels;
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
                .iterations(iterations)
                .weightInit(weightInit)
                .updater(updater).momentum(momentum)
                .learningRate(learningRate)
                .list()
                .layer(0, new ConvolutionLayer.Builder(5, 5)
                        .nIn(nChannels)
                        .stride(1, 1)
                        .nOut(20)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(2, new ConvolutionLayer.Builder(5, 5)
                        //Note that nIn need not be specified in later layers
                        .stride(1, 1)
                        .nOut(50)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(4, new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(500).build())
                .layer(5, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.SIGMOID)
                        .nOut(4)
                        .build())
                .setInputType(InputType.convolutionalFlat(height, width, nChannels))
                .backprop(true)
                .pretrain(false)
                .build();
    }

    private void initializeNet() {
        if (net != null) return;
        if (conf == null) initializeConfiguration();

        net = new MultiLayerNetwork(conf);
        net.init();
    }
}
