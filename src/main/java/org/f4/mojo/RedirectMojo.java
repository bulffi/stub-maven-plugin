package org.f4.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.f4.mojo.handler.RedirectHandler;
import org.f4.mojo.handler.impl.MyRedirectHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: redirect-maven-plugin
 * @description: To redirect output of test file
 * @author: Zijian Zhang
 * @create: 2020/04/26
 **/
@Mojo(name = "redirect", defaultPhase = LifecyclePhase.PROCESS_TEST_SOURCES)
public class RedirectMojo extends AbstractMojo {

    RedirectHandler redirectHandler = new MyRedirectHandler();

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("This is my test mojo");
        File directory = new File("./src/test/java");//设定为当前文件夹
        File testOutPutDir = new File("./target/test-output");
        if (!testOutPutDir.exists()) {
            testOutPutDir.mkdir();
        }
        try{
            getLog().info(directory.getCanonicalPath());//获取标准的路径
            File[] files = directory.listFiles();
            assert files != null;
            List<File> dirList = new LinkedList<File>(Arrays.asList(files));
            List<File> fileList = new LinkedList<File>();
            for (int i = 0; i < dirList.size(); i++) {
                File f = dirList.get(i);
                if (f.isFile()) {
                    fileList.add(f);
                } else {
                    File[] subList = f.listFiles();
                    if (subList != null) {
                        dirList.addAll(Arrays.asList(subList));
                    }
                }
            }
            for (File f : fileList) {
                try {
                    byte[] newClass = redirectHandler.stubJavaFile(f, getLog());
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(newClass);
                } catch (IOException e){getLog().info(e.getMessage());}
            }
        }catch(Exception ignored){}
    }
}
