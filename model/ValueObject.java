package com.pinguela.rentexpres.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ValueObject {

	
	@SuppressWarnings("static-access")
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE.MULTI_LINE_STYLE);

	}

}
