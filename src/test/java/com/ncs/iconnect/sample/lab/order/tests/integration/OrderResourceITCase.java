package com.ncs.iconnect.sample.lab.order.tests.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.iconnect.sample.lab.generated.IconnectSampleAppLabApp;
import com.ncs.iconnect.sample.lab.generated.security.TestTokenProvider;
import com.ncs.iconnect.sample.lab.order.domain.Order;
import com.ncs.iconnect.sample.lab.order.service.OrderService;
import com.ncs.iconnect.sample.lab.order.web.rest.OrderResource;

/**
 * Test class for the OrderResource REST controller.
 *
 * @see OrderResource
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IconnectSampleAppLabApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class OrderResourceITCase {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private TestTokenProvider testTokenProvider;
    private MockMvc mockMvc;
    private MediaType mediaType = MediaType.APPLICATION_JSON;

    private String obtainAccessToken(String username, String loginSubjectId, String password) throws Exception {
        return this.testTokenProvider.createTestJWTToken(username, loginSubjectId, password);
    }
    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        // given
        Order insertOrder = prepareOrder();

        // when
        ResultActions result = mockMvc.perform(post("/api/orders").header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1"))
            .contentType(mediaType).content(objectMapper.writeValueAsBytes(insertOrder)));

        // then
        result.andDo(print()).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.remarks", is(insertOrder.getRemarks())));
    }

    @Test
    public void testSearchAndRetrieveOrder() throws Exception {
        // given
        Order insertOrder = prepareOrder();
        Page<Order> dbOrders = orderService.findOrders(insertOrder.getRemarks(), null);

        Order dbOrder = null;
        if (dbOrders != null && dbOrders.getContent() != null && dbOrders.getContent().size() > 0) {
            dbOrder = dbOrders.getContent().get(0);
        }

        if (dbOrder == null) {
            dbOrder = orderService.add(insertOrder);
        }

        // when
        ResultActions result = mockMvc
            .perform(get("/api/orders/" + dbOrder.getId()).header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        // .with(user(new WebSecurityUserDetails(newOrder))));

        // then
        result.andDo(print()).andExpect(status().is2xxSuccessful ()).andExpect(jsonPath("$.remarks", is(dbOrder.getRemarks())));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        // given
        Order insertOrder = prepareOrder();
        Page<Order> dbOrders = orderService.findOrders(insertOrder.getRemarks(), null);

        Order dbOrder = null;
        if (dbOrders != null && dbOrders.getContent() != null && dbOrders.getContent().size() > 0) {
            dbOrder = dbOrders.getContent().get(0);
        }

        if (dbOrder == null) {
            dbOrder = orderService.update(insertOrder);
        }

        // when
        String updatedName = insertOrder.getId() + "Updated";
        dbOrder.setRemarks(dbOrder.getRemarks() + "-" + updatedName);
        ResultActions result = mockMvc
            .perform(put("/api/orders").header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1"))
                .contentType(mediaType).content(objectMapper.writeValueAsBytes(dbOrder)));
        // .with(user(new WebSecurityUserDetails(newOrder))));

        // then
        result.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        // given
        Order insertOrder = prepareOrder();
        Page<Order> dbOrders = orderService.findOrders(insertOrder.getRemarks(), null);

        Order dbOrder = null;
        if (dbOrders != null && dbOrders.getContent() != null && dbOrders.getContent().size() > 0) {
            dbOrder = dbOrders.getContent().get(0);
        }

        if (dbOrder == null) {
            dbOrder = orderService.add(insertOrder);
        }

        // when
        ResultActions result = mockMvc
            .perform(delete("/api/orders/" + dbOrder.getId()).header("Authorization","Bearer " + obtainAccessToken("appadmin", "DEF-user-appadmin", "password1")));
        // .with(user(new WebSecurityUserDetails(newOrder))));

        // then
        result.andDo(print()).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testCreateOrderWithUnauthorizedUserExpectFailure() throws Exception {
        // given
        Order insertOrder = prepareOrder();

        // when
        ResultActions result = mockMvc.perform(post("/api/orders").header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1"))
            .contentType(mediaType).content(objectMapper.writeValueAsBytes(insertOrder)));

        // then
        result.andDo(print()).andExpect(status().is(403));
    }

    @Test
    public void testSearchAndRetrieveOrderWithUnauthorizedUserExpectFailure() throws Exception {
        // given
        Order insertOrder = prepareOrder();
        Page<Order> dbOrders = orderService.findOrders(insertOrder.getRemarks(), null);

        Order dbOrder = null;
        if (dbOrders != null && dbOrders.getContent() != null && dbOrders.getContent().size() > 0) {
            dbOrder = dbOrders.getContent().get(0);
        }

        if (dbOrder == null) {
            dbOrder = orderService.add(insertOrder);
        }

        // when
        ResultActions result = mockMvc
            .perform(get("/api/orders/" + dbOrder.getId()).header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
        // .with(user(new WebSecurityUserDetails(newOrder))));

        // then
        result.andDo(print()).andExpect(status().is(403));
    }

    @Test
    public void testUpdateOrderWithUnauthorizedUserExpectFailure() throws Exception {
        // given
        Order insertOrder = prepareOrder();
        Page<Order> dbOrders = orderService.findOrders(insertOrder.getRemarks(), null);

        Order dbOrder = null;
        if (dbOrders != null && dbOrders.getContent() != null && dbOrders.getContent().size() > 0) {
            dbOrder = dbOrders.getContent().get(0);
        }

        if (dbOrder == null) {
            dbOrder = orderService.update(insertOrder);
        }

        // when
        String updatedName = insertOrder.getId() + "Updated";
        dbOrder.setRemarks(dbOrder.getRemarks() + "-" + updatedName);
        ResultActions result = mockMvc
            .perform(put("/api/orders").header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1"))
                .contentType(mediaType).content(objectMapper.writeValueAsBytes(dbOrder)));
        // .with(user(new WebSecurityUserDetails(newOrder))));

        // then
        result.andDo(print()).andExpect(status().is(403));
    }

    @Test
    public void testDeleteOrderWithUnauthorizedUserExpectFailure() throws Exception {
        // given
        Order insertOrder = prepareOrder();
        Page<Order> dbOrders = orderService.findOrders(insertOrder.getRemarks(), null);

        Order dbOrder = null;
        if (dbOrders != null && dbOrders.getContent() != null && dbOrders.getContent().size() > 0) {
            dbOrder = dbOrders.getContent().get(0);
        }

        if (dbOrder == null) {
            dbOrder = orderService.add(insertOrder);
        }

        // when
        ResultActions result = mockMvc
            .perform(delete("/api/orders/" + dbOrder.getId()).header("Authorization","Bearer " + obtainAccessToken("useradmin", "DEF-user-useradmin", "password1")));
        // .with(user(new WebSecurityUserDetails(newOrder))));

        // then
        result.andDo(print()).andExpect(status().is(403));
    }
    @Test
    public void testCreateOrderWithoutPermissionExpectFailure() throws Exception {
        // given
        Order insertOrder = prepareOrder();

        // when
        ResultActions result = mockMvc.perform(post("/orders")
            .contentType(mediaType)
            .content(objectMapper.writeValueAsBytes(insertOrder)));
        // then
        result.andDo(print())
            .andExpect(status().is(401));
    }

    private Order prepareOrder() {
        Order order = new Order();
        order.setProduct("Chicken Satay");
        order.setQuantity(50);
        order.setPrice(1.5);
        order.setPayment("Cash");
        order.setOrderDate(LocalDate.now());
        order.setRemarks("Junit Test Order Remarks");
        order.setOrderDate(LocalDate.now());
        return order;
    }
}
