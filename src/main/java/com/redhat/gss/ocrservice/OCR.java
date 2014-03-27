
package com.redhat.gss.ocrservice;

import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.Map;


import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;



@Path("/")
public class OCR {
    private static final String UPLOADED_FILE_PATH = "/tmp/";
	@Inject
    OCRService oCRService;

    @GET
    @Path("/json")
    @Produces({ "application/json" })
    public String getHelloWorldJSON() {
        return "{\"result\":\"" + oCRService.createHelloMessage("World") + "\"}";
    }

    @GET
    @Path("/xml")
    @Produces({ "application/xml" })
    public String getHelloWorldXML() {
        return "<xml><result>" + oCRService.createHelloMessage("World") + "</result></xml>";
    }

	
    @POST
	@Path("/image-upload")
	@Consumes("*/*")
	public Response uploadFile(MultipartFormDataInput input) throws IOException {
        OCRService os = new OCRService();
    	String result = "";
		//Get API input data
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		
		//Get file name
		String fileName = uploadForm.get("fileName").get(0).getBodyAsString();
		
		System.out.println("Client is going to upload file " + fileName);
		
		//Get file data to save
		List<InputPart> inputParts = uploadForm.get("image");

		for (InputPart inputPart : inputParts)
		{
			try 
			{
				//Use this header for extra processing if required
				@SuppressWarnings("unused")
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				
				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				
				byte[] bytes = IOUtils.toByteArray(inputStream);
				// constructs upload file path
				fileName = UPLOADED_FILE_PATH + fileName;
				writeFile(bytes, fileName);
				System.out.println("File " + fileName + " uploaded successfully!");
				
				File file = new File(fileName);
				
				result = os.processImageFile(file);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return Response.status(200).entity(result).build();
	}

	//Utility method
	private void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
	}
 
  
}
