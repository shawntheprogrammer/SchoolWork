import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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



public class UserInfo {
	
	public static class Map extends Mapper<LongWritable, Text, Text, Text>{
		HashMap<String,String> map;
		String mymovieid;

		
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stu
			super.setup(context);
			map = new HashMap<String, String >();
			Configuration conf = context.getConfiguration();
			mymovieid = conf.get("movieid"); // to retrieve movieid set in main method
			Path[] localPaths = context.getLocalCacheFiles();
			for(Path myfile:localPaths){
				String line=null;
				String nameofFile=myfile.getName();
				File file =new File(nameofFile+"");
				FileReader fr= new FileReader(file);
				BufferedReader br= new BufferedReader(fr);
				line=br.readLine();
				while(line!=null){
					String[] arr=line.split("::");
					map.put(arr[0], arr[1]+"~"+arr[2]); //userid and   gender age
					line=br.readLine();
				}
			}
		}

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] line = value.toString().split("::"); // line to string token
			if( line[1].equals(mymovieid) && Integer.parseInt(line[2]) >= 4) {
				String userid = line[0].trim();
				String gender = map.get(userid).substring(0,1);
				String age = map.get(userid).substring(2);
				String rating = line[2].trim();
				String composite = userid + "::" + gender + "::" + age;
				Text mykey = new Text();
				Text result = new Text(); 
				mykey.set(line[1].trim());
				result.set(composite);
				context.write(mykey, result);
			}
		}
	}
	
	public static class Reduce extends Reducer<Text,Text,Text,Text> {
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
	
			for (Text val : values) {
				context.write(key, val); 
			}
		}
	}	
	public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		// get all args
		if (otherArgs.length != 3) {
			System.err.println("Usage: JoinExample <in1> <out1> <userid>");
			System.exit(2);
		}
		conf.set("movieid", otherArgs[2].trim());
		
		Job job = new Job(conf, "uder info");
		job.setJarByClass(UserInfo.class);
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
		
		
		final String NAME_NODE = "hdfs://sandbox.hortonworks.com:8020";
		job.addCacheFile(new URI(NAME_NODE
		+ "/user/hue/users.dat"));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);		// TODO Auto-generated method stub
	}

}
