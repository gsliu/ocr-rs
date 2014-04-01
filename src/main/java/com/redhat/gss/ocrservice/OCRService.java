package com.redhat.gss.ocrservice;

import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class OCRService {


    
    String processImageFile(File file) throws TesseractException {
    	String result;
	    Tesseract instance = Tesseract.getInstance(); 
	    // JNA Interface Mapping
	    instance.setDatapath("/usr/share/tessdata");
	    instance.setLanguage("rhl");
	   
	    result = instance.doOCR(file);
	    System.out.println(result);
    	return result;
    }

}
