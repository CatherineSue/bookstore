
package com.bookstore.action;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.bookstore.domain.BuyItem;
import com.bookstore.service.BuyService;
import com.bookstore.service.CartService;
import com.bookstore.service.ConvertorService;
import com.bookstore.util.TrCartItem;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author Zhiqi Yang
 * @description 购物车的列表，以及增删改     From: cart.sjp ---- To: cart.jsp
 * @modify
 * @modifyDate 
 */
public class CartAction {
	//From cart.jsp --deleteBuyItem()
	Integer buyItemID;
	Integer num;
	
	Integer bookID;
	
	//To cart.jsp -- ordinary variable
	List<TrCartItem> trCartList;
	String totalPrice;
	
	//To cart.jap -- json variable
	Boolean isSuccess;
	TrCartItem trCartItem;
	
	//To books.jsp -- json variable
	Integer cartNum;
	
	//IOC Service
	CartService cartService;
	ConvertorService convertorService;
	BuyService buyService;
	
	
	/*in: userID
	* out: trCartList
	*      totalPrice -- should be canceled
	*/
	public String showBuyItemList(){    
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		
		List<BuyItem> buyItemList = cartService.getCartItemList(userID);
		trCartList = convertorService.buyItemListToTrCartList(buyItemList);
		Double fTotalPrice = 0.0;
		for(TrCartItem trCartItem: trCartList){
			fTotalPrice += Double.parseDouble(trCartItem.getBuyItemPrice());
		}
		DecimalFormat df = new DecimalFormat("0.00");
		totalPrice = df.format(fTotalPrice);
		return "success";
	}
	
	public String showCartNum(){
		showBuyItemList();
		cartNum = trCartList.size();
		return "success";
	}
	
	/*in:  buyItemID
	* out: isSuccess
	*/
	public String deleteBuyItem(){
		cartService.deleteCartItem(buyItemID);
		isSuccess = true;
		return "success";
	}
	
	/*in:   buyItemID
	 *      num
	 * out: isSuccess
	 */
	public String updateBuyItem(){
		cartService.changeNumOfCartItem(buyItemID, num);
		trCartItem = convertorService.buyItemIDToTrCartItem(buyItemID);
		isSuccess = true;
		return "success";
	}
	
	/** json
	 * @in: bookID
	 * @in: num
	 * @out: isSuccess
	 */
	public String addCartItem(){
		Map session = ActionContext.getContext().getSession();
		Integer userID = (Integer) session.get("userID");
		isSuccess = cartService.addCartItem(userID, bookID, num);
		return "success";
	}
	

	//getter and setter
	public Integer getBuyItemID() {
		return buyItemID;
	}

	public void setBuyItemID(Integer buyItemID) {
		this.buyItemID = buyItemID;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public List<TrCartItem> getTrCartList() {
		return trCartList;
	}

	public void setTrCartList(List<TrCartItem> trCartList) {
		this.trCartList = trCartList;
	}

	public CartService getCartService() {
		return cartService;
	}

	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}

	public ConvertorService getConvertorService() {
		return convertorService;
	}

	public void setConvertorService(ConvertorService convertorService) {
		this.convertorService = convertorService;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public TrCartItem getTrCartItem() {
		return trCartItem;
	}

	public void setTrCartItem(TrCartItem trCartItem) {
		this.trCartItem = trCartItem;
	}

	public BuyService getBuyService() {
		return buyService;
	}

	public void setBuyService(BuyService buyService) {
		this.buyService = buyService;
	}

	public Integer getBookID() {
		return bookID;
	}

	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}

	public Integer getCartNum() {
		return cartNum;
	}

	public void setCartNum(Integer cartNum) {
		this.cartNum = cartNum;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	
	
	
}
