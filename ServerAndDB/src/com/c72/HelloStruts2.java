package com.c72;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class HelloStruts2 extends ActionSupport implements ModelDriven<String>{
	private String name;
	private File myFile;	//和jsp的名字一样 ,这三个my_*会被struts2拦截器自动装配
	private String myFileContentType;
	private String myFileFileName;
	private final String destPath="E:/FTPcache/";
	public String execute() {
		System.out.println("Hello Struts2");
		System.out.println("Name is "+name);
		return "success";
	}
	
	public String upload() {
		try{
	     	 System.out.println("Src File name: " + myFile);
	     	 System.out.println("Dst File name: " + myFileFileName);
	     	    	 
	     	 File destFile  = new File(destPath, myFileFileName);
	    	 FileUtils.copyFile(myFile, destFile);//复制文件
	  
	      }catch(IOException e){
	         e.printStackTrace();
	         return ERROR;
	      }
	      return SUCCESS;
	}

	@Override
	public String getModel() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getMyFileContentType() {
		return myFileContentType;
	}
	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}
	public String getMyFileFileName() {
		return myFileFileName;
	}
	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}
	
}
