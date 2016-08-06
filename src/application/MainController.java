package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;


public class MainController {
	
	 List<Float> Accuracy  = new ArrayList<Float>();
	 List<String> Classfications = new ArrayList<String>();	
	
	 Boolean cont= false; //Its used for the file control. 
	 
	 File file = null;
	 @FXML private  VBox Vbox;
	 @FXML private TextArea txa;
	 @FXML private FileChooser File_Browser;
	 @FXML private FileChooser Bayes;
	 @FXML private FileChooser OneR;
	 
	public void Bayes(ActionEvent event) throws Exception //NaiveBayes 
	{
		
		String[] options = new String[2];
		 options[0] = "-t";
		 options[1] = file.toString();		
		String str =  Evaluation.evaluateModel(new NaiveBayes(), options);	//Calling weka classifiers. 
		 Classfications.add("NaiveBayes");
		txa.setText(str);
		
		String [] split = str.split("\n");
		
		breakloop:
		for (int i = 0; i < split.length; i++) {

			if(split[i].contains("=== Stratified cross-validation ==="))
			{
				System.out.println(split[i+2]);
				String [] percent = split[i+2].split("\\s+");
				Accuracy.add(Float.parseFloat(percent[4])/100);
				break breakloop;
			}
			
		}
		cont = false;
		System.out.println(split.length);
	
	}
	
	public void OneR(ActionEvent event) throws Exception //OneR
	{
		
		String[] options = new String[2];
		options[0] = "-t";
		options[1] = file.toString();
		
		String str = (Evaluation.evaluateModel(new weka.classifiers.rules.OneR(), options)); //Calling weka classifiers. 
		Classfications.add("OneR");
		txa.setText(str);
		
			System.out.println(str.length());		
			String [] split = str.split("\n");
			
			breakloop:
			for (int i = 0; i < split.length; i++) {

				if(split[i].contains("=== Stratified cross-validation ==="))
				{
					System.out.println(split[i+2]);
					String [] percent = split[i+2].split("\\s+");
					Accuracy.add(Float.parseFloat(percent[4])/100);
					break breakloop;
				}
				
			}

			cont = false;			
	}
	
	public void DecisionTable(ActionEvent event) throws Exception //DecisionTable
	{
		
		String[] options = new String[2];
		options[0] = "-t";
		options[1] = file.toString();
		
		String str = (Evaluation.evaluateModel(new weka.classifiers.rules.DecisionTable() , options)); //Calling weka classifiers. 
		
		Classfications.add("DecisionTable");
		txa.setText(str);
		
			System.out.println(str.length());		
			String [] split = str.split("\n");
			
			breakloop:
			for (int i = 0; i < split.length; i++) {

				if(split[i].contains("=== Stratified cross-validation ==="))
				{
					System.out.println(split[i+2]);
					String [] percent = split[i+2].split("\\s+");
					Accuracy.add(Float.parseFloat(percent[4])/100);
					break breakloop;
				}
				
			}
			
			cont = false;		
	}
		
	public void compare(ActionEvent event) //Its used to compare which class is better and shows all results rate.
	{
		txa.setText("");
		float max = 0;
		String best_class = null;
		String str = null; 
		String best_performance = null;
		String accuracy = null;
		
		for (int i = 0; i < Classfications.size(); i++) {
			
			if (max == Accuracy.get(i)) //Its used because different classification classes can produce the same results.
			{
				accuracy += Accuracy.get(i)+" ";
				best_performance += " "+ Float.toString(Accuracy.get(i)*100);
				best_class += " " + Classfications.get(i);
			}		
			
			if(max < Accuracy.get(i))
			{
				max = Accuracy.get(i);
				accuracy = Accuracy.get(i)+" ";
				best_performance = Float.toString(Accuracy.get(i)*100);
				best_class = Classfications.get(i);								
			}

			if(str == null)
				str= Classfications.get(i)+" the percentage of success % "+
			(Accuracy.get(i)*100)+". Accuracy : "+Accuracy.get(i)+"\n";
			
			else 
				str += Classfications.get(i)+" the percentage of success  % "+
			(Accuracy.get(i)*100)+". Accuracy : "+Accuracy.get(i)+"\n";
		}
	 
		if(str!=null)
			str += "\n\n"+"The best performance was realized "+best_class+" classification with % "+
					best_performance+" rate. "+"Accuracy : "+ accuracy;
		
		txa.setText(str);
	}
	
	public void FileBrowser(ActionEvent event)
	{

		FileChooser fileChooser = new FileChooser();
		file  = fileChooser.showOpenDialog(null);
      
		if(!file.isFile())
        	 System.out.println("No file.");
        else 
        {	
        	System.out.println(file.toString());
        	cont =Boolean.TRUE;
        }
		
        Classfications.clear(); // Class performance test for a single file.
        Accuracy.clear();
	}
}
