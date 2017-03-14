package exercise

object NewtonSqrt {

  //Functions like sqrtIter,improve, and isGoodEnough are ONLY used as the auxiliary functions for sqrt, so we put them inside sqrt.
  def sqrt(target: Double, err: Double): Double = {

    //Test whether the guess is good enough
    //I.e., test whether Math.abs(guess * guess - target) / target is smaller than a threshold (e.g., 0.001)
    def isGoodEnough(guess: Double): Boolean =
    	if (Math.abs(guess * guess - target)/target < err) true
    	else false

     //Take the mean of guess and target/guess to improve the guess
    def improve(guess: Double): Double =
    	(guess + target/guess)/2
    
    def sqrtIter(guess: Double): Double =
      if (isGoodEnough(guess)) guess
      else sqrtIter(improve(guess)) //It looks like a if-else in Java, but is used for expressions, not statements 

     //The return expression of the function sqrt(target: Double). Here we set the initial guess (i.e., estimate) as 1
    sqrtIter(1.0)
  }

  def main(args: Array[String]) {
    println(sqrt(2.0,0.01));
  }
}