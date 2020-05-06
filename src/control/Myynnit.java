package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import model.Myynti;
import model.dao.Dao;


@WebServlet("/myynnit/*")
public class Myynnit extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
   
    public Myynnit() {
        super();
        System.out.println("Myynnit.Myynnit()");
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynnit.doGet()");
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /audi			
		System.out.println("polku: "+pathInfo);		
		Dao dao = new Dao();
		ArrayList<Myynti> myynnit;
		String strJSON="";
		if(pathInfo==null) { //Haetaan kaikki autot 
			myynnit = dao.listaaKaikki();
			strJSON = new JSONObject().put("myynnit", myynnit).toString();	
		}else if(pathInfo.indexOf("haeyksi")!=-1) {		//polussa on sana "haeyksi", eli haetaan yhden auton tiedot
			String tunniste = pathInfo.replace("/haeyksi/", ""); //poistetaan polusta "/haeyksi/", j‰ljelle j‰‰ rekno		
			Myynti myynti = dao.etsiMyynti(tunniste);
			
			if(myynti==null) {
				strJSON = "{}";
			}else {
			JSONObject JSON = new JSONObject();
			JSON.put("tunniste", myynti.getTunniste());
			JSON.put("etunimi", myynti.getEtunimi());
			JSON.put("sukunimi", myynti.getSukunimi());
			JSON.put("puhelin", myynti.getPuhelin());
			JSON.put("email", myynti.getSposti());
			strJSON = JSON.toString();
			}
		}else{ //Haetaan hakusanan mukaiset autot
			String hakusana = pathInfo.replace("/", "");
			myynnit = dao.listaaKaikki(hakusana);
			strJSON = new JSONObject().put("myynnit", myynnit).toString();	
		}	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynnit.doPost()");
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi			
		Myynti myynti = new Myynti();
		myynti.setTunniste(jsonObj.getInt("tunniste"));
		myynti.setEtunimi(jsonObj.getString("etunimi"));
		myynti.setSukunimi(jsonObj.getString("sukunimi"));
		myynti.setPuhelin(jsonObj.getString("puhelin"));
		myynti.setSposti(jsonObj.getString("email"));
		
		System.out.println("Tahan asti onnistui");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.lisaaMyynti(myynti)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Asiakkaan lis‰‰minen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Asiakkaan lis‰‰minen ep‰onnistui {"response":0}
		}		
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynnit.doPut()");
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi			
		String vanhatunniste = jsonObj.getString("vanhatunniste");
		Myynti myynti = new Myynti();
		myynti.setTunniste(jsonObj.getInt("tunniste"));
		myynti.setEtunimi(jsonObj.getString("etunimi"));
		myynti.setSukunimi(jsonObj.getString("sukunimi"));
		myynti.setPuhelin(jsonObj.getString("puhelin"));
		myynti.setSposti(jsonObj.getString("email"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.muutaMyynti(myynti, vanhatunniste)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Auton muuttaminen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Auton muuttaminen ep‰onnistui {"response":0}
		}		
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Myynnit.doDelete()");	
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /ABC-222		
		System.out.println("polku: "+pathInfo);
		String poistettavaTunniste = pathInfo.replace("/", "");		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		int id = 0;
		Dao dao = new Dao();			
		if(dao.poistaMyynti(poistettavaTunniste)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Asiakkaan poistaminen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Asiakkaan poistaminen ep‰onnistui {"response":0}
		}	
	}

}
