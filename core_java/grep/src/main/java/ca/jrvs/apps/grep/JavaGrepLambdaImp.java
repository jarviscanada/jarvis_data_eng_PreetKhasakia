package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp{

    public static void main(String[] args) {
        if(args.length != 3){
            JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
            javaGrepLambdaImp.setRegex(args[0]);
            javaGrepLambdaImp.setRootPath(args[1]);
            javaGrepLambdaImp.setRootPath(args[2]);

            try{
                javaGrepLambdaImp.process();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();
        try (Stream<Path> dirPath = Files.walk((Paths.get(rootDir)))) {
            dirPath.filter(Files::isRegularFile).collect(Collectors.toList());
        }catch (IOException e){
            logger.error("Error", e);
        }
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            br.lines().collect(Collectors.toList());
        }catch (IOException ex){
            logger.error("Error", ex);
        }
        return lines;
    }

}
