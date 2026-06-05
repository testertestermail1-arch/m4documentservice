package com.twg.api.experience_forms_api.response;

public class FileAlreadyExist extends Exception {

	private static final long serialVersionUID = 1L;

	public FileAlreadyExist() {
		super(String.format("File already exists"));
	}
}