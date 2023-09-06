# Программа сравнения файлов

## Настройка программы
прописать путь к 1 файлу excel
```shell
/path/to/file/filename1.xlsx
```
прописать путь к 2 файлу excel
```shell
/path/to/file/filename2.xlsx
```
формат колонок для 1 файла

| any | txt | txt | txt | any | any |
|-----|-----|-----|-----|-----|-----|

формат колонок для 2 файла

| any | any | txt | any | txt | any |
|-----|-----|-----|-----|-----|-----|

логи с ошибками сохраняются в файл **mylogfile.log**

программа собирается командой maven -> package
собирается файл *-jar-with-dependencies.jar

## запуск программы

```shell
java -jar UKPG1_Different_People-1.0-SNAPSHOT-jar-with-dependencies.jar /path/to/file/filename1.xlsx /path/to/file/filename2.xlsx
```