package mapreduce.patterns.summarization;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import mrdp.utils.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.commons.lang.StringEscapeUtils;;

public class WordCountMapReduce {

public static void main(String[] args) 
		throws IOException, InterruptedException, ClassNotFoundException 
{
	Configuration conf = new Configuration();
	//String otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	//if (otherArgs.length!=2) {
	//	System.err.println("Usage: CommentWordCount <in> <out>");
	//	System.exit(2);
	//}
	
	Job job = new Job(conf, "StackOverflow Comment Word Count");
	job.setJarByClass(WordCountMapReduce.class);
	job.setMapperClass(WordCountMapper.class);
	job.setCombinerClass(WordCountReducer.class);
	job.setReducerClass(WordCountReducer.class);
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(IntWritable.class);
	FileInputFormat.addInputPath(job, new Path(args[0]));
	FileOutputFormat.setOutputPath(job, new Path(args[1]));
	
	System.exit(job.waitForCompletion(true) ? 0: 1);
}

public static class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
	private final IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException
	{
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());
		String txt = parsed.get("Text");
		
		if (txt==null) {
			//skip this record
			return;
		}
		
		txt = StringEscapeUtils.unescapeHtml(txt.toLowerCase());
		txt = txt.replaceAll("'", "");
		txt = txt.replaceAll("[^a-zA-Z]", " ");
		
		StringTokenizer itr = new StringTokenizer(txt);
		while(itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
		}
	}
}
	
public static class WordCountReducer 
extends Reducer<Text, IntWritable, Text, IntWritable> 
{
	private IntWritable result = new IntWritable();
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context) 
	throws IOException, InterruptedException
	{
		int sum = 0;
		for(IntWritable val: values) {
			sum+=val.get();
		}
		result.set(sum);
		context.write(key, result);
	}
}

}
