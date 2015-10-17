

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class Count {
	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>{
		private final static IntWritable one = new IntWritable(1);
		private Text user = new Text(); // type of output key

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] line = value.toString().split("::"); // line to string token
			int age=Integer.parseInt(line[2]);
			
			if(line[1].equals("M") && age<18) {
				user.set("7 M"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("F") && age<18) {
				user.set("7 F"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("M") && age<=24) {
				user.set("24 M"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("F") && age<=24) {
				user.set("24 F"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("M") && age<=34) {
				user.set("31 M"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("F") && age<=34) {
				user.set("31 F"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("M") && age<=44) {
				user.set("41 M"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("F") && age<=44) {
				user.set("41 F"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("M") && age<=55) {
				user.set("51 M"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("F") && age<=55) {
				user.set("51 F"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("M") && age<=61) {
				user.set("56 M"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("F") && age<=61) {
				user.set("56 F"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("M") && age>=62){
				user.set("62 M"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
			}else if(line[1].equals("F") && age>=62){
				user.set("62 F"); // set word as each input keyword
				context.write(user, one); // create a pair <keyword, 1>
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
		
		if (otherArgs.length != 2) {
			System.err.println("Usage: UserID <in> <out>");
			System.exit(2);
		}
		
		// create a job with name "wordcount"
		Job job = new Job(conf, "Count");
		job.setJarByClass(Count.class);
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