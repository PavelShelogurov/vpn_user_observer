package home.project.healthchecker.utils.ini;


import java.io.*;
import java.util.*;

public class IniParser {

    /**
     * Read ini-like file config
     *
     * @param reader
     * @return - map of List of IniSection (parameters), where key is section name, value is list of iniSection
     * @throws IOException
     */
    public Map<String, List<IniSection>> parseConfig(Reader reader) throws IOException {
        Map<String, List<IniSection>> result = new HashMap<>();

        BufferedReader br = new BufferedReader(reader);

        List<IniSection> sectionList = new ArrayList<>();
        String line;
        IniSection currentSection = null;
        while ((line = br.readLine()) != null) {
            line.trim();
            if (line.startsWith("[") && line.endsWith("]")) {
                if (currentSection != null) addSectionToMap(currentSection, result);

                currentSection = new IniSection(line.substring(1, line.length() - 1));
                continue;
            }
            if (!line.equals("")) {
                String[] split = line.split("=");
                currentSection.addParam(split[0].trim(), split[1].trim());
            }
        }
        if (currentSection != null) addSectionToMap(currentSection, result);

        return result;
    }

    public void writeConfigToWriter(Writer writer, Map<String, List<IniSection>> sectionMap) throws IOException {
        BufferedWriter bw = new BufferedWriter(writer);

        for(Map.Entry<String, List<IniSection>> map : sectionMap.entrySet()) {

            for (IniSection section : map.getValue()) {
                bw.write("[" + section.getUnitName() + "]");
                bw.newLine();

                for (Map.Entry<String, String> param : section.getParamMap().entrySet()) {
                    bw.write(param.getKey() + " = " + param.getValue());
                    bw.newLine();
                }

                bw.newLine();
            }
        }

        bw.flush();
    }

    private void addSectionToMap(IniSection section, Map<String, List<IniSection>> map){
        String unitName = section.getUnitName();
        if(!map.containsKey(unitName)){
            List<IniSection> newList = new ArrayList<>();
            newList.add(section);
            map.put(unitName, newList);
        } else {
            map.get(unitName).add(section);
        }
    }
}

