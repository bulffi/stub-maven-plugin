package org.f4.mojo.handler.impl;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;
import org.apache.maven.plugin.logging.Log;
import org.f4.mojo.handler.StubHandler;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * @program: stub-maven-plugin
 * @description: The stub handler learnt from the old generation
 * @author: Zijian Zhang
 * @create: 2020/05/01
 **/
public class MyStubHandler implements StubHandler {
    public byte[] stubClassFile(File file, Log log) throws IOException {
        log.info(file.getName() + " length " + file.length());
        byte[] originClass = new byte[(int) file.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int nextByte = bis.read(originClass);
        ClassReader classReader = new ClassReader(originClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode,0);
        transform(classNode);
        log.info("Class " + file.getName() + " stub success");
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
    public static void transform(ClassNode cn) {
        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if ("<init>".equals(mn.name) || "<clinit>".equals(mn.name)){
                continue;
            }
            InsnList inns = mn.instructions;
            if (inns.size() == 0) {
                continue;
            }
            Iterator<AbstractInsnNode> j = inns.iterator();
            while (j.hasNext()) {
                AbstractInsnNode in = j.next();
                int op = in.getOpcode();
                if (in instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) in;
                    InsnList i2 = new InsnList();
                    i2.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                    String temp = "\n" + cn.name + ":" + mn.name + " CALL " + methodInsnNode.owner + ":" + methodInsnNode.name + "=>"+mn.desc+"=>"+methodInsnNode.desc+"=>";
                    i2.add(new LdcInsnNode(temp));
                    i2.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false));
                    if (in.getPrevious() != null)
                        inns.insert(in.getPrevious(), i2);
                }
            }
//            System.out.println(mn.access);
            if((mn.access/8) % 2 != 1) {
                InsnList i2 = new InsnList();
                i2.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                i2.add(new VarInsnNode(Opcodes.ALOAD, 0));
                i2.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
                i2.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                i2.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
                mn.instructions.insert(i2);
            }
            mn.maxStack += 5;
        }
    }
}
