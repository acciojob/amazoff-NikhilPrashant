package com.driver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/add-order")
    public ResponseEntity<String> addOrder(@RequestBody Order order){
        String res = orderService.addOrder(order);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<String> addPartner(@PathVariable String partnerId){
        String res = orderService.addPartner(partnerId);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<String> addOrderPartnerPair(@RequestParam String orderId, @RequestParam String partnerId){
        //This is basically assigning that order to that partnerId
        String res = orderService.addOrderPartnerPair(orderId, partnerId);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId){
        //order should be returned with an orderId.
        Order order= orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable String partnerId){
        //deliveryPartner should contain the value given by partnerId
        DeliveryPartner deliveryPartner = orderService.getPartnerById(partnerId);
        return new ResponseEntity<>(deliveryPartner, HttpStatus.CREATED);
    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable String partnerId){
        //orderCount should denote the orders given by a partner-id
        Integer orderCount = orderService.getOrderCountByPartnerId(partnerId);
        return new ResponseEntity<>(orderCount, HttpStatus.CREATED);
    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable String partnerId){
        //orders should contain a list of orders by PartnerId
        List<String> orders = orderService.getOrdersByPartnerId(partnerId);
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders(){
        //Get all orders
        List<String> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders(){
        //Count of orders that have not been assigned to any DeliveryPartner
        Integer countOfOrders = orderService.getCountOfUnsignedOrders();
        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{time}/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable String time, @PathVariable String partnerId){
        //countOfOrders that are left after a particular time of a DeliveryPartner
        Integer countOfOrders = orderService.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable String partnerId){
        //Return the time when that partnerId will deliver his last delivery order.
        String time = orderService.getLastDeliveryTimeByPartnerId(partnerId);
        return new ResponseEntity<>(time, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<String> deletePartnerById(@PathVariable String partnerId){
        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        String res = orderService.deletePartnerById(partnerId);
        return new ResponseEntity<>(partnerId + res, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable String orderId){
        //Delete an order and also
        // remove it from the assigned order of that partnerId
        String res = orderService.deleteOrderById(orderId);
        return new ResponseEntity<>(orderId + res, HttpStatus.CREATED);
    }
}
