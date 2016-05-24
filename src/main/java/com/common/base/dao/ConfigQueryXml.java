package com.common.base.dao;

import java.util.Map;

/**
 * 
 * 版权所有：2016-武当派
 * 项目名称：quartzMonitor   
 *
 * 类描述：
 * 类名称：com.common.base.dao.ConfigQueryXml     
 * 创建人：林杰
 * 创建时间：2016-5-24 上午11:19:13   
 * 修改人：
 * 修改时间：2016-5-24 上午11:19:13   
 * 修改备注：   
 * @version   V1.0
 */
public interface ConfigQueryXml {
	
	/**
	 * 
	 * @Title: getDealConfigQueryParam
	 * @Description: TODO(解析sql参数)
	 * @author: linjie
	 * @date: 2016-5-24
	 * @param sql
	 * @param param
	 * @return
	 */
	public String getDealConfigQueryParam(String sql, Map<String,Object> param);

}
