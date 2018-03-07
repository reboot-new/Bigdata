package com.alpha.tv.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 统计总的收视人数粒度分钟
 * */
public class AvgViewershipMapReduce extends Configured implements Tool {

	public AvgViewershipMapReduce() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
    	int ec = ToolRunner.run(new Configuration(),new AvgViewershipMapReduce(), args);
    	System.exit(ec);
	}
	
	public static class AvgViewershipMapper extends Mapper<Text, Text, Text, Text>{
		@Override
		protected void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			/*
			 * 输入	机顶盒编号@日期	机顶盒编号@开始时间@结束时间@节目@频道@持续时间
			 * */
			String valueString = value.toString();
			if (valueString == null|| valueString.length() == 0)
				return;
			
			
			
			context.write(new Text(), new Text());
		}
	}
	
	public static class AvgViewershipReduce extends Reducer<Text, Text, NullWritable, Text>{
		
		@Override
		protected void reduce(Text inputKey, Iterable<Text> inputValue, Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			
			context.write(NullWritable.get(), new Text());
		}
	}
}
