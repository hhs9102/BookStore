package me.ham.exception;

import me.ham.product.Book;

import java.math.BigDecimal;

public class SoldOutException extends RuntimeException {
    static final long serialVersionUID = 293879874987324L;

    public SoldOutException() {
    }

    public SoldOutException(String message) {
        super(message);
    }

    public SoldOutException(Book book, BigDecimal purchaseCount) {
        super(String.format("(%s)의 재고가 %s개 남았습니다. %s개를 주문할 수 없습니다.", book.getName(), book.getStock(), purchaseCount));
    }
}
