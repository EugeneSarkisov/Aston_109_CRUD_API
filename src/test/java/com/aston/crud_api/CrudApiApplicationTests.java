package com.aston.crud_api;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class CrudApiApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUserInfo_ShouldGetOkStatusCode(){
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/get")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"John\" }"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(print());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void testCreateNewUserAccount_ShouldGetOkStatusCode(){
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\": \"Rut\", \"email\": \"111@mail.test\", \"age\": 21}"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().string("true"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void testUpdateAccountInfo_ShouldGetOkStatusCode(){
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{ \"oldUsername\": \"Rut\", \"username\": \"Rat\", \"email\": \"111@mail.test\", \"age\": 21 }"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().string("true"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void testDeleteAccount_ShouldGetOkStatusCode(){
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/delete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{ \"username\": \"Rat\" }"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().string("true"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
