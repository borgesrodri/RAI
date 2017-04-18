package modelo;

import java.io.File;

public class prueba {
public static void main(String[] args) {
	int i = 0;
	for (int a = 0; a < 100; a++) {
		String ruta = "";
		if(a<10){
			ruta = "C:/Users/borge/Desktop/clase/2016-2017/Segundo cuatri/rai/Motor/2010-documents.biased/0"+a;
		}else{
			ruta = "C:/Users/borge/Desktop/clase/2016-2017/Segundo cuatri/rai/Motor/2010-documents.biased/"+a;
		}
		File dir = new File(ruta);
		File[] archivos = dir.listFiles();
		i += archivos.length;
	}
		System.out.println(i);
	
	
}
}
