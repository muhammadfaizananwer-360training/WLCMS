package com.softech.common.parsing;

public interface ITabularParsingHandler {
	boolean handlingRequired(String tableName);
	boolean handleRow(String tableName,Object[] row, int rowNum) throws TabularDataException;
}
