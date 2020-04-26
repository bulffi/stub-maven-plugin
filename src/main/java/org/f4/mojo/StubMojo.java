package org.f4.mojo;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
/**
 * @program: stub-maven-plugin
 * @description: To make stub on the compiled class file
 * @author: Zijian Zhang
 * @create: 2020/04/26
 **/
@Mojo(name = "stub", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class StubMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("This is my stub mojo");
        File directory = new File("./target/classes");//设定为当前文件夹
        try{
            getLog().info(directory.getCanonicalPath());//获取标准的路径
            File[] files = directory.listFiles();
            if (files != null) {
                for (File f :
                        files) {
                    getLog().info(f.getName());
                }
            }
        }catch(Exception ignored){}
    }
}
