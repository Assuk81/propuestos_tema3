/**
 * @author David Abellán Navarro
 * @author Juan Carlos Corredor Sánchez
 * @course 2º D.A.M.
 * @date 25/10/2021
 * @github 
 * 
 */
package propuestos_Tema3_ejer5;

import java.util.ArrayList;

public class Main_ejercicio5 {

	public static void main(String[] args) {
		ArrayList <Empleado> listadoEmpleadosAProyecto;
//		Proyecto proy = new Proyecto(188);
		Proyecto proy = new Proyecto(189);
		listadoEmpleadosAProyecto = proy.getListAsigEmpleados();
		
		if (listadoEmpleadosAProyecto.isEmpty()) {
			System.out.println("No hay empleados asignados al proyecto " + proy.getNum_proy() + " son:");
		} else {
			System.out.println("Los empleados asignados al proyecto " + proy.getNum_proy());
			for (Empleado empleado : listadoEmpleadosAProyecto) {
				System.out.println("DNI: " + empleado.getDni_nif() + " Nombre: " + empleado.getNombre());
			}
		}
	}
}
