package com.holywatertemple.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class PersonData {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "type")
    private int type;
    @Property(nameInDb = "version")
    private int version;

    @Property(nameInDb = "jossId")
    private String jossId;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "phoneNum")
    private String phoneNum;
    @Property(nameInDb = "jossType")
    private String jossType;
    @Property(nameInDb = "fendPrice")
    private String fendPrice;
    @Property(nameInDb = "fendTime")
    private String fendTime;
    @Property(nameInDb = "extendTime")
    private String extendTime;
    @Property(nameInDb = "tableInfo")
    private String tableInfo;
    @Generated(hash = 1247934409)
    public PersonData(Long id, int type, int version, String jossId, String name,
            String phoneNum, String jossType, String fendPrice, String fendTime,
            String extendTime, String tableInfo) {
        this.id = id;
        this.type = type;
        this.version = version;
        this.jossId = jossId;
        this.name = name;
        this.phoneNum = phoneNum;
        this.jossType = jossType;
        this.fendPrice = fendPrice;
        this.fendTime = fendTime;
        this.extendTime = extendTime;
        this.tableInfo = tableInfo;
    }

    public PersonData(int type, int version, String jossId, String name,
                      String phoneNum, String jossType, String fendPrice, String fendTime,
                      String extendTime, String tableInfo) {
        this.type = type;
        this.version = version;
        this.jossId = jossId;
        this.name = name;
        this.phoneNum = phoneNum;
        this.jossType = jossType;
        this.fendPrice = fendPrice;
        this.fendTime = fendTime;
        this.extendTime = extendTime;
        this.tableInfo = tableInfo;
    }
    @Generated(hash = 212076876)
    public PersonData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getVersion() {
        return this.version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public String getJossId() {
        return this.jossId;
    }
    public void setJossId(String jossId) {
        this.jossId = jossId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoneNum() {
        return this.phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getJossType() {
        return this.jossType;
    }
    public void setJossType(String jossType) {
        this.jossType = jossType;
    }
    public String getFendPrice() {
        return this.fendPrice;
    }
    public void setFendPrice(String fendPrice) {
        this.fendPrice = fendPrice;
    }
    public String getFendTime() {
        return this.fendTime;
    }
    public void setFendTime(String fendTime) {
        this.fendTime = fendTime;
    }
    public String getExtendTime() {
        return this.extendTime;
    }
    public void setExtendTime(String extendTime) {
        this.extendTime = extendTime;
    }
    public String getTableInfo() {
        return this.tableInfo;
    }
    public void setTableInfo(String tableInfo) {
        this.tableInfo = tableInfo;
    }


}
