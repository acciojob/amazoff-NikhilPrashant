package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashSet<String> orderPending;
    private HashMap<String, List<String>> deliveryPartnerArrayListHashMap;
    //Check
    public OrderRepository() {
        this.orderMap = new HashMap<>();
        this.partnerMap = new HashMap<>();
        this.orderPending = new HashSet<>();
        this.deliveryPartnerArrayListHashMap = new HashMap<>();
    }

    public void addOrder(Order order) {
        orderMap.put(order.getId(), order);
        orderPending.add(order.getId());
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId, deliveryPartner);
        deliveryPartnerArrayListHashMap.put(partnerId, deliveryPartnerArrayListHashMap.getOrDefault(partnerId, new ArrayList<String>()));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if (orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId) && orderPending.contains(orderId)) {
            orderPending.remove(orderId);
            partnerMap.get(partnerId).setNumberOfOrders(partnerMap.get(partnerId).getNumberOfOrders() + 1);
            deliveryPartnerArrayListHashMap.get(partnerId).add(orderId);
        }
    }

    public Order getOrderById(String orderId) {
        if (!orderMap.containsKey(orderId)) return null;
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if (!partnerMap.containsKey(partnerId)) return null;
        return partnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        if (!partnerMap.containsKey(partnerId)) return 0;
        return partnerMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        if (!partnerMap.containsKey(partnerId)) return null;
        return deliveryPartnerArrayListHashMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> ordersList = new ArrayList<>();
        for (String orderId: orderMap.keySet()) {
            ordersList.add(orderId);
        }
        return ordersList;
    }

    public int getCountOfUnassignedOrders() {
        return orderPending.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int count = 0;
        List<String> ordersList = deliveryPartnerArrayListHashMap.get(partnerId);
        int expectedTime = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));
        for (String orderId: ordersList) {
            if( orderMap.get(orderId).getDeliveryTime() > expectedTime) count++;
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int max = 0;
        List<String> ordersList = deliveryPartnerArrayListHashMap.get(partnerId);
        for (String orderId: ordersList) {
            if(orderMap.get(orderId).getDeliveryTime() > max) max = orderMap.get(orderId).getDeliveryTime();
        }
        String hours = String.valueOf(max / 60);
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        String minutes = String.valueOf(max % 60);
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }

    public void deletePartnerById (String partnerId) {
        orderPending.addAll(deliveryPartnerArrayListHashMap.get(partnerId));
        partnerMap.remove(partnerId);
        deliveryPartnerArrayListHashMap.remove(partnerId);
    }

    public void deleteOrderById (String orderId) {
        orderMap.remove(orderId);
        if (orderPending.contains(orderId)) orderPending.remove(orderId);
        else {
            for (String partner: deliveryPartnerArrayListHashMap.keySet()) {
                List<String> ordersList = deliveryPartnerArrayListHashMap.get(orderId);
                if (ordersList.contains(orderId)) {
                    List<String> orders = new ArrayList<>();
                    for (String order: ordersList) if (order != orderId) orders.add(order);
                    deliveryPartnerArrayListHashMap.put(partner, orders);
                    return;
                }
            }
        }
    }
}
