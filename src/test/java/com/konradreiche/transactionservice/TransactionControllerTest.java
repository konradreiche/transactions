package com.konradreiche.transactionservice;

import com.konradreiche.transactionservice.domain.ITransactionRepository;
import com.konradreiche.transactionservice.domain.Transaction;
import com.konradreiche.transactionservice.domain.TransactionRepository;
import com.konradreiche.transactionservice.web.TransactionController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Josh Long
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TransactionControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private ITransactionRepository repository;

    private TransactionController controller;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        repository = new TransactionRepository();
        repository.add(new Transaction(1, 100, "cars"));
        repository.add(new Transaction(2, 250, "cars"));
        repository.add(new Transaction(3, 550, "flowers"));

        repository.add(new Transaction(20, 1000, "records"));
        repository.add(new Transaction(21, 1001, "records", 20));
        repository.add(new Transaction(22, 1002, "records", 21));
        repository.add(new Transaction(23, 1003, "records", 22));

        controller = new TransactionController(repository);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void createTransaction() throws Exception {
        String transactionJson = json(new Transaction(1, 1000, "cars"));
        this.mockMvc.perform(put("/transactionservice/transaction/10")
                .contentType(contentType)
                .content(transactionJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void readTransaction() throws Exception {
        mockMvc.perform(get("/transactionservice/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.amount", is(100.0)))
                .andExpect(jsonPath("$.type", is("cars")));
    }

    @Test
    public void readTransactionsByType() throws Exception {
        mockMvc.perform(get("/transactionservice/transaction/types/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(1)))
                .andExpect(jsonPath("$[1]", is(2)));
    }

    @Test
    public void transitiveSUm() throws Exception {
        mockMvc.perform(get("/transactionservice/transaction/sum/20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.sum", is(4006.0)));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage );
        return mockHttpOutputMessage.getBodyAsString();
    }
}