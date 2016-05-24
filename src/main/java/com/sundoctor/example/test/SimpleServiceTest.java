package com.sundoctor.example.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.common.base.dao.BaseDaoImpl;
import com.common.base.dao.Page;
import com.sundoctor.example.model.Customert;
import com.sundoctor.example.service.SimpleService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"classpath:spring/applicationContext.xml",
		"classpath:spring/applicationContext-quartz.xml"}) 

public class SimpleServiceTest {

	/*@Resource
	private SchedulerService schedulerService;*/
	
	@Resource
	private SimpleService simpleService;
	
	@Resource
	private BaseDaoImpl baseDaoImpl;
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleServiceTest.class);

	/*public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}*/
	
	public void setSimpleService(SimpleService simpleService) {
		this.simpleService = simpleService;
	}
	
	public void setBaseDaoImpl(BaseDaoImpl baseDaoImpl) {
		this.baseDaoImpl = baseDaoImpl;
	}



	@Test
	public void test() throws Exception{
		//执行业务逻辑...
		
		//设置高度任务
		//每10秒中执行调试一次
		/*schedulerService.schedule("0/10 * * ? * * *"); 
		
		Date startTime = this.parse("2009-06-01 21:50:00");
		Date endTime =  this.parse("2009-06-01 21:55:00");
        
		//2009-06-01 21:50:00开始执行调度
		schedulerService.schedule(startTime);

		//2009-06-01 21:50:00开始执行调度，2009-06-01 21:55:00结束执行调试
		schedulerService.schedule(startTime,endTime);
		
		//2009-06-01 21:50:00开始执行调度，执行5次结束
		schedulerService.schedule(startTime,null,5);

		//2009-06-01 21:50:00开始执行调度，每隔20秒执行一次，执行5次结束
		schedulerService.schedule(startTime,null,5,20);*/
		
		//等等，查看com.sundoctor.quartz.service.SchedulerService
		Map<String,Object> param=new HashMap<String, Object>();
		/*param.put("id", "1");
		List<Customert> list=baseDaoImpl.findByNameSql("testHql", param, Customert.class);
		for (int i = 0; i < list.size(); i++) {
			Customert customer=list.get(i);
			System.out.println("age:"+customer.getAge()+"-- name:"+customer.getName()+"-- email:"+customer.getEmail());
			logger.info("Customer={}", customer);
		}*/
		
		//查询分页
		Page<Customert> page=new Page<Customert>();
		page.setCurrentPage(1);
		page.setPageSize(1);
		baseDaoImpl.findByNameSqlPage("test", param, page, Customert.class);
		List<Customert> result=page.getResult();
		for (int i = 0; i < result.size(); i++) {
			Customert customer=result.get(i);
			System.out.println("age:"+customer.getAge()+"-- name:"+customer.getName()+"-- email:"+customer.getEmail());
			logger.info("Customer={}", customer);
		}
		System.out.println("分页总记录数："+page.getTotalsCount());
		System.out.println("Junit test!");
		//simpleService.testMethod("", "");
	}
	
	private Date parse(String dateStr){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
