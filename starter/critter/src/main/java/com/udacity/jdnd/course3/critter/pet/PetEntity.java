package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.schedule.ScheduleEntity;
import com.udacity.jdnd.course3.critter.user.CustomerEntity;
import com.udacity.jdnd.course3.critter.user.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PET")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetEntity {

    @Id
    @GeneratedValue
    private Long id;

    private PetType type;

    private String name;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private CustomerEntity customer;

    private LocalDate birthDate;

    private String notes;

    @ManyToMany(mappedBy = "pets", cascade = CascadeType.ALL)
    private List<ScheduleEntity> schedules;
}
