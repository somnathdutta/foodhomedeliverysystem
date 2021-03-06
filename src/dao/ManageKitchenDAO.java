package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import sql.ManageKitchenSql;
import utility.FappPstm;
import utility.PropertyFileAccess;
import Bean.ItemBean;
import Bean.ItemTypeBean;
import Bean.ManageKitchens;

public class ManageKitchenDAO {

	public static ArrayList<ManageKitchens> fetchKitchens(Connection connection){
		ArrayList<ManageKitchens> kitchenList = new ArrayList<ManageKitchens>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_kitchens_details where is_active ='Y' and is_delete = 'N'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageKitchens kitchen = new ManageKitchens();
							kitchen.kitchenId = resultSet.getInt("kitchen_id");
							kitchen.kitchenName = resultSet.getString("kitchen_name");
							kitchenList.add(kitchen);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error Due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return kitchenList;
	}
	
	public static ArrayList<ItemBean> fetchKitchenItems(int kitchenId, Connection connection){
		ArrayList<ItemBean> kitchenItemList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_all_kitchen_items where kitchen_id = ? order by item_code";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ItemBean item = new ItemBean();
							item.itemCode = resultSet.getString("item_code");
							item.itemName = resultSet.getString("item_name");
							item.itemDescription = resultSet.getString("item_description");
							item.categoryId = resultSet.getInt("category_id");
							item.categoryName = resultSet.getString("category_name");
							if(resultSet.getString("is_item_active").equals("Y")){
								item.status = "Active";
							}else{
								item.status = "Deactive";
							}
							if(resultSet.getString("is_active_tomorrow").equals("Y")){
								item.tommActive = "Active";
							}else{
								item.tommActive = "Deactive";
							}
							item.lunchStock = resultSet.getInt("stock");
							item.dinnerStock = resultSet.getInt("dinner_stock");
							item.tommorrowLunchStock = resultSet.getInt("stock_tomorrow");
							item.tomorrowDinnerStock = resultSet.getInt("dinner_stock_tomorrow");
							kitchenItemList.add(item);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error Due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return kitchenItemList;
	}
	
	public static boolean updateItem(Connection  connection,ItemBean item, int kicthenId){
		boolean updated  = false;
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "";
					/*if(isAlaCarte){
						sql = "UPDATE fapp_kitchen_items "
								   +" SET is_item_active=?,is_alacarte ='Y' "
								   +" WHERE item_code=? and  kitchen_id=? ";
					}else{*/
						sql = "UPDATE fapp_kitchen_items "
								   +" SET is_item_active=?,is_active_tomorrow=?,stock=?,dinner_stock=?,"
								   + "  stock_tomorrow=?,dinner_stock_tomorrow=?"
								   +" WHERE item_code=? and  kitchen_id=? ";
					//}
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						if(item.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(1, "Y");
						}else{
							preparedStatement.setString(1, "N");
						}
						if(item.tommActive.equalsIgnoreCase("Active")){
							preparedStatement.setString(2, "Y");
						}else{
							preparedStatement.setString(2, "N");
						}
						preparedStatement.setDouble(3, item.lunchStock);
						preparedStatement.setDouble(4, item.dinnerStock);
						preparedStatement.setDouble(5, item.tommorrowLunchStock);
						preparedStatement.setDouble(6, item.tomorrowDinnerStock);
						preparedStatement.setString(7, item.itemCode);
						preparedStatement.setInt(8, kicthenId);
						int updateCount = preparedStatement.executeUpdate();
						if(updateCount > 0){
							updated = true;
						}
					} catch (Exception e) {
						Messagebox.show("Error Due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(updated){
			Messagebox.show("Updated Successfully!","Suucess",Messagebox.OK,Messagebox.INFORMATION);
			return true;
		}else{
			return false;
		}
	}
	
	public static void updateStock(ManageKitchens kitchen, Connection connection){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "Update fapp_kitchen_items set stock = ?,dinner_stock = ?,stock_tomorrow=?, dinner_stock_tomorrow = ?"
							+ " where kitchen_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchen.lunchStock);
						preparedStatement.setInt(2, kitchen.dinnerStock);
						preparedStatement.setInt(3, kitchen.lunchStock);
						preparedStatement.setInt(4, kitchen.dinnerStock);
						preparedStatement.setInt(5, kitchen.kitchenId);
						
						int count = preparedStatement.executeUpdate();
						if(count>0){
							
							System.out.println("Stock updated!");
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e);
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static int updateDeactiveKitchenStock(Connection connection, int kitId){
		int i = 0;
		PreparedStatement preparedStatement = null;
		try {
			
			String sql = "update fapp_kitchen_items set stock = 0, dinner_stock = 0, stock_tomorrow = 0, dinner_stock_tomorrow = 0 where kitchen_id= ? ";
				preparedStatement = FappPstm.createQuery(connection, sql, Arrays.asList(kitId));
				i = preparedStatement.executeUpdate();
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}finally{
			if(preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return i;
		
	}
	
	public static ArrayList<ManageKitchens> loadlunchDinnerDetails(Connection connection, Integer kitchenId){
		ArrayList<ManageKitchens> list = new ArrayList<ManageKitchens>();
		if(list.size()>0){
			list.clear();
		}
		String sql ="select kitchen_id, category_id, category_name, is_active, category_image, is_lunch, is_dinner from vw_category_kitchen_lunch_dinner_details where kitchen_id = ? ";
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, sql, Arrays.asList(kitchenId));
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				ManageKitchens bean = new ManageKitchens();
				bean.setKitchenId(resultSet.getInt("kitchen_id"));
				bean.setCategoryId(resultSet.getInt("category_id"));
				bean.setCategoryName(resultSet.getString("category_name"));
				String status = resultSet.getString("is_active");
				if(status.equals("Y")){
					bean.setStatus("Active");
				}else{
					bean.setStatus("Inactive");
				}
				
				String lnStatus = resultSet.getString("is_lunch");
				if(lnStatus.equals("Y")){
					bean.setLunchStatus("Yes");
				}else {
					bean.setLunchStatus("No");
				}
				
				String dnStatus = resultSet.getString("is_dinner");
				if(dnStatus.equalsIgnoreCase("Y")){
					bean.setDinnerStatus("Yes");
				}else {
					bean.setDinnerStatus("No");
				}
				list.add(bean);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return list;
	}
	
	public static int upDateCategory(Connection connection, ManageKitchens bean){
		int i  =0;
		PreparedStatement preparedStatement = null;
		String sql = "update fapp_kitchen_stock set is_active = ?, is_lunch = ?, is_dinner = ? where kitchen_id = ? and kitchen_category_id = ? ";
		try {
			//preparedStatement = FappPstm.createQuery(connection, sql, Arrays.asList(bean.status, bean.lunchStatus, bean.dinnerStatus, bean.kitchenId, bean.categoryId));
			
			preparedStatement = connection.prepareStatement(sql);
			if(bean.status.equals("Active")){
				preparedStatement.setString(1, "Y");
			}else {
				preparedStatement.setString(1, "N");
			}
			if(bean.lunchStatus.equalsIgnoreCase("Yes")){
				preparedStatement.setString(2, "Y");
			}else {
				preparedStatement.setString(2, "N");
			}
			if(bean.dinnerStatus.equalsIgnoreCase("Yes")){
				preparedStatement.setString(3, "Y");
			}else {
				preparedStatement.setString(3, "N");
			}
			
			preparedStatement.setInt(4, bean.kitchenId);
			preparedStatement.setInt(5, bean.categoryId);
			
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static ArrayList<ItemTypeBean> loadItemTypes(Connection connection){
		ArrayList<ItemTypeBean> list = new ArrayList<ItemTypeBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, ManageKitchenSql.LOADITEMTYPES, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ItemTypeBean bean = new ItemTypeBean();
				bean.setItemTypeId(resultSet.getInt("item_type_id"));
				bean.setItemType(resultSet.getString("type_name"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
			
		}
		return list;
	}
	
	public static int updateItemType(Connection connection, ItemTypeBean bean, String userName){
		int i = 0;
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = FappPstm.createQuery(connection, ManageKitchenSql.UPDATEITEMTYPES, Arrays.asList(bean.getItemType(),userName, bean.getItemTypeId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return i;
	}
	
	public static int SaveItemType(Connection connection, ItemTypeBean bean, String userName){
		int i = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, ManageKitchenSql.INSERTITEMTYPESSQL, Arrays.asList(bean.getItemType(), userName, userName));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return i;
		
	}
	
	public static ArrayList<ItemTypeBean> loadItemTypeKitchen(Connection connection, Integer kitchenId){
		ArrayList<ItemTypeBean> list= new ArrayList<ItemTypeBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection,PropertyFileAccess.getPropertyObject().getPropValues("get_manage_kitchen_item_capacity_list", "manage_kitchen_items_capacity.properties") , Arrays.asList(kitchenId)); //ManageKitchenSql.LOADIEMTYPEKITCHENSQL
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ItemTypeBean bean = new ItemTypeBean();
				
				bean.setStockUpdationId(resultSet.getInt("stock_updation_id"));
				bean.setKitchenName(resultSet.getString("kitchen_name"));
				bean.setItemType(resultSet.getString("type_name"));
				bean.setItemTypeId(resultSet.getInt("item_type_id"));
				bean.setLunchStock(resultSet.getInt("lunch_stock"));
				bean.setDinnerStok(resultSet.getInt("dinner_stock"));
				
				list.add(bean);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
		
	}
	
	public static int saveApplyStock(Connection connection, Integer kitchenId , ArrayList<ItemTypeBean> list){
		int returnCode = 0;
		PreparedStatement pstm = null;
		try {
			pstm = connection.prepareStatement(PropertyFileAccess.getPropertyObject().getPropValues("save_apply_stock_manage_kitchen_item_capacity", "manage_kitchen_items_capacity.properties"));
			for(ItemTypeBean bean: list){
				pstm.setInt(1, bean.getLunchStock());
				pstm.setInt(2, bean.getDinnerStok());
				pstm.setInt(3, kitchenId);
				pstm.setInt(4, bean.getItemTypeId());
				
				pstm.addBatch();
			}
			int[] count= pstm.executeBatch();
			
			returnCode = count.length;
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return returnCode;
	} // end saveApplyStock()
	
	
	public static ArrayList<ItemTypeBean> loadKitchenItemTypeNotInKitchen(Connection connection, Integer kitchenId){
		ArrayList<ItemTypeBean> list = new ArrayList<ItemTypeBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, ManageKitchenSql.LOADITEMTYPENOTEXISTINKITCHEN, Arrays.asList(kitchenId));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ItemTypeBean bean = new ItemTypeBean();
				bean.setItemTypeId(resultSet.getInt("item_type_id"));
				bean.setItemType(resultSet.getString("type_name"));
				
				list.add(bean);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
			
		}
		return list;
	}
	
	
	public static int AddNewItemTypeToKitchen(Connection connection,Integer kId, ItemTypeBean bean){
		int i = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, ManageKitchenSql.INSERTITEMKITCHENTYPE, Arrays.asList(kId, bean.getItemTypeId(),bean.getLunchStock(), bean.getDinnerStok()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return i;
	}
	
	public static int updateKitchenItemTypeStock(Connection connection, ItemTypeBean bean){
		int i = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, ManageKitchenSql.UPDATEITEMTYPENOTEXISTINKITCHEN, Arrays.asList(bean.getLunchStock(), bean.getDinnerStok(), bean.getStockUpdationId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return i;
	}
	
	public static int updateKitchenSingleOrder(Connection connection, ManageKitchens kitchens){
		int i= 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, ManageKitchenSql.UPDATESINGLEORDER, Arrays.asList(kitchens.getNoOfSingleOrderLunch(), kitchens.getNoOfSingleOrderDinner(), kitchens.kitchenId));
			
			i = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return i;
		
	}
	
	public static ArrayList<ManageKitchens> loadKitchenSingleOrder(Connection connection){
		ArrayList<ManageKitchens> list = new ArrayList<ManageKitchens>();
		if(list.size()>0){
			list.clear(); 
		}
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, ManageKitchenSql.LOADSINGLEORDERKITCHEN, null);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ManageKitchens kitchens = new ManageKitchens();
				kitchens.kitchenId = resultSet.getInt("kitchen_id");
				kitchens.kitchenName = resultSet.getString("kitchen_name");
				kitchens.setNoOfSingleOrderLunch(resultSet.getInt("no_of_single_order_lunch"));
				kitchens.setNoOfSingleOrderDinner(resultSet.getInt("no_of_single_order_dinner"));
				
				list.add(kitchens);		
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
	}
	
	
	
}
