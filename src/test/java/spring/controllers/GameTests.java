/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spring.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import spring.daos.GameDao;
import spring.models.Game;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameController gameController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private GameDao gameDao;
    private Game game;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.game = gameDao.save(new Game("testInsert", false, 20, 5, 5, "avion", false));
    }

    @Test
    public void shouldCreateGame() throws Exception {
        mockMvc.perform
                (
                        post("/game/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(new Game("testInsert", false, 20, 5, 5, "avion", false)))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.name").value("testInsert"));
    }

    @Test
    public void shouldBadRequestCreateGame() throws Exception {
        mockMvc.perform(post("/game/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldQueryGame() throws Exception {
        mockMvc.perform
                (
                        post("/game/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(new Game("testInsert", false, 20, 5, 5, "avion", false)))
                )
                .andExpect(status().isCreated());

        mockMvc.perform(
                get("/game/get-by-name?name={name}", "testInsert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0].name").value("testInsert"));
    }

    @Test
    public void shouldQueryUnexistantGame() throws Exception {
        mockMvc.perform(
                get("/game/get-by-name?name={name}", "blablablablablabla"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0]").doesNotExist());
    }

    @Test
    public void shouldJoinAndLeaveGame() throws Exception {
        int nbPlayer = this.game.getCurrentNbPlayer();

        mockMvc.perform
                (
                        post("/game/joinGame")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"idGame\": " + this.game.getId() + ", \"idPlayer\": 1}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.currentNbPlayer").value(nbPlayer + 1));

        mockMvc.perform
                (
                        post("/game/leaveGame")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"idGame\": " + this.game.getId() + ", \"idPlayer\": 1}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.currentNbPlayer").value(nbPlayer));
    }

    @Test
    public void shouldNotJoinFullGame() throws Exception {
        //todo recuperer l'id du game une fois créé
        Game game = new Game("testInsert", false, 0, 5, 5, "avion", false);
        mockMvc.perform
                (
                        post("/game/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(game))
                )
                .andExpect(status().isCreated());

        mockMvc.perform
                (
                        post("/game/joinGame")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"idGame\": " + game.getId() + ", \"idPlayer\": 1}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false));

    }

    /*@Test
    public void shouldDeleteGame() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/people").content(
                "{ \"firstName\": \"Bilbo\", \"lastName\":\"Baggins\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }*/
}