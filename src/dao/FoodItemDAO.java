package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.image.AImage;
import org.zkoss.zhtml.Pre;
import org.zkoss.zul.Messagebox;

import utility.FappPstm;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;

public class FoodItemDAO {

	public static ArrayList<ItemBean> loadFoodItems(Connection connection, int cuisineId, int categoryId){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "";
					if(cuisineId >0 && categoryId >0){
						sql = "select * from vw_food_item_details where cuisine_id = ? and category_id = ?";
					}else if(cuisineId > 0){
						sql = "select * from vw_food_item_details where cuisine_id = ?";
					}else if(categoryId >0) {
						sql = "select * from vw_food_item_details where category_id = ?";
					}else{
						sql = "select * from vw_food_item_details ";
					}
					try {
						if(cuisineId >0 && categoryId >0){
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setInt(1, cuisineId);
							preparedStatement.setInt(2, categoryId);
							preparedStatement.setInt(1, cuisineId);
						}else if(cuisineId >0 ) {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setInt(1, cuisineId);
						}else if(categoryId > 0)  {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setInt(1, categoryId);
						}else{
							preparedStatement = connection.prepareStatement(sql);
						}
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ItemBean bean = new ItemBean();
							bean.cusineName = resultSet.getString("cuisin_name");
							bean.categoryName = resultSet.getString("category_name");
							bean.categoryId = resultSet.getInt("category_id");
							bean.itemName = resultSet.getString("item_name");
							bean.itemCode = resultSet.getString("item_code");
							bean.itemPrice = resultSet.getDouble("item_price");
							bean.itemDescription = resultSet.getString("item_description");
							bean.itemId = resultSet.getInt("item_id");
							if(resultSet.getString("item_image") != null){
								bean.itemmagePath = resultSet.getString("item_image");
							}else{
								bean.itemmagePath = null;
							}
							if(bean.itemmagePath != null){
								try {
									bean.itemImage = new AImage(bean.itemmagePath);
								} catch (Exception e) {
									bean.itemImage = null;
								}
							}else{
								bean.itemImage = null;
							}
							
							if(resultSet.getString("is_active").equalsIgnoreCase("Y")){
								bean.status = "Active";
							}else{
								bean.status = "Deactive";
							}
							bean.itemTypeId = resultSet.getInt("item_type_id");
							if(resultSet.getString("type_name") != null){
								bean.itemTypeName = resultSet.getString("type_name");
							}else{
								bean.itemTypeName = "";
							}
							/*if(resultSet.getString("apply_new_user").equals("Y")){
								bean.status = "Active";
							}else {
								bean.status = "Deactive";
							}*/
							bean.packingId = resultSet.getInt("packing_type_id");
							if(resultSet.getString("pack_type")!=null){
								bean.packingName = resultSet.getString("pack_type");
							}else{
								bean.packingName = "";
							}
							
							
							itemBeanList.add(bean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return itemBeanList;		
	}
	
	public static ArrayList<ItemBean> loadAlacarteTypeNameList(Connection connection){
		ArrayList<ItemBean> alaCarteTypeList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select item_type_id,type_name from fapp_alacarte_item_type_master where "
							+ " is_active ='Y'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next() ) {
							ItemBean alaType = new ItemBean();
							alaType.itemTypeId = resultSet.getInt("item_type_id");
							alaType.itemTypeName = resultSet.getString("type_name");
									
							alaCarteTypeList.add(alaType);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("ERROR DUE TO : "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return alaCarteTypeList;
	}
	
	public static ArrayList<ItemBean> loadItemPackingTypeList(Connection connection){
		ArrayList<ItemBean> itemPackingTypeList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select item_pack_type_id,pack_type from fapp_item_pack_type_master where "
							+ " is_active ='Y' and is_delete='N'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next() ) {
							ItemBean packType = new ItemBean();
							packType.packingId = resultSet.getInt("item_pack_type_id");
							packType.packingName = resultSet.getString("pack_type");
									
							itemPackingTypeList.add(packType);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("ERROR DUE TO : "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return itemPackingTypeList;
	}

	public static ArrayList<ManageCuisinBean> loadCuisineList(Connection connection){
		ArrayList<ManageCuisinBean> list = new ArrayList<ManageCuisinBean>();
		if(list.size()>0){
			list.clear();
		}
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT cuisin_name,cuisin_id FROM fapp_cuisins WHERE is_delete = 'N' order by cuisin_id";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCuisinBean cuisinBean = new ManageCuisinBean();
							cuisinBean.cuisinName =  resultSet.getString("cuisin_name");
							cuisinBean.cuisinId = resultSet.getInt("cuisin_id");
								
							list.add(cuisinBean);
						}
						
					}catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ArrayList<ManageCategoryBean> loadCategoryList(Connection connection, int cuisineId){
		ArrayList<ManageCategoryBean> list = new ArrayList<ManageCategoryBean>();
		if(list.size()>0){
			list.clear();
		}
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT category_name,category_id FROM food_category WHERE area_id IS NULL AND is_delete = 'N' "
							+ "AND cuisine_id = ?";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, cuisineId);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							list.add(categoryBean);
						}
						
					}catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
			}
				
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	public static int updateNewUserItemStatus(Connection connection, String status, int itemId){
		int  i =0;
		try {
			PreparedStatement preparedStatement =null;
			String sql = "update food_items set apply_new_user = ? where item_id = ? ";
			try {
				preparedStatement = FappPstm.createQuery(connection, sql, Arrays.asList(status, itemId));
				i = preparedStatement.executeUpdate();
				
			} finally {
				if(preparedStatement != null){
					preparedStatement.close();
				}
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
		
	}
	
	public static String getLastItemCode(Connection connection){
		int i = 0;
		String itemCode = null;
		String sql = "select item_code from food_items where item_id = (select max(item_id) from food_items) ";
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				itemCode = resultSet.getString(1);
				i = Integer.parseInt(itemCode);
				i = i+1;
				itemCode = itemCode.valueOf(i);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return itemCode;
	}

	
	
	
}
