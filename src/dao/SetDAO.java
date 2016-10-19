package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.shape.Cylinder;
import jdk.nashorn.internal.ir.SetSplitState;

import org.zkoss.zul.Messagebox;

import com.itextpdf.text.Phrase;

import sql.SetMasterSql;
import utility.FappPstm;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.SetBean;

public class SetDAO {

	/**
	 * Load all categories
	 */
	public static ArrayList<ManageCategoryBean> onLoadCategoryList(Connection connection){
		ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT distinct category_name,category_id FROM vw_food_item_details order by category_id  ";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							categoryBeanList.add(categoryBean);
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
		return categoryBeanList;
	}
	
	public static ArrayList<ItemBean> loadItemsFromCategory(Connection  connection, int categoryId){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		try {
			SQL:{
			 PreparedStatement preparedStatement = null;
			 ResultSet resultSet = null;
			 String sql = "select item_id,item_code,item_name,item_description from vw_food_item_details where category_id = ?";
			 try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, categoryId);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ItemBean item = new ItemBean();
					item.itemId = resultSet.getInt("item_id");
					item.itemCode = resultSet.getString("item_code");
					item.itemName = resultSet.getString("item_name");
					item.itemDescription = resultSet.getString("item_description");
					itemBeanList.add(item);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Messagebox.show("ERROR Due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return itemBeanList;
	}
	
	public static ArrayList<SetBean> fetchExistingSetItems(Connection connection){
		ArrayList<SetBean> list = new ArrayList<SetBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		try {
			
			preparedStatement = FappPstm.createQuery(connection, SetMasterSql.selctExsistingSetQuery,null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SetBean bean = new SetBean();
				
				bean.setSetId(resultSet.getInt("set_id"));
				bean.setSetName(resultSet.getString("set_name"));
				bean.getItemBean().setItemId(resultSet.getInt("item_id"));
				bean.getItemBean().setItemCode(resultSet.getString("item_code"));
				bean.getItemBean().setItemName(resultSet.getString("item_name"));
				bean.getItemBean().setItemDescription(resultSet.getString("item_description"));
				
				list.add(bean);
				
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK,Messagebox.ERROR);
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
		
		return list;
		
		
	}
}
