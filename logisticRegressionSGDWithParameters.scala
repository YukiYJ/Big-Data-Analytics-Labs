package example

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import scala.collection.mutable.ArrayBuffer

import org.apache.spark.mllib.classification._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics

object LogisticReg {

  def main(args: Array[String]): Unit = {
    // We need to use spark-submit command to run this program
    val conf = new SparkConf().setAppName("Logistic Regression Example").setMaster("local[2]").set("spark.executor.memory","1g");
    val sc = new SparkContext(conf);
    
    // Load and parse training data in LIBSVM format.
    // Remember what's the type of 'data' below?
	val data = MLUtils.loadLibSVMFile(sc, "/home/bigdata/Programs/spark/data/mllib/sample_libsvm_data.txt")
	val aucResults = ArrayBuffer[Double]()
    // Calculate 25 times
	for ( i <- 1 to 25 ){
		// Split data into training set (70%) and test set (30%).
		val splits = data.randomSplit(Array(0.7, 0.3), seed = System.currentTimeMillis)
		val trainingSet = splits(0).cache()
		val testSet = splits(1)
		
		// Train the model
		val numIterations = 100
		val regParam = 0.75
		val stepSize = 1.0
		val miniBatchFraction = 1.0
		/** 
		  * Similarly to LinearRegressionModel,
		  * here LogisticRegressionModel is a built-in object with default settings.
		  * It provides a train() function that returns a trained LogisticRegressionModel model.
		  */
		val model = new LogisticRegressionWithSGD()
		model.optimizer.
			setNumIterations(numIterations).
			setRegParam(regParam).
			setStepSize(stepSize).
			setMiniBatchFraction(miniBatchFraction)
		val trainedModel = model.run(trainingSet)
		
		/*val trainedModel = LogisticRegressionWithSGD.train(
								trainingSet, 
								numIterations,
								stepSize,
								miniBatchFraction)*/
		
		// Collect actual and predicted labels on the test set 
		val actualAndPredictedLabels = testSet.map { labeledPoint =>
		  // Similarly to LinearRegressionModel,
		  // the LogisticRegressionModel provides a predict() function
		  // that receives a feature vector and outputs a predicted label.
		  val prediction = trainedModel.predict(labeledPoint.features)
		  (prediction, labeledPoint.label)
		}
		
		/*
		 *  BinaryClassificationMetrics is a class hat helps you
		 *  calculate some quality measurements for a binary classifier.
		 *  Here we use the "area under ROC curve" measurement.
		 */
		val metrics = new BinaryClassificationMetrics(actualAndPredictedLabels)
		val auROC = metrics.areaUnderROC()
		
		println("Area under ROC = " + auROC)
		aucResults += auROC
	}
	println(aucResults.mkString("+"))
  }

}