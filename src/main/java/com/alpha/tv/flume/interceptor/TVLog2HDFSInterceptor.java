package com.alpha.tv.flume.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;


public class TVLog2HDFSInterceptor implements Interceptor {

	private String BASENAME_KEY = "basename";

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Event intercept(Event arg0) {
		// TODO Auto-generated method stub
		/*
		 * 1、验证event内容及正文内容 2、获取头部中的basenaem 3、提取basename中的有效信息，否则跳过
		 */
		if (arg0 == null || arg0.getHeaders() == null || arg0.getBody() == null)
			return null;

		Map<String, String> headMap = arg0.getHeaders();
		String basenameString = headMap.get(BASENAME_KEY);
		if (basenameString == null)
			return null;
		
		String[] splitStrings = SplitBasename(basenameString);
		if(splitStrings == null || splitStrings.length !=6){
			return null;
		}
		headMap.put("YYYY", splitStrings[0]);
		headMap.put("MM", splitStrings[1]);
		headMap.put("DD", splitStrings[2]);
		headMap.put("HH", splitStrings[3]);
		headMap.put("mm", splitStrings[4]);
		headMap.put("ss", splitStrings[5]);
		return arg0;
	}

	@Override
	public List<Event> intercept(List<Event> arg0) {
		// TODO Auto-generated method stub
		List<Event> intercepted = new ArrayList<Event>();
	    for (Event event : arg0) {
	      Event interceptedEvent = intercept(event);
	      if (interceptedEvent != null) {
	        intercepted.add(interceptedEvent);
	      }
	    }
	    return intercepted;
	}

	/*
	 * describe：	分割字符串，按照年月日时分秒的顺序返回数组。例如：ars10767@20120917013000.txt
	 * author：		tan_alpha
	 * create time:	2018-03-03
	 * */
	private String[] SplitBasename(String basename) {
		if (basename == null)
			return null;
		List<String> resList = new ArrayList<String>();
		Pattern pattern = Pattern.compile("^ars\\d{5,9}@(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2}).txt$");
		Matcher matcher = pattern.matcher(basename);
		if (matcher.find()) {
			int count = matcher.groupCount();
			for (int i = 1; i <= count; i++) {
				resList.add(matcher.group(i));
			}
		}
		return resList.toArray(new String[0]);
	}
	
	  /**
	   * Builder which builds new instances of the TimestampInterceptor.
	   */
	  public static class Builder implements Interceptor.Builder {

	    @Override
	    public Interceptor build() {
	      return new TVLog2HDFSInterceptor();
	    }

	    @Override
	    public void configure(Context context) {
	    }
	  }

	public static void main(String[] args) {
		//String[] resStrings = SplitBasename("ars10767@20120917013000.txt");
		System.out.print("hello world!");
	}
}
