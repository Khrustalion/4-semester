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
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OwnerServiceTest {
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
    void addOwner_shouldSaveToDatabase() {
        OwnerRequestDto ownerFinal = ownerService.add(owner);

        assertEquals(owner.getName(), ownerFinal.getName());
        assertEquals(owner.getBirthday(), ownerFinal.getBirthday());
    }

    @Test
    void getOwnerById_shouldReturnCorrectOwner() {
        OwnerRequestDto ownerRequest = ownerService.add(owner);
        OwnerRequestDto foundOwner = ownerService.getById(ownerRequest.getId());

        assertNotNull(foundOwner);
        assertEquals(ownerRequest.getId(), foundOwner.getId());
        assertEquals("Kolya", foundOwner.getName());
        assertEquals(LocalDate.of(2005, 2, 16), foundOwner.getBirthday());
    }

    @Test
    void getOwnerById_ShouldThrowException_whenOwnerDoesNotExist() {
        assertThrows(DoesNotExsistExcpetion.class, () -> ownerService.getById(1L));
    }

    @Test
    void deleteById_shouldDeleteOwner() {
        OwnerRequestDto ownerRequest = ownerService.add(owner);
        ownerService.deleteById(ownerRequest.getId());

        Assert.assertThrows(DoesNotExsistExcpetion.class, () -> ownerService.getById(ownerRequest.getId()));
    }

    @Test
    void getAll_shouldReturnAllOwners() {
        OwnerRequestDto ownerRequest1 = ownerService.add(owner);
        OwnerRequestDto ownerRequest2 = ownerService.add(owner);

        List<OwnerRequestDto> owners = ownerService.getAll();

        assertEquals(2, owners.size());
        Assert.assertTrue(owners.contains(ownerRequest1));
        Assert.assertTrue(owners.contains(ownerRequest2));
    }

    @Test
    void deleteAll_shouldDeleteAllOwners() {
        ownerService.add(owner);
        ownerService.add(owner);

        ownerService.deleteAll();

        List<OwnerRequestDto> owners = ownerService.getAll();

        Assert.assertTrue(owners.isEmpty());
    }

    @Test
    void addPet_shouldSaveCorrectOwner() {
        OwnerRequestDto ownerRequest = ownerService.add(owner);

        PetRequestDto pet = new PetRequestDto(
                "Vasya",
                LocalDate.of(2020, 3, 16),
                "Ordinary",
                ownerRequest.getId(),
                new ArrayList<>());

        PetRequestDto petRequest = petService.add(pet);

        OwnerRequestDto ownerFinal = ownerService.addPet(ownerRequest.getId(), petRequest.getId());

        Assert.assertEquals(ownerRequest, ownerFinal);
        Assert.assertTrue(ownerFinal.getPets().contains(petRequest.getId()));
    }
}
