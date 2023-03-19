package org.example.model;

import com.alibaba.fastjson.annotation.JSONField;

public class GroupMessage {
    @JSONField(name = "GROUP_ID")
    private String groupId;
    @JSONField(name = "USER_UID")
    private String userUid;
    @JSONField(name = "SUMMARY")
    private String summary;

    @Override
    public String toString() {
        return "GroupMessage{" +
                "groupId='" + groupId + '\'' +
                ", userUid='" + userUid + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
