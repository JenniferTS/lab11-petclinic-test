package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Entidad para la tabla vet_specialties
 * (relacion muchos a muchos entre veterinarios y especialidades)
 */
@Entity(name = "vet_specialties")
@IdClass(VetSpecialtyId.class)
@NoArgsConstructor
@Data
@AllArgsConstructor
public class VetSpecialty {

	@Id
	@Column(name = "vet_id")
	private Integer vetId;

	@Id
	@Column(name = "specialty_id")
	private Integer specialtyId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "certification_date")
	private LocalDate certificationDate;

	@Column(name = "years_experience")
	private Integer yearsExperience;

	// Indica si es la especialidad principal del veterinario
	@Column(name = "is_primary")
	private Boolean primary;

	private String notes;

}
