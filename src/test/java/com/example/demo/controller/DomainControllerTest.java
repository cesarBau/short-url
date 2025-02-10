package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Domain;
import com.example.demo.service.IDomain;

@WebMvcTest(DomainController.class)
public class DomainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IDomain domainService;

    @Test
    void testGetDomainByName() throws Exception {
        String domainName = "local";
        Domain response = new Domain((long) 1, "local", "Valor from test");
        when(domainService.getDomainByName(domainName)).thenReturn(response);
        ResultActions result;
        try {
            result = mockMvc.perform(get("/domain/{domainName}", domainName));
            result.andDo(print()).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetDomains() throws Exception {
        String domainName = "local";
        List<Domain> response = new ArrayList<>();
        response.add(new Domain((long) 1, "local", "Valor from test"));
        response.add(new Domain((long) 2, "kube", "Valor from test"));
        when(domainService.getDomains()).thenReturn(response);
        ResultActions result;
        try {
            result = mockMvc.perform(get("/domain/", domainName));
            result.andDo(print()).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetDomainByNameNotFound() throws Exception {
        String domainName = "error";
        when(domainService.getDomainByName(domainName)).thenThrow(new ResponseStatusException(
                HttpStatus.NOT_FOUND, "domain not found test", null));
        ResultActions result;
        try {
            result = mockMvc.perform(get("/domain/{domainName}", domainName));
            result.andDo(print()).andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
