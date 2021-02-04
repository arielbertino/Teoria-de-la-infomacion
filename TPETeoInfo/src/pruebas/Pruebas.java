package pruebas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Pruebas {

	public static void main(String[] args) {
		File f = new File("C:", "test.bin");
		/*
		try {
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			for(int i = 0; i<200; i++) {
				byte num = 
				Byte.parseByte(Integer.toString((int) (Math.random()*256 - 128)));
				fos.write(num);
				System.out.print(Integer.toHexString(Byte.toUnsignedInt(num))+" ");
				if((i % 4) == 3)
					System.out.println();
			}
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		try {
			FileInputStream fis = new FileInputStream(f);
			byte[] intbuffer = new byte[4];
//			fis.read(intbuffer);
			int i = 0;
//			byte b;
//			intbuffer[0] = (byte) fis.read();
//			intbuffer[1] = (byte) fis.read();
//			intbuffer[2] = (byte) fis.read();
//			intbuffer[3] = (byte) fis.read();
			while( fis.read(intbuffer) != -1) {
//				b = (byte) fis.read();
				for(byte b : intbuffer) {
				System.out.print(Integer.toHexString(Byte.toUnsignedInt(b))+ " ");
				}
//				if((i%4)==3)
					System.out.println();
//				i++;
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
