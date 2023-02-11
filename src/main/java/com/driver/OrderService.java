package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public String addOrder (Order order) {
        return orderRepository.addOrder(order);
    }

    public String addPartner (String partnerId) {
        return orderRepository.addPartner(partnerId);
    }

    public String addOrderPartnerPair (String orderId, String partnerId) {
        return orderRepository.appOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return orderRepository.getPartnerById(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public int getCountOfUnsignedOrders() {
        return orderRepository.getCountOfUnsignedOrders();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        return orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
    }

    public String deletePartnerById (String partnerId) {
        return orderRepository.deletePartnerById(partnerId);
    }

    public String deleteOrderById (String orderId) {
        return orderRepository.deleteOrderById(orderId);
    }

}
