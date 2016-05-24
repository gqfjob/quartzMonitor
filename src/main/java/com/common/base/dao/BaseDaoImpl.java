package com.common.base.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.engine.spi.NamedQueryDefinition;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.sundoctor.example.test.SimpleServiceTest;

 
  
/** 
 * <b>function:</b> 删除改查组件实现类 
 * @project NetWorkService 
 * @package com.hhh.dao  
 * @fileName BaseDAOImpl.java 
 * @createDate 2010-8-2 下午05:58:45 
 * @author hoojo 
 * @email hoojo_@126.com 
 * @blog http://blog.csdn.net/IBM_hoojo 
 */  
@SuppressWarnings("unchecked")
@Repository("baseDaoImpl")
public class BaseDaoImpl extends HibernateDaoSupport implements IBaseDao {  
	
	private final String baseCountSql="select count(*) from ({0}) t";
	private ConfigQueryXml configQueryXml;
	
	private static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);
      
	@Autowired
	public BaseDaoImpl(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
		this.configQueryXml=new ConfigQueryXmlParser();
	}
	
    /** 
     * <b>function:</b> 增加一个entity对象，返回是否添加成功 
     * @createDate 2010-8-2 下午05:28:38 
     * @author hoojo 
     * @param <T> 对象类型 
     * @param entity 对象 
     * @return boolean true/false 
     * @throws Exception 
     */  
    public <T> boolean add(T entity) throws Exception {  
        boolean bo = false;  
        try {  
            Serializable io = this.getHibernateTemplate().save(entity);  
            if (io != null) {  
                bo = true;  
            }  
        } catch (Exception e) {  
            bo = false;  
            throw new RuntimeException(e);  
        }  
        return bo;  
    }  
      
    /** 
     * <b>function:</b> 添加一个entity对象，返回添加对象的Integer类型的主键 
     * @createDate 2010-8-2 下午05:29:39 
     * @author hoojo 
     * @param <T> 对象类型 
     * @param entity 将要添加的对象 
     * @return Integer 返回主键 
     * @throws Exception 
     */  
    public <T> Integer addAndGetId4Integer(T entity) throws Exception {  
        Integer id = null;  
        try {  
            id = (Integer) this.getHibernateTemplate().save(entity);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return id;  
    }  
      
    /** 
     * <b>function:</b> 添加一个对象并且返回该对象的String类型的主键 
     * @createDate 2010-8-2 下午05:31:32 
     * @author hoojo 
     * @param <T> 对象类型 
     * @param entity 将要添加的对象 
     * @return String 返回的主键 
     * @throws Exception 
     */  
    public <T> String addAndGetId4String(T entity) throws Exception {  
        String id = null;  
        try {  
            id = (String) this.getHibernateTemplate().save(entity);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return id;  
    }  
      
    /** 
     * <b>function:</b> 修改entity对象，返回是否修改成功 
     * @createDate 2010-8-2 下午05:35:47 
     * @author hoojo 
     * @param <T> 对象类型 
     * @param entity 将要修改的对象 
     * @return boolean true/false 是否修改成功 
     * @throws Exception 
     */  
    public <T> boolean edit(T entity) throws Exception {  
        boolean bo = false;  
        try {  
            this.getHibernateTemplate().update(entity);  
            bo = true;  
        } catch (Exception e) {  
            bo = false;  
            throw new RuntimeException(e);  
        }  
        return bo;  
    }  
      
    /** 
     * <b>function:</b> 传入hql语句执行修改，返回是否修改成功 
     * @createDate 2010-8-2 下午05:36:31 
     * @author hoojo 
     * @param hql 查询的hql语句 
     * @return boolean true/false 返回是否修改成功 
     * @throws Exception 
     */  
    public boolean edit(String hql) throws Exception {  
        boolean bo = false;  
        try {  
            int count = this.getHibernateTemplate().bulkUpdate(hql);  
            bo = count > 0 ? true : false;  
        } catch (Exception e) {  
            bo = false;  
            throw new RuntimeException(e);  
        }  
        return bo;  
    }  
      
    /** 
     * <b>function:</b> 执行修改的hql语句，返回修改的行数 
     * @createDate 2010-8-2 下午05:38:58 
     * @author hoojo 
     * @param hql 修改语句 
     * @return int 返回修改的行数 
     * @throws Exception 
     */  
    public int editByHql(String hql) throws Exception {  
        int count = 0;  
        try {  
            count = this.getHibernateTemplate().bulkUpdate(hql);              
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return count;  
    }  
      
    /** 
     * <b>function:</b> 传入hql语句执行 
     * @createDate 2010-8-2 下午04:42:26 
     * @author hoojo 
     * @param hql String hql语句 
     * @return int 影响行数 
     * @throws Exception 
     */  
    public int executeByHql(String hql) throws Exception {  
        try {  
            return this.getHibernateTemplate().bulkUpdate(hql);           
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
      
    /** 
     * <b>function:</b> 传入hql语句执行查询，返回list集合 
     * @createDate 2010-8-3 上午10:00:34 
     * @author hoojo 
     * @param hql 查询的hql语句 
     * @return List集合 
     * @throws Exception 
     */  
    public <T> List<T> findByHql(String hql) throws Exception {  
        List list = null;  
        try {  
            list = (List<T>) this.getHibernateTemplate().find(hql);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return list;  
    }  
      
    /** 
     * <b>function:</b> 执行原生态的sql语句，添加、删除、修改语句 
     * @createDate 2010-8-2 下午05:33:42 
     * @author hoojo 
     * @param sql 将要执行的sql语句 
     * @return int 
     * @throws Exception 
     */  
    public int executeBySql(String sql) throws Exception {  
        try { 
            return this.currentSession().createSQLQuery(sql).executeUpdate();             
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
      
    /** 
     * <b>function:</b> 传入sql语句执行查询，返回list集合 
     * @createDate 2010-8-3 上午10:00:34 
     * @author hoojo 
     * @param sql 查询的sql语句 
     * @return List集合 
     * @throws Exception 
     */  
    public <T> List<T> findBySql(String sql) throws Exception {  
        List list = null;  
        try {  
            list = (List<T>) this.currentSession().createSQLQuery(sql).list();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return list;  
    } 
    
    @SuppressWarnings("rawtypes")
	public int executeByNameSql(final String sqlName,final Map<String,Object> param){
    	return this.getHibernateTemplate().execute(new HibernateCallback() {
    		public Object doInHibernate(Session session) {
    			SQLQuery query=session.createSQLQuery(sqlName);
    			setParam(query, param);
    			return query.executeUpdate();
    		}
    	});
    }
    
    
    
    /**
     * 
     * @Title: findByNameSql
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @author: linjie
     * @date: 2016-5-23
     * @param sqlName
     * @return
     */
    @SuppressWarnings("rawtypes")
	public <T> List<T> findByNameSql(final String sqlName,final Map<String,Object> param, final Class clazz){
    	SessionFactoryImpl factory=(SessionFactoryImpl)this.getHibernateTemplate().getSessionFactory();
    	final NamedQueryDefinition nqd=factory.getNamedSQLQuery(sqlName);
    	if(nqd==null){
    		this.logger.error("没有找到Sql定义,或者对应的为Hql语句!");
    	}
    	return (List<T>)this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				//SessionImplementor sessionImp=(SessionImplementor)session;
				//Query query=session.getNamedQuery(sqlName);
				//sessionImp.getFactory().getNamedQuery(sqlName);
				String sql=nqd.getQueryString();
				sql=configQueryXml.getDealConfigQueryParam(sql, param);
				System.out.println("执行Sql:"+sql);
				SQLQuery query=session.createSQLQuery(sql);
		    	setParam(query, param);
		    	if(clazz != null){
					query.setResultTransformer(new EscColumnToBean(clazz));
				}
				List list = query.list();
				return list;
			}
		});
    }
    
    /**
     * 
     * @Title: findByNameSqlPage
     * @Description: TODO(本地Sql查询分页)
     * @author: linjie
     * @date: 2016-5-23
     * @param sqlName
     * @param param
     * @param clazz
     * @return
     */
    public <T> void findByNameSqlPage(final String sqlName,final Map<String,Object> param,final Page<T> page, final Class clazz){
    	SessionFactoryImpl factory=(SessionFactoryImpl)this.getHibernateTemplate().getSessionFactory();
    	final NamedQueryDefinition nqd=factory.getNamedSQLQuery(sqlName);
    	if(nqd==null){
    		this.logger.error("没有找到Sql定义,或者对应的为Hql语句!");
    	}
    	this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				String sql=nqd.getQueryString();
				sql=configQueryXml.getDealConfigQueryParam(sql, param);
				System.out.println("执行Sql:"+sql);   //后续放日志系统打印
				SQLQuery query=session.createSQLQuery(sql);
		    	setParam(query, param);
		    	query.setFirstResult(page.getCurrentPage()).setMaxResults(page.getPageSize());
		    	
		    	if(clazz != null){
					query.setResultTransformer(new EscColumnToBean(clazz));
				}
				List<T> list = query.list();
				page.setResult(list);
				String[] sqlArr={sql};
				String sqlCount=fillStringByArgs(baseCountSql, sqlArr);
				System.out.println("执行的Sql："+fillStringByArgs(baseCountSql, sqlArr));
				SQLQuery queryCount=session.createSQLQuery(sqlCount);
				setParam(queryCount, param);
				page.setTotalsCount(Integer.parseInt(queryCount.setMaxResults(1).uniqueResult().toString())); 
				
				return list;
			}
		});
    	
    }
    
    /** 
     * <b>function:</b> 传入一个entity对象Class和Serializable类型主键，返回该对象 
     * @createDate 2010-8-2 下午05:48:36 
     * @author hoojo 
     * @param <T> 返回、传入对象类型 
     * @param c 对象Class 
     * @param id 主键 
     * @return T 返回该类型的对象 
     * @throws Exception 
     */  
    public <T> T get(Class<T> c, Serializable id) throws Exception {  
        T ety = null;  
        try {  
            ety = (T) this.getHibernateTemplate().get(c, id);             
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return ety;  
    }  
      
    /** 
     * <b>function:</b> 传入hql语句，查询对象 
     * @createDate 2010-8-2 下午05:49:31 
     * @author hoojo 
     * @param <T> 返回对象类型 
     * @param hql 查询的hql语句 
     * @return 对象T 
     * @throws Exception 
     */  
    public <T> T get(String hql) throws Exception {  
        T ety = null;  
        try {  
            ety = (T) this.currentSession().createQuery(hql).setMaxResults(1).uniqueResult();         
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return ety;  
    }  
      
    /** 
     * <b>function:</b> 通过hql语句查询List集合 
     * @createDate 2010-8-2 下午05:51:05 
     * @author hoojo 
     * @param hql 查询hql语句 
     * @return List<?> 
     * @throws Exception 
     */  
    public <T> List<T> getList(String hql) throws Exception {  
        List<T> list = null;  
        try {  
             list = (List<T>) this.getHibernateTemplate().find(hql);        
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return list;  
    }  
      
    /** 
     * <b>function:</b> 传入一个entity对象Class和Integer类型主键，返回该对象 
     * @createDate 2010-8-2 下午05:47:20 
     * @author hoojo 
     * @param <T> 返回、传入对象类型 
     * @param c 对象Class 
     * @param id 主键 
     * @return T 返回该类型的对象 
     * @throws Exception 
     */  
    public <T> T getById(Class<T> c, Integer id) throws Exception {  
        T ety = null;  
        try {  
            ety = (T) this.getHibernateTemplate().get(c, id);             
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return ety;  
    }  
      
    /** 
     * <b>function:</b> 传入一个entity对象Class和String型主键，返回该对象 
     * @createDate 2010-8-2 下午05:44:53 
     * @author hoojo 
     * @param <T> 返回、传入对象类型 
     * @param c 对象Class 
     * @param id 主键 
     * @return T 返回传入类型对象 
     * @throws Exception 
     */  
    public <T> T getById(Class<T> c, String id) throws Exception {  
        T ety = null;  
        try {  
            ety = (T) this.getHibernateTemplate().get(c, id);             
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return ety;  
    }  
      
    /** 
     * <b>function:</b> 传入hql查询语句和object数组类型的参数，返回查询list集合 
     * @createDate 2010-8-2 下午05:52:36 
     * @author hoojo 
     * @param hql 查询的hql语句 
     * @param obj 查询参数 
     * @return 返回list集合 
     * @throws Exception 
     */  
    public <T> List<T> getList(String hql, Object[] obj) throws Exception {  
        List<T> list = null;  
        try {  
             list = (List<T>) this.getHibernateTemplate().find(hql, obj);       
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return list;  
    }  
      
    /** 
     * <b>function:</b> 传入一个将要删除的entity对象，返回删除是否成功 
     * @createDate 2010-8-2 下午05:42:02 
     * @author hoojo 
     * @param <T> 传入对象类型 
     * @param entity 将要传入的对象 
     * @return boolean true/false 
     * @throws Exception 
     */  
    public <T> boolean remove(T entity) throws Exception {  
        boolean bo = false;  
        try {  
            this.getHibernateTemplate().delete(entity);   
            bo = true;  
        } catch (Exception e) {  
            bo = false;  
            throw new RuntimeException(e);  
        }  
        return bo;  
    }  
      
    /** 
     * <b>function:</b> 传入删除的hql语句，删除记录 
     * @createDate 2010-8-3 上午09:53:49 
     * @author hoojo 
     * @param hql 将要被执行删除的hql语句 
     * @return 是否删除成功 
     * @throws Exception 
     */  
    public boolean remove(String hql) throws Exception {  
        try {  
            return this.executeByHql(hql) > 0 ? true : false;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
      
    /** 
     * <b>function:</b> 动态查询 
     * @createDate 2010-8-3 上午10:53:37 
     * @author hoojo 
     * @param <T> 查询类的类型 
     * @param c 动态查询组合对象 
     * @return list集合 
     * @throws Exception 
     */  
    public <T> List<T> getList(Class<T> c) throws Exception {  
        List<T> list = null;  
        try {  
            this.getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(c));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return list;  
    }  
      
    /** 
     * <b>function:</b> 传入查询语句和查询总条数（总记录）的hql语句、当前页数、每页显示调试数；返回查询后的list集合； 
     * list集合保存总记录调试和记录结果 
     * @createDate 2010-8-2 下午05:54:01 
     * @author hoojo 
     * @param queryHql 查询记录hql语句 
     * @param queryCountHql 查询记录条数hql语句 
     * @param firstResult 当前查询页 
     * @param maxResult 每页显示多少条 
     * @return List返回集合 集合0保存查询结果、集合1保存总记录条数 
     * @throws Exception 
     */  
    public List<?> showPage(String queryHql, String queryCountHql, int firstResult, int maxResult) throws Exception {  
        List<Object> list = new ArrayList<Object>();  
        try {  
            Session session = this.currentSession();  
            list.add(session.createQuery(queryHql)  
                    .setFirstResult(firstResult).setMaxResults(maxResult).list());   
            list.add(session.createQuery(queryCountHql).setMaxResults(1).uniqueResult());  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return list;  
    }  
      
    /** 
     * <b>function:</b> 传入查询语句和查询总条数（总记录）的hql语句、page分页对象；返回查询后的list集合； 
     * @createDate 2010-8-3 上午11:16:59 
     * @author hoojo 
     * @param queryHql list集合结果查询 
     * @param queryCountHql 总记录调试查询 
     * @param page 分页对象 
     * @throws Exception 
     */  
    public <T> void showPage(String queryHql, String queryCountHql, Page<T> page) throws Exception {  
        try {  
            Session session = this.currentSession();  
            page.setResult(session.createQuery(queryHql)  
                    .setFirstResult(page.getCurrentPage()).setMaxResults(page.getPageSize()).list());   
            page.setTotalsCount(Integer.parseInt(session.createQuery(queryCountHql).setMaxResults(1).uniqueResult().toString()));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
      
    /** 
     * <b>function:</b> 分页查询，传入查询count的hql语句和DetachedCriteria动态查询条件进行查询分页 
     * @createDate 2010-8-3 上午11:04:39 
     * @author hoojo 
     * @param queryCountHql hql查询count语句总条数 
     * @param cResult  DetachedCriteria 动态查询条件 
     * @param firstResult 起始 
     * @param maxResult 最大页数 
     * @return List<?> 查询集合 
     * @throws Exception 
     */  
    public List<?> showPage(String queryCountHql, DetachedCriteria cResult, int firstResult, int maxResult) throws Exception {  
        List<Object> list = new ArrayList<Object>();  
        try {  
            Session session = this.currentSession();  
            list.add(this.getHibernateTemplate().findByCriteria(cResult, firstResult, maxResult));   
            list.add(session.createQuery(queryCountHql).setMaxResults(1).uniqueResult());  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return list;  
    }  
      
    /** 
     * <b>function:</b> 分页查询，传入查询的count的hql语句和动态查询DetachedCriteria类及page分页entity 
     * @createDate 2010-8-3 上午11:14:30 
     * @author hoojo 
     * @param queryCountHql 查询count语句 
     * @param cResult DetachedCriteria 动态查询组合类 
     * @param page Page分页实体类 
     * @throws Exception 
     */  
    public <T> void showPage(String queryCountHql, DetachedCriteria cResult, Page<T> page) throws Exception {  
        try {  
            Session session = this.currentSession();  
            page.setResult((List<T>)this.getHibernateTemplate().findByCriteria(cResult, page.getCurrentPage(), page.getPageSize()));   
            page.setTotalsCount(Integer.parseInt(session.createQuery(queryCountHql).setMaxResults(1).uniqueResult().toString()));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
      
    /** 
     * <b>function:</b> 传入查询条件DetachedCriteria进行查询 
     * @createDate 2010-8-3 上午11:55:28 
     * @author hoojo 
     * @param <T> 类型 
     * @param dc DetachedCriteria动态条件查询 
     * @return List 
     * @throws Exception 
     */  
    public <T> List<T> find(DetachedCriteria dc) throws Exception {  
        List<T> list = new ArrayList<T>();  
        try {  
            list = (List<T>) this.getHibernateTemplate().findByCriteria(dc);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return list;  
    }
    
    /**
	 * 设置sql or hql查询条件
	 * @param query
	 * @param queryWrapper
	 */
	private void setParam(Query query, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		if(!paramMap.isEmpty()){
			Iterator<Entry<String,Object>> it=paramMap.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String,Object> entry=it.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				if(value instanceof Collection) {
					query.setParameterList(key, (Collection)value);
				}else if(value instanceof Object[]) {
					query.setParameterList(key, (Object[])value);
				}else {
					query.setParameter(key, value);
				}
			}
		}
	}
      
    /** 
     * <b>function:</b> 暴露基类session供用户使用 
     * @createDate 2010-8-3 上午11:59:54 
     * @author hoojo 
     * @return Session 
     */  
    public Session session() {  
        return this.currentSession();  
    }  
    
    private static String fillStringByArgs(String str,String[] arr){
        Matcher m=Pattern.compile("\\{(\\d)\\}").matcher(str);
        while(m.find()){
            str=str.replace(m.group(),arr[Integer.parseInt(m.group(1))]);
        }
        return str;
    }
        
}
