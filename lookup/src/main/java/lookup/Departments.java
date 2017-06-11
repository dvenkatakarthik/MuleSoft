package lookup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Departments {

public String getDeptName(String deptId){    
    
    Map <String,String> hm = new HashMap<String,String>();
    try {
    	String path="src/main/resources/departments.csv";
    	BufferedReader  br = new BufferedReader( new FileReader(path));
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			hm.put(sCurrentLine.split(",")[0],sCurrentLine.split(",")[1]);
			if(hm.get(deptId) != null){
				return hm.get(deptId);
			}
		}		
	} catch (Exception e) {
		e.printStackTrace();
	}
    return "Not in the list";
}
}
