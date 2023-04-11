package com.order.chenna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;

    @NotNull(message = "price cannot be empty")
    @Min(value = 1, message = "price has to be minimum 1")
    private Integer price;

    @NotNull(message = "quantity cannot be empty")
    @Min(value = 1, message = "quantity cannot be empty and non negative value")
    private Integer quantity;

    @NotNull(message = "side cannot be empty")
    @Pattern(regexp="buy|sell", message = "side can be either buy or sell")
    private String side;

    private Integer soldQuantity;

    private Integer buyQuantity;

    private String status;
}
