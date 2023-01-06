package it.antonio.sp.util;

import java.util.HashMap;
import java.util.Map;

public class SimilarSpecializationMapping {
	
	private static Map<String, String> map = new HashMap();
	
	static {
		map.put("Aeroportuali", "Aeroportuali");
		map.put("Antincendio Navale", "Antincendio Navale");
		map.put("Autista II", "Autista");
		map.put("Autista II TNP", "Autista");
		map.put("Autista III", "Autista");
		map.put("Autista IV", "Autista");
		map.put("Autista Movimento Terra", "Autista");
		map.put("Autogrù", "Autista");
		map.put("Autoprotezione Ambiente Acquatico","Autoprotezione Ambiente Acquatico");
		map.put("Autoscala", "Autista");
		map.put("C.A.T.","C.A.T.");
		map.put("Cinofili","Cinofili");
		map.put("D.O.S.","D.O.S.");
		map.put("Elicotteristi e Piloti D'Aereo","Elicotteristi e Piloti D'Aereo");
		map.put("Istruttore ATP","Istruttore");
		map.put("Istruttore Ginnico","Istruttore");
		map.put("Istruttore Patenti","Istruttore");
		map.put("Istruttore Professionale","Istruttore");
		map.put("Istruttore SAF Basico","Istruttore");
		map.put("Istruttore TPSS","Istruttore");
		map.put("Laboratorio A.R.A.","Laboratorio A.R.A.");
		map.put("M.S.L.","M.S.L.");
		map.put("Nautici","Nautici");
		map.put("NBCR II","NBCR");
		map.put("NBCR III","NBCR");
		map.put("Patente Nautica I","Patente Nautica");
		map.put("Patente Nautica II","Patente Nautica");
		map.put("Patente PWC","Patente PWC");
		map.put("Polizia Giudiziaria","Polizia Giudiziaria");
		map.put("Portuali","Portuali");
		map.put("Prevenzione Incendi 1","Prevenzione Incendi");
		map.put("Prevenzione Incendi 2","Prevenzione Incendi");
		map.put("Prevenzione Incendi 3","Prevenzione Incendi");
		map.put("Prevenzione Incendi 4","Prevenzione Incendi");
		map.put("Prevenzione Incendi 5","Prevenzione Incendi");
		map.put("Prevenzione Incendi 6","Prevenzione Incendi");
		map.put("Puntellamenti","Puntellamenti");
		map.put("Radioriparatori","Radioriparatori");
		map.put("SAF 1B","SAF 1B");
		map.put("SAF 1B Flu/Sa","SAF 1B");
		map.put("SAF 1B Fluviale","SAF 1B");
		map.put("SAF 2A","SAF 2A");
		map.put("SAF 2A Flu/Sa","SAF 2A");
		map.put("SAF 2A Fluviale","SAF 2A");
		map.put("SAF 2B","SAF 2B");
		map.put("SAF 2B Flu/Sa","SAF 2B");
		map.put("SAF 2B Fluviale","SAF 2B");
		map.put("SAF Avanzato","SAF Avanzato");
		map.put("SAF Avanzato Flu/Sa","SAF Avanzato");
		map.put("SAF Avanzato Fluviale","SAF Avanzato");
		map.put("SAF Basico","SAF Basico");
		map.put("SAF Basico Flu/Sa","SAF Basico");
		map.put("SAF Basico Fluviale","SAF Basico");
		map.put("Sala Operativa","Sala Operativa");
		map.put("Salvamento a nuoto","Salvamento a nuoto");
		map.put("SAPR","SAPR");
		map.put("Scorta Tecnica","Scorta Tecnica");
		map.put("Soccorritore Aeroportuale","Soccorritore Aeroportuale");
		map.put("Sommozzatori","Sommozzatori");
		map.put("Tas I","Tas");
		map.put("Tas II","Tas");
		map.put("TPSS","TPSS");
		map.put("Usar-L","Usar-L");
		map.put("Usar-M","Usar-M");
	}
	
	public static String getSimilarMappingIfExist(String specialization) {
		if (map.containsKey(specialization)) {
			return map.get(specialization);
		} else {
			return specialization;
		}
	}

	
}
