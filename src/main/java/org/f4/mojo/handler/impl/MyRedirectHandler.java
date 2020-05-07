package org.f4.mojo.handler.impl;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import org.apache.maven.plugin.logging.Log;
import org.f4.mojo.handler.RedirectHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @program: stub-maven-plugin
 * @description:
 * @author: Zijian Zhang
 * @create: 2020/05/07
 **/
public class MyRedirectHandler implements RedirectHandler {
    @Override
    public byte[] stubJavaFile(File file, Log log) throws IOException {
        log.info("In test " + file.getName());
        String className = file.getPath().substring(2).replace(File.separator, ".");
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
        compilationUnit.addImport("java.io.*");
        AddHelloModifier modifier = new AddHelloModifier();
        modifier.visit(compilationUnit, className);
        return compilationUnit.toString().getBytes();
    }

    private static class AddHelloModifier extends ModifierVisitor<String> {
        @Override
        public Visitable visit(MethodDeclaration n, String arg) {
            String outPutPath = "./target/test-output/" + arg + ".txt";
            super.visit(n, arg);
            n.addThrownException(FileNotFoundException.class);
            n.getBody().ifPresent(blockStmt -> {
                blockStmt.addStatement(0, StaticJavaParser.parseStatement(
                        "FileOutputStream unyIUCMbzwwswyEyDlRy = new FileOutputStream(\"" + outPutPath + "\");"
                ));
                blockStmt.addStatement(1,StaticJavaParser.parseStatement("PrintStream tyvUjmsuhcHfSndOaaTM = System.out;"));
                blockStmt.addStatement(2, StaticJavaParser.parseStatement("System.setOut(new PrintStream(unyIUCMbzwwswyEyDlRy));"));
                blockStmt.addStatement(StaticJavaParser.parseStatement("System.setOut(tyvUjmsuhcHfSndOaaTM);"));
            });
            return n;
        }
    }
}
