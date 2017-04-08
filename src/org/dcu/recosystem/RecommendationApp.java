
package org.dcu.recosystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class RecommendationApp
{
	private final static String TSV_SEPARATOR = "\t";
	private static final String IO_FILE_NAME = "recysys_dataset-2017-03-27";
	private static final String CURRENT_DIR = System.getProperty("user.dir");
	private final static String CSV_EXTENTION = ".csv";
	private final static String TSV_EXTENTION = ".tsv";
	private static List<RecommendationBean> recoBeanList = new ArrayList<RecommendationBean>();
	static Map<Integer, Integer> clickCounts = new TreeMap<Integer, Integer>();
	
	
	public static void main(String[] args)
	{
		String csvName = CURRENT_DIR + "\\" + IO_FILE_NAME + CSV_EXTENTION;
		File fileCSV = new File(csvName);
		if (isPureAscii(csvName))
		{
			if (fileCSV.exists() && !fileCSV.isDirectory())
			{
				initMap();
				List<String> listDataSet = processCSVFile(fileCSV);
				collateCSVFile(listDataSet);
				JOptionPane.showMessageDialog(null, "The results are saved in the project folder as tsv file.");
			} 
			else
			{
				JOptionPane.showMessageDialog(null, "CSV file '" + csvName + "' does not exist in the path.");
			}
		} 
		else
		{
			JOptionPane.showMessageDialog(null, "CSV filename contains non-ASCII charachters.");
		}
		
	}
	
	private static void initMap()
	{
		for (int i = 1; i <= 15; i++)
		{
			clickCounts.put(i, 0);
		}
	}

	private static void collateCSVFile(List<String> listDataSet)
	{
		boolean isHeader = true;
		for (String tuple : listDataSet)
		{
			if(isHeader)
			{
				isHeader = false;
			} 
			else
			{
				tuple = tuple.replaceAll("^\"|\"$", "");
				String[] tupleBits = tuple.split(TSV_SEPARATOR);
				//System.out.println(tuple);
				recoBeanList.add(new RecommendationBean(tupleBits[0], Integer.parseInt(tupleBits[2]), Integer.parseInt(tupleBits[3]), tupleBits[9]));
				//System.out.println(tupleBits[0]+ ":" + Integer.parseInt(tupleBits[2])+ ":" + Integer.parseInt(tupleBits[3])+ ":" + tupleBits[9]);
				if(!tupleBits[9].equals("\\N"))
				{
					clickCounts.put(Integer.parseInt(tupleBits[3]), (Integer)clickCounts.get(Integer.parseInt(tupleBits[3])) + 1);
				}
			}
		}
		Collections.sort(recoBeanList);
/*		for (int i = 0; i < recoBeanList.size(); i++) {
			System.out.println(i + ":" + recoBeanList.get(i).getRec_rank());
		}*/
		//System.out.println(recoBeanList.size());
		Map<Integer, Integer> mapCounts = recoBeanList.stream().collect(Collectors.groupingBy(RecommendationBean::getRec_rank, Collectors.summingInt(RecommendationBean::getRec_amount)));
		exportToTSVFile(mapCounts);
	}

	private static void exportToTSVFile(Map<Integer, Integer> mapCounts)
	{
		// Tab delimited file will be written to the same directory where CSV was imported from
		String tsvName = CURRENT_DIR + "\\" + IO_FILE_NAME + TSV_EXTENTION;
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(tsvName);
			bw = new BufferedWriter(fw);
			bw.write("Rank Order ");
			for(Map.Entry<Integer, Integer> entry : mapCounts.entrySet()) {
				bw.write(entry.getKey() + TSV_SEPARATOR);
			}
			bw.write("\n");
			bw.write("Amount of recommendation at that Rank ");
			for(Map.Entry<Integer, Integer> entry : mapCounts.entrySet()) {
				bw.write(entry.getValue() + TSV_SEPARATOR);
			}
			bw.write("\n");
			bw.write("Number of Clicks at that Rank ");
			for(Map.Entry<Integer, Integer> entry : clickCounts.entrySet()) {
				bw.write(entry.getValue() + TSV_SEPARATOR);
			}
			bw.write("\n");
			bw.write("Click Through Rate ");
			for(Map.Entry<Integer, Integer> entry : clickCounts.entrySet()) {
				bw.write(entry.getValue() + TSV_SEPARATOR);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			try 
			{
				bw.flush();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	
		
	}

	private static List<String> processCSVFile(File csvFile)
	{
		List<String> listDataSet = new ArrayList<String>();
		try
		{
			listDataSet = Files.readAllLines(csvFile.toPath(), StandardCharsets.UTF_8);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
/*		for(String line : listDataSet) {
			System.out.println(line);
		}*/
		return listDataSet;
		
	}

	public static boolean isPureAscii(String v)
	{
		byte bytearray[] = v.getBytes();
		CharsetDecoder d = Charset.forName("US-ASCII").newDecoder();
		try
		{
			CharBuffer r = d.decode(ByteBuffer.wrap(bytearray));
			r.toString();
		}
		catch (CharacterCodingException e)
		{
			return false;
		}
		return true;
	}

}
