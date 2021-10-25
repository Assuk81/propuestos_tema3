/**
 * @author David Abellán Navarro
 * @author Juan Carlos Corredor Sánchez
 * @course 2º D.A.M.
 * @date 20/10/2021
 * @github 
 * 
 */
package propuestos_Tema3_ejer3;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main_ejercicio3 {

	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) throws SQLException {
		
		String nombre = "";
		String dni = "";
		String nombreProyecto = "";
		int id = 0;
		int opcion1 = 0;
		
		Date fechaInicio = Date.valueOf(LocalDate.now().plusDays(30));
		Date fechaFin = Date.valueOf(LocalDate.now().plusYears(3));
		
		do {
			System.out.println(".:MENU PRINCIPAL:.");
			System.out.println("1. Insertar empleado");
			System.out.println("2. Insertar proyecto");
			System.out.println("3. Asignar empleado a un proyecto");
			System.out.println("4. Salir");
			opcion1 = Integer.parseInt(sc.nextLine());
			
			switch (opcion1) {
			case 1: 
				System.out.println("Introduce el dni");
				dni = sc.nextLine();
				System.out.println("Introduce el nombre");
				nombre = sc.nextLine();
				
				if(GestorProyectos.nuevoEmpleado(dni, nombre)) {
					System.out.println("Empleado insertado correctamente");
				}else {
					System.out.println("ERROR al insertar el empleado");
					System.out.println();
				}
				
			break;
			case 2:
				System.out.println("Introduce un nombre de proyecto");
				nombreProyecto = sc.nextLine();
				System.out.println("Introduce el dni");
				dni = sc.nextLine();
				System.out.println("Su proyecto creado es el ID: "+ GestorProyectos.nuevoProyecto(nombreProyecto,fechaInicio,fechaFin,dni) );
				
			break;
			case 3:
				GestorProyectos.mostrarEmpleados();
				GestorProyectos.mostrarProyectos();
				System.out.println("Indica el DNI del empleado que quieres asociar");
				dni = sc.nextLine();
				System.out.println("Indica el ID del proyecto al que asociar al empleado");
				id = Integer.parseInt(sc.nextLine());
				if(GestorProyectos.asigEmpleAProyecto(dni, id)){
					System.out.println("Empleado con DNI " + dni + " se ha asociado correctamente al proyecto con ID " + id);
				}else {
					System.out.println("ERROR al asociar empleado al proyecto");
				}
			break;
			case 4:
				
			break;
			default:
				System.out.println("Opcion erronea, elige una entre las disponibles");;
			}
		}while(opcion1 != 4);
	}

}
