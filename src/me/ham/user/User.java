package me.ham.user;

import me.ham.Util;
import me.ham.product.Book;
import me.ham.product.Type;
import me.ham.store.BookStore;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

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

    public Map<Integer, BigDecimal> purchaseBook(Book book, BigDecimal stock){
        purchaseMap.compute(book.getNo()
        , (key, value) -> {
                   return value == null ? stock : value.add(stock);
                });
        return purchaseMap;
    }

    public Map<Integer, BigDecimal> getPurchaseHistory() {
        return purchaseHistory;
    }

    public Map<Integer, BigDecimal> addPurchaseHistoryBook(BookStore bookStore){
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            purchaseHistory.compute(purchaseBook.getKey()
                    , (key, value) -> {
                        if(Type.CLASS == bookStore.getBookMap().get(purchaseBook.getKey()).getType() && !validationPurchaseClass(purchaseBook.getKey())){
                            throw new RuntimeException("Class는 중복구매 불가");
                        }
                        return value == null ? purchaseBook.getValue() : value.add(purchaseBook.getValue());
                    });
        }
        this.purchaseMap.clear();
        return purchaseHistory;
    }

    //구매이력이 없어야함
    private boolean validationPurchaseClass(Integer no) {
        return purchaseHistory.get(no) == null;
    }

    public void getOrderList(BookStore bookStore){
        System.out.println("주문 내역:");
        Util.printDash();
        for(Map.Entry<Integer, BigDecimal> book :  purchaseMap.entrySet()){
            System.out.printf("%s - %s개%n",bookStore.getBookMap().get(book.getKey()).getName(), book.getValue().toString());
        }
    }

    public BigDecimal getOrderPrice(BookStore bookStore) {
        BigDecimal totalPrice = new BigDecimal(0);
        for(Map.Entry<Integer, BigDecimal> purchaseBook : purchaseMap.entrySet()){
            //BookPrice x 구매 개수
            totalPrice = totalPrice.add(bookStore.getBookMap().get(purchaseBook.getKey()).getPrice().multiply(purchaseBook.getValue()));
        }

        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        Util.printDash();
        System.out.printf("주문금액: %s%n", decimalFormat.format(totalPrice));
        if(totalPrice.compareTo(new BigDecimal("50000"))==-1){
            totalPrice.add(bookStore.getDeliveryPrice());
            System.out.printf("배송비: %s%n", decimalFormat.format(bookStore.getDeliveryPrice()));
        }

        return totalPrice;
    }
}
