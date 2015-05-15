package mapreduce.patterns.summarization;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class MedianStdDevReducer extends Reducer<IntWritable, IntWritable, IntWritable, MedianStdDevTuple> {

	private MedianStdDevTuple result = new MedianStdDevTuple();
	private ArrayList<Float> commentLengths = new ArrayList<Float>(); 
	
	public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{

		float sum = 0;
		float count = 0;
		commentLengths.clear();
		
		//iterate thru the values
		for(IntWritable val: values) {
			commentLengths.add((float) val.get());
			sum+=val.get();
			++count;
		}
		
		//Sort collection to calculate median
		Collections.sort(commentLengths);
		
		//If the collection length is even the average the 2 middle numbers
		if (count % 2 ==0) {
			result.setMedian((commentLengths.get((int) count/2 -1) + commentLengths.get((int) count))/2 );
		}
		else {
			result.setMedian(commentLengths.get((int) count)/2);
		}
		
		//calculate standard deviation
		float mean = sum / count;
		float sumOfSquares = 0.0f;
		
		for(Float f : commentLengths) {
			sumOfSquares += (f-mean) * (f - mean);
		}
		
		result.setStdDev((float) Math.sqrt(sumOfSquares / (count - 1)));
		context.write(key, result);
	}
	
	
}
