package com.sundoctor.example.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.common.base.dao.EscColumnToBean;
import com.sundoctor.example.model.Customer;
import com.sundoctor.example.model.Customert;
import com.sundoctor.example.service.SimpleService;

@Repository("customerDao")
public class CustomerDaoImpl extends HibernateDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger(SimpleService.class);

	
	@Autowired
	public CustomerDaoImpl(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	
	public Customer backupDateabase() {

		return this.getHibernateTemplate().execute(new HibernateCallback<Customer>() {
			public Customer doInHibernate(Session session) {
				logger.info("Session2=={}", session);
				Customer customer = (Customer) session.createQuery("from Customer where id = 1").uniqueResult();
				logger.info("Customer2={}", customer);
				return customer;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void test() {
		//org.hibernate.engine.query.spi.sql.NativeSQLQueryRootReturn
		/*Customer customer = this.getHibernateTemplate().get(Customer.class, 1);
		SQLQuery sqlQuery=this.currentSession().createSQLQuery("");
		sqlQuery.addEntity(Customer.class);*/
		
		Query query=this.currentSession().getNamedQuery("test");
		query.setParameter("id", "1");
		query.setResultTransformer(new EscColumnToBean(Customert.class));
		List<Customert> result=(List<Customert>)query.list();
		for (int i = 0; i < result.size(); i++) {
			Customert o=result.get(i);
			System.out.println("age:"+o.getAge()+"-- name:"+o.getName()+"-- email:"+o.getEmail());
		}
		//logger.info("Customer={}", customer);
	}

}
