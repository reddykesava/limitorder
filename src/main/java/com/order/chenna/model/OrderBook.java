package com.order.chenna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBook {
    private Map<Integer, List<Order>> buyList;
    private Map<Integer, List<Order>> sellList;
}
