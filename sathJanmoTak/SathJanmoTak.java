//SathJanmoTak.java

/* Customer details
 
   First Name
   Last Name
   Gender
   DOB
   DOJ of first job
   Photo
   Resume
   Audio_info
   Vedio_info
   Biodata
 */
package com.nt.jdbc.project;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//JDBC application for Inserting the customer details of matrimonial site  into the Oracle DB
public class SathJanmoTak {
	private static final String INSERT_QUERY="INSERT INTO CUSTOMER_INFO VALUES(MRGID.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";
	private static Logger logger=Logger.getLogger("SathJanmoTak.class");
	//static block for initialize the property configurator
	static {
		try {
			PropertyConfigurator.configure("src/com/nt/jdbc/propertyconfegurator/log4j.properties");
			logger.debug("com.nt.jdbc.project.SathJanmoTak :: Logger file creaed");
		} catch (Exception e) {
			logger.fatal("com.nt.jdbc.project.SathJanmoTak :: There is some  problem in creting the Loger file");
			e.printStackTrace();
		}//catch
	}//static
	public static void main(String[] args) {
		try(Scanner scn=new Scanner(System.in);) {
			logger.debug("com.nt.jdbc.project.SathJanmoTak :: Scanner Object created");
			//Reading the customer details
			String first_name=null,last_name=null,gender=null,dob=null,doj_fj=null,photoLocation=null,
					resumeLocation=null,audioLocation=null,vedioLocation=null,biodataLocation=null;
			if (scn!=null) {
				System.out.print("Enter the First Name : ");
				first_name=scn.next();
				System.out.print("Enter the Last Name : ");
				last_name=scn.next();
				System.out.print("Enter the Gender : ");
				gender=scn.next();
				System.out.print("Enter the Date Of Bith  in dd-MMM-yyyy: ");
				dob=scn.next();
				System.out.print("Enter the Date Of Join In first job in dd-MM-yyyy : ");
				doj_fj=scn.next();
				System.out.print("Enter the your Photo Loction : ");
				photoLocation=scn.next();
				System.out.print("Enter the your Resume Location : ");
				resumeLocation=scn.next();
				System.out.print("Enter the Favorite song Location : ");
				audioLocation=scn.next();
				System.out.print("Enter your favorite vedio Location : ");
			    vedioLocation=scn.next();
			    System.out.print("Enter enter your biodata location  : ");
				biodataLocation=scn.next();
			}//if
			logger.debug("com.nt.jdbc.project.SathJanmoTak :: All required input taken from end user");
			//Converting  the String date value to the java.sql.date format for DOB
			SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yyyy");
			java.sql.Date sdob=new java.sql.Date(sdf1.parse(dob).getTime());
			//Converting the string date to the java.sql.date format for date of join
			SimpleDateFormat sdf2=new SimpleDateFormat("dd-MM-yyyy");
			java.sql.Date sdoj=new java.sql.Date(sdf2.parse(doj_fj).getTime());
			logger.debug("com.nt.jdbc.project.SathJanmoTak :: Dates are converted to SQL dates");
			//creating the InputStream object for BLOB and CLOB value
			try(InputStream photo=new FileInputStream(photoLocation);
					InputStream resume=new FileInputStream(resumeLocation);
					InputStream audio=new FileInputStream(audioLocation);
					InputStream vedio=new FileInputStream(vedioLocation);
					Reader biodata=new FileReader(biodataLocation);){
				logger.debug("com.nt.jdbc.project.SathJanmoTak :: Binary object for Photo,Resume,Vedio and Audio is creaed");
				logger.debug("com.nt.jdbc.project.SathJanmoTak :: Character object for biodata is created");
				//Establishing the connection and creating the prepared statement object
				try(Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","PAPU","PAPU");
						PreparedStatement ps=con.prepareStatement(INSERT_QUERY)){
					//set the value to the pre-compiled query
					if (con!=null && ps!=null) {
						logger.info("com.nt.jdbc.project.SathJanmoTak :: Connection established to the Oacle");
						logger.debug("com.nt.jdbc.project.SathJanmoTak :: PreparedStatement object created");
						////set the value to the pre-compiled query
						ps.setString(1, first_name);
						ps.setString(2, last_name);
						ps.setString(3, gender);
					
						ps.setDate(4, sdob);
						ps.setDate(5, sdoj);
					
						ps.setBinaryStream(6, photo);
						ps.setBinaryStream(7, resume);
						ps.setBinaryStream(8, audio);
						ps.setBinaryStream(9, vedio);
					
						ps.setCharacterStream(10, biodata);
					}//if
					logger.debug("com.nt.jdbc.project.SathJanmoTak :: all parametr set to the pre-compiled query");
					//Execute the Pre-compiled query with Value
					int count=0;
					if (ps!=null) {
						count=ps.executeUpdate();
						logger.debug("com.nt.jdbc.project.SathJanmoTak :: SQLquery executed");
					}//if
					if (count==0) {
						System.out.println(" Data didn't Inserted");
						logger.debug("com.nt.jdbc.project.SathJanmoTak :: Data didn't inseerted to Oracle");
					}//if
					else {
						System.out.println(" Data inserted");
						logger.debug("com.nt.jdbc.project.SathJanmoTak :: Data inserted to oracle");
					}//else
				}// try for connection ans prepared statement
			}//try for BLOB and CLOB object
		}//try for Scanner
		catch (SQLException se) {
			logger.error("com.nt.jdbc.project.SathJanmoTak :: There is a known exception with message"+se.getMessage()+" with code"+se.getErrorCode());
			se.printStackTrace();
		}
		catch (Exception e) {
			logger.fatal("com.nt.jdbc.project.SathJanmoTak ::  here is some unknown exception with message"+e.getMessage());
			e.printStackTrace();
		}//catch
	}//main
}//class