package me.ham.user;

import me.ham.Util;
import me.ham.product.Book;
import me.ham.product.Type;
import me.ham.store.BookStore;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class User {
    private Integer id;
    private Map<Integer, BigDecimal> purchaseMap;
    private Map<Integer, BigDecimal> purchaseHistory;

    public User(Integer id) {
        this.id = id;
        this.purchaseMap = new HashMap<>();
        this.purchaseHistory = new HashMap<>();
    }

    public Integer getId() {
        return id;
    }

    public Map<Integer, BigDecimal> getPurchaseMap() {
        return purchaseMap;
    }

    public Map<Integer, BigDecimal> getPurchaseHistory() {
        return purchaseHistory;
    }

    public Map<Integer, BigDecimal> addPurchaseHistoryBook(){
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            purchaseHistory.compute(purchaseBook.getKey()
                    , (key, value) -> {
                        return value == null ? purchaseBook.getValue() : value.add(purchaseBook.getValue());
                    });
        }
        return purchaseHistory;
    }

    public void clasePurchaseMap() {
        this.purchaseMap.clear();
    }


    //구매이력이 없어야함
    private boolean alreadyPurchaseClassType(Integer no) {
        return purchaseHistory.get(no) != null;
    }

    public void printOrderList(BookStore bookStore){
        System.out.println("주문 내역:");
        Util.printDash();
        for(Map.Entry<Integer, BigDecimal> book :  purchaseMap.entrySet()){
            System.out.printf("%s - %s개%n",bookStore.getBookInfo().get(book.getKey()).getName(), book.getValue().toString());
        }
    }

    public void printOrderPrice(BookStore bookStore){
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        BigDecimal totalPrice = getOrderPrice(bookStore);

        Util.printDash();
        System.out.printf("주문금액: %s원%n", decimalFormat.format(totalPrice));
        if(totalPrice.compareTo(new BigDecimal("50000"))==-1){
            totalPrice.add(bookStore.getDeliveryPrice());
            System.out.printf("배송비: %s원%n", decimalFormat.format(bookStore.getDeliveryPrice()));
        }
    }

    public BigDecimal getOrderPrice(BookStore bookStore) {
        BigDecimal totalPrice = new BigDecimal(0);
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            //BookPrice x 구매 개수
            totalPrice = totalPrice.add(bookStore.getBookInfo().get(purchaseBook.getKey()).getPrice().multiply(purchaseBook.getValue()));
        }
        return totalPrice;
    }

    public boolean purchaseValidation(BookStore bookStore) {
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            Type type = bookStore.getBookInfo().get(purchaseBook.getKey()).getType();
            if(Type.CLASS == type && alreadyPurchaseClassType(purchaseBook.getKey())){
                throw new RuntimeException("Class 중복 구매 불가::"+bookStore.getBookInfo().get(purchaseBook.getKey()).getName());
            }
        }
        return true;
    }
}
