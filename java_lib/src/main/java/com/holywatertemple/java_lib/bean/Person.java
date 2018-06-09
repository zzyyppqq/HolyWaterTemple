package com.holywatertemple.java_lib.bean;

import java.io.Serializable;

public class Person implements Serializable{
    private String jossId;
    private String name;
    private String phoneNum;
    private String jossType;
    private String fendPrice;
    private String fendTime;
    private String extendTime;

    public Person(String jossId, String name, String phoneNum, String jossType, String fendPrice, String fendTime, String extendTime) {
        this.jossId = jossId;
        this.name = name;
        this.phoneNum = phoneNum;
        this.jossType = jossType;
        this.fendPrice = fendPrice;
        this.fendTime = fendTime;
        this.extendTime = extendTime;
    }

    public String getJossId() {
        return jossId;
    }

    public void setJossId(String jossId) {
        this.jossId = jossId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getJossType() {
        return jossType;
    }

    public void setJossType(String jossType) {
        this.jossType = jossType;
    }

    public String getFendPrice() {
        return fendPrice;
    }

    public void setFendPrice(String fendPrice) {
        this.fendPrice = fendPrice;
    }

    public String getFendTime() {
        return fendTime;
    }

    public void setFendTime(String fendTime) {
        this.fendTime = fendTime;
    }

    public String getExtendTime() {
        return extendTime;
    }

    public void setExtendTime(String extendTime) {
        this.extendTime = extendTime;
    }

    @Override
    public String toString() {
        return "Persion{" +
                "jossId='" + jossId + '\'' +
                ", name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", jossType='" + jossType + '\'' +
                ", fendPrice=" + fendPrice +
                ", fendTime='" + fendTime + '\'' +
                ", extendTime='" + extendTime + '\'' +
                '}';
    }
}
