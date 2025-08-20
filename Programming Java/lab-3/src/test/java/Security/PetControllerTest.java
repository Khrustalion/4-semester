package Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khrustalev.controllers.PetController;
import org.khrustalev.model.entities.owners.Owner;
import org.khrustalev.model.entities.pets.Color;
import org.khrustalev.model.entities.users.Role;
import org.khrustalev.model.entities.users.User;
import org.khrustalev.model.services.OwnerService;
import org.khrustalev.model.services.PetService;
import org.khrustalev.model.services.dto.PetDto;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PetController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = { PetController.class })
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private OwnerService ownerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private PetDto pet;

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        pet = new PetDto(1L, "Vaska",
                LocalDate.of(2021, 3, 15),
                "ordinal",
                Color.red,
                1,
                new ArrayList<>());

        Owner owner1 = new Owner();
        owner1.setId(1L);
        owner1.setName("Kolya");
        owner1.setBirthday(LocalDate.of(2005, 2, 15));
        owner1.setPets(new ArrayList<>());

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Kolya");
        user1.setPassword("password");
        user1.setRole(Role.ROLE_USER);
        user1.setOwner(owner1);

        Owner owner2 = new Owner();
        owner2.setId(2L);
        owner2.setName("Dima");
        owner2.setBirthday(LocalDate.of(2005, 2, 15));
        owner2.setPets(new ArrayList<>());

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("Dima");
        user2.setPassword("password");
        user2.setRole(Role.ROLE_USER);
        user2.setOwner(owner1);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user1, null, List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createPet_shouldReturnCreated() throws Exception {
        mockMvc.perform(post("/pet/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(pet)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updatePet_shouldReturnUpdated() throws Exception {
        Mockito.when(petService.update(pet)).thenReturn(pet);

        mockMvc.perform(put("/pet/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(pet)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deletePet_shouldReturnNoContent() throws Exception {
        Mockito.when(petService.findById(1L)).thenReturn(pet);
        Mockito.when(petService.deleteById(1L)).thenReturn(pet);

        mockMvc.perform(delete("/pet/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void addFriend_shouldReturnPetWithFriend() throws Exception {
        PetDto updated = new PetDto();
        updated.setFriends(Collections.singletonList(2L));
        Mockito.when(petService.findById(1L)).thenReturn(pet);
        Mockito.when(petService.addFriend(1L, 2L)).thenReturn(updated);

        mockMvc.perform(put("/pet/1/add-friend/2").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updated)));
    }

    @Test
    public void addFriend_shouldThrowException_WhenNonAuthorized() throws Exception {
        Owner wrongOwner = new Owner();
        wrongOwner.setId(2L);
        wrongOwner.setName("Dima");

        User wrongUser = new User();
        wrongUser.setId(100L);
        wrongUser.setUsername("Dima");
        wrongUser.setOwner(wrongOwner);
        wrongUser.setRole(Role.ROLE_USER);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                wrongUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        Mockito.when(petService.findById(1L)).thenReturn(pet);

        mockMvc.perform(put("/pet/1/add-friend/2").with(csrf()))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void getPet_shouldReturnPet() throws Exception {
        Mockito.when(petService.findById(1L)).thenReturn(pet);

        mockMvc.perform(get("/pet/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pet)));
    }
}
