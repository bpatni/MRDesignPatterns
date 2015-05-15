package mapreduce.patterns.summarization;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MinMaxCountDriver {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		//Automatically delete output folder if exists
		Files.deleteIfExists(Paths.get(args[1]));
		
		Configuration conf = new Configuration();
		@SuppressWarnings("deprecation")
		Job job = new Job(conf);
		
		job.setJarByClass(MinMaxCountDriver.class);
		job.setMapperClass(MinMaxCountMapper.class);
		job.setCombinerClass(MinMaxCountReducer.class);
		job.setReducerClass(MinMaxCountReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MinMaxCountTuple.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		System.exit(job.waitForCompletion(true) ? 0: 1);
	}
}
