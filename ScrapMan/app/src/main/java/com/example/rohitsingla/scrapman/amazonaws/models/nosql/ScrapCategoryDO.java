package com.example.rohitsingla.scrapman.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "appforscrap-mobilehub-1250419665-ScrapCategory")

public class ScrapCategoryDO {
    private String _categoryName;
    private Double _unitPrice;

    @DynamoDBHashKey(attributeName = "CategoryName")
    @DynamoDBAttribute(attributeName = "CategoryName")
    public String getCategoryName() {
        return _categoryName;
    }

    public void setCategoryName(final String _categoryName) {
        this._categoryName = _categoryName;
    }
    @DynamoDBAttribute(attributeName = "UnitPrice")
    public Double getUnitPrice() {
        return _unitPrice;
    }

    public void setUnitPrice(final Double _unitPrice) {
        this._unitPrice = _unitPrice;
    }

}
