package com.bookstore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bookstore.domain.Address;
import com.bookstore.domain.Book;
import com.bookstore.domain.BuyItem;
import com.bookstore.domain.Order;
import com.bookstore.domain.User;
import com.bookstore.service.ConvertorService;
import com.bookstore.service.PersonalInfoService;
import com.bookstore.service.RegisterService;
import com.bookstore.util.DivBook;
import com.bookstore.util.DivOrder;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author Chang Su
 * @author Chang Wei
 * @description 用户个人主页相关Action实现:订单的取消与删除，个人订单的显示，评论，用户密码与邮箱修改(CW)，地址
 * @descriotion password and email line 51-74
 * @descriotion order line 78-127
 * @descriotion bookunapprised line 128-142
 * @modify
 * @modifyDate
 */
public class UserAction {
	//From userinfo.jsp
	
	//To userinfo.jsp -- dataType: json
	List<DivOrder> divOrderList;	//To order-detail.jsp
	List<DivBook> unapprisedList;
	Integer paidNum;
	Integer unpaidNum;
	Integer unapprisedNum;
	String email;
	String result;
	
	//To changepassword function
	String oldpassword;
	String newpassword;
	String newemail;
	
	//To myAddress.jsp
	List<Address> addressList;
	
	PersonalInfoService personalInfoService;
	ConvertorService convertorService;
	RegisterService registerService;
	
	
	///////////////////////////
	Integer userID;
	Integer orderID;
	
	String usrName;
	
	//Email
	public String GetEmail(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		User user = personalInfoService.UserInfo(userID);
		email = user.getUserEmail();
		return "success";
	}
	
	//Username
	public String GetUsrname(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		if(userID == null){
			usrName = "";
		}
		else{
			User user = personalInfoService.UserInfo(userID);
			usrName = user.getUserName();
		}
		return "success";
	}
	//Password and Email
	public String isPasswordValid(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		if(personalInfoService.isPasswordValid(userID, oldpassword))
			result = "success";
		else
			result = "fail";
		return result;
	}
	
	public String modifyPassword(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		if(personalInfoService.modifyPassword(userID, newpassword))
			result = "success";
		else
			result = "false";
		return result;
	}
	
	public String modifyEmail(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		if(registerService.hasUserEmail(newemail)){
			result = "hasEmail";
			return "success";
		}
		if(personalInfoService.modifyEmail(userID, newemail))
			result = "success";
		else
			result = "false";
		return result;
	}
	
	//Order
	public String showPaidOrder() {
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		List<Order> orderList = personalInfoService.getPaidOrder(userID);
		divOrderList = new ArrayList<DivOrder>();
		for(Order order: orderList){
			Integer orderId = order.getOrderID();
			List<BuyItem> buyItemList = convertorService.getBuyItemList(orderId);
			DivOrder divOrder = convertorService.buyItemListAddToDivOrder(buyItemList, orderId);
			divOrderList.add(divOrder);
		}
		return "success";
	}
	
	public String cancelOrder(){
		personalInfoService.cancelOrder(orderID);
		this.result = "success";
		return "success";
	}
	
	public String deleteOrder(){
//		List<BuyItem> buyItemInOrder = personalInfoService.findBuyItemByOrderID(orderID);
//		for(BuyItem buyItem: buyItemInOrder){
//			buyItem.setBoughtDate(null);
//			buyItem.setOrderID(null);
//			personalInfoService.updateBuyItem(buyItem);
//		}
		personalInfoService.deleteOrder(orderID);
		this.result = "success";
		return "success";
	}
	
	public String showOrderListAll(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		List<Order> orderList = personalInfoService.getAllOrder(userID);
		divOrderList = new ArrayList<DivOrder>();
		for(Order order: orderList){
			Integer orderId = order.getOrderID();
			List<BuyItem> buyItemList = convertorService.getBuyItemList(orderId);
			DivOrder divOrder = convertorService.buyItemListAddToDivOrder(buyItemList, orderId);
			divOrderList.add(divOrder);
		}
		return "success";
	}

	public String showPaidOrderNumber() {
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		List<Order> orderList = personalInfoService.getPaidOrder(userID);
		if(orderList.isEmpty())
			paidNum = 0;
		else paidNum = orderList.size();
		return "success";
	}
	
	public String showUnpaidOrderNumber() {
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		List<Order> orderList = personalInfoService.getUnpaidOrder(userID);
		if(orderList.isEmpty())
			unpaidNum = 0;
		else unpaidNum = orderList.size();
		return "success";
	}
	
	//Unapprised Book
	public String showUnapprisedBooksNumber() {
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		List<Book> bookList = personalInfoService.getUnappriseBook(userID);
		if(bookList.isEmpty())
			unapprisedNum = 0;
		else unapprisedNum = bookList.size();
		return "success";
	}
	
	public String showUnapprisedBookList(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		List<Book> bookList = personalInfoService.getUnappriseBook(userID);
		unapprisedList = convertorService.bookIDToDivBook(bookList);
		return "success";
	}
	
	//Address
	public String showAddress(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		addressList = personalInfoService.getAddress(userID);
		this.result = "success";
		return "success";
	}
	
	// getter and setter
	public List<DivOrder> getDivOrderList() {
		return divOrderList;
	}

	public void setDivOrderList(List<DivOrder> divOrderList) {
		this.divOrderList = divOrderList;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public PersonalInfoService getPersonalInfoService() {
		return personalInfoService;
	}

	public void setPersonalInfoService(PersonalInfoService personalInfoService) {
		this.personalInfoService = personalInfoService;
	}


	public ConvertorService getConvertorService() {
		return convertorService;
	}

	public void setConvertorService(ConvertorService convertorService) {
		this.convertorService = convertorService;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public Integer getPaidNum() {
		return paidNum;
	}

	public void setPaidNum(Integer paidNum) {
		this.paidNum = paidNum;
	}

	public Integer getUnpaidNum() {
		return unpaidNum;
	}

	public void setUnpaidNum(Integer unpaidNum) {
		this.unpaidNum = unpaidNum;
	}

	public Integer getUnapprisedNum() {
		return unapprisedNum;
	}

	public void setUnapprisedNum(Integer unapprisedNum) {
		this.unapprisedNum = unapprisedNum;
	}
	
	public String getOldpassword() {
		return oldpassword;
	}
	
	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}
	
	public String getNewpassword() {
		return newpassword;
	}
	
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	public String getNewemail() {
		return newemail;
	}

	public void setNewemail(String newemail) {
		this.newemail = newemail;
	}

	public List<DivBook> getUnapprisedList() {
		return unapprisedList;
	}

	public void setUnapprisedList(List<DivBook> unapprisedList) {
		this.unapprisedList = unapprisedList;
	}

	public Integer getOrderID() {
		return orderID;
	}

	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}
	
	
}
