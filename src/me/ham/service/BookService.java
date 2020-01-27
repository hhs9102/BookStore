package me.ham.service;

import me.ham.product.Book;
import me.ham.user.User;

import java.math.BigDecimal;

public interface BookService {
    void addBook(User user, Integer bookNo, BigDecimal purchaseCount);
    void order(User user);
}
