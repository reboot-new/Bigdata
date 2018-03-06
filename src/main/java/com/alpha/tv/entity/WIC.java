package com.alpha.tv.entity;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class WIC {

	private String stbNumString;
	private String startDateString;
	
	public List<A> aList;
	
	public WIC() {
		// TODO Auto-generated constructor stub
		this(null,null);
	}
	
	public WIC(String stbNum_,String startDate){
		this.stbNumString = stbNum_;
		this.startDateString = startDate;
		this.aList = new ArrayList<A>();
	}

	public String getStbNumString() {
		return stbNumString;
	}


	public void setStbNumString(String stbNumString) {
		this.stbNumString = stbNumString;
	}
	
	/*
	 * 日志内容转化为实体
	 * */
	public Boolean Parse(String lineValue_) throws DocumentException, ParseException{
		//<GHApp><WIC cardNum="115255894" stbNum="03061001270661765" date="2012-09-16" pageWidgetVersion="1.0"><A e="23:57:38" s="23:52:45" n="124" t="2" pi="707" p="%E4%B8%AD%E5%9B%BD%E6%96%87%E8%89%BA(37)" sn="CCTV-4 中文国际" /><A e="23:57:45" s="23:57:38" n="123" t="2" pi="871" p="%E6%AC%A2%E4%B9%90%E4%B8%80%E5%AE%B6%E4%BA%B2(36)" sn="CCTV-3 综艺" /><A e="23:57:54" s="23:57:46" n="122" t="3" pi="525" p="%E5%91%A8%E6%9C%AB%E7%89%B9%E4%BE%9B(486)" sn="CCTV-2 财经" /><A e="23:58:12" s="23:57:54" n="121" t="3" pi="678" p="%E9%A6%96%E5%B8%AD%E5%A4%9C%E8%AF%9D(25)" sn="CCTV-1 综合" /><A e="23:58:20" s="23:58:12" n="101" t="1" pi="800" p="%E5%85%89%E9%98%B4" sn="BTV北京卫视" /><A e="23:59:01" s="23:58:21" n="102" t="1" pi="935" p="%E6%AF%8F%E6%97%A5%E6%96%87%E5%A8%B1%E6%92%AD%E6%8A%A5" sn="BTV文艺" /><A e="23:59:20" s="23:59:09" n="103" t="1" pi="713" p="%E6%B3%95%E6%B2%BB%E8%BF%9B%E8%A1%8C%E6%97%B6" sn="BTV科教" /></WIC></GHApp>
		lineValue_ = "<GHApp><WIC cardNum=\"115255894\" stbNum=\"03061001270661765\" date=\"2012-09-16\" pageWidgetVersion=\"1.0\"><A e=\"23:57:38\" s=\"23:52:45\" n=\"124\" t=\"2\" pi=\"707\" p=\"%E4%B8%AD%E5%9B%BD%E6%96%87%E8%89%BA(37)\" sn=\"CCTV-4 中文国际\" /><A e=\"23:57:45\" s=\"23:57:38\" n=\"123\" t=\"2\" pi=\"871\" p=\"%E6%AC%A2%E4%B9%90%E4%B8%80%E5%AE%B6%E4%BA%B2(36)\" sn=\"CCTV-3 综艺\" /><A e=\"23:57:54\" s=\"23:57:46\" n=\"122\" t=\"3\" pi=\"525\" p=\"%E5%91%A8%E6%9C%AB%E7%89%B9%E4%BE%9B(486)\" sn=\"CCTV-2 财经\" /><A e=\"23:58:12\" s=\"23:57:54\" n=\"121\" t=\"3\" pi=\"678\" p=\"%E9%A6%96%E5%B8%AD%E5%A4%9C%E8%AF%9D(25)\" sn=\"CCTV-1 综合\" /><A e=\"23:58:20\" s=\"23:58:12\" n=\"101\" t=\"1\" pi=\"800\" p=\"%E5%85%89%E9%98%B4\" sn=\"BTV北京卫视\" /><A e=\"23:59:01\" s=\"23:58:21\" n=\"102\" t=\"1\" pi=\"935\" p=\"%E6%AF%8F%E6%97%A5%E6%96%87%E5%A8%B1%E6%92%AD%E6%8A%A5\" sn=\"BTV文艺\" /><A e=\"23:59:20\" s=\"23:59:09\" n=\"103\" t=\"1\" pi=\"713\" p=\"%E6%B3%95%E6%B2%BB%E8%BF%9B%E8%A1%8C%E6%97%B6\" sn=\"BTV科教\" /></WIC></GHApp>";
		
		try {
			Document document = DocumentHelper.parseText(lineValue_);
			Element root = document.getRootElement();
			Element WICElement = root.element("WIC");
			/*获取机顶盒编号、开始日期*/
			String stbNumString = WICElement.attributeValue("stbNum");
			String startDateString = WICElement.attributeValue("date");
			this.stbNumString = stbNumString;
			this.startDateString = startDateString;
			SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Iterator<Element> it = WICElement.elementIterator("A"); it.hasNext();) {
		        Element foo = it.next();
		        
		        /*获取开始时间、结束时间、节目、频道*/
		        String sTimeString = foo.attributeValue("s");//开始时间
		        Date sDate = dateSDF.parse(startDateString + " "+sTimeString);
		        String eTimeString = foo.attributeValue("e");//结束时间
		        Date eDate = dateSDF.parse(startDateString + " "+eTimeString);
		        String pString = URLDecoder.decode(foo.attributeValue("p"),"utf-8");//节目
		        String snString = foo.attributeValue("sn");//频道
		        
		        //计算时长,可能存在跨天情况
		        if(!eDate.after(sDate)){
		        	Calendar c = Calendar.getInstance();
		            c.setTime(eDate);
		            c.add(Calendar.DAY_OF_MONTH, 1);//+1天
		        }
		        Long durationLong = (eDate.getTime() -sDate.getTime())/1000;
		        aList.add(new A(sDate,eDate,pString,snString,durationLong));
		    }
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public String getStartDateString() {
		return startDateString;
	}

	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}

}
