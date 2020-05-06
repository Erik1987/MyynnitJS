<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<title>Insert title here</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
<style>

table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}

.oikealle{
	text-align: right;
}
</style>
</head>
<body onkeydown="tutkiKey(event)">
<table id="listaus">
	<thead>	
		<tr>
			<th colspan="4" id="ilmo"></th>
			<th><a id="uusiMyynti" href="lisaamyynti.jsp">Lisää uusi asiakas</a></th>
		</tr>	
		<tr>
			<th class="oikealle">Hakusana:</th>
			<th colspan="3"><input type="text" id="hakusana"></th>
			<th><input type="button" value="hae" id="hakunappi" onclick="haeTiedot()"></th>
		</tr>			
		<tr>
			<th>Tunniste</th>
			<th>Etunimi</th>
			<th>Sukunimi</th>
			<th>Puhelin</th>
			<th>Email</th>
			<th></th>							
		</tr>
	</thead>
	<tbody id="tbody">
	</tbody>
</table>
<script>

haeTiedot();	
document.getElementById("hakusana").focus();//viedään kursori hakusana-kenttään sivun latauksen yhteydessä

function tutkiKey(event){
	if(event.keyCode==13){//Enter
		haeTiedot();
	}		
}
//Funktio tietojen hakemista varten
//GET   /autot/{hakusana}
function haeTiedot(){	
	document.getElementById("tbody").innerHTML = "";
	fetch("myynnit/" + document.getElementById("hakusana").value,{//Lähetetään kutsu backendiin
	      method: 'GET'
	    })
	.then(function (response) {//Odotetaan vastausta ja muutetaan JSON-vastaus objektiksi
		return response.json()	
	})
	.then(function (responseJson) {//Otetaan vastaan objekti responseJson-parametrissä		
		var myynnit = responseJson.myynnit;	
		var htmlStr="";
		for(var i=0;i<myynnit.length;i++){			
        	htmlStr+="<tr>";
        	htmlStr+="<td>"+myynnit[i].tunniste+"</td>";
        	htmlStr+="<td>"+myynnit[i].etunimi+"</td>";
        	htmlStr+="<td>"+myynnit[i].sukunimi+"</td>";
        	htmlStr+="<td>"+myynnit[i].puhelin+"</td>";
        	htmlStr+="<td>"+myynnit[i].sposti+"</td>";
        	htmlStr+="<td><a href='muutamyynti.jsp?tunniste="+myynnit[i].tunniste+"'>Muuta</a>&nbsp;";
        	htmlStr+="<span class='poista' onclick=poista('"+myynnit[i].tunniste+"')>Poista</span></td>";
        	
        	htmlStr+="</tr>";        	
		}
		document.getElementById("tbody").innerHTML = htmlStr;		
	})	
}

//Funktio tietojen poistamista varten. Kutsutaan backin DELETE-metodia ja välitetään poistettavan tiedon id. 
//DELETE /myynnit/id
function poista(tunniste){
	if(confirm("Poista myynti " + tunniste +"?")){	
		fetch("myynnit/"+ tunniste,{//Lähetetään kutsu backendiin
		      method: 'DELETE'		      	      
		    })
		.then(function (response) {//Odotetaan vastausta ja muutetaan JSON-vastaus objektiksi
			return response.json()
		})
		.then(function (responseJson) {//Otetaan vastaan objekti responseJson-parametrissä		
			var vastaus = responseJson.response;		
			if(vastaus==0){
				document.getElementById("ilmo").innerHTML= "Asiakkaan poisto epäonnistui.";
	        }else if(vastaus==1){	        	
	        	document.getElementById("ilmo").innerHTML="Asiakkaan " + tunniste +" poisto onnistui.";
				haeTiedot();        	
			}	
			setTimeout(function(){ document.getElementById("ilmo").innerHTML=""; }, 5000);
		})		
	}	
}
</script>
</body>
</html>