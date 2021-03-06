import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TransformerGenerator {
	
	public static LinkedHashMap[] generate(ArrayList<LinkedHashMap> arr) {
		System.out.println("start to generate");
		// try
		LinkedHashMap[] configs = new LinkedHashMap[1];
		configs[0] = new LinkedHashMap();

		for (LinkedHashMap m : arr) {
			
			
//			String[] targetPathArr = ((String) m.get("target")).split("\\.");
//		    String[] sourcePathArr = ((String) m.get("source")).split("\\.");
			
			
			String[] targetPathArr = splitPathToArr((String) m.get("target"));
			String[] sourcePathArr = splitPathToArr((String) m.get("source"));
		    int maxDepth = Math.max(targetPathArr.length, sourcePathArr.length) - 1;
		    
		    boolean canFindSuchConfig = false;
		    for (int i = 0; i < configs.length; i++) {
		    	if (configs[i].containsKey("target") && configs[i].get("target").equals(trimPath(targetPathArr[0]))) {
		    		generateMapping(configs[i], targetPathArr, sourcePathArr, maxDepth, 0, 0);
		    		canFindSuchConfig = true;
		    		break;
		    	}
		    }
		    
		    if (!canFindSuchConfig) {
		    	if (configs[configs.length - 1].containsKey("target")) {
		    		LinkedHashMap config = new LinkedHashMap();
					LinkedHashMap[] newConfigs = new LinkedHashMap[configs.length + 1];
					for (int i = 0; i < configs.length; i++) {
						newConfigs[i] = configs[i];
					}
					newConfigs[configs.length] = config;
					configs = newConfigs;
		    	}
				generateMapping(configs[configs.length - 1], targetPathArr, sourcePathArr, maxDepth, 0, 0);
		    }
		}
		
		return configs;
				
	}
	
	public static String[] splitPathToArr(String path) {
        boolean containsContent = false;
        String firstHalf = null, secondHalf = null;
        if (path.charAt(path.length() - 1) == ')') {
        	if (path.indexOf('(') == 0) {
        		return new String[] {path};
        	}
            firstHalf = path.substring(0,path.indexOf('('));
            secondHalf = path.substring(path.indexOf('('));
            path = firstHalf;
            containsContent = true;
        }
        String[] pathArr = path.split("\\.");
        if (containsContent) {
            String[] newPathArr = new String[pathArr.length + 1];
            for (int i = 0; i < pathArr.length; i++) {
                newPathArr[i] = pathArr[i];
            }
            newPathArr[pathArr.length] = secondHalf;
            pathArr = newPathArr;
        }
        return pathArr;
	}
	
	
	
	public static String trimPath(String path) {
		if (path.charAt(0) == '*') path = path.substring(1);
		return path;
	}
	

	
	public static void generateMapping(LinkedHashMap m, String[] targetPathArr, String[] sourcePathArr, int maxDepth, int curTargetDepth, int curSourceDepth) {
//		int curTargetDepth = curDepth > targetPathArr.length - 1 ? targetPathArr.length - 1 : curDepth;
//		int curSourceDepth = curDepth > sourcePathArr.length - 1 ? sourcePathArr.length - 1 : curDepth;
		int nextTargetDepth = curTargetDepth + 1 > targetPathArr.length - 1 ? targetPathArr.length - 1 : curTargetDepth + 1;
		int nextSourceDepth = curSourceDepth + 1 > sourcePathArr.length - 1 ? sourcePathArr.length - 1 : curSourceDepth + 1;
		
		
		String curTargetPath = targetPathArr[curTargetDepth];
		String curSourcePath = sourcePathArr[curSourceDepth];
		
		String curType = getMappingType(curTargetPath, curSourcePath, curTargetDepth, targetPathArr.length - 1, curSourceDepth, sourcePathArr.length - 1);
		
		// if the path is array which contains '*', remove the '*' in config json
		if (curTargetPath.charAt(0) == '*') curTargetPath = curTargetPath.substring(1);
		if (curSourcePath.charAt(0) == '*') curSourcePath = curSourcePath.substring(1);
		
		// if the path is content which is surrounded by parenthesis, remove the parenthesis in config json
		if (curSourcePath.charAt(0) == '(') curSourcePath = curSourcePath.substring(1, curSourcePath.length() - 1);
		
		
	    // if type is "arrayItem_to_something", specify the itemIndex as an element in the config object
	    if (curType.equals("arrayItem_to_field") || curType.equals("arrayItem_to_object")) {
	    	int indexOfOpenSquareBracket = curSourcePath.indexOf('[');
	    	String itemIndexWithBracket = curSourcePath.substring(indexOfOpenSquareBracket);
	    	int itemIndex = Integer.parseInt(itemIndexWithBracket.substring(1, itemIndexWithBracket.length() - 1));
	    	m.put("itemIndex", itemIndex);
	    	curSourcePath = curSourcePath.substring(0, indexOfOpenSquareBracket);
	    }
	    
	    
	    // if the curTargetPath is the field name and contains data type coercion, (eg. price:number)
	    // then add the targetDataType as an element in mapping.
	    int indexOfColon = curTargetPath.indexOf(':');
	    if (indexOfColon > 0 && !curType.equals("content_to_field")) {
	    	m.put("targetDataType", curTargetPath.substring(indexOfColon + 1));
	    	curTargetPath = curTargetPath.substring(0, indexOfColon);
	    }
	    
	    
		
	    m.put("target", curTargetPath);
	    if (curType.equals("object_to_object") || curType.equals("field_to_object")) {
	    	m.put("source", "multiple");
	    }
	    else {
		    m.put("source", curSourcePath);
	    }
	    m.put("type", curType);
	    
	    
	    if (curTargetDepth < targetPathArr.length - 1 || curSourceDepth < sourcePathArr.length - 1) {
//	    	ArrayList<LinkedHashMap> mappings = new ArrayList<>();
	    	int mappingIdx = 0;
	    	
	    	if (m.containsKey("mappings")) {
	    		
	    
	    		LinkedHashMap[] mappings = (LinkedHashMap[]) m.get("mappings"); 
    			String nextTargetPath = targetPathArr[nextTargetDepth].charAt(0) == '*' ? targetPathArr[nextTargetDepth].substring(1) : targetPathArr[nextTargetDepth];
    			String nextSourcePath = sourcePathArr[nextSourceDepth].charAt(0) == '*' ? sourcePathArr[nextSourceDepth].substring(1) : sourcePathArr[nextSourceDepth];
    					
    					
	    		int[] offsets = new int[2];
	    		offsets[0] = 0;    // offsets[0] is the offset of the targetPathDepth
	    		offsets[1] = 0;    // offsets[1] is the offset of the sourcePathDepth
	    		
	    		
	    		int idx = mappingContainsTarget(mappings, nextTargetPath, nextSourcePath, offsets);
    			while (idx >= 0) {
    				m = mappings[idx];
    				mappings = (LinkedHashMap[])mappings[idx].get("mappings");
    				offsets[0]++;
    				nextTargetPath = targetPathArr[nextTargetDepth + offsets[0]].charAt(0) == '*' ? targetPathArr[nextTargetDepth + offsets[0]].substring(1) : targetPathArr[nextTargetDepth + offsets[0]];
    				
    				// try
    				nextSourcePath = sourcePathArr[nextSourceDepth + offsets[1]].charAt(0) == '*' ? sourcePathArr[nextSourceDepth + offsets[1]].substring(1) : sourcePathArr[nextSourceDepth + offsets[1]];
    				
    				idx = mappingContainsTarget(mappings, nextTargetPath, nextSourcePath, offsets);
    			}
    			m.put("mappings", growMappingsArray(mappings));
    			mappingIdx = ((LinkedHashMap[]) m.get("mappings")).length - 1;
    			generateMapping(((LinkedHashMap[]) m.get("mappings"))[mappingIdx], targetPathArr, sourcePathArr, maxDepth, nextTargetDepth + offsets[0], nextSourceDepth + offsets[1]);
    			
	    	}
	    	
	    	else {
	    	    LinkedHashMap[] mappings = new LinkedHashMap[1];
	    	    mappings[0] = new LinkedHashMap();
	    	    m.put("mappings",mappings);
	    	    
	    	    //try
	    	    if (curType.equals("object_to_object") || curType.equals("field_to_object")) {
		    	    generateMapping(((LinkedHashMap[]) m.get("mappings"))[mappingIdx], targetPathArr, sourcePathArr, maxDepth, nextTargetDepth, curSourceDepth);

	    	    }
	    	    else {
		    	    generateMapping(((LinkedHashMap[]) m.get("mappings"))[mappingIdx], targetPathArr, sourcePathArr, maxDepth, nextTargetDepth, nextSourceDepth);

	    	    }
	    	}
	    	
	    }
	}
	
	
	public static int mappingContainsTarget(LinkedHashMap[] mappings, String nextTargetPath, String nextSourcePath, int[] offsets) {
		for (int i = 0; i < mappings.length; i++) {
			if (mappings[i].get("target").equals(nextTargetPath)) {
				
				
//				if (mappings[i].get("type").equals("field_to_object") || (mappings[i].get("type").equals("object_to_object") && !mappings[i].get("source").equals(nextSourcePath))) {
//					mappings[i].put("source", "multiple");
//				}
//			    else {
//					offsets[1]++;
//				}
				
				
				//try
				if (!mappings[i].get("source").equals("multiple")) offsets[1]++;
			

				return i;
			}
		}
		return -1;
	}
	
	
	
	
	public static LinkedHashMap[] growMappingsArray(LinkedHashMap[] mappings) {
		int prevLen = mappings.length;
		LinkedHashMap[] newMappings = new LinkedHashMap[prevLen + 1];
		for (int i = 0; i < prevLen; i++) {
			newMappings[i] = mappings[i];
		}
		newMappings[prevLen] = new LinkedHashMap();
		return newMappings;
	}
	
	
	
	
	
	
	public static String getMappingType(String curTargetPath, String curSourcePath, int curTargetDepth, int targetDepth, int curSourceDepth, int sourceDepth) {
		if (curTargetPath.charAt(0) == '*' && curSourcePath.charAt(0) == '*') {
			return "array_to_array";
		} 
		else if (curSourcePath.charAt(0) == '*' && curSourcePath.charAt(curSourcePath.length() - 1) == ']') {
		    if (curTargetDepth == targetDepth) {
			    return "arrayItem_to_field";
			}
			else {
				return "arrayItem_to_object";
			}
			
		}
		
		else if (curTargetDepth < targetDepth && curSourceDepth < sourceDepth) {
			return "object_to_object";
		}
		else if (curTargetDepth < targetDepth && curSourceDepth == sourceDepth) {
			return "field_to_object";
		}
		else if (curTargetDepth == targetDepth && curSourceDepth < sourceDepth) {
			return "object_to_field";
		}
		else if ((curTargetDepth == targetDepth && curSourceDepth == sourceDepth) && curSourcePath.equals("_index")) {
			return "index_to_field";
		}
		else if (curSourceDepth == sourceDepth && curSourcePath.charAt(0) == '(') {
			return "content_to_field";
		}
		else {
			return "field_to_field";
		}
	}
}
