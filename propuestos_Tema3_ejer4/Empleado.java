/**
 * @author David Abellán Navarro
 * @author Juan Carlos Corredor Sánchez
 * @course 2º D.A.M.
 * @date 25/10/2021
 * @github 
 * 
 */
package propuestos_Tema3_ejer4;

import java.sql.SQLException;

public class Empleado {
	
	private String dni_nif, nombre;

	public Empleado () {
		
	}

	public Empleado (String dni_nif) {
		this.dni_nif = dni_nif;
		try {
			Empleado e = GestorProyectos.getEmpleado(dni_nif);
			this.nombre = e.nombre;
		} catch (SQLException e) {
			save();
		}
	}
	
	public void save() {
		try {
			if ( GestorProyectos.guardarEmpleado(this.dni_nif, this.nombre)) {
				System.out.println("Datos guardados correctamente");
			} else {
				System.out.println("Ha ocurrido un problema");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getDni_nif() {
		return dni_nif;
	}

	public void setDni_nif(String dni_nif) {
		this.dni_nif = dni_nif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}