@echo off
set CONTAINER_NAME=oracle-xe
set IMAGE_NAME=oracle-xe:latest
set SQL_FILE=bases.sql
set DB_USER=system
set DB_PASS=tu_contraseña

REM Iniciar contenedor si no está corriendo
docker ps -a | findstr %CONTAINER_NAME% >nul
if %ERRORLEVEL% NEQ 0 (
    echo Iniciando contenedor %CONTAINER_NAME%...
    docker run -d --name %CONTAINER_NAME% %IMAGE_NAME%
) else (
    echo El contenedor %CONTAINER_NAME% ya existe.
    docker start %CONTAINER_NAME%
)

REM Esperar a que la base de datos esté lista (ajustar si tarda más)
echo Esperando a que Oracle XE se inicialice...
timeout /t 20 >nul

REM Copiar el archivo SQL al contenedor
echo Copiando %SQL_FILE% al contenedor...
docker cp %SQL_FILE% %CONTAINER_NAME%:/tmp/

REM Ejecutar el archivo SQL con sqlplus
echo Ejecutando script SQL dentro del contenedor...
docker exec -i %CONTAINER_NAME% bash -c "echo exit | sqlplus %DB_USER%/%DB_PASS%@XE @/tmp/%SQL_FILE%"

echo Listo.
pause
