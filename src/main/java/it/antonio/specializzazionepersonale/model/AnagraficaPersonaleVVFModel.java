package it.antonio.specializzazionepersonale.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;


public class AnagraficaPersonaleVVFModel {
	
		@Field("_id")
	  	private ObjectId _id;
		public String qualifica;
	    public String cognome;
	    public String nome;
	    public Integer ruolo;
	    public String listaSpecializzazioni;
	    public String turno;
	    public String codiceFiscale;
	    public String numeroTelefonico;
	    public String eMail;
	    public String dataDiInserimento;
	    public String utenteLoggato;
	    
		public AnagraficaPersonaleVVFModel(ObjectId _id, String qualifica, String cognome, String nome, Integer ruolo,
				String listaSpecializzazioni, String turno, String codiceFiscale, String numeroTelefonico, String eMail, String dataDiInserimento, String utenteLoggato) {
			// TODO Auto-generated constructor stub
		}
		
		
		public ObjectId get_id() {
			return _id;
		}


		public void set_id(ObjectId _id) {
			this._id = _id;
		}

		public String getQualifica() {
			return qualifica;
		}
		public void setQualifica(String qualifica) {
			this.qualifica = qualifica;
		}
		public String getCognome() {
			return cognome;
		}
		public void setCognome(String cognome) {
			this.cognome = cognome;
		}
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public Integer getRuolo() {
			return ruolo;
		}
		public void setRuolo(Integer ruolo) {
			this.ruolo = ruolo;
		}
		public String getListaSpecializzazioni() {
			return listaSpecializzazioni;
		}
		public void setListaSpecializzazioni(String listaSpecializzazioni) {
			this.listaSpecializzazioni = listaSpecializzazioni;
		}
		public String getTurno() {
			return turno;
		}
		public void setTurno(String turno) {
			this.turno = turno;
		}
		public String getCodiceFiscale() {
			return codiceFiscale;
		}
		public void setCodiceFiscale(String codiceFiscale) {
			this.codiceFiscale = codiceFiscale;
		}
		public String getNumeroTelefonico() {
			return numeroTelefonico;
		}
		public void setNumeroTelefonico(String numeroTelefonico) {
			this.numeroTelefonico = numeroTelefonico;
		}
		public String geteMail() {
			return eMail;
		}
		public void seteMail(String eMail) {
			this.eMail = eMail;
		}
		
		public String getDataDiInserimento() {
			return dataDiInserimento;
		}

		public void setDataDiInserimento(String dataDiInserimento) {
			this.dataDiInserimento = dataDiInserimento;
		}

		public String getUtenteLoggato() {
			return utenteLoggato;
		}

		public void setUtenteLoggato(String utenteLoggato) {
			this.utenteLoggato = utenteLoggato;
		}


		@Override
		public String toString() {
			return "AnagraficaPersonaleVVFModel [_id=" + _id + ", qualifica=" + qualifica + ", cognome="
					+ cognome + ", nome=" + nome + ", ruolo=" + ruolo + ", listaSpecializzazioni="
					+ listaSpecializzazioni + ", turno=" + turno + ", codiceFiscale=" + codiceFiscale
					+ ", numeroTelefonico=" + numeroTelefonico + ", eMail=" + eMail + ", dataDiInserimento="
					+ dataDiInserimento + ", utenteLoggato=" + utenteLoggato + "]";
		}

	

	
		
}
