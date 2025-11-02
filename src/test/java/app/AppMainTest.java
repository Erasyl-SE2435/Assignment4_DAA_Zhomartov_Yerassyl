package app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

public class AppMainTest {

    @ParameterizedTest(name = "Running file: {0}") // Добавляем имя файла в вывод теста
    @ValueSource(strings = {
            "data/small-1.json", "data/small-2.json", "data/small-3.json",
            "data/medium-1.json", "data/medium-2.json", "data/medium-3.json",
            "data/large-1.json", "data/large-2.json", "data/large-3.json"
    })
    void appMainRunsSuccessfullyOnAllFiles(String filePath) {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        try {
            System.out.println("\n\n##############################################################");
            System.out.println("STARTING TEST FOR FILE: " + filePath);
            System.out.println("##############################################################");

            String[] args = {filePath};

            assertDoesNotThrow(() -> AppMain.main(args), "AppMain должен выполниться без исключений.");

            System.setOut(originalOut);

            String output = outputStreamCaptor.toString();
            System.out.println(output);

            assertTrue(output.contains("SCC count:"), "ОШИБКА: Не удалось найти анализ SCC в выводе.");

            assertTrue(output.contains("**Critical Path Length"), "ОШИБКА: Не удалось найти длину критического пути.");

            String[] parts = output.split("\\*\\*Critical Path Length \\(Min Project Time\\):\\*\\* ");
            if (parts.length > 1) {
                String pathValueStr = parts[1].split("\n")[0].trim();
                try {
                    long pathValue = Long.parseLong(pathValueStr);
                    assertTrue(pathValue >= 0, "Длина критического пути не должна быть отрицательной.");
                } catch (NumberFormatException e) {
                    fail("Не удалось распарсить длину критического пути из вывода.");
                }
            } else {
                fail("Не удалось найти секцию критического пути в выводе.");
            }

        } catch (Exception e) {
            System.setOut(originalOut);
            fail("AppMain failed on file " + filePath + " with exception: " + e.getMessage());
        }
    }
}