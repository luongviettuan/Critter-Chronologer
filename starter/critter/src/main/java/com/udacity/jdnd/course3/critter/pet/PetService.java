package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.exception.BusinessException;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.CustomerEntity;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public PetDTO create(PetDTO petDTO) {
        PetEntity petEntity = new PetEntity();
        petEntity.setType(petDTO.getType());
        petEntity.setName(petDTO.getName());
        CustomerEntity customerEntity = convertCustomerIdToCustomerEntity(petDTO.getOwnerId());
        petEntity.setCustomer(customerEntity);
        petEntity.setBirthDate(petDTO.getBirthDate());
        petEntity.setNotes(petDTO.getNotes());
        petRepository.save(petEntity);

        List<PetEntity> petEntityList = customerEntity.getPets();
        petEntityList.add(petEntity);
        customerEntity.setPets(petEntityList);
        customerRepository.save(customerEntity);

        //clone deep object
        PetDTO result = new PetDTO();
        result.setId(petEntity.getId());
        result.setType(petEntity.getType());
        result.setName(petEntity.getName());
        result.setOwnerId(petEntity.getCustomer().getId());
        result.setBirthDate(petEntity.getBirthDate());
        result.setNotes(petEntity.getNotes());
        return result;
    }

    public PetDTO getPetById(Long petId) throws BusinessException {
         Optional<PetEntity> petEntityOptional = petRepository.findById(petId);
         if (!petEntityOptional.isPresent()) {
             throw new BusinessException("NOK", "Cannot find pet have id: " + petId);
         }
        PetEntity petEntity = petEntityOptional.get();

        PetDTO petDTO = new PetDTO();
        petDTO.setId(petEntity.getId());
        petDTO.setType(petEntity.getType());
        petDTO.setName(petEntity.getName());
        petDTO.setOwnerId(petEntity.getCustomer().getId());
        petDTO.setBirthDate(petEntity.getBirthDate());
        petDTO.setNotes(petEntity.getNotes());
        return petDTO;
    }

    public List<PetDTO> getPetsByOwnerId(long ownerId) {
        List<PetDTO> petDTOList = new ArrayList<>();
        List<PetEntity> petEntityList = petRepository.findByCustomerId(ownerId);
        for (PetEntity petEntity : petEntityList ) {
            PetDTO petDTO = new PetDTO();
            petDTO.setId(petEntity.getId());
            petDTO.setType(petEntity.getType());
            petDTO.setName(petEntity.getName());
            petDTO.setOwnerId(petEntity.getCustomer().getId());
            petDTO.setBirthDate(petEntity.getBirthDate());
            petDTO.setNotes(petEntity.getNotes());
            petDTOList.add(petDTO);
        }
        return petDTOList;
    }

    public CustomerDTO getOwnerByPetId(long petId) {
        Optional<PetEntity> petEntityOptional = petRepository.findById(petId);
        if (petEntityOptional.isPresent()) {
            CustomerEntity customerEntity = petEntityOptional.get().getCustomer();
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(customerEntity.getId());
            customerDTO.setName(customerEntity.getName());
            customerDTO.setPhoneNumber(customerEntity.getPhoneNumber());
            customerDTO.setNotes(customerEntity.getNotes());
            customerDTO.setPetIds(convertPetEntitiesToPetIds(customerEntity.getPets()));
            return customerDTO;
        }
        return null;
    }

    private CustomerEntity convertCustomerIdToCustomerEntity(Long customerId) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(customerId);
        return customerEntityOptional.orElse(null);
    }

    private List<Long> convertPetEntitiesToPetIds(List<PetEntity> petEntities) {
        List<Long> petIds = new ArrayList<>();
        for (PetEntity petEntity : CollectionUtils.emptyIfNull(petEntities)) {
            if (petEntity.getId() != null) petIds.add(petEntity.getId());
        }
        return petIds;
    }

    public List<PetDTO> getAllPets() {
        List<PetEntity> petEntities = petRepository.findAll();
        List<PetDTO> petDTOList = new ArrayList<>();
        for (PetEntity petEntity : petEntities ) {
            PetDTO petDTO = new PetDTO();
            petDTO.setId(petEntity.getId());
            petDTO.setType(petEntity.getType());
            petDTO.setName(petEntity.getName());
            petDTO.setOwnerId(petEntity.getCustomer().getId());
            petDTO.setBirthDate(petEntity.getBirthDate());
            petDTO.setNotes(petEntity.getNotes());
            petDTOList.add(petDTO);
        }
        return petDTOList;
    }
}
