package Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khrustalev.controllers.OwnerController;
import org.khrustalev.model.services.OwnerService;
import org.khrustalev.model.services.dto.OwnerDto;
import org.khrustalev.model.services.exceptions.EntityDoesNotExistException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
@ContextConfiguration(classes = {OwnerController.class})
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerService ownerService;

    private ObjectMapper objectMapper;
    private OwnerDto testOwner;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        testOwner = new OwnerDto(1L, "Test Owner", LocalDate.of(2000, 1, 1), Collections.emptyList());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testCreateOwner() throws Exception {
        mockMvc.perform(post("/owner/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOwner)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testUpdateOwnerSuccess() throws Exception {
        Mockito.when(ownerService.update(testOwner)).thenReturn(testOwner);

        mockMvc.perform(put("/owner/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOwner)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testUpdateOwnerNotFound() throws Exception {
        Mockito.when(ownerService.update(testOwner))
                .thenThrow(new EntityDoesNotExistException("Owner not found"));

        mockMvc.perform(put("/owner/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOwner)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteOwnerSuccess() throws Exception {
        Mockito.when(ownerService.deleteById(1L)).thenReturn(testOwner);

        mockMvc.perform(delete("/owner/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testDeleteOwnerNotFound() throws Exception {
        Mockito.when(ownerService.deleteById(1L))
                .thenThrow(new EntityDoesNotExistException("Not found"));

        mockMvc.perform(delete("/owner/1").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testAddPetToOwner() throws Exception {
        Mockito.when(ownerService.addPet(1L, 2L)).thenReturn(testOwner);

        mockMvc.perform(put("/owner/1/add-pet/2").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetOwnerByIdSuccess() throws Exception {
        Mockito.when(ownerService.findById(1L)).thenReturn(testOwner);

        mockMvc.perform(get("/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Owner"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetOwnerByIdNotFound() throws Exception {
        Mockito.when(ownerService.findById(1L)).thenThrow(new EntityDoesNotExistException("Not found"));

        mockMvc.perform(get("/owner/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetAllOwnersByPaging() throws Exception {
        Mockito.when(ownerService.getOwnersBySortingPaging(0, 10, "id", false))
                .thenReturn(new PageImpl<>(List.of(testOwner)));

        mockMvc.perform(get("/owner/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
}
