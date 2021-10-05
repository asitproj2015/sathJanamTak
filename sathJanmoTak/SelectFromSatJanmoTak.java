//SelectFromSatJanmoTak.java
package com.nt.jdbc.project;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//JDBC Application for retrieving the customer data from DB 
public class SelectFromSatJanmoTak {
	private static final String SELECT_CUSTOMER_INFO="SELECT CID,FIRSTNAME,LASTNAME,GENDER,DOB,DOJ,PHOTO,RESUME,AUDIO,VEDIO,BIODATA FROM CUSTOMER_INFO WHERE CID=?";
	private static Logger logger=Logger.getLogger(SelectFromSatJanmoTak.class);
	//static method to create logger file
	static {
		try {
			PropertyConfigurator.configure("src/com/nt/jdbc/propertyconfegurator/log4j.properties");
			logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak :: Property file confegured and Logger file created");
		} catch (Exception e) {
			logger.fatal("com.nt.jdbc.project.SelectFromSatJanmoTak :: There is some problem in creating the propery file");
			e.printStackTrace();
		}//catch
	}//static
	public static void main(String[] args) {
		logger.info("com.nt.jdbc.project.SelectFromSatJanmoTak :: Main Method executed");
		try(Scanner scn= new Scanner(System.in)){
			//Reading the value for the cid
			System.out.print("Enter Your Account ID : ");
			int cid=scn.nextInt();
			//establishing the connection to the Oracle
			try(Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","PAPU","PAPU");
					PreparedStatement ps=con.prepareStatement(SELECT_CUSTOMER_INFO)) {
				//Setting the value to pre-compiled queery
				if (con!=null && ps!=null) {
					logger.info("com.nt.jdbc.project.SelectFromSatJanmoTak ::  Connection established to oracle");
					logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak ::  Query compiled nd ready to set the parameter");
					ps.setInt(1, cid);
					logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak ::  Inpu value set to thepre-compiled query");
				}//if
				//Execute the query with value
				try(ResultSet rs=ps.executeQuery()){
					if (rs!=null) {
						
						//Process the resultSet Object
						
						logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak ::  ResultSet Object created");
						java.sql.Date sdob,sdoj=null;
						SimpleDateFormat sdf= new SimpleDateFormat("dd-MMM-yyyy");
						if (rs.next()) {
							cid=rs.getInt(1);
							String firstName=rs.getString(2);
							String lastName=rs.getString(3);
							String gender=rs.getString(4);
							logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak :: All normal value like integer and string value extracted");
							
							//process the Date value
							
							sdob=rs.getDate(5);
							String dob=sdf.format(sdob);
							sdoj=rs.getDate(6);
							String doj=sdf.format(sdoj);
							logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak :: All date value retrive");
							//Displaying the Data
							System.out.println("ID\t\t\t\t: "+cid);
							System.out.println("Name\t\t\t\t: "+firstName+" "+lastName);
							System.out.println("gender\t\t\t\t: "+gender);
							System.out.println("Date of birth\t\t\t: "+dob);
							System.out.println("Date of join in first job\t:"+doj);
							logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak :: Required value displayed");
							
							//process the BLOB and CLOB value
							//create the InputStream and Reader to get the BLOB and CLOB value respectively
							try(InputStream photoI=rs.getBinaryStream(7);
									InputStream resumeI=rs.getBinaryStream(8);
									InputStream audioI=rs.getBinaryStream(9);	
									InputStream vedioI=rs.getBinaryStream(10);
									Reader biodataR=rs.getCharacterStream(11);
									// Creating the OutputStream for pointing to the destination file
									OutputStream photo=new FileOutputStream("D:\\retive_data\\"+firstName+".jpg");
									OutputStream audio=new FileOutputStream("D:\\retive_data\\"+firstName+"_fev_song.mp3");
									OutputStream vedio=new FileOutputStream("D:\\retive_data\\"+firstName+"_fev_vedio.mp4");
									OutputStream resume=new FileOutputStream("D:\\retive_data\\"+firstName+"resume.pdf");
									Writer biodata=new FileWriter("D:\\retive_data\\"+firstName+"_biodata.txt")){
								
								logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak :: All Blob And CLOB value ready to process");
								//Copy the input value to he Destination Location
								IOUtils.copy(photoI,photo);
								IOUtils.copy(vedioI, vedio);
								IOUtils.copy(audioI, audio);
								IOUtils.copy(resumeI, resume);
								IOUtils.copy(biodataR, biodata);
								logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak :: All Clob and BLOB value are store in the select folder");
								System.out.println("\nYour Data is restored");
							}//try//all streams
						}//if
						else {
							System.out.println("The ID u have entered is invalid");
							logger.debug("com.nt.jdbc.project.SelectFromSatJanmoTak :: The input data CID is incorret");
						}//else
					}//if
				}//try for result set object
			}//try connection
		}//try for scanner
		catch (SQLException se) {
			logger.error("com.nt.jdbc.project.SelectFromSatJanmoTak :: there is a SQL Excwption with message"+se.getMessage()+" with code"+se.getErrorCode());
			se.printStackTrace();
		}//catch
		catch (Exception e) {
			logger.fatal("com.nt.jdbc.project.SelectFromSatJanmoTak :: there a some Exceptions with message"+e.getMessage());
		}//catch
	}//main
}//class
