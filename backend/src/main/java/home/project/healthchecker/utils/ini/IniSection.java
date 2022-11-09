package home.project.healthchecker.utils.ini;

import java.util.HashMap;
import java.util.Map;

public class IniSection {

    private String unitName;
    private Map<String, String> parameters;

    public IniSection(String name){
        this.unitName = name;
        this.parameters = new HashMap<>();
    }

    public void addParam(String key, String value){
        parameters.put(key, value);
    }

    public String getParam(String key){
        return parameters.get(key);
    }

    public Map<String, String> getParamMap(){
        return parameters;
    }

    public String getUnitName(){
        return unitName;
    }

}
