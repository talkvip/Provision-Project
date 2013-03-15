package com.cpn.os4j.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SerializedFile implements Serializable {

	private static final long serialVersionUID = 2904773678458586926L;
	String path;
	String contents;
	String postxfer;


	public SerializedFile() {
	}

	public SerializedFile(String aPath, String someData){
		this(aPath, someData.getBytes());
	}

	public SerializedFile(String aPath, byte[] someBytes) {
		path = aPath;
		contents = Base64.encodeBase64String(someBytes);
	}

	public SerializedFile(String aPath, byte[] someBytes, String aPostxfer) {
		this(aPath, someBytes);
		postxfer = aPostxfer;
	}

	public SerializedFile(String aPath, InputStream aStream) throws IOException {
		this(aPath, IOUtils.toByteArray(aStream));
	}

	public SerializedFile(String aPath, InputStream aStream, String aPostXfer) throws IOException {
		this(aPath, IOUtils.toByteArray(aStream), aPostXfer);
	}

	public SerializedFile(String aPath, File aFile) throws FileNotFoundException, IOException {
		this(aPath, new FileInputStream(aFile));
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("path", path).append("contents", contents).append("postxfer", postxfer);
		return builder.toString();
	}

	public String getPostxfer() {
		return postxfer;
	}

	public void setPostxfer(String postxfer) {
		this.postxfer = postxfer;
	}

}