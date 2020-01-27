package me.ham.order;

import me.ham.user.User;

public interface OrderService {
    boolean order(User user);

    void postOrder(User user);
}
