Для запуска сервиса в докере сначала соберите jar-файл, а потом в директории с проектом выполните следующие команды:
1. `docker build -t dbcontroller .`  
2. `docker run -d -p 8080:8080 --name=app dbcontroller:latest` 

(Если нужно запустить приложение без использования докера, то замените в файле application.properties **host.docker.internal** на **localhost**)