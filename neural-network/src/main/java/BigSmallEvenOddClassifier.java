import org.apache.commons.math3.util.MathArrays;
import org.bytedeco.javacpp.opencv_core;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.datasets.iterator.DoublesDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.eval.RegressionEvaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.FeedForwardLayer;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Joachim on 04.04.2017.
 */
public class BigSmallEvenOddClassifier {
    private static final Logger LOGGER = Logger.getLogger(BigSmallEvenOddClassifier.class.getName());

    public static void main(String[] args) {

        int trainSize = 1000;

        DataSetIterator trainDataIter = new DoublesDataSetIterator(IntStream.range(0, trainSize).mapToObj(i -> new double[]{Math.random(), Math.random()}).map(d -> new Pair<double[], double[]>(d, new double[]{d[0] > d[1] ? 1 : 0, d[1] > d[0] ? 1 : 0}))
                .collect(Collectors.toList()), 1);

        DataSetIterator testDataIter = new DoublesDataSetIterator(IntStream.range(0, trainSize).mapToObj(i -> new double[]{Math.random(), Math.random()}).map(d -> new Pair<double[], double[]>(d, new double[]{d[0] > d[1] ? 1 : 0, d[1] > d[0] ? 1 : 0}))
                .collect(Collectors.toList()), 1);

        //Normalize the training data
        NormalizerMinMaxScaler normalizer = new NormalizerMinMaxScaler(0, 1);
        normalizer.fitLabel(false);
        normalizer.fit(trainDataIter);              //Collect training data statistics
        trainDataIter.reset();

        trainDataIter.setPreProcessor(normalizer);
        testDataIter.setPreProcessor(normalizer);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(140)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(1)
                .weightInit(WeightInit.XAVIER)
                .updater(Updater.NESTEROVS).momentum(0.9)
                .learningRate(0.15)
                .list()
                .layer(0, new GravesLSTM.Builder().activation(Activation.TANH).nIn(2).nOut(10)
                        .build())
                .layer(1, new DenseLayer.Builder().nIn(10).nOut(10).activation(Activation.SIGMOID).build())
                .layer(2, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.SIGMOID).nIn(10).nOut(2).build())
                .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        net.setListeners(new ScoreIterationListener(20));

        // ----- Train the network, evaluating the test set performance at each epoch -----
        int nEpochs = 50;

        for (int i = 0; i < nEpochs; i++) {
            //LOGGER.info("Epoch "+ i+" started.");
            net.fit(trainDataIter);
            trainDataIter.reset();
            LOGGER.info("Epoch " + i + " complete. Time series evaluation:");

            RegressionEvaluation regressionEvaluation = new RegressionEvaluation("FirstGreaterSecond", "SecondGreaterFirst");
            Evaluation evaluation = new Evaluation(Arrays.asList("FirstGreaterSecond", "SecondGreaterFirst"));

            //Run evaluation. This is on 25k reviews, so can take some time
            while (testDataIter.hasNext()) {
                DataSet t = testDataIter.next();
                INDArray features = t.getFeatureMatrix();
                INDArray labels = t.getLabels();
                INDArray predicted = net.output(features, false);

                predicted = predicted.reshape(labels.shape());

                regressionEvaluation.eval(labels, predicted);
                evaluation.eval(labels, predicted);
            }

            System.out.println(regressionEvaluation.stats());
            System.out.println(evaluation.stats());

            testDataIter.reset();
        }
    }
}
