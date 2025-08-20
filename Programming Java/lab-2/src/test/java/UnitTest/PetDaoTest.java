package UnitTest;

import org.Khrustalev.Presentation.dto.OwnerMapping;
import org.Khrustalev.Presentation.dto.PetMapping;
import org.Khrustalev.Presentation.dto.PetRequestDto;
import org.Khrustalev.application.contracts.services.PetService;
import org.Khrustalev.application.services.PetServiceImpl;
import org.Khrustalev.domain.Entities.Pets.Pet;
import org.Khrustalev.infrastructure.Dao.OwnersDaoImpl;
import org.Khrustalev.infrastructure.Dao.PetsDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PetDaoTest {
    private PetsDaoImpl petsDao;
    private OwnersDaoImpl ownersDao;
    private PetService petService;

    private OwnerMapping ownerMapping;
    private PetMapping petMapping;

    private PetRequestDto pet;

    @BeforeEach
    public void setUp() {
        petsDao = Mockito.mock(PetsDaoImpl.class);
        ownersDao = Mockito.mock(OwnersDaoImpl.class);

        ownerMapping = Mockito.mock(OwnerMapping.class);
        petMapping = Mockito.mock(PetMapping.class);

        petService = new PetServiceImpl(ownersDao, petsDao, ownerMapping, petMapping);

        pet = new PetRequestDto("Fluffy",
                LocalDate.of(2020, 5, 20),
                "ordinary",
                1L,
                new ArrayList<>());
    }

    @Test
    public void save_shouldCallRightMethod() {
        petService.add(pet);

        verify(petsDao).save(petMapping.dto2Entity(pet));
    }

    @Test
    public void findById_shouldCallRightMethod() {
        Pet pet = new Pet();

        when(petsDao.getById(1L)).thenReturn(pet);

        petService.getById(1L);

        verify(petsDao).getById(1L);
    }

    @Test
    public void deleteById_shouldCallRightMethod() {
        petService.deleteById(1L);

        verify(petsDao).deleteById(1L);
    }

    @Test
    public void deleteAll_shouldCallRightMethod() {
        petService.deleteAll();

        verify(petsDao).deleteAll();
    }
}
