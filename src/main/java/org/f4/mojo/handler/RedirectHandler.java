package org.f4.mojo.handler;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;

public interface RedirectHandler {
        byte[] stubJavaFile(File file, Log log) throws IOException;
}
