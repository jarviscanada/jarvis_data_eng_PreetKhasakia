package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepImp implements JavaGrep {
    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        //Use default logger config
        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try{
            javaGrepImp.process();
        } catch (Exception ex){
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }
    }

    @Override

    public void process() {
        //List<String> readLines = new ArrayList<>();
        List<String> matchedLines = new ArrayList<>();
        List<File> files = listFiles(getRootPath());

        for(File file: files){
            for(String line: readLines(file)){
                if(containsPattern(line))
                    matchedLines.add(line);
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {

        File filePath = new File(rootDir);
        List<File> files = new ArrayList<>();
        try {
            if(filePath.exists() || filePath.isDirectory()){
                System.out.println("File or directory exists!");
                files = Files.list(Paths.get(rootDir)).map(Path::toFile).collect(Collectors.toList());
                
                files.forEach(System.out::println);
            }
            
            else {
                System.out.println("File or directory does not exist!");
            }

        }catch(Exception ex){
            logger.error("Enter correct input path!", ex);
        }


        return files;
    }

    @Override
    public List<String> readLines(File inputFile) throws IllegalArgumentException {

        List<String> result = new ArrayList<>();
        try{
            if(inputFile.exists() && inputFile.isFile()){
                BufferedReader br = new BufferedReader(new FileReader(inputFile));

                String line;
                while((line = br.readLine()) != null){
                    result.add(line);
                }
                br.close();
            }
            else{
                System.out.println("File not found!");
            }
        } catch (IllegalArgumentException ex){
            logger.error("Input file is not a file");
        } catch (FileNotFoundException e) {
            logger.error("Input file not found");
        } catch (IOException e) {
            logger.error("Input/Output error");
        }

        return result;
    }

    @Override
    public boolean containsPattern(String line) {

        return line.matches(getRegex());
    }

    @Override
    public void writeToFile(List<String> lines) {

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(getOutFile()));
            bw.write(String.valueOf(lines));
            bw.close();

        }catch (Exception ex){
            logger.error("Exception error");
        }

    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}
