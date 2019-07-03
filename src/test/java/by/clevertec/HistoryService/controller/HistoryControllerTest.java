package by.clevertec.HistoryService.controller;

import by.clevertec.HistoryService.dto.FilterForHistory;
import by.clevertec.HistoryService.dto.History;
import by.clevertec.HistoryService.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(HistoryController.class)
public class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private Date date1 = simpleDateFormat.parse("2019-11-30T10:15:39.476Z");
    private Date date2 = simpleDateFormat.parse("2019-02-30T10:15:39.476Z");
    private Date date3 = simpleDateFormat.parse("2019-05-30T10:15:39.476Z");

    private Date dateForFilterGte = simpleDateFormat.parse("2019-01-30T10:15:39.476Z");
    private Date dateForFilterLte = simpleDateFormat.parse("2019-06-30T10:15:39.476Z");

    private final History history1 = new History(
            "1",
            "Tom",
            date1,
            "registration",
            "company",
            true,
            true,
            "description",
            "description",
            "description"
    );

    private final History history2 = new History(
            "2",
            "Jim",
            date2,
            "deletion",
            "vehicle",
            true,
            true,
            "description",
            "description",
            "description"
    );

    private final History history3 = new History(
            "3",
            "Jack",
            date3,
            "updating",
            "driver",
            false,
            true,
            "description",
            "description",
            "description"
    );

    private final List<History> histories = Arrays.asList(history1, history2, history3);
    private final Page<History> page = new PageImpl<>(histories);

    private FilterForHistory filterForHistory = new FilterForHistory(
            new String[]{"Jack", "Vlad"},
            dateForFilterGte,
            dateForFilterLte,
            new String[]{"registration", "correct"},
            new String[]{"company"},
            new Boolean[]{true},
            new Boolean[]{true}
    );

    public HistoryControllerTest() throws ParseException {
    }

    @Test
    public void whenSaveHistoryThenExpectStatusOkAndNameAddedUserIsTrue() throws Exception {
        given(historyService.save(history1)).willReturn(history1.getUserName());
        MvcResult mvcResult = this.mockMvc.perform(post("/history")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(history1)))
                .andExpect(status().isOk())
                .andReturn();

        String name = mvcResult.getResponse().getContentAsString();
        assertEquals(name, "Tom");
    }

    @Test
    public void whenSaveHistoryWithExceptonThenExpectStatusInternalServerError() throws Exception {
        given(this.historyService.save(history1)).willThrow(new ArithmeticException());
        this.mockMvc.perform(post("/history")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(history1)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void whenFindHistoryThenExpectStatusOkAndContextJSON() throws Exception {
        given(this.historyService.find(anyString())).willReturn(java.util.Optional.of(history1));
        this.mockMvc.perform(get("/history/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenFingThenExpectStatusNotFound() throws Exception {
        given(this.historyService.find(anyString())).willReturn(Optional.empty());
        this.mockMvc.perform(get("/history/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetPageHistoryPageableThenExpectTrueData() throws Exception {
        given(this.historyService.getHistory(anyInt(), anyInt())).willReturn(page);
        this.mockMvc.perform(get("/history?pagesize=3&pagenumber=0")
                .param("pagenumber", "0")
                .param("pagesize", "3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].userName").value("Jim"))
                .andExpect(jsonPath("$.content[2].entityType").value("driver"))
                .andReturn();
    }

    @Test
    public void whenGetRequestPageableWithExceptionThenExpectStatusBadRequest() throws Exception {
        given(this.historyService.getHistory(anyInt(), anyInt())).willThrow(new ArithmeticException());
        this.mockMvc.perform(get("/history"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenDeleteThenExpectStatusOkAndResponceTrue() throws Exception {
        given(this.historyService.find(anyString())).willReturn(java.util.Optional.of(history1));
        given(this.historyService.delete(anyString())).willReturn(true);
        MvcResult mvcResult = this.mockMvc.perform(delete("/history/1"))
                .andExpect(status().isOk())
                .andReturn();

        Boolean responce = Boolean.valueOf(mvcResult.getResponse().getContentAsString());
        assertEquals(responce, true);
    }

    @Test
    public void whenDeleteWithExceptionThenExpectStatusInternalServerError() throws Exception {
        given(this.historyService.find(anyString())).willReturn(java.util.Optional.of(history1));
        given(this.historyService.delete(anyString())).willThrow(new ArithmeticException());
        this.mockMvc.perform(delete("/history/1"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void whenDeleteInvalidHistoryThenExpectStatusNotFound() throws Exception {
        given(this.historyService.delete(anyString())).willReturn(true);
        this.mockMvc.perform(delete("/history/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetAllThenExpectStatusOk() throws Exception {
        given(this.historyService.getAll()).willReturn(histories);
        this.mockMvc.perform(get("/history/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenGetAllWithExceptionThenExpectStatusInternalServerError() throws Exception {
        given(this.historyService.getAll()).willThrow(new ArithmeticException());
        this.mockMvc.perform(get("/history/getAll"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void whenUpdateThenExpectStatusOk() throws Exception {
        given(this.historyService.find(history1.getId())).willReturn(Optional.of(history1));
        given(this.historyService.update(history1)).willReturn(true);
        this.mockMvc.perform(put("/history/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(history1)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateNullHistoryThenExpectStatusNotFound() throws Exception {
        given(this.historyService.find(history1.getId())).willReturn(Optional.empty());
        given(this.historyService.update(history1)).willReturn(true);
        this.mockMvc.perform(put("/history/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(history1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindHistoryWithFilterThenExpectStatusOkAndTrueData() throws Exception {
        given(this.historyService.findAllWithFilter(anyInt(), anyInt(), any(FilterForHistory.class))).willReturn(page);
        this.mockMvc.perform(post("/history/filter")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(filterForHistory)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].userName").value("Jim"))
                .andExpect(jsonPath("$.content[2].entityType").value("driver"))
                .andReturn();
    }

    @Test
    public void whenFindHistoryWithFilterWithExceptionThenExpectStatusBadRequest() throws Exception {
        given(this.historyService.findAllWithFilter(anyInt(), anyInt(), any(FilterForHistory.class))).willThrow(new ArithmeticException());
        this.mockMvc.perform(post("/history/filter")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(filterForHistory)))
                .andExpect(status().is4xxClientError());
    }
}