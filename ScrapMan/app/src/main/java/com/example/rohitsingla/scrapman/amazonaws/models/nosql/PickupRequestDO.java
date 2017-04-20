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

@DynamoDBTable(tableName = "appforscrap-mobilehub-1250419665-PickupRequest")

public class PickupRequestDO {
    private Double _requestId;
    private String _day;
    private Double _status;
    private String _timeSlot;

    @DynamoDBHashKey(attributeName = "RequestId")
    @DynamoDBAttribute(attributeName = "RequestId")
    public Double getRequestId() {
        return _requestId;
    }

    public void setRequestId(final Double _requestId) {
        this._requestId = _requestId;
    }
    @DynamoDBAttribute(attributeName = "Day")
    public String getDay() {
        return _day;
    }

    public void setDay(final String _day) {
        this._day = _day;
    }
    @DynamoDBAttribute(attributeName = "Status")
    public Double getStatus() {
        return _status;
    }

    public void setStatus(final Double _status) {
        this._status = _status;
    }
    @DynamoDBAttribute(attributeName = "TimeSlot")
    public String getTimeSlot() {
        return _timeSlot;
    }

    public void setTimeSlot(final String _timeSlot) {
        this._timeSlot = _timeSlot;
    }

}
