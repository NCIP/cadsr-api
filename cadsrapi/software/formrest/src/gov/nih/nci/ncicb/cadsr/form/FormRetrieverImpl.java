package gov.nih.nci.ncicb.cadsr.form;

import gov.nih.cadsr.transform.FilesTransformation;
import gov.nih.nci.cadsr.formloader.service.common.FormXMLConverter;
import gov.nih.nci.ncicb.cadsr.common.dto.FormTransferObject;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.jdbc.JDBCClassificationSchemeDAOV2;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.jdbc.JDBCContextDAOV2;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.jdbc.JDBCFormDAOV2;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.jdbc.JDBCProtocolDAOV2;
import gov.nih.nci.ncicb.cadsr.common.resource.Classification;
import gov.nih.nci.ncicb.cadsr.common.resource.Protocol;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.FormV2;
import gov.nih.nci.ncicb.cadsr.common.util.StringUtils;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.apache.cxf.helpers.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class FormRetrieverImpl implements FormRetriever{
	
	public FormRetrieverImpl() {}
	
	@Override
	public String getForm(String formPublicId) {
		return formPublicId;
	}
	
	@Override
	public Response getForm(	String formPublicId, 
								String formLongName, 
								String context,
								String classification,
								String protocol,
								String createdBy,
								String workFlowStatus,
								String registrationStatus,
								String format ) {
		String contextIdSeq = "";
		String version = "";
		String classificationIdSeq = "";
		String protocolIdSeq = "";
		Collection formCollection = null;
		
		boolean skip=false;
		if( skip ) {
			
			XMLSerializer xmlSerializer = new XMLSerializer();                 
			
			File f = new File("/local/content/transform/data/","CdBefYIp.txt");
			JSON json = xmlSerializer.readFromFile( f );  
			
			String jsonString = json.toString(2);
			
			File file = new File("/local/content/transform/data/","test.json");
			 
			FileWriter fw;
			try {
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
	 
				fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(jsonString);
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	        ResponseBuilder response = Response.ok(jsonString);
	        response.header("Content-Disposition", "attachment; filename=\"download.json\"");
	        return response.build();
		}
		
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext-service-db.xml");
		
		JDBCFormDAOV2 formDAO = (JDBCFormDAOV2)applicationContext.getBean("formV2Dao");
		
		if ( StringUtils.doesValueExist(context) ) {
			JDBCContextDAOV2 contextDAO = (JDBCContextDAOV2)applicationContext.getBean("contextV2Dao");
			StringTokenizer contexttokenizer = new StringTokenizer(context, ",");
			 
			StringBuffer contextBuffer = new StringBuffer();
			while (contexttokenizer.hasMoreElements()) {
				if( contextBuffer.length() > 0 )
					contextBuffer.append(",");
				
				String idSeq = ((Context)contextDAO.getContextByName((String)contexttokenizer.nextElement())).getConteIdseq();
				
				if ( StringUtils.doesValueExist(idSeq) )
					contextBuffer.append("'").append(idSeq).append("'");
			}

			//contextIdSeq = ((Context)contextDAO.getContextByName(context)).getConteIdseq();
			contextIdSeq = contextBuffer.toString();
			version = "latestVersion";
			
			if ( !StringUtils.doesValueExist(workFlowStatus) )
				workFlowStatus = "RELEASED";
		}
		
		if ( StringUtils.doesValueExist(classification) ) {
			JDBCClassificationSchemeDAOV2 classificationDAO = (JDBCClassificationSchemeDAOV2)applicationContext.getBean("classificationV2Dao");
			StringTokenizer classtokenizer = new StringTokenizer(classification, ",");
			 
			StringBuffer classBuffer = new StringBuffer();
			while (classtokenizer.hasMoreElements()) {
				if( classBuffer.length() > 0 )
					classBuffer.append(",");
				
				String idSeq = ((Classification)classificationDAO.getClassificationByName((String)classtokenizer.nextElement())).getCsIdseq();
				
				if ( StringUtils.doesValueExist(idSeq) )
					classBuffer.append("'").append(idSeq).append("'");
			}
			classificationIdSeq = classBuffer.toString();
		}
		
		if ( StringUtils.doesValueExist(protocol) ) {
			JDBCProtocolDAOV2 protocolDAO = (JDBCProtocolDAOV2)applicationContext.getBean("protocolV2Dao");
			StringTokenizer protocoltokenizer = new StringTokenizer(protocol, ",");
			 
			StringBuffer protocolBuffer = new StringBuffer();
			while (protocoltokenizer.hasMoreElements()) {
				if( protocolBuffer.length() > 0 )
					protocolBuffer.append(",");
				
				String idSeq = ((Protocol)protocolDAO.getProtocolByName((String)protocoltokenizer.nextElement())).getProtoIdseq();
				
				if ( StringUtils.doesValueExist(idSeq) )
					protocolBuffer.append("'").append(idSeq).append("'");
			}
			//protocolIdSeq = ((Protocol)protocolDAO.getProtocolByName(protocol)).getProtoIdseq();
			protocolIdSeq = protocolBuffer.toString();
		}
		
		if ( StringUtils.doesValueExist(classification) ) {
			formCollection = formDAO.getAllFormsForClassification(classificationIdSeq);
		}
		else {
			 formCollection = formDAO.getAllForms(formLongName, protocolIdSeq, contextIdSeq, workFlowStatus, "", "", classificationIdSeq, "", formPublicId, version, "", "", createdBy);
		}
		/* Retrieve One Form
		FormTransferObject formObject = ((FormTransferObject) ((ArrayList)formCollection).get(0));
		
		FormV2 form = formDAO.getFormDetailsV2(formObject.getFormIdseq());
		
		String xmlFile = "";
		try {
			xmlFile = FormXMLConverter.instance().convertFormToXML(form);
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
		End Retrieve One Form */
		
		StringBuffer xmlFileBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<formCollection>\n");
		//StringBuffer xmlFileBuffer = new StringBuffer("");
		for(Object formObject: formCollection) {
			FormV2 form = formDAO.getFormDetailsV2(((FormTransferObject)formObject).getFormIdseq());
			
			String xmlLocalFile = "";
			try {
				xmlLocalFile = FormXMLConverter.instance().convertFormToXML(form);
			}
			catch (Exception e) 
			{
				System.out.println(e);
			}
			
			xmlLocalFile = xmlLocalFile.substring(xmlLocalFile.indexOf('\n')+1);  // remove the first line (xml version)
			
			xmlFileBuffer.append(xmlLocalFile);
		}
		xmlFileBuffer.append("</formCollection>\n");
		
		if ( format.equals("XML")) {
			return Response.ok(xmlFileBuffer.toString()).header("Content-Disposition", "application/xml").build();
		}
		else if ( format.equals("CSV")) {
			String csvFile = FilesTransformation.transformFormToCSV(xmlFileBuffer.toString());
			return Response.ok(csvFile).header("Content-Disposition", "attachment; filename=download.csv").build();
			
/*			File f = new File("/local/content/transform/data/","CdBefYIp.txt");

	        ResponseBuilder response = Response.ok((Object) f);
	        response.type("application/csv");
	        response.header("Content-Disposition", "attachment; filename=\"download.csv\"");
	        return response.build();		*/
		}
		else {
			XMLSerializer xmlSerializer = new XMLSerializer();                 
			JSON json = xmlSerializer.read( xmlFileBuffer.toString() );  
			//return Response.ok(json.toString(2)).header("Content-Disposition", "application/json").build();
			
			ResponseBuilder response = Response.ok(json.toString(2));
	        response.header("Content-Disposition", "attachment; filename=\"download.json\"");
	        return response.build();
		}
		
		//return xmlFileBuffer.toString();

	}

}