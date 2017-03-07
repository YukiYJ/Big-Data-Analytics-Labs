# Implementation of gradient descent
# for a single predictor (x)
# Derek Mingyu MA
# derek.ma

compute_sum_part <- function(beta_0, beta_1, x, y, index){
  #index shows this sum function is for beta_0 or beta_1 update
  sum <- 0
  for(count in 1:nrow(x)){
    if (index == 0){
      sum <- sum + beta_0 + beta_1*x[count,1] - y[count,1]
    }
    else if (index == 1){
      sum <- sum + (beta_0 + beta_1*x[count,1] - y[count,1])*x[count,1]
    }
  }
  sum
}

gradient_descent <- function(x, y, maxit) {
  # initialize estimate
  beta_0 <- 0.8; beta_1 <- 0.7; i <- 0; 
  old_beta_0 <- Inf;  old_beta_1 <- Inf;
  
  # starting step size
  alpha <- 0.02
  
  # check for convergence
  # (in practice, we do include a limit on the number of iterations)
  while (i < maxit) {
    cat("it: ", i, " beta_0: ", round(beta_0, 2), " beta_1: ", round(beta_1, 2), " alpha: ", round(alpha, 6), "\n")
    
    # store the last estimate
    old_beta_0 <- beta_0
    old_beta_1 <- beta_1
    
    # update estimate
    cat(compute_sum_part(old_beta_0, old_beta_1, x, y, 0),"\n")
    cat(compute_sum_part(old_beta_0, old_beta_1, x, y, 1),"\n")
    beta_0 <- old_beta_0 - (alpha/nrow(x))*compute_sum_part(old_beta_0, old_beta_1, x, y, 0)
    beta_1 <- old_beta_1 - (alpha/nrow(x))*compute_sum_part(old_beta_0, old_beta_1, x, y, 1)
    i <- i+1
    
    # shorten the step size
     #if ((i %% 3) == 0) alpha <- alpha / 2
  }
  c(beta_0,beta_1)
}

#import the data as two one column data frame
x = data.frame(x=c(4,8,10,16,18))
y = data.frame(y=c(4.02, 5.96, 7.0, 10.04, 10.14))
#call the function
gradient_descent(x, y, 5)
