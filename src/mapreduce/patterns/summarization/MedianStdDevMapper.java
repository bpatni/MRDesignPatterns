package mapreduce.patterns.summarization;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import mrdp.utils.MRDPUtils;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MedianStdDevMapper extends Mapper<Object, Text, IntWritable, IntWritable> {

	private IntWritable outHour = new IntWritable();
	private IntWritable outCommentLength = new IntWritable();
	
	private final static SimpleDateFormat frmt = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.SSS");
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());
		
		//Extract CreationDate Field
		String strCreationDate;
		Date creationDate;
		try {
			strCreationDate = parsed.get("CreationDate");
			creationDate = frmt.parse(strCreationDate);
			outHour.set(creationDate.getHours());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
			return;			
		}
		
		//Extract Comment to find the length
		String strComment = parsed.get("Text");
		
		//Set the comment length
		outCommentLength.set(strComment.length());
		
		//Write out the hour and comment length
		context.write(outHour, outCommentLength);
	}
}
