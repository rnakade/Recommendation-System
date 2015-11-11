package DataTransformation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;


public class TransformData {
	
	private static final String DELIMS = "-: ";
	private static final int DATE = 0;
	private static final int ACCOUNT_ID = 2;
	private static final int PRODUCT_ID = 6;
	
	public TransformData() {}
	
	public void transform() {
		try {
			CSVReader reader = new CSVReader(new FileReader(InputFileName));
			FileWriter writer = new FileWriter(OutputFileNmae);
			
			String[] nextLine;
			nextLine = reader.readNext(); // this is the column header line
			
			while((nextLine = reader.readNext()) != null) {
				long date = getDate(nextLine[DATE]);
				long account = getAccount(nextLine[ACCOUNT_ID]);
				long product = getProduct(nextLine[PRODUCT_ID]);
				
				writer.append(Long.toString(account) + ',');
				writer.append(Long.toString(product) + ",,");
				writer.append(Long.toString(date) + '\n');
			}
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private long getDate(String strDate) {
		StringTokenizer st = new StringTokenizer(strDate, DELIMS);
		int year = Integer.parseInt(st.nextToken());
		int month = Integer.parseInt(st.nextToken());
		int date = Integer.parseInt(st.nextToken());
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, date);
		long millisecondsSinceEpoch = cal.getTimeInMillis();
		
		return millisecondsSinceEpoch;
	}
	
	private long getAccount(String strAccount) {
		String betterAccount = strAccount.replaceAll("-", "");
		long acct = Long.parseLong(betterAccount, 36);
		return acct;
	}
	
	private long getProduct(String strProduct) {
		String betterProduct = strProduct.replaceAll("-", "");
		long prod = Long.parseLong(betterProduct, 36);
		return prod;
	}

}
