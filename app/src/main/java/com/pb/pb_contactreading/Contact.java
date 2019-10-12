package com.pb.pb_contactreading;

public class Contact {
    public String name;
    public String number;
    public String add;
  //  public String

    public Contact(String name, String number, String add){
        this.name = name;
        this.number = number;
        this.add = add;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }
}
