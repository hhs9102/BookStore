package me.ham.store;

import me.ham.exception.SoldOutException;
import me.ham.product.Book;
import me.ham.user.User;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookStore {

    private BigDecimal deliveryPrice;   //배송금액
    private BigDecimal freeShipsAmount; //해당금액 이상 무료배송
    private ConcurrentHashMap<Integer, Book> bookInfo; //no, Book


    public BookStore(BigDecimal deliveryPrice, BigDecimal freeShipsAmount) {
        this.deliveryPrice = deliveryPrice;
        this.freeShipsAmount = freeShipsAmount;
        this.bookInfo = new ConcurrentHashMap<>();
    }

    public void setBookInfo(ConcurrentHashMap<Integer, Book> bookInfo) {
        this.bookInfo = bookInfo;
    }

    public Map<Integer, Book> getBookInfo() {
        return bookInfo;
    }

    public boolean checkStock(Map<Integer, BigDecimal> purchaseMap) {
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            if(bookInfo.get(purchaseBook.getKey()).getStock().compareTo(purchaseBook.getValue())==-1){
                throw new SoldOutException(bookInfo.get(purchaseBook.getKey()), purchaseBook.getValue());
            }
        }
        return true;
    }

    public void updateStock(Map<Integer, BigDecimal> purchaseMap) {
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            Book book = bookInfo.get(purchaseBook.getKey());
            book.setStock(book.getStock().subtract(purchaseBook.getValue()));
        }
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public boolean isFreeShips(BigDecimal amount){
        return amount.compareTo(freeShipsAmount)>0;
    }

    public boolean purchaseValidation(User user) {
        for(Map.Entry<Integer, BigDecimal> purchaseBook : user.getPurchaseBooks().entrySet()){
            Integer puchaseBookKey = purchaseBook.getKey();
            if(bookInfo.get(puchaseBookKey).getStock().compareTo(purchaseBook.getValue())==-1){
                throw new SoldOutException(bookInfo.get(purchaseBook), purchaseBook.getValue());
            }
        }
        return true;
    }
}
