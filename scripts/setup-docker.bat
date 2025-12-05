@echo off
REM =============================================
REM Script para configurar Docker para el proyecto
REM =============================================

echo === Configurando entorno Docker para Sucursales ===

REM 1. Crear la red si no existe
echo.
echo [1/4] Verificando red Docker...
docker network inspect sucursales-net >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Creando red 'sucursales-net'...
    docker network create sucursales-net --driver bridge
) else (
    echo Red 'sucursales-net' ya existe.
)

REM 2. Construir imagen del servicio de inventario
echo.
echo [2/4] Construyendo imagen de inventario...
cd /d "%~dp0..\..\inventory-module-hexagonal-spring"
docker build -t inventory-service:latest .

REM 3. Verificar que Docker está accesible via TCP (para Windows)
echo.
echo [3/4] Verificando configuracion de Docker...
echo.
echo IMPORTANTE: Para que el servicio de sucursales pueda crear contenedores,
echo Docker Desktop debe tener habilitado "Expose daemon on tcp://localhost:2375"
echo.
echo Para habilitarlo:
echo   1. Abrir Docker Desktop
echo   2. Ir a Settings -^> General
echo   3. Marcar "Expose daemon on tcp://localhost:2375 without TLS"
echo   4. Aplicar y reiniciar Docker
echo.

REM 4. Mostrar estado
echo [4/4] Estado actual:
echo.
echo Redes Docker:
docker network ls | findstr sucursales-net
echo.
echo Imagenes disponibles:
docker images | findstr inventory-service
echo.
echo Contenedores de inventario:
docker ps -a | findstr inv-
echo.

echo === Configuracion completada ===
pause
