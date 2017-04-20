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

@DynamoDBTable(tableName = "appforscrap-mobilehub-1250419665-MakesPickupRequest")

public class MakesPickupRequestDO {
    private Double _requestId;
    private String _username;

    @DynamoDBHashKey(attributeName = "RequestId")
    @DynamoDBAttribute(attributeName = "RequestId")
    public Double getRequestId() {
        return _requestId;
    }

    public void setRequestId(final Double _requestId) {
        this._requestId = _requestId;
    }
    @DynamoDBAttribute(attributeName = "Username")
    public String getUsername() {
        return _username;
    }

    public void setUsername(final String _username) {
        this._username = _username;
    }

}
