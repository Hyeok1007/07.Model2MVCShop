package com.model2.mvc.service.purchase.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao{

	
	///Field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private Sqlsession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	public PurchaseDaoImpl() {
		System.out.println(this.getClass());
	}

}
