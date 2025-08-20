package UnitTest;

import org.Khrustalev.Presentation.dto.OwnerMapping;
import org.Khrustalev.Presentation.dto.OwnerRequestDto;
import org.Khrustalev.Presentation.dto.PetMapping;
import org.Khrustalev.application.contracts.services.OwnerService;
import org.Khrustalev.application.services.OwnerServiceImpl;
import org.Khrustalev.domain.Entities.Owners.Owner;
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

public class OwnerDaoTest {
    private OwnersDaoImpl ownersDao;
    private PetsDaoImpl petsDao;
    private OwnerService ownerService;

    private OwnerMapping ownerMapping;
    private PetMapping petMapping;

    private OwnerRequestDto owner;

    @BeforeEach
    public void setUp() {
        ownersDao = Mockito.mock(OwnersDaoImpl.class);
        petsDao = Mockito.mock(PetsDaoImpl.class);

        ownerMapping = Mockito.mock(OwnerMapping.class);
        petMapping = Mockito.mock(PetMapping.class);

        ownerService = new OwnerServiceImpl(ownersDao, petsDao, ownerMapping, petMapping);

        owner = new OwnerRequestDto("Kolya", LocalDate.of(2005, 2, 16), new ArrayList<>());
    }

    @Test
    public void save_shouldCallRightMethod() {
        ownerService.add(owner);

        verify(ownersDao).save(ownerMapping.dto2Entity(owner));
    }

    @Test
    public void findById_shouldCallRightMethod() {
        Owner ownerRequest = new Owner();
        when(ownersDao.getById(1L)).thenReturn(ownerRequest);

        ownerService.getById(1L);

        verify(ownersDao).getById(1L);
    }

    @Test
    public void deleteById_shouldCallRightMethod() {
        ownerService.deleteById(1L);

        verify(ownersDao).deleteById(1L);
    }

    @Test
    public void deleteAll_shouldCallRightMethod() {
        ownerService.deleteAll();

        verify(ownersDao).deleteAll();
    }

    @Test
    public void update_shouldCallRightMethod() {
        Owner ownerEntity = new Owner();
        ownerEntity.setId(1L);
        ownerEntity.setName("Kolya");
        ownerEntity.setBirthday(LocalDate.of(2005, 2, 16));
        ownerEntity.setPets(new ArrayList<>());

        Pet petEntity = new Pet();
        petEntity.setId(1L);
        petEntity.setNickname("Fluffy");
        petEntity.setBreed("Ordinary");
        petEntity.setBirthday(LocalDate.of(2020, 5, 20));
        petEntity.setOwner(ownerEntity);

        when(ownersDao.getById(1L)).thenReturn(ownerEntity);
        when(petsDao.getById(1L)).thenReturn(petEntity);

        ownerService.addPet(1, 1);

        verify(ownersDao).getById(1);
        verify(ownersDao).update(ownerEntity);
    }
}
