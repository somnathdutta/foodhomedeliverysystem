package sql;

public class SetMasterSql {

	public static final String insertSetMasterQuery = "INSERT INTO fapp_set_master(set_name, created_by, updated_by) VALUES (?, ?, ?) ";
	
	public static final String selectMaxSetId = "select max(set_id)as max_id from fapp_set_master ";
	
	public static final String insertSetItemQuery = "INSERT INTO fapp_set_item(set_id, item_code, created_by, updated_by) VALUES (?, ?, ?, ?)";
}
