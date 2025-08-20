package TestContainer;

import org.Khrustalev.Presentation.dto.OwnerMapping;
import org.Khrustalev.Presentation.dto.OwnerRequestDto;
import org.Khrustalev.Presentation.dto.PetMapping;
import org.Khrustalev.Presentation.dto.PetRequestDto;
import org.Khrustalev.application.contracts.dao.Dao;
import org.Khrustalev.application.contracts.services.OwnerService;
import org.Khrustalev.application.contracts.services.PetService;
import org.Khrustalev.application.exceptions.DoesNotExsistExcpetion;
import org.Khrustalev.application.services.OwnerServiceImpl;
import org.Khrustalev.application.services.PetServiceImpl;
import org.Khrustalev.domain.Entities.Owners.Owner;
import org.Khrustalev.domain.Entities.Pets.Pet;
import org.Khrustalev.infrastructure.Dao.OwnersDaoImpl;
import org.Khrustalev.infrastructure.Dao.PetsDaoImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PetServiceTest {
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11-alpine")
            .withDatabaseName("test-db")
            .withUsername("user")
            .withPassword("user");

    private static SessionFactory sessionFactory;
    private static Dao<Owner> ownersDao;
    private static Dao<Pet> petsDao;
    private static PetMapping petMapping;
    private static OwnerMapping ownerMapping;
    private static OwnerService ownerService;
    private static PetService petService;

    private static OwnerRequestDto owner;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.url", postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword())
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .addAnnotatedClass(Owner.class)
                .addAnnotatedClass(Pet.class);

        sessionFactory = configuration.buildSessionFactory();

        ownersDao = new OwnersDaoImpl(sessionFactory);
        petsDao = new PetsDaoImpl(sessionFactory);

        ownerMapping = new OwnerMapping(petsDao, ownersDao);
        petMapping = new PetMapping(petsDao, ownersDao);

        ownerService = new OwnerServiceImpl(ownersDao, petsDao, ownerMapping, petMapping);
        petService = new PetServiceImpl(ownersDao, petsDao, ownerMapping, petMapping);

        owner = new OwnerRequestDto(
                "Kolya",
                LocalDate.of(2005, 2, 16),
                new ArrayList<>());
    }

    @AfterAll
    static void afterAll() {
        sessionFactory.close();
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        petsDao.deleteAll();
        ownersDao.deleteAll();
    }

    @Test
    void addPet_shouldSaveToDatabase() {
        OwnerRequestDto ownerRequest = ownerService.add(owner);

        PetRequestDto pet = new PetRequestDto(
                "Vasya",
                LocalDate.of(2020, 3, 16),
                "Ordinary",
                ownerRequest.getId(),
                new ArrayList<>()
        );

        PetRequestDto petRequest = petService.add(pet);

        assertEquals(pet.getnickname(), petRequest.getnickname());
        assertEquals(pet.getBirthday(), petRequest.getBirthday());
    }

    @Test
    void getPetById_shouldReturnCorrectOwner() {
        OwnerRequestDto ownerRequest = ownerService.add(owner);

        PetRequestDto pet = new PetRequestDto(
                "Vasya",
                LocalDate.of(2020, 3, 16),
                "Ordinary",
                ownerRequest.getId(),
                new ArrayList<>()
        );

        PetRequestDto petRequest =  petService.add(pet);
        PetRequestDto foundPet = petService.getById(petRequest.getId());

        assertNotNull(foundPet);
        assertEquals(petRequest.getId(), foundPet.getId());
        assertEquals(petRequest.getnickname(), foundPet.getnickname());
        assertEquals(petRequest.getBirthday(), foundPet.getBirthday());
    }

    @Test
    void getOwnerById_ShouldThrowException_whenOwnerDoesNotExist() {
        assertThrows(DoesNotExsistExcpetion.class, () -> petService.getById(1L));
    }


}
