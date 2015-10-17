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



public class Top5FemaleRating {
	
	public static class Map1 extends Mapper<LongWritable, Text, Text, Text>{

		private Text user_rating;
		private Text movieid = new Text();  // type of output key 
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] mydata = value.toString().split("::");
			///	System.out.println(value.toString());
			String userid = mydata[0];
			String intrating = mydata[2];
			
			user_rating = new Text("rat~" + userid + "~" + intrating);
			movieid.set(mydata[1].trim());
			context.write(movieid, user_rating);
		}
	}


	public static class Map2 extends Mapper<LongWritable, Text, Text, Text>{
		private Text myTitle = new Text();
		private Text movieid = new Text();  // type of output key 
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] mydata = value.toString().split("::");
			String title = mydata[1];
			myTitle.set("mov~" + title);
			movieid.set(mydata[0].trim());
			context.write(movieid, myTitle);
		}
	}
	
	//The reducer class	
	public static class Reduce extends Reducer<Text,Text,Text,Text> {
		private Text result = new Text();
		private Text myKey = new Text();
		private String title = "";
		HashMap<String,String> map;
		private int num = 0;
		private int sum = 0;
		//note you can create a list here to store the values
		//added to your mapper class for map side join
		
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stu
			super.setup(context);
			map = new HashMap<String, String >();
			Configuration conf = context.getConfiguration();
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
					map.put(arr[0], arr[1]); //userid and gender
					line=br.readLine();
				}
			}
		}
		
		public void reduce(Text key, Iterable<Text> values,Context context ) throws IOException, InterruptedException {
	       
			for (Text val : values) 
	        {
	             String valueSplitted[] = val.toString().split("~");
	             
	             if(valueSplitted[0].equals("rat"))
	             {
	               String userid = valueSplitted[1].trim();
	               int rating = Integer.parseInt(valueSplitted[2].trim());
	               
	               if(map.containsKey(userid) && map.get(userid).equals("F")){
	            	   num++;
	            	   sum += rating;
	               }
	             }
	             else if(valueSplitted[0].equals("mov"))
	             {
	               title = valueSplitted[1].trim();
	             }
	        }
	        
			if(num!=0){
				double averageRating = (double)sum / (double)num;
				String convertedString = String.format("%.3f", averageRating);
				
				myKey.set(title);
				result.set("::"+convertedString);
				context.write(myKey,result);
			}
		}		
	}
	
	public static class Map3 extends Mapper<LongWritable, Text, Text, Text>{
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

	public static class Reduce2 extends Reducer<Text,Text,Text,Text> {
		
		public void reduce(Text key, Text value,Context context ) throws IOException, InterruptedException { 
	        
	        context.write(key,value);
		}		
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
		// TODO Auto-generated method stub
		
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		// get all args
		if (otherArgs.length != 4) {
			System.err.println("Usage: JoinExample <in1> <in2> <out1> <out2>");
			System.exit(2);
		}

		Job job = new Job(conf, "UserID");
		job.setJarByClass(Top5FemaleRating.class);


		job.setReducerClass(Reduce.class);

		// OPTIONAL :: uncomment the following line to add the Combiner
		// job.setCombinerClass(Reduce.class);


		MultipleInputs.addInputPath(job, new Path(otherArgs[0]), TextInputFormat.class ,
				Map1.class );

		MultipleInputs.addInputPath(job, new Path(otherArgs[1]),TextInputFormat.class,Map2.class );

		job.setOutputKeyClass(Text.class);
		// set output value type
		job.setOutputValueClass(Text.class);

		//set the HDFS path of the input data
		// set the HDFS path for the output 
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));
		
		final String NAME_NODE = "hdfs://sandbox.hortonworks.com:8020";
		job.addCacheFile(new URI(NAME_NODE
		+ "/user/hue/users.dat"));
		
		job.waitForCompletion(true);
		
	
		
		Job job2 = new Job(conf, "top5");
		job2.setJarByClass(Top5FemaleRating.class);
		job2.setMapperClass(Map3.class);
		job2.setReducerClass(Reduce2.class);
		
		// uncomment the following line to add the Combiner
		job2.setCombinerClass(Reduce2.class);
		// set output key type
		job2.setOutputKeyClass(Text.class);
		// set output value type
		job2.setOutputValueClass(Text.class);
		//set the HDFS path of the input data
		FileInputFormat.addInputPath(job2, new Path(otherArgs[2]));
		// set the HDFS path for the output
		FileOutputFormat.setOutputPath(job2, new Path(otherArgs[3]));
		//Wait till job completion
		System.exit(job2.waitForCompletion(true) ? 0 : 1);		// TODO Auto-generated method stub
		
	}
}
