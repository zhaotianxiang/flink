package org.example.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class GroupSession {
    @JSONField(name = "GROUP_ID")
    private String groupId;
    @JSONField(name = "USER_UIDS")
    private List<String> userUids;
    @JSONField(name = "SUMMARYS")
    private List<String> summarys;

    @Override
    public String toString() {
        return "GroupSession{" +
                "groupId='" + groupId + '\'' +
                ", userUids=" + userUids +
                ", summarys=" + summarys +
                '}';
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getUserUids() {
        return userUids;
    }

    public void setUserUids(List<String> userUids) {
        this.userUids = userUids;
    }

    public List<String> getSummarys() {
        return summarys;
    }

    public void setSummarys(List<String> summarys) {
        this.summarys = summarys;
    }
}
