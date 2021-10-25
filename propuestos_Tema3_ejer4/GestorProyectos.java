   
/**
 * @author David Abellán Navarro
 * @author Juan Carlos Corredor Sánchez
 * @course 2º D.A.M.
 * @date 25/10/2021
 * @github 
 * 
 */
package propuestos_Tema3_ejer4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.eclipse.jdt.annotation.Nullable;

import java.sql.Date;

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

	//	Queries
	private static final String SQL_INSERT_ON_UPDATE_EMP = "INSERT INTO EMPLEADOS (DNI_NIF, NOMBRE) VALUES (?,?)"
			+ " ON DUPLICATE KEY UPDATE DNI_NIF=VALUES(DNI_NIF), NOMBRE=VALUES(NOMBRE)";
	private static final String SQL_INSERT_ON_UPDATE_PROY = "INSERT INTO PROYECTOS (NUM_PROY, NOM_PROY, F_INICIO, F_FIN, DNI_JEFE_PROY) "
			+ "VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE NOM_PROY=VALUES(NOM_PROY), F_INICIO=VALUES(F_INICIO),"
			+ " F_FIN=VALUES(F_FIN), DNI_JEFE_PROY=VALUES(DNI_JEFE_PROY)";
	private static final String SQL_INSERT_ON_UPDATE_ASIG = "INSERT INTO ASIG_PROYECTOS (DNI_NIF_EMP, NUM_PROY, F_INICIO, F_FIN) "
			+ "VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE DNI_NIF_EMP=VALUES(DNI_NIF_EMP), NUM_PROY=VALUES(NUM_PROY), "
			+ "F_INICIO=VALUES(F_INICIO), F_FIN=VALUES(F_FIN)";
	
	private static final String SQL_INSERT_EMP = "INSERT INTO EMPLEADOS (DNI_NIF, NOMBRE) VALUES (?,?)";
	private static final String SQL_INSERT_PROY = "INSERT INTO PROYECTOS (NOM_PROY, F_INICIO, F_FIN, DNI_JEFE_PROY) VALUES (?,?,?,?)";
	private static final String SQL_INSERT_ASIG = "INSERT INTO ASIG_PROYECTOS (DNI_NIF_EMP, NUM_PROY, F_INICIO, F_FIN) VALUES (?,?,?,?)";
	
	private static final String SQL_SELECT_EMP = "SELECT * FROM EMPLEADOS WHERE DNI_NIF=?";
	private static final String SQL_SELECT_PROY = "SELECT * FROM PROYECTOS WHERE NUM_PROY=?";
	private static final String SQL_SELECT_ASIG = "SELECT * FROM ASIG_PROYECTOS WHERE DNI_NIF_EMP=? AND NUM_PROY=? AND F_INICIO=?";
	
	private static final String SQL_SELECT_ALL_EMP = "SELECT * FROM EMPLEADOS";
	private static final String SQL_SELECT_ALL_PROY = "SELECT * FROM PROYECTOS";
	private static final String SQL_SELECT_ALL_ASIG = "SELECT * FROM ASIG_PROYECTOS";
	private static final String SQL_SELECT_ALL_ASIG_TO_PROY = "SELECT * FROM ASIG_PROYECTOS WHERE NUM_PROY=? AND F_INICIO<? AND F_FIN>? OR F_FIN = NULL";

	public static boolean nuevoEmpleado(String dni, String nombre) throws SQLException {
		boolean insertado = false;
		
		try(Connection c = DriverManager.getConnection(urlConnection, user, pwd);
				PreparedStatement consulta = c.prepareStatement(SQL_SELECT_EMP);) {
			consulta.setString(1, dni);
			ResultSet rs = consulta.executeQuery();
			
			if (rs.next()) {
				System.out.println("Ya hay un registro con el dni:" + dni + ".");
			} else {
				try (PreparedStatement insert = c.prepareStatement(SQL_INSERT_EMP)){
					c.setAutoCommit(insertado);
					insert.setString(1, dni);
					insert.setString(2, nombre);
					
					if(insert.executeUpdate() != 0) {
						insertado = true;
					}
					c.commit();
					c.setAutoCommit(true);
				}
			}
		}
		return insertado;
	}
	
	public static Empleado getEmpleado(String dni) throws SQLException{

		Empleado emp =null;

		try (	
				Connection conn = DriverManager.getConnection(urlConnection, user, pwd);
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT_EMP);
				) 
		{
			preparedStatement.setString(1, dni);

			try (ResultSet resultSet = preparedStatement.executeQuery();) 
			{
				if (resultSet.next()) {
					emp = new Empleado();	
					emp.setDni_nif(resultSet.getString("DNI_NIF"));
					emp.setNombre(resultSet.getString("NOMBRE"));
				}
			}
		}
		return emp;
	}
	
	public static boolean guardarEmpleado(@Nullable String dni_nif, @Nullable String nombre) throws SQLException {
		boolean insertado = false;

        try (    Connection c = DriverManager.getConnection(urlConnection, user, pwd);
                PreparedStatement consulta = c.prepareStatement(SQL_SELECT_EMP);
                PreparedStatement insert = c.prepareStatement(SQL_INSERT_ON_UPDATE_EMP)){
        	
        	if (dni_nif != null) {
				consulta.setString(1, dni_nif);
				
				ResultSet rs = consulta.executeQuery();

	            if(rs.next()) {
	            	
	                c.setAutoCommit(false);
	               
	                if(nombre == null) {
	                	nombre = rs.getString("nombre");
	                }
	            }
			}

            insert.setString(1, dni_nif);
            insert.setString(2, nombre);
            

			if(insert.executeUpdate() != 0) {
				insertado = true;
			}
			
            c.commit();
            c.setAutoCommit(true);
        }
        return insertado;
	}
	
	public static int nuevoProyecto (String nombre, Date inicio, Date fin, String dni) throws SQLException {
		int insertado = 0;
		
		try(Connection c = DriverManager.getConnection(urlConnection, user, pwd);
				PreparedStatement consulta = c.prepareStatement(SQL_SELECT_EMP);) {
			consulta.setString(1, dni);
			ResultSet rs = consulta.executeQuery();
			
			if (!rs.next()) {
				System.out.println("No existe ningún empleado con el dni:" + dni + ".");
			} else {
				
				try (PreparedStatement insert = c.prepareStatement(SQL_INSERT_PROY, PreparedStatement.RETURN_GENERATED_KEYS)){
					c.setAutoCommit(false);
					insert.setString(1, nombre);
					insert.setDate(2, inicio);
					insert.setDate(3, fin);
					insert.setString(4, dni);
					insert.executeUpdate();
					
					ResultSet rsInsert = insert.getGeneratedKeys();
					rsInsert.next();
					insertado = rsInsert.getInt("num_proy");
					c.commit();
					c.setAutoCommit(true);
				}
			}
		}
		return insertado;
	}
	
	public static Proyecto getProyecto(int num_proy) throws SQLException{
		Proyecto proy =null;

		try (	
				Connection conn = DriverManager.getConnection(urlConnection, user, pwd);
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT_PROY);
				) 
		{
			preparedStatement.setLong(1, num_proy);

			try (ResultSet resultSet = preparedStatement.executeQuery();) 
			{
				if (resultSet.next()) {
					proy = new Proyecto();	
					proy.setNom_proy(resultSet.getString("NOM_PROY"));
					proy.setF_inicio(resultSet.getDate("F_INICIO"));
					proy.setF_fin(resultSet.getDate("F_FIN"));
					proy.setDni_jefe_proy(resultSet.getString("DNI_JEFE_PROY"));
				}
			}
		}

		return proy;
	}

	public static boolean guardarProyecto (@Nullable int num_proy,@Nullable String nom_proy,@Nullable Date f_inicio,@Nullable Date f_fin,@Nullable String dni_jefe_emp) throws SQLException {
        boolean insertado = false;

        try (   Connection c = DriverManager.getConnection(urlConnection, user, pwd);
                PreparedStatement consulta = c.prepareStatement(SQL_SELECT_PROY);
                PreparedStatement insert = c.prepareStatement(SQL_INSERT_ON_UPDATE_PROY)){
            c.setAutoCommit(false);
            
        	if (num_proy > 0) {
        		//si parametro num_proy es mayor de 0 es que no viene null
				consulta.setInt(1, num_proy);
				
				ResultSet rs = consulta.executeQuery();
                
	            if(rs.next()) {
	                if(nom_proy == null) {
	                    nom_proy = rs.getString("nom_proy");
	                }
	                if(f_inicio == null) {
	                    f_inicio = rs.getDate("f_inicio");
	                }
	                if(f_fin == null) {
	                    f_fin =  rs.getDate("f_fin");
	                }
	                if(dni_jefe_emp == null) {
	                    dni_jefe_emp = rs.getString("dni_jefe_emp");
	                }
	            }
			} else {//buscamos id en bd, esto en el caso de venir num_proy en null,vacio, en 0
	            	PreparedStatement buscaId = c.prepareStatement(SQL_SELECT_ALL_PROY,
	            			ResultSet.TYPE_SCROLL_INSENSITIVE,
						    ResultSet.CONCUR_UPDATABLE);
	            	
	            	ResultSet rs1 = buscaId.executeQuery();
	            	rs1.last();//vamos al ultimo id y le sumamos 1
	            	int ultimoId =  rs1.getInt("num_proy") + 1;
	            	num_proy = ultimoId;
	            }
            if(f_inicio == null) {
                f_inicio = Date.valueOf(LocalDate.now());
            }
            if(f_fin == null) {
                f_fin = Date.valueOf(LocalDate.now());
            }
        	
            insert.setInt(1, num_proy);
            insert.setString(2, nom_proy);
            insert.setDate(3, f_inicio);
            insert.setDate(4, f_fin);
            insert.setString(5, dni_jefe_emp);
            
            int resultado = insert.executeUpdate();

			if(resultado == 0 || resultado == 1 || resultado == 2) {
				insertado = true;
			}
			
            c.commit();
            c.setAutoCommit(true);
        }
        return insertado;
    }


	public static boolean asignaEmpAProyecto (String dni, int id) throws SQLException {
		boolean insertado = false;

		try(Connection c = DriverManager.getConnection(urlConnection, user, pwd);
				PreparedStatement cEmpleados = c.prepareStatement(SQL_SELECT_EMP);
				PreparedStatement cProyectos = c.prepareStatement(SQL_SELECT_PROY)) {
			cEmpleados.setString(1, dni);
			ResultSet rsEmpleados = cEmpleados.executeQuery();
			cProyectos.setInt(1, id);
			ResultSet rsProyectos = cProyectos.executeQuery();

			if (!rsEmpleados.next()) {
				System.out.println("No existe ningún empleado con el dni:" + dni + ".");
			} else if (!rsProyectos.next()) {
				System.out.println("No existe ningún proyectos con el identificador:" + id + ".");
			} else {

				try (PreparedStatement insert = c.prepareStatement(SQL_INSERT_ASIG)){
					c.setAutoCommit(insertado);
					insert.setString(1, dni);
					insert.setInt(2, id);
					insert.setDate(3, rsProyectos.getDate("f_inicio"));
					insert.setDate(4, rsProyectos.getDate("f_fin"));

					if(insert.executeUpdate() != 0) {
						insertado = true;
					}
					c.commit();
					c.setAutoCommit(true);
				}
			}
		}
		return insertado;
	}

	public static AsignacionEmpAProyecto getAsigEmpAProy(String dni_nif_emp, int num_proy, Date f_inicio) throws SQLException {
		AsignacionEmpAProyecto aep =null;
		try (	
				Connection c = DriverManager.getConnection(urlConnection, user, pwd);
				PreparedStatement preparedStatement = c.prepareStatement(SQL_SELECT_ASIG);
				) 
		{
			preparedStatement.setString(1, dni_nif_emp);
			preparedStatement.setInt(2, num_proy);
			preparedStatement.setDate(3, f_inicio);

			try (ResultSet resultSet = preparedStatement.executeQuery();) 
			{
				if (resultSet.next()) {
					aep = new AsignacionEmpAProyecto();
					aep.setF_fin(resultSet.getDate("F_FIN"));
				}
			}
		}

		return aep;
	}

	public static boolean guardarAsignarEmpAProyecto(String dni_nif_emp, int num_proy, Date f_inicio, @Nullable Date f_fin) throws SQLException {
		boolean insertado = false;

        try (   Connection c = DriverManager.getConnection(urlConnection, user, pwd);
                PreparedStatement consulta = c.prepareStatement(SQL_SELECT_ASIG);
                PreparedStatement insert = c.prepareStatement(SQL_INSERT_ON_UPDATE_ASIG)){
        	
            c.setAutoCommit(false);
            
        	if (dni_nif_emp != null) {
				consulta.setString(1, dni_nif_emp);
			}
        	if (num_proy > 0) {
				consulta.setInt(2, num_proy);
			}
        	if (f_inicio != null) {
				consulta.setDate(3, f_inicio);
			}
        	
            ResultSet rs = consulta.executeQuery();

            if(rs.next()) {
                if(f_fin == null) {
                    f_fin =  rs.getDate("f_fin");
                }
            }

            insert.setString(1, dni_nif_emp);
            insert.setInt(2, num_proy);
            insert.setDate(3, f_inicio);
            insert.setDate(4, f_fin);
            

			if(insert.executeUpdate() != 0) {
				insertado = true;
			}
			
            c.commit();
            c.setAutoCommit(true);
        }
        return insertado;
	}

























	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	private static void logErrorSQL(SQLException e) {
//	System.err.println("SQL ERROR mensaje: " + e.getMessage());
//	System.err.println("SQL Estado: " + e.getSQLState());
//	System.err.println("SQL codigo especifico: " + e.getErrorCode());
//	//		    System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
//}
//	
//	
//	
//	public static boolean deleteClienteByDni(String dni) {
//		boolean deleted = false;
//
//		try (	Connection conn = DriverManager.getConnection(urlConnection, user, pwd);
//				PreparedStatement preparedStatement = conn.prepareStatement(SQL_DELETE)
//				) 
//		{
//			preparedStatement.setString(1, dni);
//			int row = preparedStatement.executeUpdate();
//
//			// rows affected
//			//			System.out.println(row);
//			if(row!=0) {
//				deleted = true;
//			}
//
//		} catch (SQLException e) {
//			logErrorSQL(e);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return deleted;
//	}
//
//	public static boolean updateClienteApellidosByDni(String apellido , String dni) {
//		boolean updated = false;
//
//		try (	Connection conn = DriverManager.getConnection(urlConnection, user, pwd);
//				PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE)
//				) 
//		{
//			preparedStatement.setString(1, apellido);
//			preparedStatement.setString(2, dni);
//			int row = preparedStatement.executeUpdate();
//
////			rows affected
////			System.out.println(row);
//			if(row!=0) {
//				updated = true;
//			}
//
//		} catch (SQLException e) {
//			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return updated;
//	}
//
//	public static void mostrarClienteByDni(String dni) {
//
//		try (	
//				Connection conn = DriverManager.getConnection(urlConnection, user, pwd);
//				PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);
//				) 
//		{
//			preparedStatement.setString(1, dni);
//
//			try (ResultSet resultSet = preparedStatement.executeQuery();) 
//			{
//				if (resultSet.next()) {
//					dni = resultSet.getString("DNI");
//					String apellidos = resultSet.getString("APELLIDOS");
//					String cp = resultSet.getString("CP");
//
//					System.out.println("Cliente encontrado. DNI: "+dni+" APELLIDOS: "+apellidos+" CP: "+cp);
//				}
//			}
//		} catch (SQLException e) {
//			logErrorSQL(e);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//
//	public static Cliente getClienteByDni(String dni) {
//
//		Cliente cliente=null;
//
//		try (	
//				Connection conn = DriverManager.getConnection(urlConnection, user, pwd);
//				PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);
//				) 
//		{
//			preparedStatement.setString(1, dni);
//
//			try (ResultSet resultSet = preparedStatement.executeQuery();) 
//			{
//				if (resultSet.next()) {
//					cliente = new Cliente	( 	
//							resultSet.getString("DNI"),
//							resultSet.getString("APELLIDOS"),
//							resultSet.getString("CP")
//							);
//				}
//			}
//		} catch (SQLException e) {
//			logErrorSQL(e);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return cliente;
//	}
//
//	public static ArrayList<Cliente> getClientes() {
//
//		ArrayList<Cliente> clientes= new ArrayList<>();
//
//		try (	
//				Connection conn = DriverManager.getConnection(urlConnection, user, pwd);
//				PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT_ALL);
//				) 
//		{
//
//			try (ResultSet resultSet = preparedStatement.executeQuery();) 
//			{
//
//				while (resultSet.next()) {
//					clientes.add( new Cliente	( 	
//							resultSet.getString("DNI"),
//							resultSet.getString("APELLIDOS"),
//							resultSet.getString("CP")
//							)
//							);
//				}
//			}
//		} catch (SQLException e) {
//			logErrorSQL(e);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return clientes;
//	}

}
