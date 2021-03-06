package sql;

public class SetMasterSql {

	public static final String insertSetMasterQuery = "INSERT INTO fapp_set_master(set_name, created_by, updated_by) VALUES (?, ?, ?) ";
	
	public static final String selectMaxSetId = "select max(set_id)as max_id from fapp_set_master ";
	
	public static final String insertSetItemQuery = "INSERT INTO fapp_set_item(set_id, item_code, created_by, updated_by) VALUES (?, ?, ?, ?) ";
	
	public static final String selctExsistingSetQuery = "select * from vw_set_item_food_details where set_id = ? ";
	
	public static final String selectSetListQuery = "select set_id, set_name from  fapp_set_master where is_delete ='N' and is_active = 'Y' order by set_id ";
	
	public static final String SelectDayTypeQuery = "select order_type_id, order_type, is_active, is_delete from  fapp_order_type_master order by order_type_id ";
	
	public static final String updateTodayStatusQuery = "update fapp_kitchen_items set is_active = 'Y',is_item_active='Y' where item_code = ? and is_delete = 'N' ";
	
	public static final String updateTomorrowStatusQuery = "update fapp_kitchen_items set is_active_tomorrow = 'Y',is_item_active='Y'  where item_code = ? and is_delete = 'N' " ;
	
	public static final String upDateSetMasterQuery = "update fapp_set_master set order_type_id = ? where set_id = ? ";
	
	public static final String updateSetItemStatus = "update fapp_set_item set is_active = ? where set_item_id = ? ";
	
	public static final String updateKitchenItemSetStatus= "update fapp_kitchen_items set is_active = ?, is_active_tomorrow = ? where item_code = ? ";
	
	public static final String fetchExistingSetItemQuery = "select item_code from vw_set_item_food_details where set_id = ? ";
	
	public static final String selectSetItemQuery = "select item_id,item_code,item_name,item_description from vw_food_item_details where category_id = ? and item_id <> in ";
	
	
}
