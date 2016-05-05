package com.sundoctor.quartz.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.CronExpression;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sundoctor.example.Constant;
import com.sundoctor.quartz.dao.QuartzDao;

@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {

	private static final String NULLSTRING = null;
	private static final Date NULLDATE = null;

	@Autowired
	private Scheduler scheduler;
	@Autowired
	private JobDetail jobDetail;

	@Autowired
	private QuartzDao quartzDao;

	
	public List<Map<String, Object>> getQrtzTriggers() {
		return quartzDao.getQrtzTriggers();
	}

	
	public void schedule(String cronExpression) {
		schedule(NULLSTRING, cronExpression);
	}

	
	public void schedule(String name, String cronExpression) {
		schedule(name, NULLSTRING, cronExpression);
	}

	
	public void schedule(String name, String group, String cronExpression) {
		try {
			schedule(name, group, new CronExpression(cronExpression));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	
	public void schedule(CronExpression cronExpression) {
		schedule(NULLSTRING, cronExpression);
	}

	
	public void schedule(String name, CronExpression cronExpression) {
		schedule(name, NULLSTRING, cronExpression);
	}

	
	public void schedule(String name, String group, CronExpression cronExpression) {

		if (isValidExpression(cronExpression)) {

			if (name == null || name.trim().equals("")) {
				name = UUID.randomUUID().toString();
			}

			CronTriggerImpl trigger = new CronTriggerImpl();
			trigger.setCronExpression(cronExpression);

			TriggerKey triggerKey = new TriggerKey(name, group);

			trigger.setJobName(jobDetail.getKey().getName());
			trigger.setKey(triggerKey);

			try {
				scheduler.addJob(jobDetail, true);
				if (scheduler.checkExists(triggerKey)) {
					scheduler.rescheduleJob(triggerKey, trigger);
				} else {
					scheduler.scheduleJob(trigger);
				}
			} catch (SchedulerException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	
	public void schedule(Date startTime) {
		schedule(startTime, NULLDATE);
	}

	
	public void schedule(Date startTime, String group) {
		schedule(startTime, NULLDATE, group);
	}

	
	public void schedule(String name, Date startTime) {
		schedule(name, startTime, NULLDATE);
	}

	
	public void schedule(String name, Date startTime, String group) {
		schedule(name, startTime, NULLDATE, group);
	}

	
	public void schedule(Date startTime, Date endTime) {
		schedule(startTime, endTime, 0);
	}

	
	public void schedule(Date startTime, Date endTime, String group) {
		schedule(startTime, endTime, 0, group);
	}

	
	public void schedule(String name, Date startTime, Date endTime) {
		schedule(name, startTime, endTime, 0);
	}

	
	public void schedule(String name, Date startTime, Date endTime, String group) {
		schedule(name, startTime, endTime, 0, group);
	}

	
	public void schedule(Date startTime, int repeatCount) {
		schedule(null, startTime, NULLDATE, 0);
	}

	
	public void schedule(Date startTime, Date endTime, int repeatCount) {
		schedule(null, startTime, endTime, 0);
	}

	
	public void schedule(Date startTime, Date endTime, int repeatCount, String group) {
		schedule(null, startTime, endTime, 0, group);
	}

	
	public void schedule(String name, Date startTime, Date endTime, int repeatCount) {
		schedule(name, startTime, endTime, 0, 0L);
	}

	
	public void schedule(String name, Date startTime, Date endTime, int repeatCount, String group) {
		schedule(name, startTime, endTime, 0, 0L, group);
	}

	
	public void schedule(Date startTime, int repeatCount, long repeatInterval) {
		schedule(null, startTime, NULLDATE, repeatCount, repeatInterval);
	}

	
	public void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval) {
		schedule(null, startTime, endTime, repeatCount, repeatInterval);
	}

	
	public void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval, String group) {
		schedule(null, startTime, endTime, repeatCount, repeatInterval, group);
	}

	
	public void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval) {
		schedule(name, startTime, endTime, repeatCount, repeatInterval, NULLSTRING);
	}

	
	public void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval, String group) {

		if (this.isValidExpression(startTime)) {

			if (name == null || name.trim().equals("")) {
				name = UUID.randomUUID().toString();
			}

			TriggerKey triggerKey = new TriggerKey(name, group);

			SimpleTriggerImpl trigger = new SimpleTriggerImpl();
			trigger.setKey(triggerKey);
			trigger.setJobName(jobDetail.getKey().getName());

			trigger.setStartTime(startTime);
			trigger.setEndTime(endTime);
			trigger.setRepeatCount(repeatCount);
			trigger.setRepeatInterval(repeatInterval);

			try {
				scheduler.addJob(jobDetail, true);
				if (scheduler.checkExists(triggerKey)) {
					scheduler.rescheduleJob(triggerKey, trigger);
				} else {
					scheduler.scheduleJob(trigger);
				}
			} catch (SchedulerException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	
	public void schedule(Map<String, Object> map) {

		// 设置名称
		String name = MapUtils.getString(map, Constant.TRIGGERNAME);
		if (StringUtils.isEmpty(StringUtils.trim(name))) {
			name = UUID.randomUUID().toString();
		} else {
			// 在名称后添加UUID，保证名称的唯一性
			name += "&" + UUID.randomUUID().toString();
		}

		// 设置Trigger分组
		String group = MapUtils.getString(map, Constant.TRIGGERGROUP);
		if (StringUtils.isEmpty(group)) {
			group = Scheduler.DEFAULT_GROUP;
		}

		TriggerKey triggerKey = new TriggerKey(name, group);

		// 实例化SimpleTrigger
		SimpleTriggerImpl trigger = new SimpleTriggerImpl();

		trigger.setJobName(jobDetail.getKey().getName());
		trigger.setKey(triggerKey);
		trigger.setRepeatInterval(1000L);

		// 设置开始时间
		String temp = MapUtils.getString(map, Constant.STARTTIME);
		if (StringUtils.isNotEmpty(temp)) {
			try {
				trigger.setStartTime(DateUtils.parseDate(temp, new String[] { "yyyy-MM-dd HH:mm" }));
			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		}

		// 设置结束时间
		temp = MapUtils.getString(map, Constant.ENDTIME);
		if (StringUtils.isNotEmpty(temp)) {
			try {
				trigger.setEndTime(DateUtils.parseDate(temp, new String[] { "yyyy-MM-dd HH:mm" }));
			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		}

		// 设置执行次数
		temp = MapUtils.getString(map, Constant.REPEATCOUNT);
		if (StringUtils.isNotEmpty(temp) && NumberUtils.toInt(temp) > 0) {
			trigger.setRepeatCount(NumberUtils.toInt(temp));
		}

		// 设置执行时间间隔
		temp = MapUtils.getString(map, Constant.REPEATINTERVEL);
		if (StringUtils.isNotEmpty(temp) && NumberUtils.toLong(temp) > 0) {
			trigger.setRepeatInterval(NumberUtils.toLong(temp) * 1000);
		}

		try {
			scheduler.addJob(jobDetail, true);
			if (scheduler.checkExists(triggerKey)) {
				scheduler.rescheduleJob(triggerKey, trigger);
			} else {
				scheduler.scheduleJob(trigger);
			}
		} catch (SchedulerException e) {
			throw new IllegalArgumentException(e);
		}
	}

	
	public void pauseTrigger(String triggerName) {
		pauseTrigger(triggerName, NULLSTRING);
	}

	
	public void pauseTrigger(String triggerName, String group) {
		try {
			scheduler.pauseTrigger(new TriggerKey(triggerName, group));// 停止触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void resumeTrigger(String triggerName) {
		resumeTrigger(triggerName, NULLSTRING);
	}

	
	public void resumeTrigger(String triggerName, String group) {
		try {
			scheduler.resumeTrigger(new TriggerKey(triggerName, group));// 重启触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	
	public boolean removeTrigdger(String triggerName) {
		return removeTrigdger(triggerName, NULLSTRING);
	}

	
	public boolean removeTrigdger(String triggerName, String group) {
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		try {
			scheduler.pauseTrigger(triggerKey);// 停止触发器
			return scheduler.unscheduleJob(triggerKey);// 移除触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isValidExpression(final CronExpression cronExpression) {

		CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression(cronExpression);

		Date date = trigger.computeFirstFireTime(null);

		return date != null && date.after(new Date());
	}

	private boolean isValidExpression(final Date startTime) {

		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		trigger.setStartTime(startTime);

		Date date = trigger.computeFirstFireTime(null);

		return date != null && date.after(new Date());
	}
}
