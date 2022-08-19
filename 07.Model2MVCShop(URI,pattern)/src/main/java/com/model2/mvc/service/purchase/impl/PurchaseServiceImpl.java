package com.model2.mvc.service.purchase.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.PurchaseDao;

@Service("purchaseServiceImpl")
	public class PurchaseServiceImpl implements PurchaseService{

	///Field
	@Autowired
	@Qualifier("purchaseDaoImpl")
	private PurchaseDao purchaseDao;
	private ProductDao productDao;

	public PurchaseServiceImpl() {
		System.out.println(this.getClass());
	}
	
	public Purchase addPurchase(Purchase purchase) throws Exception {
		purchaseDao.insertPurchase(purchase);
		return purchase;
	}
	
	public Purchase getPurchase(int tranNo) throws Exception {
		return purchaseDao.findPurchase(tranNo);
	}
	
	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		return purchaseDao.getPurchaseList(search, userId);
	}
	
	public Map<String, Object> getSaleList(Search search) throws Exception {
		return purchaseDao.getSaleList(search);
	}
	
	public Purchase updatePurchase(Purchase purchase) throws Exception {
		purchaseDao.updatePurchase(purchase);
		return purchaseDao.findPurchase(purchase.getTranNo());
		
	}
	
	public void updateTranCode(Purchase purchase) throws Exception {
		purchaseDao.updateTranCode(purchase);
	}
}
