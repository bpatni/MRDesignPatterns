package mapreduce.patterns.summarization;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;


public class MedianStdDevTuple implements Writable {

	private float median = 0.0f;
	private float stdDev = 0.0f;
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(median);
		out.writeFloat(stdDev);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		median = in.readFloat();
		stdDev = in.readFloat();
	}

	public void setMedian(float median) {
		this.median = median;
	}

	public float getMedian() {
		return this.median;
	}

	
	public void setStdDev(float stdDev) {
		this.stdDev = stdDev;
	}

	public float getStdDev() {
		return this.stdDev;
	}

	public String toString() {
		
		String retString = "";
		try
		{
		retString = median + "\t" + stdDev;
		}
		catch (Exception e) {}
		return retString;
	}
	
}
