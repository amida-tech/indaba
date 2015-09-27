package com.ocs.indaba.dao.common;

import java.sql.ResultSet;

public interface CustomizedRowMapper<T> {
	
	public void map(T object, ResultSet rs);
	
}
