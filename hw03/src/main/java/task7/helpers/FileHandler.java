package task7.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class FileHandler {
	
	public static void writeToFile(String fileName, List<String> lines) {
		
		File file = new File(fileName);
		boolean fileExists = file.exists();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
			if (!fileExists) {
				file.createNewFile();
			}
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<CandidateStatus> loadResults(String fileName) {
		
		List<CandidateStatus> allResults = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] separated = line.split("\\t");
				int id = Integer.parseInt(separated[0]);
				int votes = Integer.parseInt(separated[1]); 
				allResults.add(new CandidateStatus(id, votes));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return allResults;
	}
	
	public static List<Candidate> loadCandidates(String fileName) {
		
		List<Candidate> candidates = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] separated = line.split("\\t");
				int id = Integer.parseInt(separated[0]);
				candidates.add(new Candidate(id, separated[1], separated[2]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		candidates.sort((o1, o2) -> {
			if(o1.getId() < o2.getId()) {
				return -1;
			} else if(o1.getId() > o2.getId()) {
				return 1;
			} else {
				return 0;
			}
		});
		
		return candidates;
	}
	
	public static List<String> getInitializedCandidates(String fileName) {

		List<Candidate> candidates = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] separated = line.split("\\t");
				int id = Integer.parseInt(separated[0]);
				candidates.add(new Candidate(id, separated[1], separated[2]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		candidates.sort((o1, o2) -> {
			if (o1.getId() < o2.getId()) {
				return -1;
			} else if (o1.getId() > o2.getId()) {
				return 1;
			} else {
				return 0;
			}
		});

		List<String> result = new ArrayList<>();
		candidates.forEach(c -> {
			result.add(c.getId() + "\t" + "0");
		});

		return result;
	}
	
	public static List<CandidateWithResult> getCandidateResults(String resultsFileName, String definitionFileName) {
		
		List<CandidateStatus> allVotes = FileHandler.loadResults(resultsFileName);
		List<Candidate> candidates = FileHandler.loadCandidates(definitionFileName);
		
		List<CandidateWithResult> results = new ArrayList<>();
		
		for(CandidateStatus one: allVotes) {
			for(Candidate cand: candidates) {
				if(one.getId() == cand.getId()) {
					results.add(new CandidateWithResult(cand, one.getVotes()));
					break;
				}
			}
		}
		
		results.sort((v1, v2) -> {
			if(v1.getVotes() < v2.getVotes()) {
				return 1;
			} else if (v1.getVotes() > v2.getVotes()) {
				return -1;
			} else {
				return 0;
			}
		});
		
		return results;
	}
}
