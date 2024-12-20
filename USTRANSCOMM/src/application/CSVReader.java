//citation credit for csv data -> https://corgis-edu.github.io/corgis/csv/medal_of_honor/
package application;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//creating class for reading csv excel file
public class CSVReader {

	public List<Troop> readTroopNames(String csvFile) throws IOException {
		List<Troop> troops = new ArrayList<>();
		//utilizing buffer to read data
	    try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
	        //creating variable to hold the content of each line
	    	String line;
	        
	        //flag for skipping header
	        boolean skipHeader = false;
	        //reading each line of the file until we reach the end, or no other content exists
	        while ((line = reader.readLine()) != null) {
	            if (!skipHeader) {
	                skipHeader = true;
	                continue; 
	            }
	            
	          //splitting the lines with a comma in the array, and using -1 to include empty fields in case of missing values
	            String[] data = line.split(",", -1); 

	            // Check if we have valid data in the necessary columns
	            if (data.length > 2 && !data[0].trim().isEmpty() && !data[1].trim().isEmpty()) {
	                // Separate last name and first name by splitting by the comma
	               //removing quotes from last name
	                String lastName = data[0].trim().replace("\"", " "); 
	                //removing quotes from first name
	                String firstName = data[1].trim().replace("\"", ""); 
	                //extracting the military branch from the third column
	                String branchName = data[2].trim();
	                String fullName = firstName + lastName;

	               //adding troops to the array list
	                troops.add(new Troop(fullName, branchName));
	            }
	            
	        }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
            throw e;  
        }
        //collections.shuffle here is taking the troops and randomly sorting them in the list
        Collections.shuffle(troops);
        System.out.println("Total troops loaded: " + troops.size());
        //returning at most 3500 troops
        return troops.subList(0, Math.min(troops.size(), 3500)); 
    }
}

