package model.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Myynti;

public class Dao {
	private Connection con=null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep=null; 
	private String sql;
	private String db ="Myynti.sqlite";
	
	private Connection yhdista(){
    	Connection con = null;    	
    	String path = System.getProperty("catalina.base");    	
    	path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); //Eclipsessa
    	//path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus epäonnistui.");
	        e.printStackTrace();	         
	     }
	     return con;
	}
	
	public ArrayList<Myynti> listaaKaikki(){
		ArrayList<Myynti> myynnit = new ArrayList<Myynti>();
		sql = "SELECT * FROM asiakkaat";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);        		
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui
					//con.close();					
					while(rs.next()){
						Myynti myynti = new Myynti();
						myynti.setTunniste(rs.getInt(1));
						myynti.setEtunimi(rs.getString(2));
						myynti.setSukunimi(rs.getString(3));
						myynti.setPuhelin(rs.getString(4));	
						myynti.setSposti(rs.getString(5));
						myynnit.add(myynti);
					}					
				}				
			}	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return myynnit;
	}
	
	public ArrayList<Myynti> listaaKaikki(String hakusana){
		ArrayList<Myynti> myynnit = new ArrayList<Myynti>();
		sql = "SELECT * FROM asiakkaat WHERE etunimi LIKE ? or sukunimi LIKE ? or puhelin LIKE ? or sposti LIKE ?";      
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, "%" + hakusana + "%");
				stmtPrep.setString(2, "%" + hakusana + "%");   
				stmtPrep.setString(3, "%" + hakusana + "%");
				stmtPrep.setString(4, "%" + hakusana + "%");
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui
					//con.close();					
					while(rs.next()){
						Myynti myynti = new Myynti();
						myynti.setTunniste(rs.getInt(1));
						myynti.setEtunimi(rs.getString(2));
						myynti.setSukunimi(rs.getString(3));
						myynti.setPuhelin(rs.getString(4));	
						myynti.setSposti(rs.getString(5));
						myynnit.add(myynti);
					}					
				}				
			}	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return myynnit;
	}
	public boolean lisaaMyynti(Myynti myynti){
		boolean paluuArvo=true;
		sql="INSERT INTO asiakkaat VALUES(?,?,?,?,?)";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setInt(1, myynti.getTunniste());
			stmtPrep.setString(2, myynti.getEtunimi());
			stmtPrep.setString(3, myynti.getSukunimi());
			stmtPrep.setString(4, myynti.getPuhelin());
			stmtPrep.setString(5, myynti.getSposti());
			stmtPrep.executeUpdate();
	        con.close();
		} catch (Exception e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
	
	public boolean poistaMyynti(String id){ //Oikeassa elämässä tiedot ensisijaisesti merkitään poistetuksi.
		boolean paluuArvo=true;
		sql="DELETE FROM asiakkaat WHERE asiakas_id=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setString(1, id);			
			stmtPrep.executeUpdate();
	        con.close();
		} catch (Exception e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
	
	public Myynti etsiMyynti(String tunniste) {
		Myynti myynti = null;
		sql = "SELECT * FROM asiakkaat WHERE asiakas_id=?";       
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setString(1, tunniste);
        		rs = stmtPrep.executeQuery();  
        		if(rs.isBeforeFirst()){ //jos kysely tuotti dataa, eli rekNo on käytössä
        			rs.next();
        			myynti = new Myynti();
        			myynti.setTunniste(rs.getInt(1));
        			myynti.setEtunimi(rs.getString(2));
					myynti.setSukunimi(rs.getString(3));
					myynti.setPuhelin(rs.getString(4));	
					myynti.setSposti(rs.getString(5));       			      			
				}        		
			}	
			con.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return myynti;		
	}
	
	public boolean muutaMyynti(Myynti myynti, String tunniste){
		boolean paluuArvo=true;
		sql="UPDATE asiakkaat SET asiakas_id=?, etunimi=?, sukunimi=?, puhelin=?, sposti=? WHERE asiakas_id=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setInt(1, myynti.getTunniste());
			stmtPrep.setString(2, myynti.getEtunimi());
			stmtPrep.setString(3, myynti.getSukunimi());
			stmtPrep.setString(4, myynti.getPuhelin());
			stmtPrep.setString(5, myynti.getSposti());
			stmtPrep.setString(6, tunniste);
			stmtPrep.executeUpdate();
	        con.close();
		} catch (Exception e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
	public boolean poistaKaikkiAutot(String pwd){ //Oikeassa elämässä tiedot ensisijaisesti merkitään poistetuksi.
		boolean paluuArvo=true;
		if(pwd!="nimda") {
			return false;
		}			
		sql="DELETE FROM asiakkaat";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);						
			stmtPrep.executeUpdate();
	        con.close();
		} catch (Exception e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}	
}
