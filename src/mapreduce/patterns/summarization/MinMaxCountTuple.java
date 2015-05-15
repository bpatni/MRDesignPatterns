package mapreduce.patterns.summarization;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.Writable;

public class MinMaxCountTuple implements Writable {

	private Date min = new Date();
	private Date max = new Date();
	private long count = 0;
	
	private final static SimpleDateFormat frmt = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.SSS");
	
	public Date getMin() {
		return min;
	}
	
	public void setMin(Date min) {
		this.min = min;
	}
	
	public Date getMax() {
		return max;
	}
	
	public void setMax(Date max) {
		this.max = max;
	}	
	
	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public void readFields(DataInput in) throws IOException {
		//Read the data out in the order it is written
		//Creating the new Date objects from the linux timestamp
		min = new Date(in.readLong());
		max = new Date(in.readLong());
		count = in.readLong();
	}
	
	public void write(DataOutput out) throws IOException {
		if (min==null) min = new Date();
		if (max==null) max = new Date();
		//Write the data out in the order it is written
		//using the Unix date timestamp to represent the date
		out.writeLong(min.getTime());
		out.writeLong(max.getTime());
		out.writeLong(count);
	}
	
	public String toString() {
		
		String retString = "";
		try
		{
		retString = frmt.format(min) + "\t" + frmt.format(max) + "\t" + count;
		}
		catch (Exception e) {}
		return retString;
	}
}
