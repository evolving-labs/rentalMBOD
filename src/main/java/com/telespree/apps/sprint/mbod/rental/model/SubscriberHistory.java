package com.telespree.apps.sprint.mbod.rental.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * SubscriberHistory
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2012/11/29 17:34:18 $
 * @version $Revision: #1 $
 * 
 */
public class SubscriberHistory implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String userId;
    private String deviceId;
    private String historyNotes;
    private Calendar historyTime;
    private String historyType;

    /**
     * @param deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param historyNotes
     */
    public void setHistoryNotes(String historyNotes) {
        this.historyNotes = historyNotes;
    }

    /**
     * @return
     */
    public String getHistoryNotes() {
        return historyNotes;
    }

    /**
     * @param historyTime
     */
    public void setHistoryTime(Calendar historyTime) {
        this.historyTime = historyTime;
    }

    /**
     * @return
     */
    public Calendar getHistoryTime() {
        return historyTime;
    }

    /**
     * @param historyType
     */
    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }

    /**
     * @return
     */
    public String getHistoryType() {
        return historyType;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
