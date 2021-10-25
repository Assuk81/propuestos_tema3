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
import java.util.ArrayList;

public class Proyecto {
	
	private String nom_proy, dni_jefe_proy;
	private int num_proy;
	private Date f_inicio, f_fin;

	public Proyecto() {
		
	}

	public Proyecto (int num_proy) {
		this.num_proy = num_proy;
		try {
			Proyecto p = GestorProyectos.getProyecto(num_proy);
			this.nom_proy = p.nom_proy;
			this.dni_jefe_proy = p.dni_jefe_proy;
			this.f_inicio = p.f_inicio;
			this.f_fin = p.f_fin;
		} catch (SQLException e) {
			save();
		}
	}
	
	public void save() {
		try {
			if ( GestorProyectos.guardarProyecto(this.num_proy, this.nom_proy, this.f_inicio, this.f_fin, this.dni_jefe_proy)) {
				System.out.println("Datos guardados correctamente");
			} else {
				System.out.println("Ha ocurrido un problema");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList <Empleado> getListAsigEmpleados() {
		return GestorProyectos.getListadoProyAsig(this.num_proy);
	}
	
	public String getNom_proy() {
		return nom_proy;
	}

	public void setNom_proy(String nom_proy) {
		this.nom_proy = nom_proy;
	}

	public String getDni_jefe_proy() {
		return dni_jefe_proy;
	}

	public void setDni_jefe_proy(String dni_jefe_proy) {
		this.dni_jefe_proy = dni_jefe_proy;
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
