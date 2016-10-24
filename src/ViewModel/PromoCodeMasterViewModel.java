package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.PromoCodeMasterBean;
import service.PromoCodeMasterService;

public class PromoCodeMasterViewModel {
	
	PromoCodeMasterBean promoCodeMasterBean = new PromoCodeMasterBean();
	PromoCodeMasterBean promoCodeTypeBean = new PromoCodeMasterBean();
	PromoCodeMasterBean promoCodeApplicationTypeBean = new PromoCodeMasterBean();
	
	ArrayList<PromoCodeMasterBean> promoCodeDetailsBeanList;
	ArrayList<PromoCodeMasterBean> promoCodeTypeList;
	ArrayList<PromoCodeMasterBean> promoCodeApplicationTypeList;
	ArrayList<PromoCodeMasterBean> promoCodeList;
	
	String promoCode;
	
	Session session = null;
	private Connection connection = null;
	private String userName = "";
	
	@AfterCompose
	public void initSetUp(@ContextParam(ContextType.VIEW) Component view) throws Exception{
		Selectors.wireComponents(view, this, false);
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		connection.setAutoCommit(true);
		
		promoCodeMasterBean.setUser(userName);
		System.out.println("promoCodeMasterBean.setUser(userName) " + promoCodeMasterBean.getUser());
		promoCodeList = PromoCodeMasterService.loadPromoCodeList(connection);
		promoCodeDetailsBeanList = PromoCodeMasterService.loadPromoCodeDetails(connection);
		
		System.out.println("zul page >> promoCodeMaster.zul");
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickSavePromoCode(){
		int i = 0;
		if(promoCode !=null){
			
		i = PromoCodeMasterService.insertPromocode(connection, promoCodeMasterBean, promoCode);
		if(i>0){
			promoCode = null;
			Messagebox.show("Promo code Saved Succesfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
		}else {
			Messagebox.show("Enter Promo code", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectPromocode(){
		promoCodeMasterBean.setPromoApplyDivVis(true);
		promoCodeTypeList = PromoCodeMasterService.loadPromoCodeType(connection);
		promoCodeApplicationTypeList = PromoCodeMasterService.loadPromoCodeApplicationType(connection);
		promoCodeTypeBean.setPromoVauleVis(true);
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectType(){
		promoCodeMasterBean.setPromoVauleVis(false);
	}

	@Command
	@NotifyChange("*")
	public void onclickApplyPromoCode(){
		promoCodeMasterBean.setUser(userName);
		int i = 0;
		if(PromoCodeMasterService.isValidate(promoCodeMasterBean, promoCodeTypeBean, promoCodeApplicationTypeBean)){
			i = PromoCodeMasterService.insertPromoCodeDetails(connection, promoCodeMasterBean, promoCodeTypeBean, promoCodeApplicationTypeBean);
			if(i>0){
				onClickClear();
				Messagebox.show("Saved Succesfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
			}
			
		}
		
	}
	
	
	/*@Command
	@NotifyChange("*")
	public void onclickApplyPromoCode(@BindingParam("bean") PromoCodeMasterBean bean){
		int i= 0;
		i = PromoCodeMasterService.insertPromoCodeDetails(connection, bean);
		if(i>0){
			Messagebox.show("Saved Succesfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}*/
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		promoCodeMasterBean.setPromoApplyDivVis(false);
		
		promoCodeMasterBean.setPromoCode(null);
		promoCodeList = PromoCodeMasterService.loadPromoCodeList(connection);
		
		promoCodeMasterBean.setFromDateUtil(null);
		promoCodeMasterBean.setToDateUtil(null);
		
		promoCodeTypeBean.setPromoType(null);
		promoCodeTypeList = PromoCodeMasterService.loadPromoCodeType(connection);
		
		promoCodeMasterBean.setPromoValue(null);
		promoCodeTypeBean.setPromoVauleVis(true);
		
		promoCodeApplicationTypeBean.setPromoCodeApplyType(null);
		promoCodeApplicationTypeList = PromoCodeMasterService.loadPromoCodeApplicationType(connection);
		
	}
	
	

	public PromoCodeMasterBean getPromoCodeMasterBean() {
		return promoCodeMasterBean;
	}


	public void setPromoCodeMasterBean(PromoCodeMasterBean promoCodeMasterBean) {
		this.promoCodeMasterBean = promoCodeMasterBean;
	}

	public Session getSession() {
		return session;
	}


	public void setSession(Session session) {
		this.session = session;
	}


	public Connection getConnection() {
		return connection;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public PromoCodeMasterBean getPromoCodeTypeBean() {
		return promoCodeTypeBean;
	}


	public void setPromoCodeTypeBean(PromoCodeMasterBean promoCodeTypeBean) {
		this.promoCodeTypeBean = promoCodeTypeBean;
	}


	public ArrayList<PromoCodeMasterBean> getPromoCodeDetailsBeanList() {
		return promoCodeDetailsBeanList;
	}


	public void setPromoCodeDetailsBeanList(
			ArrayList<PromoCodeMasterBean> promoCodeDetailsBeanList) {
		this.promoCodeDetailsBeanList = promoCodeDetailsBeanList;
	}


	public PromoCodeMasterBean getPromoCodeApplicationTypeBean() {
		return promoCodeApplicationTypeBean;
	}


	public void setPromoCodeApplicationTypeBean(
			PromoCodeMasterBean promoCodeApplicationTypeBean) {
		this.promoCodeApplicationTypeBean = promoCodeApplicationTypeBean;
	}


	public ArrayList<PromoCodeMasterBean> getPromoCodeTypeList() {
		return promoCodeTypeList;
	}


	public void setPromoCodeTypeList(
			ArrayList<PromoCodeMasterBean> promoCodeTypeList) {
		this.promoCodeTypeList = promoCodeTypeList;
	}


	public ArrayList<PromoCodeMasterBean> getPromoCodeApplicationTypeList() {
		return promoCodeApplicationTypeList;
	}


	public void setPromoCodeApplicationTypeList(
			ArrayList<PromoCodeMasterBean> promoCodeApplicationTypeList) {
		this.promoCodeApplicationTypeList = promoCodeApplicationTypeList;
	}


	public String getPromoCode() {
		return promoCode;
	}


	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}


	public ArrayList<PromoCodeMasterBean> getPromoCodeList() {
		return promoCodeList;
	}


	public void setPromoCodeList(ArrayList<PromoCodeMasterBean> promoCodeList) {
		this.promoCodeList = promoCodeList;
	}
	

}