package com.wayn.common.base;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BusinessEntity<T> extends BaseEntity<T> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1354549673923692016L;
	private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public BusinessEntity<T> setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("remarks", remarks)
                .toString();
    }
}
