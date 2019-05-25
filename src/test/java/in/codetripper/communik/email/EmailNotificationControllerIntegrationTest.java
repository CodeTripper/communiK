package in.codetripper.communik.email;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailNotificationControllerIntegrationTest {
    String BODY_VALUE = "";
    // @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    EmailController notificationController;

    @Before
    public void setup() throws Exception {

        //this.mockMvc = standaloneSetup(this.notificationController).build();// Standalone context
        // mockMvc = MockMvcBuilders.webAppContextSetup(wac)
        // .build();
    }

    @Autowired
    void setWebApplicationContext(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    public void testEmail() throws Exception {
        mockMvc.perform(get("/email").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //		.andExpect(content().contentType("sd		"));
                .andExpect(MockMvcResultMatchers.content().string("SENDGRID"));

    }

}
