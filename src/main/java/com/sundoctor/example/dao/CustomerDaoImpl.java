package com.sundoctor.example.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.sundoctor.example.model.Customer;
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

	public void test() {
		
		Customer customer = this.getHibernateTemplate().get(Customer.class, 1);
		logger.info("Customer={}", customer);
	}

}
