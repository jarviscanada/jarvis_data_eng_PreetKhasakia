package ca.jrvs.apps.grep;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaGrepImp implements JavaGrep {
    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

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
    public void process() throws IOException {
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
        File[] f = filePath.listFiles();
        List<File> fileList = new ArrayList<>();

        if(f == null) {
            System.out.println("Empty");
        }
        if(f != null && f.length > 0) {
            for (File file : f) {
                if (file.isFile()) {
                    fileList.add(file);
                } else {
                    fileList.addAll(listFiles(file.getAbsolutePath()));
                }
            }
        }

        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) throws IllegalArgumentException{

        List<String> result = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String line;
            while((line = br.readLine()) != null){
                result.add(line);
            }
            br.close();

        } catch (IOException ex){
            logger.error("Input file is not a file");
        }

        return result;
    }

    @Override
    public boolean containsPattern(String line) {

        return line.matches(getRegex());
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {

            BufferedWriter bw = new BufferedWriter(new FileWriter(getOutFile()));
            bw.write(String.valueOf(lines));
            bw.close();

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
