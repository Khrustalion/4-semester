package presentation;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.khrustalev.controllers.PetController;
import org.khrustalev.model.entities.pets.Color;
import org.khrustalev.model.services.PetService;
import org.khrustalev.model.services.dto.PetDto;
import org.khrustalev.model.services.exceptions.EntityDoesNotExistException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PetController.class)
@ContextConfiguration(classes = { PetController.class })
public class PetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PetService petService;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void getPet_ShouldReturnPet() throws Exception {
        PetDto pet = createPets().get(0);
        Mockito.when(petService.findById(pet.getId())).thenReturn(pet);

        mvc.perform(get("/pet/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(pet)));
    }

    @Test
    public void getPet_ShouldReturnNotFound() throws Exception {
        Mockito.when(petService.findById(1)).thenThrow(new EntityDoesNotExistException("Pet not found"));

        mvc.perform(get("/pet/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createPet_ShouldReturnCreatedPet() throws Exception {
        PetDto pet = createPets().get(0);

        Mockito.when(this.petService.save(pet)).thenReturn(pet);

        mvc.perform(post("/pet/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(pet)));
    }

    @Test
    public void updatePet_ShouldReturnUpdatedPet() throws Exception {
        PetDto pet = createPets().get(0);

        Mockito.when(petService.update(pet)).thenReturn(pet);

        mvc.perform(put("/pet/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(pet)));
    }

    @Test
    public void updatePet_ShouldReturnNotFound() throws Exception {
        PetDto pet = createPets().get(0);

        Mockito.when(petService.update(pet)).thenThrow(new EntityDoesNotExistException("Pet not found"));

        mvc.perform(put("/pet/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePet_ShouldReturnNoContent() throws Exception {
        PetDto pet = createPets().get(0);

        Mockito.when(petService.deleteById(pet.getId())).thenReturn(pet);

        mvc.perform(delete("/pet/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePet_ShouldReturnNotFound() throws Exception {
        Mockito.when(petService.deleteById(1)).thenThrow(new EntityDoesNotExistException("Pet not found"));

        mvc.perform(delete("/pet/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addFriend_ShouldReturnUpdatedPet() throws Exception {
        PetDto pet = createPets().get(0);
        PetDto friend = createPets().get(1);

        pet.addFriend(friend.getId());

        Mockito.when(petService.addFriend(pet.getId(), friend.getId())).thenReturn(pet);

        mvc.perform(put("/pet/1/add-friend/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(pet)));
    }

    private static List<PetDto> createPets() {
        PetDto pet1 = new PetDto(1, "Vaska", LocalDate.of(2020, 6, 14), "ordinary", Color.red, 1);
        PetDto pet2 = new PetDto(2, "Barsik", LocalDate.of(2019, 3, 11), "fancy", Color.black, 1);
        PetDto pet3 = new PetDto(3, "Murzik", LocalDate.of(2018, 7, 20), "siam", Color.white, 2);

        return List.of(pet1, pet2, pet3);
    }
}
