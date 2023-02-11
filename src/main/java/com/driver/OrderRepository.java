package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, String> orderDeliveryPartnerHashMap;
    private HashMap<String, List<String>> deliveryPartnerArrayListHashMap;

    public OrderRepository() {
        this.orderMap = new HashMap<>();
        this.partnerMap = new HashMap<>();
        this.orderDeliveryPartnerHashMap = new HashMap<>();
        this.deliveryPartnerArrayListHashMap = new HashMap<>();
    }

    public String addOrder(Order order) {
        if (orderMap.containsKey(order.getId())) return "Already Exists";
        orderMap.put(order.getId(), order);
        return "New order added successfully";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId, deliveryPartner);
        deliveryPartnerArrayListHashMap.put(partnerId, deliveryPartnerArrayListHashMap.getOrDefault(partnerId, new ArrayList<String>()));
        return "New delivery partner added successfully";
    }

    public String appOrderPartnerPair(String orderId, String partnerId) {
        if (orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            orderDeliveryPartnerHashMap.put(orderId, partnerId);
            partnerMap.get(partnerId).setNumberOfOrders(partnerMap.get(partnerId).getNumberOfOrders() + 1);
            deliveryPartnerArrayListHashMap.get(partnerId).add(orderId);
        }
        return "New order-partner pair added successfully";
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

    public int getCountOfUnsignedOrders() {
        return orderMap.size() - orderDeliveryPartnerHashMap.size();
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
        int max = -1;
        List<String> ordersList = deliveryPartnerArrayListHashMap.get(partnerId);
        for (String orderId: ordersList) {
            if(orderMap.get(orderId).getDeliveryTime() > max) max = orderMap.get(orderId).getDeliveryTime();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(max / 60);
        stringBuilder.append(':');
        stringBuilder.append(max % 60);
        String latestTime = stringBuilder.toString();
        return latestTime;
    }

    public String deletePartnerById (String partnerId) {
        partnerMap.remove(partnerId);
        if (deliveryPartnerArrayListHashMap.containsKey(partnerId)) {
            deliveryPartnerArrayListHashMap.remove(partnerId);
            List<String> orderList = new ArrayList<>();
            for (String order: orderDeliveryPartnerHashMap.keySet()) {
                if (orderDeliveryPartnerHashMap.get(order).equals(partnerId)) orderList.add(order);
            }
            for (String order: orderList) orderDeliveryPartnerHashMap.remove(order);
        }
        return " removed successfully";
    }

    public String deleteOrderById (String orderId) {
        orderMap.remove(orderId);
        if (orderDeliveryPartnerHashMap.containsKey(orderId)) {
            String partnerId = orderDeliveryPartnerHashMap.get(orderId);
            orderDeliveryPartnerHashMap.remove(orderId);
            List<String> ordersList = deliveryPartnerArrayListHashMap.get(partnerId);
            List<String> newOrdersList = new ArrayList<String>();
            for (String order: ordersList) {
                if (order != orderId) newOrdersList.add(order);
            }
            deliveryPartnerArrayListHashMap.put(partnerId, newOrdersList);
        }
        return " removed successfully";
    }
}
