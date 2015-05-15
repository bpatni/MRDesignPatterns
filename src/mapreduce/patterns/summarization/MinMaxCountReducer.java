package mapreduce.patterns.summarization;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MinMaxCountReducer extends Reducer<Text, MinMaxCountTuple, Text, MinMaxCountTuple> {

	//Output value Writable
	private MinMaxCountTuple result = new MinMaxCountTuple();
	
	public void reduce(Text key, Iterable<MinMaxCountTuple> values, Context context) throws IOException, InterruptedException {
		//initialize the result
		result.setMin(new Date());
		result.setMax(new Date());
		result.setCount(0);
		
		int sum=0;
		
		//iterate through all values for the key
		for (MinMaxCountTuple val: values) {
			try
			{
			//if the value of min is null or less than result then assign min val to result
			if (result.getMin()==null || val.getMin().compareTo(result.getMin()) < 0)
				result.setMin(val.getMin());
			//if the value of max is null or greater than the result then assign max val to result
			if (result.getMax()== null || val.getMax().compareTo(result.getMax()) > 0)
				result.setMax(val.getMax());
			//Add count to the sum
			sum += result.getCount();
			}
			catch(Exception e) {
				//
			}
		}
		result.setCount(sum);
		if (key!=null)
			context.write(key, result);
	}
	
}
