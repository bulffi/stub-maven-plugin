package org.f4.mojo;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.f4.mojo.handler.StubHandler;
import org.f4.mojo.handler.impl.MyStubHandler;

import javax.inject.Inject;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: stub-maven-plugin
 * @description: To make stub on the compiled class file
 * @author: Zijian Zhang
 * @create: 2020/04/26
 **/
@Mojo(name = "stub", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class StubMojo extends AbstractMojo {
    // is there any sorts of dependency injection tool?
    StubHandler stubHandler = new MyStubHandler();

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("This is my stub mojo");
        File directory = new File("./target/classes");//设定为当前文件夹
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
                    byte[] newClass = stubHandler.stubClassFile(f, getLog());
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(newClass);
                } catch (IOException e){getLog().info(e.getMessage());}
            }
        }catch(Exception ignored){}
    }
}
