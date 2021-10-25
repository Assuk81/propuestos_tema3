/**
 * @author David Abellán Navarro
 * @author Juan Carlos Corredor Sánchez
 * @course 2º D.A.M.
 * @date 25/10/2021
 * @github 
 * 
 */
package propuestos_Tema3_ejer5;

import java.sql.Date;
import java.sql.SQLException;

public class AsignacionEmpAProyecto {
	
	private String dni_nif_emp;
	private int num_proy;
	private Date f_inicio, f_fin;

	public AsignacionEmpAProyecto () {
		
	}

	public AsignacionEmpAProyecto (String dni_nif_emp, int num_proy, Date f_inicio) {
		this.dni_nif_emp = dni_nif_emp;
		this.num_proy = num_proy;
		this.f_inicio = f_inicio;
		try {
			AsignacionEmpAProyecto aep = GestorProyectos.getAsigEmpAProy(dni_nif_emp, num_proy, f_inicio);
			this.f_fin = aep.f_fin;
		} catch (SQLException e) {
//			e.printStackTrace();
			save();
		}
	}

	public void save() {
		try {
			if ( GestorProyectos.guardarAsignarEmpAProyecto(this.dni_nif_emp, this.num_proy, this.f_inicio, this.f_fin)) {
				System.out.println("Datos guardados correctamente");
			} else {
				System.out.println("Ha ocurrido un problema");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public String getDni_nif_emp() {
		return dni_nif_emp;
	}

	public void setDni_nif_emp(String dni_nif_emp) {
		this.dni_nif_emp = dni_nif_emp;
	}

	public int getNum_proy() {
		return num_proy;
	}

	public void setNum_proy(int num_proy) {
		this.num_proy = num_proy;
	}

	public Date getF_inicio() {
		return f_inicio;
	}

	public void setF_inicio(Date f_inicio) {
		this.f_inicio = f_inicio;
	}

	public Date getF_fin() {
		return f_fin;
	}

	public void setF_fin(Date f_fin) {
		this.f_fin = f_fin;
	}
	
}