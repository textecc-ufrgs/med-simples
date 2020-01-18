package com.tonyleiva.ufrgs.passport;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.FILE_PREFIX;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.FILE_FORMAT;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_PATH;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.tonyleiva.ufrgs.model.LemaWord;

public class PassportService {

	public static void main(String[] args) {
		PassportService p = new PassportService();
		p.getLemas("newFile");
//		p.getOutputStringFromJar("newFile");
	}

	public List<LemaWord> getLemas(String filename) {
		List<LemaWord> lemaWordList = new ArrayList<>();

		List<String> passportOutput = executePassport(filename);

		return lemaWordList;
	}

	/**
     * Executes the passport.jar tool over the file with name equals to {@code filename}
     * @param filename name of the file to be analyzed by passport tool
     * @return string list containing the output of passport.jar 
     */
    private List<String> executePassport(String filename) {
        List<String> passportOutput = new ArrayList<>();
        try {
    		String file = filename + FILE_FORMAT;//TODO String file = FILE_PREFIX + filename + FILE_FORMAT;

    		String command = "java -jar passport.jar passport.config files/" + file +" FORM;LEMMA;UPOS ";
    		File dir = new File(PASSPORT_PATH);
    		Process p = Runtime.getRuntime().exec(command, null, dir);
	        p.waitFor();
	
	        // Grab output and print to display
	        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.ISO_8859_1));
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	if (!line.startsWith("##"))
	        		passportOutput.add(line);
	        }
    	} catch (Exception e) {
			// TODO: handle exception
		}
        return passportOutput;
    }

    public String getOutputStringFromJar(String filename) {
    	try {
    		String file = filename + FILE_FORMAT;//TODO String file = FILE_PREFIX + filename + FILE_FORMAT;

    		String command = "java -jar passport.jar passport.config files/" + file +" FORM;LEMMA;UPOS ";
    		File dir = new File(PASSPORT_PATH);
//    		System.out.println(dir.isDirectory() ? "IF" : "ELSE");
//    		if (dir.isDirectory()) {
//    			for (String f : dir.list()) {
//    				System.out.println(f);
//    			}
//    		}
    		
    		
	    	Process p = Runtime.getRuntime().exec(command, null, dir);
	        p.waitFor();
	
	        // Grab output and print to display
	        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.ISO_8859_1));
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	if (!line.startsWith("##"))
	        		System.err.println("-" + line + "-");
	        }
	        
	        
	        
	        InputStream is = p.getInputStream();

	        String st = new String(is.readAllBytes(), StandardCharsets.ISO_8859_1);
	        String[] stA = st.split("\\n");
	        System.out.println("length: " + stA.length);

	        StringBuilder builder = new StringBuilder();
	        int i=0;
	        int b=0;
	        while((b = is.read()) >= 0 ) {
	        	builder.append((char) b);
//	        	System.out.print(Character.toString ((char) b));
	        	System.out.print(b +"."+ Character.toString ((char) b));
	        }
	        
//	        System.out.println(builder.toString());
    	} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	
    	String ret = ""; // or = null;
        return ret;
    }
}
