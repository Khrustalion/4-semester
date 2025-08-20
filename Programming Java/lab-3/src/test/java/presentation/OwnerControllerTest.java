package presentation;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.khrustalev.controllers.OwnerController;
import org.khrustalev.model.entities.pets.Color;
import org.khrustalev.model.services.OwnerService;
import org.khrustalev.model.services.dto.OwnerDto;
import org.khrustalev.model.services.dto.PetDto;
import org.khrustalev.model.services.exceptions.EntityDoesNotExistException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OwnerController.class)
@ContextConfiguration(classes =  { OwnerController.class })
public class OwnerControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private OwnerService ownerService;

    private static ObjectMapper objectMapper = new ObjectMapper();


    @BeforeAll
    public static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    @Test
    public void getOwner_ShouldGetRightOwner() throws Exception {
        OwnerDto owner = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));
        Mockito.when(this.ownerService.findById(owner.getId())).thenReturn(owner);

        String expectedJson = objectMapper.writeValueAsString(owner);

        mvc.perform(get("/owner/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getOwner_ShouldGetErrorStatusCode_WhenNotFound() throws Exception {
        Mockito.when(this.ownerService.findById(1)).
                thenThrow(new EntityDoesNotExistException("Owner not found"));

        mvc.perform(get("/owner/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createOwner_ShouldUpdateOwner() throws Exception {
        OwnerDto requestOwner = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));
        OwnerDto responseOwner = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));

        Mockito.when(this.ownerService.save(requestOwner)).thenReturn(responseOwner);


        mvc.perform(post("/owner/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestOwner)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(responseOwner)));
    }

    @Test
    public void updateOwner_ShouldUpdateOwner() throws Exception {
        OwnerDto requestOwner = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));
        OwnerDto responseOwner = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));

        Mockito.when(this.ownerService.update(requestOwner)).thenReturn(responseOwner);

        mvc.perform(put("/owner/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestOwner)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(responseOwner)));
    }

    @Test
    public void updateOwner_ShouldUpdateErrorStatusCode_WhenNotFound() throws Exception {
        OwnerDto requestOwner = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));

        Mockito.when(this.ownerService.update(requestOwner)).thenThrow(new EntityDoesNotExistException("Owner not found"));

        mvc.perform(put("/owner/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestOwner)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllOwners_ShouldGetRightOwners() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OwnerDto> expectedOwners = new PageImpl<>(createOwners(), pageable, createOwners().size());

        Mockito.when(this.ownerService.getOwnersBySortingPaging(0, 10, "id", false))
                .thenReturn(expectedOwners);

        mvc.perform(get("/owner/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOwners)));
    }

    @Test
    public void getAllOwner_ShouldGetRightOwners_WhenPageIsLess() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        List<OwnerDto> owners = List.of(createOwners().get(0), createOwners().get(1));
        Page<OwnerDto> expectedOwners = new PageImpl<>(owners, pageable, owners.size());

        Mockito.when(this.ownerService.getOwnersBySortingPaging(0, 2, "id", false))
                .thenReturn(expectedOwners);

        mvc.perform(get("/owner/all")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOwners)));
    }

    @Test
    public void deleteOwner_ShouldDeleteOwner() throws Exception {
        OwnerDto responseOwner = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));

        Mockito.when(this.ownerService.deleteById(responseOwner.getId())).thenReturn(responseOwner);

        mvc.perform(delete("/owner/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteOwner_ShouldDeleteErrorStatusCode_WhenNotFound() throws Exception {
        Mockito.when(this.ownerService.deleteById(1)).thenThrow(new EntityDoesNotExistException("Owner not found"));

        mvc.perform(delete("/owner/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addPet_ShouldAddPet() throws Exception {
        OwnerDto owner = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));
        PetDto pet = new PetDto(1,
                "Vaska",
                LocalDate.of(2020, 6, 14),
                "ordinary",
                Color.red,
                1);


        owner.addPet(pet.getId());

        Mockito.when(this.ownerService.addPet(owner.getId(), pet.getId())).thenReturn(owner);

        mvc.perform(put("/owner/1/add-pet/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(owner)));
    }

    private static List<OwnerDto> createOwners() {
        OwnerDto owner1 = new OwnerDto(1,"Kolya", LocalDate.of(2005, 2, 16));
        OwnerDto owner2 = new OwnerDto(2,"Dima", LocalDate.of(2000, 1, 16));
        OwnerDto owner3 = new OwnerDto(3,"Nikita", LocalDate.of(1995, 1, 20));

        return List.of(owner1, owner2, owner3);
    }
}
