package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		//conviene farlo una volta nel costruttore
		EsameDAO dao=new EsameDAO();
		this.partenza=dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale=new HashSet<Esame>();
		soluzioneMigliore=new HashSet<Esame>();
		mediaSoluzioneMigliore=0;
		//cerca(parziale, 0, numeroCrediti);
		cerca2(parziale, 0, numeroCrediti);
		return soluzioneMigliore;	
	}
	
	/*complessità 2^N  */
	private void cerca2(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
				int crediti=sommaCrediti(parziale);
				
				if(crediti>m) {
					return;
					//non faccio niente, torno indietro, questo
					//ramo lo lascio perdere
				}
				
				if(crediti==m) {
					double media=calcolaMedia(parziale);
					if(media>mediaSoluzioneMigliore) {
						soluzioneMigliore=new HashSet<>(parziale);
						mediaSoluzioneMigliore=media;
					}
					return;
				}
				
				//se arrivo qui, sicuramente crediti<m
				//posso andare avanti, a meno che non li 
				//abbia considerati tutti
				
				//raggiungiamo la fine dei livelli
				//ho meno crediti di quanti ne vorrei,
				//ma non posso neanche più aggiungere esami
				//poichè semplicemente non ne ho più
				if(L==partenza.size()) {
					return;
				}
				
				//generazione sottoproblemi
				//partenza[L] è da aggiungere oppure no?
				//provo entrambe le cose
				parziale.add(partenza.get(L));
				cerca2(parziale, L+1, m);
				parziale.remove(partenza.get(L));
				cerca2(parziale, L+1, m);
				//ho due chiamate della ricorsione
	}

	/* Complessità: N!  */
	private void cerca(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
		int crediti=sommaCrediti(parziale);
		
		if(crediti>m) {
			return;
			//non faccio niente, torno indietro, questo
			//ramo lo lascio perdere
		}
		
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new HashSet<>(parziale);
				mediaSoluzioneMigliore=media;
			}
			return;
		}
		
		//se arrivo qui, sicuramente crediti<m
		//posso andare avanti, a meno che non li 
		//abbia considerati tutti
		
		//raggiungiamo la fine dei livelli
		//ho meno crediti di quanti ne vorrei,
		//ma non posso neanche più aggiungere esami
		//poichè semplicemente non ne ho più
		if(L==partenza.size()) {
			return;
		}
		
		for(Esame e: partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale, L+1, m);
				parziale.remove(e);
			}
		}
		
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
