package hr.java.game.puertorico.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static hr.java.game.puertorico.util.DialogUtils.successDialog;

public class DocumentationUtils {
    public static void generateHtmlDocumentation(){
        StringBuilder documentationGenerator = new StringBuilder();
        String path = Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "java"
        ).toString();

        try {
            List<Path> classNameList = Files.walk(Paths.get(path))
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .filter(p -> !p.getFileName().toString().equals("module-info.java"))
                    .toList();

            for(Path classPath : classNameList){
                int indexOfHr = classPath.toString().indexOf("hr");
                String fqcn = classPath.toString().substring(indexOfHr);
                fqcn = fqcn.replace('\\','.');
                fqcn = fqcn.substring(0, fqcn.length() - 5);

                Class<?> documentationClass = Class.forName(fqcn);
                String classModifiers = Modifier.toString(documentationClass.getModifiers());

                documentationGenerator.append("<h2>"
                        + classModifiers + " "
                        + fqcn
                        + "</h2>\n");

                Field[] classVariables = documentationClass.getDeclaredFields();
                documentationGenerator.append("<h2>Variables</h2>\n");
                for(Field field : classVariables){
                    String modifiers = Modifier.toString(field.getModifiers());
                    String type = field.getGenericType().getTypeName().replace("<","< ");
                    documentationGenerator.append("<p>"
                            + modifiers + " "
                            + getSimpleName(type) + " "
                            + field.getName()
                            + "</p>");
                }

                Method[] classMethods = documentationClass.getDeclaredMethods();
                documentationGenerator.append("\n<h2>Functions</h2>\n");
                for(Method method : classMethods){
                    if (method.getName().contains("lambda$"))
                        continue;
                    String modifiers = Modifier.toString(method.getModifiers());
                    String returnType = method.getGenericReturnType().getTypeName().replace("<","< ");
                    documentationGenerator.append("<p>"
                            + modifiers + " "
                            + getSimpleName(returnType) + " "
                            + method.getName() + "(");

                    StringBuilder parameterList = new StringBuilder();
                    Type[] parameterTypes = method.getGenericParameterTypes();
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++){
                        String typeName = parameterTypes[i].getTypeName().replace("<","< ");
                        parameterList.append(getSimpleName(typeName))
                                .append(" ")
                                .append(parameters[i].getName())
                                .append(", ");
                    }
                    if (!parameterList.isEmpty()) {
                        parameterList.setLength(parameterList.length() - 2);
                    }
                    documentationGenerator.append(parameterList.toString()
                            + ")"
                            + "</p>\n");
                }

                Constructor[] classConstructors = documentationClass.getDeclaredConstructors();

                documentationGenerator.append("<h2>Constructors</h2>\n");
                for (Constructor c : classConstructors) {
                    String modifiers = Modifier.toString(c.getModifiers());
                    int index = c.getName().lastIndexOf('.');
                    String constructorName = (index == -1) ? c.getName() : c.getName().substring(index + 1);
                    documentationGenerator.append("<p>"
                            + modifiers + " "
                            + constructorName + "(");

                    StringBuilder parameterList = new StringBuilder();
                    Parameter[] parameters = c.getParameters();
                    for (int i = 0; i < parameters.length; i++){
                        String typeName = parameters[i].getType().getTypeName().replace("<","< ");
                        parameterList.append(getSimpleName(typeName))
                                .append(" ")
                                .append(parameters[i].getName())
                                .append(", ");
                    }
                    if (!parameterList.isEmpty()) {
                        parameterList.setLength(parameterList.length() - 2);
                    }
                    documentationGenerator.append(parameterList.toString()
                            + ")"
                            + "</p>\n");
                }
                documentationGenerator.append("__________________________________________________________________________________________________\n");

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>Documentation</title>
                </head>
                <body>
                <h1>List of classes</h1>
                """
                + documentationGenerator.toString() +
                """
                </body>
                </html>
                """;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("documentation/doc.html"))){
            writer.write(html);
            successDialog("Document was successfully generated!");
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private static String getSimpleName(String fullyQualifiedName) {
        if (!fullyQualifiedName.contains("<")) {
            int lastDotIndex = fullyQualifiedName.lastIndexOf('.');
            if (lastDotIndex != -1) {
                return fullyQualifiedName.substring(lastDotIndex + 1);
            }
        }
        else {
            fullyQualifiedName = fullyQualifiedName.replace("java.util.","");
        }
        return fullyQualifiedName;
    }

    private static String getTypeName(Class<?> type) {
        if (type.isArray()) {
            return getTypeName(type.getComponentType()) + "[]";
        }
        return type.getName();
    }
}
