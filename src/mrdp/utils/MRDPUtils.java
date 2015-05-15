package mrdp.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.fs.Path;


public class MRDPUtils {
	
	public static Map<String, String>transformXmlToMap(String xml) {
			Map<String, String> map = new HashMap<String, String>();
			try
			{
				String[] tokens = xml.trim().substring(5, xml.trim().length()-3).split("\"");
				for(int i=0; i<tokens.length; i++)
				{
					String key = tokens[i].trim();
					String value = tokens[i+1].trim();
					
					map.put(key.substring(0, key.length()-1), value);
				}
			}
			catch(StringIndexOutOfBoundsException e  ) {
				System.err.println(xml);
			}
			catch(Exception e  ) {
				System.err.println(xml);
			}
			
			return map;
	}
		
		
}
	
