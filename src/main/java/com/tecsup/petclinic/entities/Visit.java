package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad para la tabla visits (visitas veterinarias)
 */
@Entity(name = "visits")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Visit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "pet_id")
	private Integer petId;

	@Column(name = "vet_id")
	private Integer vetId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "visit_date")
	private LocalDate visitDate;

	private String description;

	private BigDecimal cost;

}
