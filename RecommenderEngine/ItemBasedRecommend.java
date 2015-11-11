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
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemBasedRecommend {

	public static void main(String[] args) {
		
		try 
		{
			
			DataModel dm = new FileDataModel(new File("Data/TransformedZebra.csv"));
			
			HashMap <String, String> ProdMap = new HashMap<String, String>();
			
			String line = "";
			
			BufferedReader br_prod = new BufferedReader(new FileReader(new File("Data/product_lookup.csv")));
			while ((line = br_prod.readLine()) != null) {
				String[] str = line.split(",");
				ProdMap.put(str[1], str[0]);
			}
			
			File results = new File("Data/ItemRecommender_Results.csv");
			FileOutputStream fos = new FileOutputStream(results);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			bw.write("ItemId , Similarity, Recommendation1 , Recommendation2 , Recommendation3, Recommendation4, Recommendation5 , Recommendation6 , Recommendation7 , Recommendation8, Recommendation9, Recommendation10");
			bw.newLine();
			
			ItemSimilarity sim1 = new LogLikelihoodSimilarity(dm);
			ItemSimilarity sim2 = new TanimotoCoefficientSimilarity(dm);
			
			GenericBooleanPrefItemBasedRecommender recommender1 = new GenericBooleanPrefItemBasedRecommender(dm, sim1);
			GenericBooleanPrefItemBasedRecommender recommender2 = new GenericBooleanPrefItemBasedRecommender(dm, sim2);

			for(LongPrimitiveIterator items = dm.getItemIDs(); items.hasNext();) {
				String result1 = "";
				String result2 = "";
				
				Long itemId = items.next();
				String user = ProdMap.get(itemId.toString());
				
				result1 = user + ", LogLikelihood";
				List<RecommendedItem>recommendations1 = recommender1.mostSimilarItems(itemId, 10);
				for(RecommendedItem recommendation : recommendations1)
					result1 = result1.concat("," + ProdMap.get(String.valueOf(recommendation.getItemID())));
				
				result2 = user + ", Tanimoto";
				List<RecommendedItem>recommendations2 = recommender2.mostSimilarItems(itemId, 10);
				for(RecommendedItem recommendation : recommendations2)
					result2 = result2.concat("," + ProdMap.get(String.valueOf(recommendation.getItemID())));

				bw.write(result1);
				bw.newLine();
				
				bw.write(result2);
				bw.newLine();
				
				bw.close();
				br_prod.close();
			}
		} catch (IOException e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		} catch (TasteException e) {
			System.out.println("There was a Taste Exception");
			e.printStackTrace();
		}
	}
}

