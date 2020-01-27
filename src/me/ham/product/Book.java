package me.ham.product;

import java.math.BigDecimal;

public class Book {
    private Integer no;
    private Type type;
    private String name;
    private BigDecimal price;
    private BigDecimal stock;

    public Book(Integer no, Type type, String name, BigDecimal price, BigDecimal stock) {
        this.no = no;
        this.type = type;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }
}
