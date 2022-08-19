package com.model2.mvc.service.purchase;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;


public class PurchaseDao {

	public PurchaseDao() {
	}

	public Purchase findPurchase(int tranNo) throws Exception {
		
		Connection con = DBUtil.getConnection();		
		String sql = "SELECT * FROM transaction WHERE tran_no=? ";		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, tranNo);		
		ResultSet rs = pStmt.executeQuery();
		
		Product product = new Product();
		User user = new User();
		Purchase purchase = new Purchase();
		while(rs.next()) {			
			purchase.setTranNo(rs.getInt("TRAN_NO"));
			product.setProdNo(rs.getInt("PROD_NO"));
			purchase.setPurchaseProd(product);
			user.setUserId(rs.getString("BUYER_ID"));
			purchase.setBuyer(user);
			purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
			purchase.setOrderDate(rs.getDate("ORDER_DATA"));
			purchase.setDivyDate(rs.getString("DLVY_DATE")+"");
			
		}
		rs.close();
		pStmt.close();
		con.close();
		
		return purchase;
	}
	
	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		
		Connection con = DBUtil.getConnection();
		System.out.println("구매 리스트 시작~!!");
		String sql = "SELECT * FROM transaction WHERE buyer_id='"+userId+"'ORDER BY order_data DESC";
		
		int total = getTotalCount(sql);
		System.out.println("전체 레코드 수: " +total);
		

		ArrayList<Purchase> list = new ArrayList<Purchase>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", total);
//		map.put("count", total);
		
		sql=makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
			
		Product product = new Product();
		User user = new User();
				
		if(total>0) {
			while(rs.next()) {
			Purchase purchase = new Purchase();
			purchase.setTranNo(rs.getInt("TRAN_NO"));
			product.setProdNo(rs.getInt("PROD_NO"));
			purchase.setPurchaseProd(product);
			user.setUserId(rs.getString("BUYER_ID"));
			purchase.setBuyer(user);
			purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
			purchase.setDivyDate(rs.getDate("DLVY_DATE")+"");
			
			list.add(purchase);
			System.out.println("구매 리스트 여기까지 왔나??");
			}
		}
			
		map.put("list",list);
		for(int i =0; i<list.size();i++) {
			System.out.println("list : "+list.get(i));
		}
		rs.close();
		pStmt.close();
		con.close();
		
		return map;
	}
	
	public Map<String, Object> getSaleList(Search search) throws Exception {
	
		Map<String, Object> map = new HashMap<String, Object>();
		return map;		
				
	}
	
	public void insertPurchase(Purchase purchase) throws Exception{
		
	Connection con = DBUtil.getConnection();
	
	System.out.println("구매 DAO왔는지");
	String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.NEXTVAL,?, ?, ?, ?, ?, ?, ?, '1', sysdate, ?)";
	PreparedStatement pStmt = con.prepareStatement(sql);
	
	pStmt.setInt(1, purchase.getPurchaseProd().getProdNo());
	pStmt.setString(2, purchase.getBuyer().getUserId());
	pStmt.setString(3, purchase.getPaymentOption());
	pStmt.setString(4, purchase.getReceiverName());
	pStmt.setString(5, purchase.getReceiverPhone());
	pStmt.setString(6, purchase.getDivyAddr());
	pStmt.setString(7, purchase.getDivyRequest());
	pStmt.setString(8, purchase.getDivyDate());
	
	int i = pStmt.executeUpdate();
	if(i==1) {
		System.out.println("구매하기 성공");
	}
	
	pStmt.close();
	con.close();
	
	System.out.println("구매 insert DAO 완료");
	
	}
	
	public void updatePurchase(Purchase purchase) throws Exception {
		Connection con = DBUtil.getConnection();		
		String sql = "UPDATE transaction SET payment_option=?, receiver_name=?, receiver_phone=?, demailaddr=?, dlvy_request=?, dlvy_date=? WHERE tran_no=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, purchase.getPaymentOption());
		pStmt.setString(2, purchase.getReceiverName());
		pStmt.setString(3, purchase.getReceiverPhone());
		pStmt.setString(4, purchase.getDivyAddr());
		pStmt.setString(5, purchase.getDivyRequest());
		pStmt.setString(6, purchase.getDivyDate());
		pStmt.setInt(7, purchase.getTranNo());
				
		int i = pStmt.executeUpdate();
		con.close();
		
		if(i==1) {
			System.out.println("수정됨");
		}else {
			System.out.println("수정 실패");
		}
	}
	
	public void updateTranCode(Purchase purchase) throws Exception {
		System.out.println("updateTranCode DAO 시작~~~~~~");
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = null;				
		String sql = "UPDATE transaction SET tran_status_code=? WHERE ";		
		int i = 0;
		if(purchase.getTranNo() != 0) {
			sql +=" tran_no=? ";
			pStmt = con.prepareStatement(sql);
			pStmt.setString(1, purchase.getTranCode());
			pStmt.setInt(2, purchase.getTranNo());
			i = pStmt.executeUpdate();
		}else {
			sql += " prod_no=? ";
			pStmt = con.prepareStatement(sql);
			pStmt.setString(1, purchase.getTranCode());
			pStmt.setInt(2, purchase.getPurchaseProd().getProdNo());
			i = pStmt.executeUpdate();
		}
		con.close();
		
		System.out.println("sql : " + sql);
		
		if(i!=1) {
			System.out.println("tranCode 수정 실패");
		} else {
			System.out.println("tranCode 수정 성공");
		}
	}
	
	private int getTotalCount(String sql) throws Exception {
		Connection con = DBUtil.getConnection();
		
		sql = "SELECT COUNT(*)"
				+ "FROM ( " +sql+ ") countTable";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if(rs.next()) {
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	private String makeCurrentPageSql(String sql, Search search) {
		sql = "SELECT * "
				+ "FROM( SELECT inner_table. * , ROWNUM AS row_seq "
				+ " FROM ("+sql+" ) inner_table "
						+ " WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) "
								+ "WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("PurchaseDAO :: make SQL :: "+sql);
		
		return sql;
	}
}
