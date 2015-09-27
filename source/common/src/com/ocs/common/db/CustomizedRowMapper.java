package com.ocs.common.db;

import java.sql.ResultSet;

public interface CustomizedRowMapper<T> {
	
	public void map(T object, ResultSet rs);
	
}
