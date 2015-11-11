package RecommenderEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserBasedRecommend {

	public static void main(String[] args) {
		
		HashMap <String, String> ProdMap = new HashMap<String, String>();
		HashMap <String, String> AcctMap = new HashMap<String, String>();
		
		File acct_lookup = new File("Data/account_lookup.csv");
		File prod_lookup = new File("Data/product_lookup.csv");
		
		try 
		{
			String line = "";
			
			BufferedReader br_acct = new BufferedReader(new FileReader(acct_lookup));
			while ((line = br_acct.readLine()) != null) {
				String[] str = line.split(",");
				AcctMap.put(str[1], str[0]);
			}
			
			BufferedReader br_prod = new BufferedReader(new FileReader(prod_lookup));
			while ((line = br_prod.readLine()) != null) {
				String[] str = line.split(",");
				ProdMap.put(str[1], str[0]);
			}
			
			File results = new File("Data/UserRecommender_Results.csv");
			FileOutputStream fos = new FileOutputStream(results);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				
			DataModel dm = new FileDataModel(new File("Data/TransformedZebra.csv"));
			
			UserSimilarity sim1 = new LogLikelihoodSimilarity(dm);
			UserSimilarity sim2 = new TanimotoCoefficientSimilarity(dm);
			
			UserNeighborhood neighborhood1 = new NearestNUserNeighborhood(5, sim1, dm);
			UserBasedRecommender recommender1 = new GenericBooleanPrefUserBasedRecommender(dm, neighborhood1, sim1);
			
			UserNeighborhood neighborhood2 = new NearestNUserNeighborhood(5, sim2, dm);
			UserBasedRecommender recommender2 = new GenericBooleanPrefUserBasedRecommender(dm, neighborhood2, sim2);
			
			bw.write("UserID , Similarity, Recommendation1 , Recommendation2 , Recommendation3, Recommendation4, Recommendation5 , Recommendation6 , Recommendation7 , Recommendation8, Recommendation9, Recommendation10");
			bw.newLine();
			
			for(LongPrimitiveIterator users = dm.getUserIDs(); users.hasNext();) {
				
				String result1 = "";
				String result2 = "";
				
				Long userId = users.next();
				String user = AcctMap.get(userId.toString());
				
				result1 = user + ", LogLikelihood";
				List<RecommendedItem> recommendations1 = recommender1.recommend(userId, 10);
				for (RecommendedItem recommendation : recommendations1) {
					result1 = result1.concat("," + ProdMap.get(String.valueOf(recommendation.getItemID())));
				}
				
				result2 = user + ", Tanimoto" ;
				List<RecommendedItem> recommendations2 = recommender2.recommend(userId, 10);
				for (RecommendedItem recommendation : recommendations2) {
					result2 = result2.concat("," + ProdMap.get(String.valueOf(recommendation.getItemID())));
				}
				
				bw.write(result1);
				bw.newLine();
				
				bw.write(result2);
				bw.newLine();
			}
			
			br_acct.close();
			br_prod.close();
			bw.close();
		} catch (IOException e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		} catch (TasteException e) {
			System.out.println("There was a Taste Exception");
			e.printStackTrace();
		}
	}
}

