package hotel.reservation;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for {@link ReservationController}.
 * 
 * @author Johannes Pforr
 */
@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerIntegrationTests {

    @Autowired
    MockMvc mvc;

    @Test
    void preventsPublicAccessForRoomCart() throws Exception {

        mvc.perform(get("/rooms/cart"))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
    }

    @Test
    @WithMockUser(username = "johannes@hotel.de", roles = "GUEST")
    void roomCartIsAccessibleForGuest() throws Exception {

        mvc.perform(get("/rooms/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reservation"));
    }
}
