package by.psu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);




       /* System.out.println("=== Заполнение и чтение таблицы Excursion ===\n");

        // ВАРИАНТ 1: Через ConnectionManager с транзакцией (как в первом коде)
        try (var connectionManager = new ConnectionManager()) {
            // Получаем соединение
            var connection = connectionManager.getConnection();
            var metadata = connection.getMetaData();

            // Выводим информацию о БД
            var infoString = "Database: " + metadata.getDatabaseProductName()
                    + "\nversion: " + metadata.getDatabaseMajorVersion() + '.' + metadata.getDatabaseMinorVersion();
            System.out.println(infoString + "\n");

            // Начинаем транзакцию
            connection.setAutoCommit(false);

            // Создаем JdbcHelper с соединением
            JdbcHelper jdbcHelper = new JdbcHelper(connection);

            // 1. Читаем экскурсию по id (если есть)
            System.out.println("1. Поиск экскурсии по id = 1:");
            var excursion = jdbcHelper.findExcursionById(1);
            if (excursion != null) {
                System.out.println(excursion);
            } else {
                System.out.println("Экскурсия с id=1 не найдена\n");
            }

            // 2. Добавляем (INSERT) новую экскурсию
            System.out.println("\n2. Добавление новой экскурсии:");
            var newExcursion = new Excursion(null, "Путешествие в Мирский замок",
                    new BigDecimal("123.45"), LocalDate.now(),
                    LocalDate.now().plusDays(2L), "Ф.Е. Цыган", "Автобусная", true);
            jdbcHelper.saveExcursion(newExcursion);
            System.out.println("Добавлена экскурсия с id = " + newExcursion.getId());

            // 3. Обновляем (UPDATE) экскурсию
            System.out.println("\n3. Обновление экскурсии:");
            newExcursion.setLunchIncluded(false);
            jdbcHelper.saveExcursion(newExcursion);
            System.out.println("Обновлено: lunch_included = false");

            // 4. Читаем ВСЕ экскурсии из БД
            System.out.println("\n4. Список всех экскурсий из БД:");
            var list = jdbcHelper.findAllExcursions();
            for (Excursion e : list) {
                System.out.println(e);
            }

            // Фиксируем транзакцию
            connection.commit();
            System.out.println("\n Транзакция успешно завершена!");

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        */
    }
}