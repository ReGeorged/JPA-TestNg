package j.di;


import j.annotations.SimpleComponent;
import j.annotations.SimplyAutoWired;

@SimpleComponent
public class OrderService {

    @SimplyAutoWired
    private OrderRepository repository;

    public Order getOrderDetails(Integer orderId) {
        return repository.getById(orderId);
    }
}