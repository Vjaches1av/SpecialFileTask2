import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public final class Main {

    private static <T> String listObjToJson(List<T> list) {
        Type listType = new TypeToken<List<T>>() {}.getType();
        return new Gson().toJson(list, listType);
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Укажите путь к файлу *.xml, включая диск: ");
            File file = new File(scanner.nextLine());
            try {
                if (file.exists() && file.isFile() && file.getName().endsWith(".xml")) {

                } else {
                    throw new FileNotFoundException();
                }
            } catch (FileNotFoundException e) {
                System.err.println("Проверьте правильность указания пути и имени файла.");
            } catch (IOException e) {
                System.err.println("Произошла ошибка при попытке чтения указанного файла.");
            }
        }
    }
}