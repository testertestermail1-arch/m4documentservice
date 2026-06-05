package com.twg.api.experience_forms_api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

public class MergePDF {
	//Converting list of byte array to single byte array
	public byte[] mergePDF(List<byte[]> listOfByteArray) throws IOException{
		 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    Document doc = null;
		    PdfCopy writer = null;

		    for (byte[] byteArray : listOfByteArray) {

		        try {
		            PdfReader reader = new PdfReader(byteArray);
		            int totalPages = reader.getNumberOfPages();
		            //Check doc is null -To avoid overlapping
		            if (doc == null) {
		            	doc = new Document(reader.getPageSizeWithRotation(1));
		                writer = new PdfCopy(doc, outputStream);
		                doc.open();
		            }
		            PdfImportedPage page;
		            for (int i = 0; i < totalPages;) {
		                ++i;
		                page = writer.getImportedPage(reader, i);
		                writer.addPage(page);
		            }
		        }

		        catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		    doc.close();
		    outputStream.close();
		    return outputStream.toByteArray();
	}
}
