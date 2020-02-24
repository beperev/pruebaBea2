package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import model.Cliente;
import model.Empleado;
import model.ExcepcionDeAplicacion;
import model.Finca;
import model.Propietario;
import model.Sucursal;
import model.Visita;
import sol.GestorBD;
import util.TestsUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestsPr4 extends TestsUtil {

	@BeforeClass
	public static void creacionGestorBD() {
		gbd = new sol.GestorBD();
		url = GestorBD.getPropiedad("url");
		user = GestorBD.getPropiedad("user");
		password = GestorBD.getPropiedad("password");
		schema = GestorBD.getPropiedad("schema");
	}

	// Antes de ejecutar cada test, eliminamos el estado previo de la BD, eliminando
	// los registros insertados en el test previo y cargando los datos requeridos
	// para dicho test.
	@Before
	public void importDataSet() throws Exception {
		IDataSet dataSet = readDataSet();
		cleanlyInsertDataset(dataSet);
	}

	@Test
	public void testGetFinca() throws ExcepcionDeAplicacion {
		try {
			// Obtenemos de la BD la finca esperada
			Finca fincaObtenida = gbd.getFinca("HCp001");
			// Creamos un objeto con la Finca esperada
			Propietario prop = new Propietario("ElGC01", "Eladio", "Gutierrez Casado", "P. Constitucion 3",
					"976112233");
			Finca fincaEsperada = new Finca("HCp001", "C/ Hernan Cortes 1", "Zaragoza", "50005", "piso", 6, 2,
					"central", true, prop, 300);
			// Comprobamos que coinciden
			assertEquals("Falla al comprobar la finca", fincaEsperada, fincaObtenida);
		} catch (ExcepcionDeAplicacion e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetEmpleado() throws ExcepcionDeAplicacion {
		try {
			// Obtenemos de la BD el empleado esperado
			Empleado empleadoObtenido = gbd.getEmpleado("AGar01");
			// Creamos un objeto con el empleado esperado
			Sucursal suc = new Sucursal("BrZa01", "Breton 4", "Zaragoza", "50009");
			GregorianCalendar fechNacim = new GregorianCalendar(1966, 0, 1);
			Empleado empleadoEsperado = new Empleado("AGar01", "Alberto", "Garcia Romero", "director", 'h', 2035.0,
					fechNacim, suc);
			System.out.println(empleadoEsperado);
			System.out.println(empleadoObtenido);
			// Comprobamos que ambos empleados coinciden
			assertEquals("Falla al comprobar el empleado", empleadoEsperado, empleadoObtenido);
		} catch (ExcepcionDeAplicacion e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBuscaFincas() {
		try {
			// Obtenemos de la BD el listado de las fincas cuyo percio de alquiler se
			// encuentra en el intervalo [100,900]
			List<Finca> resultadoObtenido = gbd.buscaFincas(100, 900);
			List<String> resultadoObtenidoString = new ArrayList<String>();
			for (Finca f : resultadoObtenido) {
				resultadoObtenidoString.add(f.getId());
			}
			// Cargo los identificadores de las fincas esperadas desde el XML
			// fincasEsperadas.xml
			List<String> resultadoEsperado = leerXML("src/fincasEsperadas.xml", "finca", "ID_FINCA");
			// Comprobamos que coinciden
			assertTrue("Falla al buscar fincas: el numero de registros no coincide",
					resultadoObtenidoString.size() == resultadoEsperado.size());
			assertTrue("Falla al buscar fincas: las fincas obtenidas no son las correctas",
					resultadoObtenidoString.containsAll(resultadoEsperado)
							&& resultadoEsperado.containsAll(resultadoObtenidoString));
		} catch (ExcepcionDeAplicacion ex) {
			fail("Error buscando" + ex);
			ex.printStackTrace();
		}
	}

	@Test
	public void testIncrementarSueldo() {
		try {
			// Incrementamos el sueldo un 20% invocando al metodo incrementarSueldo
			gbd.incrementarSueldo(20);
			// Obtenemos de la BD el contenido de la tabla Empleado tras la modificacion
			// del sueldo
			ITable tablaEmpleadoObtenida = getTablaActual(url, user, password, schema, "empleado");
			// Cargamos los datos esperados del XML
			ITable tablaEmpleadoEsperada = getTablaEsperada("empleado", "src/salariosEmpleadosEsperados.xml");
			// Comprobamos que el contenido actual de la tabla Empleado en la BD coincide
			// con la
			// tabla esperada cargada en el XML
			Assertion.assertEquals(tablaEmpleadoEsperada, tablaEmpleadoObtenida);
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		} catch (ExcepcionDeAplicacion e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testEliminarCliente() {
		try {
			// Invoco al metodo eliminarCliente con objeto de eliminar al cliente con
			// identificador 'RMaC01'
			gbd.eliminarCliente("RMaC01");
			// Obtengo de la BD el contenido de la tabla Cliente resultante tras la
			// eliminacion del cliente anterior
			ITable tablaClienteObtenida = getTablaActual(url, user, password, schema, "cliente");
			// Cargo los datos de los clientes esperados tras la eliminacion del 'RMaC01',
			// desde el XML clientesEsperados.xml
			ITable tablaClienteEsperada = getTablaEsperada("cliente", "src/clientesEsperados.xml");
			// Compruebo que el contenido actual de la tabla Cliente en la BD coincide con
			// la informacion esperada cargada del XML
			Assertion.assertEquals(tablaClienteEsperada, tablaClienteObtenida);
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		} catch (ExcepcionDeAplicacion e) {
			e.printStackTrace();
		}
	}


}
