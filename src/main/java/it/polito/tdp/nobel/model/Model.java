package it.polito.tdp.nobel.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;
import it.polito.tdp.nobel.db.PopulateDB;

public class Model {

	private double max;
	private EsameDAO esameDAO;
	private int numeroCrediti;
	private Set<Esame> risultato;
	
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		max=0.00;
		esameDAO=new EsameDAO();
		this.numeroCrediti=numeroCrediti;
		PopulateDB.main(null);
		List<Esame> parziale=new ArrayList<Esame>();
		List<Esame> tutti=new ArrayList<Esame>(this.getTuttiEsami());
		esplora(tutti, parziale, 0);
		
	//	System.out.println("TODO!");
		return this.risultato;	
		
	}

	
	private void esplora(List<Esame> tutti, List<Esame> parziale, int livello) {
		if(sommaCrediti(new HashSet<Esame>(parziale))==numeroCrediti) {
			double a=calcolaMedia(new HashSet<Esame>(parziale));
			if(a>max) {
				risultato=new HashSet<Esame>(parziale);
				max=a;
			}
			
		}
		else {
			for(int i=0; i<tutti.size(); i++) {
				parziale.add(tutti.get(i));
				tutti.remove(i);
				if(sommaCrediti(new HashSet<Esame>(parziale))<= numeroCrediti) {
				esplora(tutti, parziale, livello+1);
				}
				tutti.add(i, parziale.get(livello));
				parziale.remove(livello);
				
			}
		}
	}
	
	private List<Esame> getTuttiEsami(){
		return this.esameDAO.getTuttiEsami();
	}
	
	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
