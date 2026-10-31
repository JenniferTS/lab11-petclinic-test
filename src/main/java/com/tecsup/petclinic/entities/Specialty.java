package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad para la tabla specialties (especialidades)
 */
@Entity(name = "specialties")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Specialty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String office;

	// Hora de apertura del consultorio (0 - 23)
	@Column(name = "h_open")
	private Integer openHour;

	// Hora de cierre del consultorio (0 - 23)
	@Column(name = "h_close")
	private Integer closeHour;

}
