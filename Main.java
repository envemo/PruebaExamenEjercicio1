import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
	
	public static void main (String [] args) 
	{
		
		//1 INICIALIZAMOS LAS CLASES/VARIABLES

		Programa p1, p2, p3, pTOTAL;
		
		p1 = new Programa();
		p2 = new Programa();
		p3 = new Programa();
		pTOTAL = new Programa();
		
		String a1, a2, a3, aTOTAL;
		
		a1 = "USUARIOS_1.txt";
		a2 = "USUARIOS_2.txt";
		a3 = "Usuarios_3.txt";
		aTOTAL = "USUARIOS.txt";
		
		//2 SE LEEN TODOS LOS ARCHIVOS
		
		pTOTAL.lista.addAll(p1.leerArchivo(a1));
		pTOTAL.lista.addAll(p2.leerArchivo(a2));
		pTOTAL.lista.addAll(p3.leerArchivo(a3));
		
		//3 LEEMOS CADA UNO DE LOS USUARIOS
		
		pTOTAL.usuarios = pTOTAL.leerUsuarios(pTOTAL.lista);		
		pTOTAL.lista = pTOTAL.unificarLista(pTOTAL.usuarios);
				
		//4 ESCRIBIMOS LOS ARCHIVOS
		
		pTOTAL.escribirArchivo(aTOTAL, pTOTAL.lista);
		
	}

}

class Programa implements LeerArchivos, LeerUsuarios, UnificarListas, EscribirArchivos
{
	
	List <String> lista;
	List <Usuario> usuarios;
	
	public Programa() {
		lista = new ArrayList<String>();
		usuarios = new ArrayList<Usuario>();
	}
	
	@Override
	public void escribirArchivo(String nombreArchivo, List<String> lista) {
		Path file = Paths.get(nombreArchivo + LocalDate.now() + ".txt");
		try {
			List<String> lineas = new ArrayList<String>();
			lineas.add("Nombre		Nacimiento	País			Registro	Login		  Compras");
			lineas.addAll(lista);
			Files.write(file, lineas, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<String> unificarLista(List<Usuario> usuarios) {
		Set<Usuario> hashSet = new HashSet<Usuario>();
		for (Usuario usuario : usuarios) {
			hashSet.add(usuario);
		}
		List<String> lista = new ArrayList<String>();
		for (Usuario usuario : hashSet) {
			try {
				int n = Integer.valueOf(usuario.compras);
				System.out.println();
				System.out.print(usuario.nombre + ": ");
				if (n > 11) {
					System.out.print("Premier, ");
				} else if (n > 5) {
					System.out.print("Affiliate, ");
				} else if (n > 1) {
					System.out.print("Standar User, ");
				}
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate localDateNacimiento = LocalDate.parse(usuario.nacimiento, formatter);
				LocalDate localDateRegistro = LocalDate.parse(usuario.fechaRegistro, formatter);
				LocalDate localDateLogin = LocalDate.parse(usuario.fechaRegistro, formatter);
				LocalDate localDateHoy = LocalDate.now();
				long diff = localDateHoy.toEpochDay() - localDateRegistro.toEpochDay();
				diff/=365;
				System.out.print(diff + " años de antiguedad, ");
				diff = localDateLogin.toEpochDay() - localDateRegistro.toEpochDay();
				diff/=365;
				if (diff > 2) {
					System.out.print("Activo, ");
				} else {
					System.out.print("Inactivo, ");
				}
				diff = localDateHoy.toEpochDay() - localDateNacimiento.toEpochDay();
				diff/=365;
				System.out.print(diff + " años.");
				lista.add(usuario.toString());
			} catch (NumberFormatException e) {};
		}
		return lista;
	}
	@Override
	public List<Usuario> leerUsuarios(List<String> lista) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		for (String string : lista) {
			string = string.replace("\t", "---");
			string = string.replace("------   ", "---");
			string = string.replace("------  ", "---");
			string = string.replace("---------", "---");
			string = string.replace("------ ", "---");
			string = string.replace(" ------", "---");
			string = string.replace("------", "---");
			string = string.replace("  ---", "---");
			string = string.replace("------", "---");
			string = string.replace("------", "---");
			string = string.replace("   ---", "---");
			string = string.replace("---  ", "---");
			String[] strings = string.split("---");
			Usuario usuario = new Usuario();
			usuario.nombre = strings[0];
			usuario.nacimiento = strings[1];
			usuario.pais = strings[5];
			usuario.fechaRegistro =  strings[2];
			usuario.fechaLogin = strings[3];
			usuario.compras = strings[4];
			usuarios.add(usuario);
		}
		return usuarios;
	}
	@Override
	public List<String> leerArchivo(String nombreArchivo) {
		List<String> lineas = new ArrayList<String>();
		File documento = new File(nombreArchivo);
		try {
			Scanner sc = new Scanner(documento);
			while (sc.hasNextLine())
			{
				lineas.add(sc.nextLine());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lineas;
	}
	
}

interface LeerArchivos 
{
	public List<String> leerArchivo(String nombreArchivo);
}

interface LeerUsuarios 
{
	public List<Usuario> leerUsuarios(List<String> lista);
}

interface UnificarListas
{
	public List<String> unificarLista(List<Usuario> usuarios);
}

interface EscribirArchivos
{
	public void escribirArchivo(String nombreArchivo, List<String> lista);
}

class Usuario extends Persona
{
	String fechaRegistro;
	String fechaLogin;
	String compras;
	
	@Override
	public String toString() {
		return super.nombre + "		" + super.nacimiento + " 	" + super.pais + " 		" + fechaRegistro + " 	" + fechaLogin + "    " + compras;
	}
}

class Persona
{
	String nombre;
	String nacimiento;
	String pais;
}
