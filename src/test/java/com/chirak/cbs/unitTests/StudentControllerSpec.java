//package com.chirak.cbs.unitTests;
//
//import com.chirak.cbs.controller.StudentController;
//import com.chirak.cbs.dto.StudentDto;
//import com.chirak.cbs.object.ForgotPasswordBody;
//import com.chirak.cbs.service.StudentService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
//
//@WebMvcTest(controllers = {StudentController.class}) //test controller only. Rest controller too?
//public class StudentControllerSpec {
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    StudentService studentService;
//
//    @Value("${students.baseUrl}")
//    String baseUrl;
//
//    StudentDto studentDto;
//
////    @DisplayName("Handles forgotten password")
////    @Nested
////    class PasswordResetSpec {
////
//////        @DisplayName("Handles forgot-password request.")
//////        @Test
//////        void sendForgotPasswordReq() throws Exception {
//////
//////            ForgotPasswordBody body = new ForgotPasswordBody();
//////            body.setEmail("divadchigozie@gmail.com");
//////
//////            ObjectMapper mapper = new ObjectMapper();
//////            String json = mapper.writeValueAsString(body);
//////
//////            mockMvc
//////                    .perform(MockMvcRequestBuilders.post(baseUrl + "/forgot-password")
//////                                        .contentType(MediaType.APPLICATION_JSON)
//////                                        .content(json))
//////                    .andExpect(MockMvcResultMatchers.status().isOk());
//////        }
//////    }
////}
