# README #
Программное обеспечение для синхронизации и загрузки данных из XML или JSON файлов в базу даннных.

**1. Основные положения**
  Комплект поставки:
  
    1. Исходный код
    
    2. Файл test.sh для запуска приложения с параметрами
    
    3. Файл docker-compose.yaml для локального развертывания БД postgres.
    
    4. Собранный jar файл со всеми зависимостями
 
**2. Тип и формат используемых файлов**

   Программное обеспечение способно считывать два типа файлов: XML и JSON. Все файлы, имеющие тип,
    отличный от описанных ранее, обработаны не будут и программа завершит свою работу с ошибкой. Так же во время работы программного 
    обеспечения проверяется уникальность полей предоставленных файлов - пара code и job должна быть уникальна. В противном
    случае, если это правило нарушается, работа программного обеспечения завершится с ошибкой.
     
  -Формат файлов:
  
      1. JSON. Представляет из себя список объектов. Пример файла показан ниже:
      
         {
           "departments": [
             {
               "code": "_code_string_",
               "job": "_job_string_",
               "description": "_description_string_"
             },
             ...
           ]
         }
         
      2. XML.
      
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
         <departments>
             <department>
                 <code>_code_string_</code>
                 <job>__job_string__</job>
                 <description>_description_string_</description>
             </department>
             <department>
                 <code>_code1_string_</code>
                 <job>__job1_string__</job>
                 <description>_description1_string_</description>
             </department>
         </departments>
         
**3. Возможные операции** 
     
     _SYNC_ - данные из таблицы синхронизируются с данными из файла. Удаляться будут только те записи, которые отсутствуют в файле.
     Добавляться будут только те, которые есть в файле и отстутсвуют в базе. Измененные данные будут обновлены. 
     Сравнения данных происходит по ключу, который формируется из полей code и job. Два ключа считаются совпавшими если 
     code и job поля первого ключа, равны соответственно code и job второго. Если для синхронизации предоставлен пустой файл 
     то работа программного обеспечения завершится с ошибкой. Если файл не соотвествует описанному ваше формату,
     работа программного обеспечения так же завершится с ошибкой.
     _LOAD_ - данные из таблицы будут выгружены в указанный файл, если указанного файла не существует, программное обеспечение
     предпримет попытку создать его самостоятельно и завершится с ошибкой, в случае неудачи.

**4. Запуск приложения** 

  Для запуска приложения необходимо поместить в одну директорию следующие файлы:
  
    1. test.sh - для него должны быть установлены права на запуск(chmod 755 test.sh)
    
    2. docker-compose.yaml
    
    3. parser-1.0.0.jar
    
    4. создать файл или иметь готовый для проведения выгрузки или синхронизации данных.
    
    
   - для test.sh возможен показ доступных команд, по ключу -h
   
   `./test.sh -h`
   - запуск операции выгрузки в файл test.json с ручным стартом docker-compose
   
   `./test.sh -o load -f test.json`
   - запуск операции выгрузки в файл test.json с авто стартом docker-compose
   
   `./test.sh -o load -f test.json -d`
   - запуск операции синхронизации БД с файлом test.xml с ручным стартом docker-compose
   
   `./test.sh -o sync -f test.mxl`
   
**4. Логирование приложения** 
    
   Настройки файла для логирования производятся в application.properties. В нем можно указать отдельно каталог для логирования
   и название файла. По-умолчанию все логи пишутся в файл расположенный в log/application.log, который создается при старте 
   Программного обеспечения в том же каталоге.
