package RecommenderEvaluator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class RecommendationEvaluator {
	public static void main(String[] args) {
		
		try {
			
			File results = new File("Data/Results.csv");
			FileOutputStream fos = new FileOutputStream(results);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			DataModel dm = new FileDataModel(new File("Data/TransformedZebra.csv"));
			//DataModel dm = new FileDataModel(new File("Data/pref_vals_transactions.csv"));
			
			RecommenderEvaluator avgabsdiffevaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
			RecommenderEvaluator rmsevaluator = new RMSRecommenderEvaluator();
			RecommenderIRStatsEvaluator irstatsevaluator = new GenericRecommenderIRStatsEvaluator();
			
			RecommenderBuilder rb1 = new RecommenderBuilder(){
				@Override
                public Recommender buildRecommender(DataModel dm)throws TasteException
                {
					UserSimilarity sim = new TanimotoCoefficientSimilarity(dm);
					UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, sim, dm);
					GenericBooleanPrefUserBasedRecommender recommender = new GenericBooleanPrefUserBasedRecommender(dm, neighborhood, sim);
					return recommender;
                }
			};

			RecommenderBuilder rb2 = new RecommenderBuilder(){
				@Override
                public Recommender buildRecommender(DataModel dm)throws TasteException
                {
					UserSimilarity sim = new LogLikelihoodSimilarity(dm);
					UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, sim, dm);
					GenericBooleanPrefUserBasedRecommender recommender = new GenericBooleanPrefUserBasedRecommender(dm, neighborhood, sim);
					return recommender;
                }
			};
			
			RecommenderBuilder rb3 = new RecommenderBuilder(){
				@Override
                public Recommender buildRecommender(DataModel dm)throws TasteException
                {
					ItemSimilarity sim = new TanimotoCoefficientSimilarity(dm);
					GenericBooleanPrefItemBasedRecommender recommender = new GenericBooleanPrefItemBasedRecommender(dm, sim);
					return recommender;
                }
			};
			
			RecommenderBuilder rb4 = new RecommenderBuilder(){
				@Override
                public Recommender buildRecommender(DataModel dm)throws TasteException
                {
					ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
					GenericBooleanPrefItemBasedRecommender recommender = new GenericBooleanPrefItemBasedRecommender(dm, sim);
                    return recommender;
                }
			};
			
			RecommenderBuilder rb5 = new RecommenderBuilder(){
				@Override
                public Recommender buildRecommender(DataModel dm)throws TasteException
                {
					UserSimilarity sim = new TanimotoCoefficientSimilarity(dm);
					UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, sim, dm);
					GenericBooleanPrefUserBasedRecommender recommender = new GenericBooleanPrefUserBasedRecommender(dm, neighborhood, sim);
					return recommender;
                }
			};
			
			double result1 = avgabsdiffevaluator.evaluate(rb1, null, dm, 0.9, 1.0);
			double result2 = avgabsdiffevaluator.evaluate(rb2, null, dm, 0.9, 1.0);
			double result3 = avgabsdiffevaluator.evaluate(rb3, null, dm, 0.9, 1.0);
			double result4 = avgabsdiffevaluator.evaluate(rb4, null, dm, 0.9, 1.0);
			
			double result5 = rmsevaluator.evaluate(rb1, null, dm, 0.9, 1.0);
			double result6 = rmsevaluator.evaluate(rb2, null, dm, 0.9, 1.0);
			double result7 = rmsevaluator.evaluate(rb3, null, dm, 0.9, 1.0);
			double result8 = rmsevaluator.evaluate(rb4, null, dm, 0.9, 1.0);
			
			double result9 = avgabsdiffevaluator.evaluate(rb5, null, dm, 0.9, 1.0);
			double result10 = rmsevaluator.evaluate(rb5, null, dm, 0.9, 1.0);
			
			IRStatistics stats1 = irstatsevaluator.evaluate(rb1, null, dm, null, 5, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);
			IRStatistics stats2 = irstatsevaluator.evaluate(rb2, null, dm, null, 5, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);
			IRStatistics stats3 = irstatsevaluator.evaluate(rb3, null, dm, null, 5, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);
			IRStatistics stats4 = irstatsevaluator.evaluate(rb4, null, dm, null, 5, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);
			IRStatistics stats5 = irstatsevaluator.evaluate(rb5, null, dm, null, 5, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);
			
			//Save results to file
			bw.write("Recommender_Type,Similarity_Metric,Neighborhood_Size,Neighborhood_Type,@,avg_abs_diff_result,rms_result,precision,recall,f1_measure,fallout,NormalizedDiscountedCumulativeGain");
			bw.newLine();
			
			bw.write("User,Tanimoto,0.1,Threshold,5," + result1 + "," + result5 + "," + stats1.getPrecision() 
						+ "," + stats1.getRecall() + "," + stats1.getF1Measure()
						+ "," + stats1.getFallOut() + "," + stats1.getNormalizedDiscountedCumulativeGain());
			bw.newLine();
			
			bw.write("User,Loglikelihood,0.1,Threshold,5," + result2 + "," + result6 + "," + stats2.getPrecision() 
					+ "," + stats2.getRecall() + "," + stats2.getF1Measure()
					+ "," + stats2.getFallOut() + "," + stats2.getNormalizedDiscountedCumulativeGain());
			bw.newLine();
			
			bw.write("Item,Tanimoto,-,-,5," + result3 + "," + result7 + "," + stats3.getPrecision() 
					+ "," + stats3.getRecall() + "," + stats3.getF1Measure()
					+ "," + stats3.getFallOut() + "," + stats3.getNormalizedDiscountedCumulativeGain());
			bw.newLine();
		
			bw.write("Item,Loglikelihood,-,-,5," + result4 + "," + result8 + "," + stats4.getPrecision() 
				+ "," + stats4.getRecall() + "," + stats4.getF1Measure()
				+ "," + stats4.getFallOut() + "," + stats4.getNormalizedDiscountedCumulativeGain());
			bw.newLine();
			
			bw.write("User,Tanimoto,5,NearestN,5," + result9 + "," + result10 + "," + stats5.getPrecision() 
					+ "," + stats5.getRecall() + "," + stats5.getF1Measure()
					+ "," + stats5.getFallOut() + "," + stats5.getNormalizedDiscountedCumulativeGain());
			bw.newLine();
		
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
