package polyu.bigdata;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

  //Mapper which implement the mapper() function
  public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();//for 1 and 2
    private Text count = new Text();//for 3
    private Text letter = new Text();//for 4
    private String next = new String();//for 4

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        //1-Count the words beginning with 'a'
    	  /*
        word.set(itr.nextToken());
    	  if(word.charAt(0) == 'a'){ //whether the first character of the string is 'a'
        	System.out.println("Map phase: We are processing word " + word);
        	context.write(word, one);
      	}
        */
        
        //2-Count the words with length larger than 10
        /*
    	  word.set(itr.nextToken());
        if(word.getLength() > 10){ // whether the length of a word is longer than 10
    		  System.out.println("Map phase: We are processing word " + word);
    		  context.write(word, one);
    	  }
        */
        
        //3-Count the word with length from 0 to n
    	  /*
        word.set(itr.nextToken());
        count.set(Integer.toString(word.getLength()));
        System.out.println("Map phase: We are processing word " + word);
        context.write(count, one);
        */
        
        //4-Count the letters appear in the documents
        next = itr.nextToken();
        for (int i = 0; i < next.length(); i++){
          char c = next.charAt(i);  
          if (Character.isLetter(c)){
            letter.set(Character.toString(c).toLowerCase());
            System.out.println("Map phase: We are processing word " + word);
            context.write(letter, one);
          }
        }
      }
    }
  }
  //Reducer which implement the reduce() function
  public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
    	System.out.println("Reduce phase: The key is " + key + " The value is " + val.get());
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }
  //Driver class to specific the Mapper and Reducer
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}