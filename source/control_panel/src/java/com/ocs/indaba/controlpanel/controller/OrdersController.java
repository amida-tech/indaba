/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import com.ocs.indaba.controlpanel.model.Order;
import com.ocs.indaba.controlpanel.service.OrdersService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.ValidationAwareSupport;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

/**
 *
 * @author Jeff Jiang
 */
@Results({
    @Result(name="success", type="redirectAction", params = {"actionName" , "orders"})
})
@ResultPath("/WEB-INF/pages/orders")
public class OrdersController extends ValidationAwareSupport implements ModelDriven<Object> {
    private static final Logger logger = Logger.getLogger(OrdersController.class);

    private Order model = new Order();
    private String id;
    private Collection<Order> list;
    private OrdersService ordersService = new OrdersService();

    // GET /orders/1
    public HttpHeaders show() {
        return new DefaultHttpHeaders("show");
    }

    // GET /orders
    public HttpHeaders index() {
        list = ordersService.getAll();
        return new DefaultHttpHeaders("index").disableCaching();
    }

    // GET /orders/1/edit
    public String edit() {
        return "edit";
    }

    // GET /orders/new
    public String editNew() {
        model = new Order();
        return "editNew";
    }

    // GET /orders/1/deleteConfirm
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    // DELETE /orders/1
    public String destroy() {
        ordersService.remove(id);
        addActionMessage("Order removed successfully");
        return "success";
    }

    // POST /orders
    public HttpHeaders create() {
        ordersService.save(model);
        addActionMessage("New order created successfully");
        return new DefaultHttpHeaders("success").setLocationId(model.getId());
    }

    // PUT /orders/1
    public String update() {
        ordersService.save(model);
        addActionMessage("Order updated successfully");
        return "success";
    }

    public void validate() {
        if (model.getClientName() == null || model.getClientName().length() == 0) {
            addFieldError("clientName", "The client name is empty");
        }
    }

    public void setId(String id) {
        if (id != null) {
            this.model = ordersService.get(id);
        }
        this.id = id;
    }

    public Object getModel() {
        return (list != null ? list : model);
    }
}
