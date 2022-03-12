import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class Main {

    private static Employee parseEmployee(Node employee) {
        long id             = 0L;
        String firstName    = "";
        String lastName     = "";
        String country      = "";
        int age             = 0;

        NodeList children = employee.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
                Node node = children.item(j);
                String name = node.getNodeName();
                String value = node.getFirstChild().getNodeValue();
                switch (name) {
                    case ("id"):        id          = Long.parseLong(value);    break;
                    case ("firstName"): firstName   = value;                    break;
                    case ("lastName"):  lastName    = value;                    break;
                    case ("country"):   country     = value;                    break;
                    case ("age"):       age         = Integer.parseInt(value);  break;
                }
            }
        }
        return new Employee(id, firstName, lastName, country, age);
    }

    private static List<Employee> xmlToListEmployee(File file) throws Exception {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        Element root = document.getDocumentElement();
        NodeList employees = root.getElementsByTagName("employee");
        for (int i = 0; i < employees.getLength(); i++)
            list.add(parseEmployee(employees.item(i)));
        return list;
    }

    private static <T> String listObjToJson(List<T> list) {
        return new Gson().toJson(list, new TypeToken<List<T>>() {}.getType());
    }

    private static void writeJson(File file, String json) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file, false), StandardCharsets.UTF_8))) {
            writer.write(json);
            writer.flush();
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Укажите путь к файлу *.xml, включая диск: ");
            File file = new File(scanner.nextLine());
            try {
                if (file.exists() && file.isFile() && file.getName().endsWith(".xml")) {
                    List<Employee> employees = xmlToListEmployee(file);
                    String json = listObjToJson(employees);
                    File savePath = new File(file.getParentFile(), "data.json");
                    try {
                        writeJson(savePath, json);
                        System.out.println("Результаты работы успешно записаны на диск " + savePath.getAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("Произошла ошибка при попытке записи файла " + savePath.getAbsolutePath());
                    }
                } else throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                System.err.println("Проверьте правильность указания пути и имени файла.");
            } catch (Exception e) {
                System.err.println("Произошла ошибка при попытке чтения указанного файла.");
            }
        }
    }
}
