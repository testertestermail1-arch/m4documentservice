package com.twg.api.experience_forms_api.response;

import java.util.Set;

import com.twg.api.experience_forms_api.document.Pdf;
import com.twg.api.experience_forms_api.request.KeyValuePair;

/**
 * 
 * @author sundaramurthyg
 *
 */
public class DocumentResponse {

	private Status status;
	private Set<Pdf> pdfs;
	private Set<KeyValuePair> fillers;
	
	/**
	 * Creates the response.<br>
	 * This method may be required by the services framework.
	 */
	public DocumentResponse() {}
	
	/**
	 * Creates a response with errors.
	 * @param errors the errors.
	 */
	public DocumentResponse(Set<Error> errors) {
		this.status = new Status(errors);
	}
	
	/**
	 * Creates a successful response.
	 * @param pdfs the form's pdfs.
	 * @param dummyParam a dummy param for differentiation this constructor from the one with errors.
	 */
	public DocumentResponse(Set<Pdf> pdfs, boolean dummyParam) {
		this.pdfs = pdfs;
		this.status = new Status();
	}
	
}
