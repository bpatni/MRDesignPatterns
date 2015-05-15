package mapreduce.patterns.summarization;

import java.io.Console;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import mrdp.utils.MRDPUtils;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MinMaxCountMapper extends Mapper<Object, Text, Text, MinMaxCountTuple> {
	//Output key and value pair
	private Text outUserId = new Text();
	private MinMaxCountTuple outTuple = new MinMaxCountTuple();
	
	//This object will be used to format the creation date string into a date object
	private final static SimpleDateFormat frmt = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.SSS");
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());
		
		String strDate;
		Date creationDate;
		String userId="0";
		try {
			//Extract the CreationDate to find min and max date
			strDate = parsed.get("CreationDate");
			//Parse String into Date object
			creationDate = frmt.parse(strDate);
		}
		catch(Exception e) {
			System.err.println("Error in parsing CreationDate!");
			System.err.println(value);
			creationDate=new Date();
			return;
		}
		try {
			//Extract the UserID since it is used for group by
			userId = parsed.get("UserId");
			//Set the out UserID
			outUserId.set(userId);
		}
		catch(Exception e) {
			System.err.println("Error in parsing UserID!");
			System.err.println(value);
			userId="99999999999";
			return;
		}
		
		
		//Set the minimum, maximum and count
		outTuple.setMin(creationDate);
		outTuple.setMax(creationDate);
		outTuple.setCount(1);
		
		
		//Write out the UserID and Output Tuple with Min, Max and Count
		context.write(outUserId, outTuple);
	}
}
