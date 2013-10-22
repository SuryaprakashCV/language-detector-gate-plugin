package com.moonraft.textlytics.gate;
import java.util.ArrayList;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
import java.util.*; 
import gate.*; 
import gate.creole.*; 
import gate.creole.metadata.CreoleResource;
import gate.util.*; 
import static gate.Utils.*;
import java.io.*;
import java.util.Scanner;
import java.net.URL;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;

@CreoleResource(name = "LanguageDetector", 
	       comment = "Wrapper for http://code.google.com/p/language-detection/") 

public class LanguageDetector extends AbstractLanguageAnalyser implements ProcessingResource  {

	
	URL model;
	String strFileName = "";
	String strEncoding = "UTF-8";
	StringBuilder strBufFileContent = new StringBuilder();
		
	private static final long serialVersionUID = 1L;
	
	String inputASName;


	public String getInputASName() {
		return inputASName;
	}

	public void setInputASName(String inputASName) {
		this.inputASName = inputASName;
	}
	
	String annotationType;


	public String getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}

	public void setModel(URL model) {		
		this.model = model;		
	}
	public URL getModel() {
		return model;
	}
		
	public void execute()
	{
		
		AnnotationSet bindings = document.getAnnotations(inputASName);
		AnnotationSet conAnnots = bindings.get(annotationType);	  
		Annotation conAnn = conAnnots.iterator().next();
		try {
		String text = document.getContent().getContent(conAnnots.firstNode().getOffset(), conAnnots.lastNode().getOffset()).toString();
		if(text != null)
			conAnn.getFeatures().put("lang", detect(text));
		} catch (InvalidOffsetException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public  String detect(String text) throws LangDetectException {
	    Detector detector = DetectorFactory.create();
	    detector.append(text);
	    return detector.detect();
	}
	public ArrayList<Language> detectLangs(String text) throws LangDetectException {
	    Detector detector = DetectorFactory.create();
	    detector.append(text);
	    return detector.getProbabilities();
	}
	@Override
	public LanguageDetector init() throws ResourceInstantiationException {
		
		this.strFileName = model.toString().replaceAll(("(file:)"), "");
	    try
	    {
	    	DetectorFactory.loadProfile(strFileName);
	    }
		catch (Exception e) {
			// TODO: handle exception
		}
	return this;
	}
	@Override
	public void reInit() throws ResourceInstantiationException {
		
		init();
		
	}
}
