
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class Genre {
	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>{
		private final static IntWritable one = new IntWritable(1);
		private Text user = new Text(); // type of output key
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			String[] line = value.toString().split("::"); // line to string token
			Configuration conf = context.getConfiguration();
			String name=conf.get("genre");
			
			if(line[2].toLowerCase().contains(name)) {
				user.set(line[1]); // set word as each input keyword
				context.write(user,one); // create a pair <keyword, 1>
			}
		}
	}
	
	public static class Reduce extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0; // initialize the sum for each keyword
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result); // create a pair <keyword, number of occurences>
		}
	}	
	
	public static void main(String[] args) throws Exception{
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		// get all args
		
		if (otherArgs.length != 2 && otherArgs.length != 3) {
			System.err.println("Usage: Movie <in> <out> <Genre>");
			System.exit(2);
		}
		
		if(otherArgs.length==3){
			conf.set("genre", otherArgs[2].toLowerCase());
		}else{
			conf.set("genre", "fantasy");
		}
		
		// create a job with name "wordcount"
		
		Job job = new Job(conf, "Genre");
		
		job.setJarByClass(Genre.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		// uncomment the following line to add the Combiner
		job.setCombinerClass(Reduce.class);
		// set output key type
		job.setOutputKeyClass(Text.class);
		// set output value type
		job.setOutputValueClass(IntWritable.class);
		//set the HDFS path of the input data
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		// set the HDFS path for the output
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		//Wait till job completion
		System.exit(job.waitForCompletion(true) ? 0 : 1);		// TODO Auto-generated method stub

	}

}
