package sol;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import model.Cliente;
import model.Empleado;
import model.ExcepcionDeAplicacion;
import model.Finca;
import model.Propietario;
import model.Sucursal;
import model.Visita;
import pers.Persistencia;

// ZOOM +2
public class GestorBD implements Persistencia {
	// + Constantes
	private static final String BD_URL = getPropiedad("url");;
	//jdbc:mysql://localhost:3306/inmobiliaria?serverTimezone=UTC&useSSL=false
	// "jdbc:mysql://localhost:3306/inmobiliaria?autoReconnect=true&useSSL=false";
	// private static final String BD_URL =
	// "jdbc:mysql://localhost:3306/inmobiliaria?autoReconnect=true&useSSL=false";
	//jdbc:mysql://localhost:3306/inmobiliaria

	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String USR = getPropiedad("user");
	private static final String PWD = getPropiedad("password");
	// private static final String BD_URL =
	// "jdbc:oracle:thin:@localhost:1521:pbd";
	// private static final String DRIVER_CLASS =
	// "oracle.jdbc.driver.OracleDriver";
	// private static final String USR = "prbd";
	// private static final String PWD = "prbdprbd";

	// Registro del driver apropiado para la BD a utilizar
//	static {
//		try {
//			Class.forName(DRIVER_CLASS);
//		} catch (ClassNotFoundException e) {
//			System.out.println("No puedo cargar el driver JDBC de la BD (" + DRIVER_CLASS + ")");
//		}
//	}

	public static String getPropiedad(String clave) {
		String valor = null;
		try {
			Properties props = new Properties();
			props.load(new FileInputStream("src/conexion.properties"));

			valor = props.getProperty(clave);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return valor;
	}
	// + No usar los nombres de los campos
	// + No cerrar la conexi�n en el finally
	// + No lanzar Excepci�n de aplicaci�n
	// + String -> boolean
	// + Ayuda a depuraci�n: mostrar el comadno ejecutado
	// System.out.printlen(sql)
	// Que pasa si no se hace rs.next() --> estado de cursor inv�lido
	// Si no se pone driver o la clase esta mal - no suitable driver
	// Si se reutiliza el stm en una segunda consulta - rs is closed
	// Cambios enrte tipos de datos String-char, sql.Date - Calendar, String -
	// boolean
	// Referencias en tablas equivalen a referencias entre clases
	// Reutilizaci�n de la conexi�n
	// public Finca getFinca(String id) throws ExcepcionDeAplicacion {
	// Finca f = null;
	// Connection con = null;
	// String sql = "select * from finca where id_finca='" + id + "'";
	// try {
	// con = DriverManager.getConnection(BD_URL, USR, PWD);
	//
	// Statement stmt = con.createStatement();
	// ResultSet rs = stmt.executeQuery(sql);
	// if (rs.next()) {
	// Statement stm2 = con.createStatement();
	// ResultSet rs2 = stm2.executeQuery("select * from Propietario where
	// id_propietario='" + rs.getString("propietario") + "'");
	//// ResultSet rs2 = stmt.executeQuery("select * from Propietario where
	// id_propietario='" + rs.getString("propietario") + "'");
	//
	// if (rs2.next()) {
	// Propietario p = new Propietario(id, rs2.getString("nombre_propietario"),
	// rs2.getString("apellidos_propietario"),
	// rs2.getString("direcci�n"), rs2.getString("tel�fono"));
	//
	// f = new Finca(id, rs.getString("direcci�n"), rs.getString("ciudad"),
	// rs.getString("c�digo_postal"), rs.getString("tipo"),
	// rs.getInt("habitaciones"), rs.getInt("ba�os"),
	// rs.getString("calefacci�n"), rs.getString("ascensor").equals("si"),
	// p,
	// rs.getDouble("alquiler"));
	// }
	// rs2.close();
	// stm2.close();
	// }
	// rs.close();
	// stmt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// throw new ExcepcionDeAplicacion("Error al recuperar una finca", e);
	// } finally {
	// try {
	// if (con != null) {
	// con.close();
	// }
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	// return f;
	// }

	// // Segunda versi�n (con m�todo auxiliar para el propietario)
	// public Finca getFinca(String id) throws ExcepcionDeAplicacion {
	// Finca f = null;
	// Connection con = null;
	// String sql = "select * from finca where id_finca='" + id + "'";
	// try {
	// con = DriverManager.getConnection(BD_URL, USR, PWD);
	//
	// Statement stmt = con.createStatement();
	// ResultSet rs = stmt.executeQuery(sql);
	// if (rs.next()) {
	// f = new Finca(id, rs.getString("direcci�n"), rs.getString("ciudad"),
	// rs.getString("c�digo_postal"), rs.getString("tipo"),
	// rs.getInt("habitaciones"), rs.getInt("ba�os"),
	// rs.getString("calefacci�n"), rs.getString("ascensor").equals("si"),
	// this.getPropietario(con, rs.getString("propietario")),
	// rs.getDouble("alquiler"));
	// }
	// rs.close();
	// stmt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// throw new ExcepcionDeAplicacion("Error al recuperar una finca", e);
	// } finally {
	// try {
	// if (con != null) {
	// con.close();
	// }
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	// return f;
	// }
	//
	//
	// + Alternativa
	// - Error t�pico: pedir todas las fincas a la BD e ir filtrando en Java
	public List<Finca> buscaFincas(double precioMin, double precioMax) throws ExcepcionDeAplicacion {
		List<Finca> lista = new java.util.ArrayList<Finca>();
		Connection con = null;
		String sql = "select * from Finca where alquiler>=" + precioMin + " and alquiler<=" + precioMax + "";
		try {
			con = DriverManager.getConnection(BD_URL, USR, PWD);
			//System.out.println(con.getMetaData().getDatabaseProductVersion());
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Finca f = new Finca(rs.getString("id_finca"), rs.getString("direccion"), rs.getString("ciudad"),
						rs.getString("codigo_postal"), rs.getString("tipo"), rs.getInt("habitaciones"),
						rs.getInt("banios"), rs.getString("calefaccion"), rs.getString("ascensor").equals("si"),
						this.getPropietario(con, rs.getString("propietario")), rs.getDouble("alquiler"));
				lista.add(f);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ExcepcionDeAplicacion("Error al recuperar la lista de fincas", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return lista;
	}

	// // Tercer versi�n (un getFinca con Connection que es utilizable por
	// getFinca y buscaFinca)
	// public Finca getFinca(String id) throws ExcepcionDeAplicacion {
	// Finca f = null;
	// Connection con = null;
	// try {
	// con = DriverManager.getConnection(BD_URL, USR, PWD);
	// f = this.getFinca(con, id);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// throw new ExcepcionDeAplicacion("Error al recuperar una finca", e);
	// } finally {
	// try {
	// if (con != null) {
	// con.close();
	// }
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	// return f;
	// }
	//
	// private Finca getFinca(Connection con, String id) throws SQLException {
	// Finca f = null;
	// String sql = "select * from finca where id_finca='" + id + "'";
	//
	// Statement stmt = con.createStatement();
	// ResultSet rs = stmt.executeQuery(sql);
	// if (rs.next()) {
	// f = new Finca(id, rs.getString("direcci�n"), rs.getString("ciudad"),
	// rs.getString("c�digo_postal"), rs.getString("tipo"),
	// rs.getInt("habitaciones"), rs.getInt("ba�os"),
	// rs.getString("calefacci�n"), rs.getString("ascensor").equals("si"),
	// this.getPropietario(con, rs.getString("propietario")),
	// rs.getDouble("alquiler"));
	// }
	// rs.close();
	// stmt.close();
	//
	// return f;
	// }
	//
	// public List buscaFincas(double precioMin, double precioMax) throws
	// ExcepcionDeAplicacion {
	// List lista = new java.util.ArrayList();
	// Connection con = null;
	// String sql = "select id_finca from Finca where alquiler>=" + precioMin +
	// " and alquiler<=" + precioMax + "";
	// try {
	// con = DriverManager.getConnection(BD_URL, USR, PWD);
	//
	// Statement stmt = con.createStatement();
	// ResultSet rs = stmt.executeQuery(sql);
	// while (rs.next()) {
	// String idf = rs.getString(1);
	// lista.add(this.getFinca(con, idf));
	// }
	// rs.close();
	// stmt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// throw new ExcepcionDeAplicacion("Error al recuperar la lista de fincas",
	// e);
	// } finally {
	// try {
	// if (con != null) {
	// con.close();
	// }
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	// return lista;
	// }

	// + private o public
	// Comentar que paso de conexi�n es para hacerlo m�s r�pido y en misma
	// transacci�n
	private Propietario getPropietario(Connection con, String id) throws SQLException {
		Propietario p = null;
		String sql = "select * from propietario where id_propietario='" + id + "'";

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			// System.out.println(id);
			p = new Propietario(id, rs.getString("nombre_propietario"), rs.getString("apellidos_propietario"),
					rs.getString("direccion"), rs.getString("telefono"));
		}
		rs.close();
		stmt.close();

		return p;
	}

	// // Cuarta vesi�n (todo en un �nico select)
	public Finca getFinca(String id) throws ExcepcionDeAplicacion {
		Finca f = null;
		Connection con = null;
		String sql = "select  * from finca , propietario where finca.propietario = propietario.id_propietario and id_finca='"
				+ id + "'";
		try {
			con = DriverManager.getConnection(BD_URL, USR, PWD);

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				// f = new Finca(id, rs.getString("finca.direcci�n"),
				// rs.getString("ciudad"),
				// rs.getString("c�digo_postal"), rs.getString("tipo"),
				// rs.getInt("habitaciones"), rs.getInt("ba�os"),
				// rs.getString("calefacci�n"),
				// rs.getString("ascensor").equals("si"),
				// new
				// Propietario(rs.getString("propietario"),rs.getString("propietario.nombre_propietario"),rs.getString("propietario.apellidos_propietario")
				// ,rs.getString("propietario.direcci�n"),rs.getString("propietario.telefono")),
				// rs.getDouble("alquiler"));

				f = new Finca(id, rs.getString(2), rs.getString("ciudad"), rs.getString("codigo_postal"),
						rs.getString("tipo"), rs.getInt("habitaciones"), rs.getInt("banios"),
						rs.getString("calefaccion"), rs.getString("ascensor").equals("si"),
						new Propietario(rs.getString("propietario"), rs.getString("nombre_propietario"),
								rs.getString("apellidos_propietario"), rs.getString(15), rs.getString("telefono")),
						rs.getDouble("alquiler"));

				// f = new Finca(id, rs.getString(2), rs.getString(3),
				// rs.getString(4),
				// rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getString(8),
				// rs.getString(9).equals("si"),
				// new Propietario(rs.getString(11), rs.getString(13),
				// rs.getString(14), rs.getString(15), rs.getString(16)),
				// rs.getDouble(10));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ExcepcionDeAplicacion("Error al recuperar una finca", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return f;
	}

	// + String -> char
	public Empleado getEmpleado(String id) throws ExcepcionDeAplicacion {
		Empleado e = null;
		Connection con = null;
		String sql = "select * from Empleado where id_empleado='" + id + "'";
		try {
			con = DriverManager.getConnection(BD_URL, USR, PWD);

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				Calendar fn = new GregorianCalendar();
				fn.setTime(rs.getDate("fecha_nacimiento"));
				e = new Empleado(id, rs.getString("nombre_empleado"), rs.getString("apellidos_empleado"),
						rs.getString("trabajo"), rs.getString("sexo").charAt(0), rs.getFloat("salario"), fn,
						this.getSucursal(con, rs.getString("sucursal")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new ExcepcionDeAplicacion("Error al recuperar un empleado", ex);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return e;
	}

	private Sucursal getSucursal(Connection con, String id) throws SQLException {
		Sucursal s = null;
		String sql = "select * from sucursal where id_sucursal='" + id + "'";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			s = new Sucursal(id, rs.getString(2), rs.getString(3), rs.getString(4));
		}
		rs.close();
		stmt.close();
		return s;
	}

	public int incrementarSueldo(float porcentaje) throws ExcepcionDeAplicacion {
		int n = 0;
		Connection con = null;
		String sql = "update empleado set salario = salario * (1+" + porcentaje + "/100)";
		try {
			con = DriverManager.getConnection(BD_URL, USR, PWD);

			Statement stmt = con.createStatement();
			n = stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new ExcepcionDeAplicacion("Error al subir el sueldo", ex);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return n;
	}

	// + La transacci�n
	public boolean eliminarCliente(String id) throws ExcepcionDeAplicacion {
		// Borrar visitas incluido
		Connection con = null;
		boolean i = false;
		String sql1 = "delete from visita where id_cliente = '" + id + "'";
		String sql2 = "delete from cliente where id_cliente = '" + id + "'";

		try {
			con = DriverManager.getConnection(BD_URL, USR, PWD);
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			stmt.close();
			con.commit();
			// System.out.println("autocommit "+con.getAutoCommit());
			i = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
			throw new ExcepcionDeAplicacion("Error al borrar cliente", ex);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return i;
	}

	// // Esta soluci�n est� mal (no cierra la conexi�n), pero me la pregunt� un
	// // alumno y no se xq no funciona. Ejecuta dos statements, con auto cmmit
	// a
	// // true, y aunque no cierra la conexi�n al estar el autommit a true
	// deber�a
	// // funcionar. Pero no. El segundo statemente no falla pero no tiene
	// efecto. Si
	// // se cierra la conexi�n va bien, si se hace un commit va bien, incluso
	// si se
	// // ejecuta cualquier cosa dspues de la segunda instr va bien. Parece que
	// no
	// // confirma las actualizaciones hasta que no se cierra, se hace commit o
	// se
	// // ejcuta otra cosa por la conexi�n. El comportamiento es identico para
	// los
	// // tres drivers de access que hay en ODBC
	// public void eliminarCliente(String id) throws ExcepcionDeAplicacion {
	// // Borrar visitas incluido
	// Connection con = null;
	// String sql2 = "delete from cliente where id_cliente = '" + id + "'";
	// String sql1 = "delete from visita where id_cliente = '" + id + "'";
	// try {
	// con = DriverManager.getConnection(BD_URL, USR, PWD);
	// Statement stmt = con.createStatement();
	// stmt.executeUpdate(sql2);
	// stmt.executeUpdate(sql1);
	// stmt.close();
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// throw new ExcepcionDeAplicacion("Error al borrar cliente", ex);
	// } finally {
	// }
	// }

	private static Cliente getCliente(String id) throws ExcepcionDeAplicacion {
		Cliente c = null;
		Connection con = null;
		String sql = "select * from cliente where id_cliente='" + id + "'";
		try {
			con = DriverManager.getConnection(BD_URL, USR, PWD);
		
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
			
				c = new Cliente(id, rs.getString("nombre_cliente"), rs.getString("apellidos_cliente"),
						rs.getString("telefono"), rs.getString("preferencia"), rs.getDouble("presupuesto"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new ExcepcionDeAplicacion("Error al recuperar un cliente", ex);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return c;
	}

	public static void main(String[] args) {
		GestorBD gbd = new GestorBD();
		try {
			Finca f = gbd.getFinca("GLp001");
			System.out.println(f);
			//
			// Empleado e = gbd.getEmpleado("AGar01");
			// System.out.println(e);
			//

//			System.out.println(gbd.buscaFincas(100, 900));
//			Cliente c = getCliente("MPAF01");
//			System.out.println("Cliente" + c);
//			Cliente c2 = getCliente("JCRC01");
//
//			 Cliente cl1= new Cliente("MPAF01","Mar�a Pilar", "Amar Fonda",
//			 "976141516", null, 200.0);//Se encuentra en la BD
//			 Cliente cl2= new Cliente("RMaB01","Pepe", "Torrecilla Olarte",
//			 "941262896", "Soleado", 350.0);//No se encuentra en la BD
//			 Finca fi= gbd.getFinca("HCp001");
//			Empleado em = gbd.getEmpleado("AGar01");
//			//System.out.println(em);
//			 Visita v1= new Visita(cl1,fi,em,Calendar.getInstance());
//			 Visita v2= new Visita(cl2,fi,em,Calendar.getInstance());
//			 List<Visita> listaVisitas= new ArrayList<Visita>();
//			 listaVisitas.add(v1);
//			 listaVisitas.add(v2);
//			 gbd.aniadirVisitas(listaVisitas);
////
//			System.out.println(c2);
//			// gbd.eliminarCliente("DAcC01");
//			// gbd.eliminarCliente("JCRC01");
//			// List l = new ArrayList();
//			// l.add(c);
//			// l.add(c2);
//			// gbd.aniadirClientes(l);
try {
	gbd.existe("Finca","id_finca", "HCp001");
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		} catch (ExcepcionDeAplicacion ex) {
			ex.printStackTrace();
		}
	}

	public boolean existe(String tabla, String campo, Object value) throws SQLException {

		boolean existe = false;
		Connection con = null;
		String sql = "select " + campo + " from " + tabla + " where " + campo + "=?";
		con = DriverManager.getConnection(BD_URL, USR, PWD);
System.out.println();
		PreparedStatement stmt = con.prepareStatement(sql);
		if (value instanceof String)
			stmt.setString(1, (String) value);
		else if (value instanceof Integer) {
			stmt.setInt(1, (Integer) value);
		}
		ResultSet rs = stmt.executeQuery();
		existe = rs.next();
		rs.close();
		stmt.close();

		return existe;
	}

	public int aniadirVisitas(List<Visita> visitas) throws ExcepcionDeAplicacion {

		int n = 0;
		String comentarios=null;
		Double presupuesto=null;
		Cliente c = null;
		Connection con = null;
		String sqlC = "insert into Cliente values (?,?,?,?,?,?)";
		String sqlV = "insert into Visita values (?,?,?,?,?)";
		try {
			con = DriverManager.getConnection(BD_URL, USR, PWD);
			con.setAutoCommit(false);
			PreparedStatement stmtC = con.prepareStatement(sqlC);
			PreparedStatement stmtV = con.prepareStatement(sqlV);
			for (Visita v : visitas) {
				c = v.getCliente();
				if (!existe("Cliente", "id_cliente", c.getId())) {
					stmtC.setString(1, c.getId());
					stmtC.setString(2, c.getNombre());
					stmtC.setString(3, c.getApellidos());
					stmtC.setString(4, c.getTelefono());
					stmtC.setString(5, c.getPreferencia());
					presupuesto=c.getPresupuesto();
					if(presupuesto==null)
						stmtC.setNull(6, Types.DOUBLE);
					else
						stmtC.setDouble(6, c.getPresupuesto());
					stmtC.executeUpdate();  
				}
				stmtV.setString(1, c.getId());
				stmtV.setString(3, v.getEmpleado().getId());
				stmtV.setString(2, v.getFinca().getId());
				stmtV.setDate(4, new Date(v.getFecha().getTimeInMillis()));
				comentarios=v.getComentarios();
				if(comentarios==null)
					stmtV.setString(5, "");
				else
					stmtV.setString(5, comentarios);
				n = n + stmtV.executeUpdate();
			}
			stmtC.close();
			stmtV.close();
			con.commit();
		} catch (SQLException ex) {
			
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
			throw new ExcepcionDeAplicacion("Error al borrar cliente", ex);
			
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return n;

	}

}
