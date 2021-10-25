/**
 * @author David Abellán Navarro
 * @author Juan Carlos Corredor Sánchez
 * @course 2º D.A.M.
 * @date 20/10/2021
 * @github 
 * 
 */
package propuestos_Tema3_ejer3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorProyectos {


	private static final String basedatos;
	private static final String host;
	private static final String port;
	private static final String user;
	private static final String pwd;
	private static final String parAdic;
	private static final String urlConnection;

	static {
		basedatos = "gestor_proyectos";
		host = "localhost";
		port = "3306";
		user = "JuanCarlos";
		pwd = "1234";
		parAdic = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos + parAdic;
	}

	//QUERIES
	private static final String SQL_SELECT = "select * from empleado where dni=?";
	private static final String SQL_SELECT_ALL_EMPLEADOS = "select * from empleado";
	private static final String SQL_SELECT_ALL_PROYECTOS = "select * from proyecto";
	private static final String SQL_SELECT_ID_PROY = "select * from proyecto where id_proy=?";
	private static final String SQL_INSERT_EMPLEADO ="insert into empleado(dni,nom_emp) VALUES (?,?)";
	private static final String SQL_INSERT_PROY = "insert into proyecto(nom_proy,f_inicio,f_fin,dni_jefe_proy) VALUES (?,?,?,?)";
	private static final String SQL_INSERT_ASIGNAR = "insert into asig_proyecto(dni_emp,id_proy,f_inicio,f_fin)values(?,?,?,?)";

	public static boolean nuevoEmpleado(String dni,String nombre) throws SQLException {
		boolean insertado = false;

		try(	Connection conn = DriverManager.getConnection(urlConnection,user,pwd);
				PreparedStatement sConsulta = conn.prepareStatement(SQL_SELECT);
				){

			sConsulta.setString(1,dni);

			ResultSet rs = sConsulta.executeQuery(); 

			if(rs.next()) {
				System.out.println("Ya existe el DNI indicado, elige otro.");
			}else {
				try(PreparedStatement sInsert = conn.prepareStatement(SQL_INSERT_EMPLEADO)){
					conn.setAutoCommit(false);
					int i = 1;
					sInsert.setString(i++, dni);
					sInsert.setString(i++, nombre);

					int row1 = sInsert.executeUpdate();
					conn.commit();
					conn.setAutoCommit(true);

					if(row1 != 0) {
						insertado = true;
					}

				}
			}
		}
		return insertado;
	}

	public static int nuevoProyecto(String nombreProyecto, Date fechaInicio,Date fechaFin,String dniJefe) throws SQLException {
		int id = 0;
		
		try(	Connection conn = DriverManager.getConnection(urlConnection,user,pwd);
				PreparedStatement sConsulta = conn.prepareStatement(SQL_SELECT);
				){

			sConsulta.setString(1,dniJefe);

			ResultSet rs = sConsulta.executeQuery(); 

			if(rs.next()) {
				try(PreparedStatement sInsert = conn.prepareStatement(SQL_INSERT_PROY,PreparedStatement.RETURN_GENERATED_KEYS)){
					conn.setAutoCommit(false);
					int i = 1;

					sInsert.setString(i++,nombreProyecto);
					sInsert.setDate(i++,fechaInicio);
					sInsert.setDate(i++,fechaFin);
					sInsert.setString(i++, dniJefe);

					int row = sInsert.executeUpdate();

					conn.commit();
					conn.setAutoCommit(true);

					if(row != 0) {
						ResultSet rsId = sInsert.getGeneratedKeys();
						rsId.next();
						id = rsId.getInt(1);
					}
				}
			}else {
				System.out.println("El dni indicado no corresponde a ningún empleado");
			}
		}
		return id;
	}

	public static boolean asigEmpleAProyecto(String dni,int id) throws SQLException {
		boolean asignado = false;

		try(	Connection conn = DriverManager.getConnection(urlConnection,user,pwd);
				PreparedStatement sConsulta1 = conn.prepareStatement(SQL_SELECT);
				PreparedStatement sConsulta2 = conn.prepareStatement(SQL_SELECT_ID_PROY)
				){

			sConsulta1.setString(1,dni);
			sConsulta2.setInt(1,id);
			ResultSet rs1 = sConsulta1.executeQuery(); 
			ResultSet rs2 = sConsulta2.executeQuery();

			if(rs1.next()) {
				if(rs2.next()) {
					try(PreparedStatement sAsignar = conn.prepareStatement(SQL_INSERT_ASIGNAR)){
						conn.setAutoCommit(false);
						int i = 1;

						sAsignar.setString(i++, dni);
						sAsignar.setInt(i++, id);
						sAsignar.setDate(i++, rs2.getDate("f_inicio"));
						sAsignar.setDate(i++, rs2.getDate("f_fin"));

						int row = sAsignar.executeUpdate();

						conn.commit();
						conn.setAutoCommit(true);

						if(row != 0) {
							asignado = true;
						}
					}
				}else {
					System.out.println("El ID indicado no corresponde a ningún proyecto");
				}
			}else {
				System.out.println("El dni indicado no corresponde a ningún empleado");
			}
		}
		return asignado;
	}

	public static void mostrarEmpleados() throws SQLException {
		try(	Connection conn = DriverManager.getConnection(urlConnection,user,pwd);
				PreparedStatement sMostrarEmpleados = conn.prepareStatement(SQL_SELECT_ALL_EMPLEADOS)){

			ResultSet rs = sMostrarEmpleados.executeQuery();
			while (rs.next()) {
				System.out.println("DNI: " + rs.getString("dni") + " -> Nombre: " + rs.getString("nom_emp"));
			}
		}

	}
	public static void mostrarProyectos() throws SQLException {
		try(	Connection conn = DriverManager.getConnection(urlConnection,user,pwd);
				PreparedStatement sMostrarProyectos = conn.prepareStatement(SQL_SELECT_ALL_PROYECTOS)){

			ResultSet rs = sMostrarProyectos.executeQuery();
			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("id_proy") + " -> Nombre: " + rs.getString("nom_proy"));
			}
		}

	}
}
