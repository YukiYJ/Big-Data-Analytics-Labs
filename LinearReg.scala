package example

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import org.apache.spark.mllib.regression._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors


object LinearReg {

  def main(args: Array[String]): Unit = {
    // We need to use spark-submit command to run this program
    val conf = new SparkConf().setAppName("Linear Regression Example");
    val sc = new SparkContext(conf);
    
    // Load and parse the text file into an RDD[LabeledPoint]
	val data = sc.textFile("data/mllib/ridge-data/lpsa.data")
	val parsedData = data.map { line =>
	  val parts = line.split(',')
	  LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
	}.cache()
	
	// Train a linear model based on the input data
	val numIterations = 3000
	//10 6.255410304764278
	//30 6.215667329684537
	//100 6.207597210613578
	//1000 6.207597210613578
	//3000 6.207597210613578
	/** 
	  * LinearRegressionWithSGD is the name of a built-in object.
	  * The train() function returns an object of type LinearRegressionModel
	  * that has been trained on the input data.
	  * It uses stochastic gradient descent (SGD) as the training algorithm.
	  * It uses the default model settings (e.g., no intercept).
	  */
	//val trainedModel = LinearRegressionWithSGD.train(parsedData, numIterations)
	val trainedModel = RidgeRegressionWithSGD.train(parsedData, numIterations)
	// Evaluate the quality of the trained model and compute the error
	val actualAndPredictedLabels = parsedData.map { labeledPoint =>
	  // The predict() function of a model receives a feature vector,
	  // and returns a predicted label value.
	  val prediction = trainedModel.predict(labeledPoint.features)
	  (labeledPoint.label, prediction)
	}
	// For linear regression, we use the mean square error (MSE) as a metric.
	val MSE = actualAndPredictedLabels.map{case(v, p) => math.pow((v - p), 2)}.mean()
	println("Training Mean Squared Error = " + MSE)
  }
  

}
