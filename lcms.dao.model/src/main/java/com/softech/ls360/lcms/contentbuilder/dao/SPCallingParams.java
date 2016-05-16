package com.softech.ls360.lcms.contentbuilder.dao;

import javax.persistence.ParameterMode;


public class SPCallingParams
{
	public String Param;	
	public String Value;
	public Class<?>  ValueType;
	public ParameterMode parameterMode;
	
	
	public String getParam() {
		return Param;
	}
	public void setParam(String param) {
		Param = param;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public Class<?> getValueType() {
		return ValueType;
	}
	public void setValueType(Class<?> valueType) {
		ValueType = valueType;
	}
	public ParameterMode getParameterMode() {
		return parameterMode;
	}
	public void setParameterMode(ParameterMode parameterMode) {
		this.parameterMode = parameterMode;
	}

}