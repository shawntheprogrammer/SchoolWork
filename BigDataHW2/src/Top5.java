import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Top5 {
	
	public static class Map extends Mapper<LongWritable, Text, Text, Text>{
		private TreeMap<Double, String> treemap = new TreeMap<Double, String>();
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] mydata = value.toString().split("::");
			
			Double rating = Double.parseDouble(mydata[1].trim());
			String title = mydata[0].trim();
			treemap.put(rating,title);
			
	        if (treemap.size() > 5) {
	        	treemap.remove(treemap.firstKey());
	        }
		}
		
	@Override
		
	    protected void cleanup(Context context) throws IOException, InterruptedException {
	    
	       for (Double k : treemap.keySet()) {
	            context.write( new Text(treemap.get(k)), new Text(k.toString()) );
	       } 
	    }
	}

	public static class Reduce extends Reducer<Text,Text,Text,Text> {
		
		public void reduce(Text key, Text value,Context context ) throws IOException, InterruptedException { 
	        
	        context.write(key,value);
		}		
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		// get all args
		
		if (otherArgs.length != 2) {
			System.err.println("Usage: UserID <in> <out>");
			System.exit(2);
		}
		
		// create a job with name "wordcount"
		Job job = new Job(conf, "top5");
		job.setJarByClass(Top5.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		// uncomment the following line to add the Combiner
		job.setCombinerClass(Reduce.class);
		// set output key type
		job.setOutputKeyClass(Text.class);
		// set output value type
		job.setOutputValueClass(Text.class);
		//set the HDFS path of the input data
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		// set the HDFS path for the output
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		//Wait till job completion
		System.exit(job.waitForCompletion(true) ? 0 : 1);		// TODO Auto-generated method stub
	}

}
