/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.karaofix;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author japar
 */
public class Fixer {


	public static void main(String[] args) {

		/* Copiar directorios y archivos */
		File dir = new File(".\\src\\main\\resources");
		CopyOption[] options = new CopyOption[] {
  		      StandardCopyOption.REPLACE_EXISTING,
  		      StandardCopyOption.COPY_ATTRIBUTES
  		    }; 
        
		for (File numberName : new File(dir, "temazos").listFiles()) {
        	
            String name = numberName.getName();
            File dst = new File(dir, "grandesexistos/" + name);
            dst.mkdirs();
            
            for (File src : numberName.listFiles()) {
            	File file = new File(dst, src.getName());
            	
            	if (!src.getName().endsWith(".txt")) {
                	try {
						Files.copy(src.toPath(), file.toPath(), options);
					} catch (IOException e) {
						System.out.println("Error al copiar archivos");
						e.printStackTrace();
					}
            	}
            	else {
            		/* Lectura de fichero y copia en otro */
            		
            		Path original = src.toPath();
            		Path copia = file.toPath();
            		
            		Charset charset = Charset.forName("ISO-8859-15");
            		String line = null;
            		
            		try (BufferedReader reader = Files.newBufferedReader(original, charset); PrintWriter writer = new PrintWriter(Files.newBufferedWriter(copia))) {
            			line = reader.readLine();
            			while (line != null) {
            				if (line.startsWith("#BPM:") || line.startsWith("#GAP:")) {
            					String inicio = line.substring(0, 5);
            					String num = line.substring(5);
            					String[] partes = num.split(",");
            					
            					int entero = Integer.parseInt(partes[0]);
            					
            					if (partes[1].startsWith("5") || partes[1].startsWith("6") ||partes[1].startsWith("7") ||partes[1].startsWith("8") ||partes[1].startsWith("9")) {
            						entero ++;
            					}
            					
            					line = inicio + entero;
            				}
            				
            				writer.println(line);
            				line=reader.readLine();
            			}
            		} catch (IOException x) {
            			System.out.println("Error al leer");
            			x.printStackTrace();
            		}
            	}	
            } 
        }	
					
	}
}
