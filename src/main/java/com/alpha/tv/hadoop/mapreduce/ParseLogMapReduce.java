package com.alpha.tv.hadoop.mapreduce;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alpha.tv.entity.A;
import com.alpha.tv.entity.WIC;


/*
 * describe:	解析日志文件提取有用信息
 * author：		tan_alpha
 * create time:	2018-03-04
 * */
public class ParseLogMapReduce extends Configured implements Tool {

	private Logger _log = LogManager.getLogger(ParseLogMapReduce.class);
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub 
		
		//获取配置文件
		Configuration conf = getConf();
		
		Path outPath = new Path(args[1]);
		FileSystem fileSystem = outPath.getFileSystem(conf);
		if (fileSystem.exists(outPath)){
			fileSystem.delete(outPath);
		}

		Job job = new Job(conf,"ParseLogMapReduce");
		job.setJarByClass(ParseLogMapReduce.class);
				
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileInputFormat.addInputPath(job, new Path(args[1]));

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setNumReduceTasks(0);
		
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	public static void main(String[] args) throws Exception {
    	int ec = ToolRunner.run(new Configuration(),new ParseLogMapReduce(), args);
    	System.exit(ec);
	}
	
	/*
	 * 解析日志文件
	 * */
	public static class ParseLogMap extends Mapper<LongWritable, Text, Text, Text>{
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			try {
				if(value == null){ return; }
				WIC wic = new WIC();
				
				if (!wic.Parse(value.toString())){ return; }
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (int i = 0; i < wic.aList.size(); i++) {
					A eA = wic.aList.get(i);
					String keyString = String.format("%s@%s", wic.getStbNumString(),wic.getStartDateString());
					String valueString = String.format("%s@%s@%s@%s@%s@%d",
							wic.getStbNumString(),
							sdf.format(eA.getStartTimeDate()),
							sdf.format(eA.getEndTimeDate()),
							eA.getpString(),
							eA.getSnString(),
							eA.getDurationLong());
					context.write(new Text(keyString),new Text(valueString));
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
