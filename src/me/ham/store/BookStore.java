package me.ham.store;

import me.ham.product.Book;
import me.ham.user.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookStore {

    private BigDecimal deliveryPrice;
    private ConcurrentHashMap<Integer, Book> bookMap; //no, Book


    public BookStore(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
        this.bookMap = new ConcurrentHashMap<>();
    }

    public void setBookMap(ConcurrentHashMap<Integer, Book> bookMap) {
        this.bookMap = bookMap;
    }

    public Map<Integer, Book> getBookMap() {
        return bookMap;
    }

    public boolean checkStock(Map<Integer, BigDecimal> purchaseMap) {
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            if(bookMap.get(purchaseBook.getKey()).getStock().compareTo(purchaseBook.getValue())==-1){
                throw new RuntimeException(bookMap.get(purchaseBook.getKey()).getName()+" IS SOLD OUT");
            }
        }
        return true;
    }

    public void updateStock(Map<Integer, BigDecimal> purchaseMap) {
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            Book book = bookMap.get(purchaseBook.getKey());
            book.setStock(book.getStock().subtract(purchaseBook.getValue()));
        }
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }
}
