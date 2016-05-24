package com.common.base.dao;

import java.util.Map;

public class ConfigQueryXmlParser implements ConfigQueryXml {

	@Override
	public String getDealConfigQueryParam(String sql,
			Map<String, Object> param) {
		StringBuilder sb = new StringBuilder(sql);
		sql = dealQueryStringForColon(sb, sql, param);
		return sql;
	}
	
	/**
	 * 处理冒号(:)条件变量
	 * @param sb
	 * @param queryString
	 * @param conditionMap
	 * @return
	 */
	private String dealQueryStringForColon(StringBuilder sb,
			String queryString, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		int index = -1;	
		int end = 0;
		int begin = 0;
		int paramEnd = 0;
		String paramString = null;
		while((index = queryString.indexOf(":", end)) != -1){
			begin = queryString.lastIndexOf("{", index);
			end = queryString.indexOf("}", index);
			paramEnd = queryString.indexOf(" ",index+1);
			paramString = queryString.substring(index+1,paramEnd);
			if(paramMap==null || paramMap.get(paramString)==null){
				sb.replace(begin, end+1, "");
				end=begin;	
			}else{
				sb.deleteCharAt(begin);
				sb.deleteCharAt(end-1);
			}
			queryString = sb.toString();
		}
		return queryString;
	}

}
