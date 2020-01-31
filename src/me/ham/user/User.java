package me.ham.user;

import me.ham.product.Type;
import me.ham.store.BookStore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class User {
    private Integer id;
    private Map<Integer, BigDecimal> purchaseBooks;
    private Map<Integer, BigDecimal> purchaseBooksHistory;

    public User(Integer id) {
        this.id = id;
        this.purchaseBooks = new HashMap<>();
        this.purchaseBooksHistory = new HashMap<>();
    }

    public Integer getId() {
        return id;
    }

    public Map<Integer, BigDecimal> getPurchaseBooks() {
        return purchaseBooks;
    }

    public Map<Integer, BigDecimal> getPurchaseBooksHistory() {
        return purchaseBooksHistory;
    }

    //구매
    public void purchaseBook(Integer bookNo, BigDecimal quantity) {
        purchaseBooks.compute(bookNo
                , (key, value) -> {
                    return value == null ? quantity : value.add(quantity);
                });
    }

    //구매 이력 쌓기
    public Map<Integer, BigDecimal> addPurchaseHistoryBook(){
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseBooks.entrySet()){
            purchaseBooksHistory.compute(purchaseBook.getKey()
                    , (key, value) -> {
                        return value == null ? purchaseBook.getValue() : value.add(purchaseBook.getValue());
                    });
        }
        return purchaseBooksHistory;
    }

    public void clearPurchaseBooks() {
        this.purchaseBooks.clear();
    }


    //구매이력이 없어야함
    private boolean alreadyPurchaseClassType(Integer no) {
        return purchaseBooksHistory.get(no) != null;
    }

    //총 주문가격(배송비계산)
    public BigDecimal getOrderTotalAmount(BookStore bookStore) {
        BigDecimal amount = getOrderPrice(bookStore);
        if(!bookStore.isFreeShips(amount)){
            return amount.add(bookStore.getDeliveryPrice());
        }else{
            return amount;
        }
    }

    public BigDecimal getOrderPrice(BookStore bookStore) {
        return purchaseBooks.entrySet().stream()
                .map(e -> {
                    BigDecimal bookPrice = bookStore.getBookInfo().get(e.getKey()).getPrice();
                    return bookPrice.multiply(e.getValue());
                }).reduce(new BigDecimal(0), BigDecimal::add);
    }

    //TYPE.CLASS 상품은 구매이력 존재시 재구매 불가
    public boolean purchaseValidation(BookStore bookStore) {
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseBooks.entrySet()){
            Type type = bookStore.getBookInfo().get(purchaseBook.getKey()).getType();
            if(Type.CLASS == type && alreadyPurchaseClassType(purchaseBook.getKey())){
                throw new RuntimeException("Class 중복 구매 불가::"+bookStore.getBookInfo().get(purchaseBook.getKey()).getName());
            }
        }
        return true;
    }
}
