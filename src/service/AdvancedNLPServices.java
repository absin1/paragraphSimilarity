package service;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.extjwnl.data.POS;

public class AdvancedNLPServices {
	public static void main(String[] args) {
		SortedSet<String> priorityQueue = new TreeSet<>();
		for (String string : new AdvancedNLPServices().getCoreNLPvsWordNetPOSMap().keySet()) {
			priorityQueue.add(string);
		}
		int i = 0;
		for (String string : priorityQueue) {
			System.out.println(++i + ">>" + string);
		}
	}

	public HashMap<String, POS> getCoreNLPvsWordNetPOSMap() {
		// VERB ADVERB ADJECTIVE NOUN
		// https://catalog.ldc.upenn.edu/docs/LDC99T42/tagguid1.pdf
		HashMap<String, POS> coreNLPvsWordNetPOSMap = new HashMap<>();
		coreNLPvsWordNetPOSMap.put("JJ", POS.ADJECTIVE);
		coreNLPvsWordNetPOSMap.put("JJR", POS.ADJECTIVE);
		coreNLPvsWordNetPOSMap.put("JJS", POS.ADJECTIVE);
		coreNLPvsWordNetPOSMap.put("RB", POS.ADVERB);
		coreNLPvsWordNetPOSMap.put("RBR", POS.ADVERB);
		coreNLPvsWordNetPOSMap.put("RBS", POS.ADVERB);
		coreNLPvsWordNetPOSMap.put("DT", null);
		coreNLPvsWordNetPOSMap.put("CD", null);
		coreNLPvsWordNetPOSMap.put("NNS", POS.NOUN);
		coreNLPvsWordNetPOSMap.put("NN", POS.NOUN);
		coreNLPvsWordNetPOSMap.put("CC", null);
		coreNLPvsWordNetPOSMap.put("IN", null);
		coreNLPvsWordNetPOSMap.put("UH", null);
		coreNLPvsWordNetPOSMap.put("EX", null);
		coreNLPvsWordNetPOSMap.put("UH", null);
		coreNLPvsWordNetPOSMap.put("FW", null);
		coreNLPvsWordNetPOSMap.put("VBG", POS.VERB);
		coreNLPvsWordNetPOSMap.put("LS", null);
		coreNLPvsWordNetPOSMap.put("MD", POS.VERB);
		coreNLPvsWordNetPOSMap.put("VBN", POS.VERB);
		coreNLPvsWordNetPOSMap.put("RP", null);
		coreNLPvsWordNetPOSMap.put("VBD", POS.VERB);
		coreNLPvsWordNetPOSMap.put("PRP", null);
		coreNLPvsWordNetPOSMap.put("POS", null);
		coreNLPvsWordNetPOSMap.put("PRP$", null);
		coreNLPvsWordNetPOSMap.put("WP$", null);
		coreNLPvsWordNetPOSMap.put("PDT", null);
		coreNLPvsWordNetPOSMap.put("VBP", POS.VERB);
		coreNLPvsWordNetPOSMap.put("VBZ", POS.VERB);
		coreNLPvsWordNetPOSMap.put("NNPS", POS.NOUN);
		coreNLPvsWordNetPOSMap.put("NNP", POS.NOUN);
		coreNLPvsWordNetPOSMap.put("JJS", POS.ADJECTIVE);
		coreNLPvsWordNetPOSMap.put("SYM", null);
		coreNLPvsWordNetPOSMap.put("VB", POS.VERB);
		coreNLPvsWordNetPOSMap.put("WDT", null);
		coreNLPvsWordNetPOSMap.put("WP", null);
		coreNLPvsWordNetPOSMap.put("WRB", null);
		coreNLPvsWordNetPOSMap.put("TO", null);
		return coreNLPvsWordNetPOSMap;
	}

}
