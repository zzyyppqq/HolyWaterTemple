package com.holywatertemple.java_lib.bean;

import java.io.Serializable;
import java.util.List;

public class Table implements Serializable{
    private Header header;
    private List<Person> personList;

    public Table() {
    }

    public Table(Header header, List<Person> personList) {
        this.header = header;
        this.personList = personList;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public String toString() {
        return "Table{" +
                "header=" + header +
                "size =" + (personList != null ? personList.size() : 0) +
                ", personList=" + personList +
                '}';
    }
}
