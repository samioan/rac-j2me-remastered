@echo off
rem The shipping build, and then the folder that gets zipped. Ported from
rem the sibling ports' own port/build_dist.bat (itself from shadowkey-decomp).
rem
rem Differs from build.bat in three ways, all of which matter to someone
rem who downloaded this rather than built it:
rem   Release              - optimised, and /MT via CMAKE_MSVC_RUNTIME_LIBRARY,
rem                          so no Visual C++ redistributable is needed
rem   GOINGMOBILE_DIST=ON    - builds the two shipped executables, not every
rem                          milestone's own smoke test too
rem   GOINGMOBILE_VERSION    - what the launcher prints in its corner
rem
rem Usage:  port\build_dist.bat [version]
rem Output: port\dist\  (ready to zip)  and  port\build-dist\  (objects)

setlocal
call "%~dp0vcvars.bat"
if errorlevel 1 exit /b 1
cd /d "%~dp0"

set GOINGMOBILE_VER=%1
if "%GOINGMOBILE_VER%"=="" set GOINGMOBILE_VER=0.0.0-local

cmake -G Ninja -S . -B build-dist -DCMAKE_BUILD_TYPE=Release -DGOINGMOBILE_DIST=ON ^
      -DGOINGMOBILE_VERSION=%GOINGMOBILE_VER%
if errorlevel 1 exit /b 1
cmake --build build-dist
if errorlevel 1 exit /b 1

rem Assemble the layout the launcher expects: itself at the top, the game
rem in bin\. `data\` and `user\` are created by the launcher on first run,
rem not shipped -- they are where the player's own files go.
if exist dist rmdir /s /q dist
mkdir dist\bin
copy /y build-dist\GoingMobile.exe dist\ >nul
if errorlevel 1 exit /b 1
copy /y build-dist\goingmobile_port_a1.exe dist\bin\ >nul
if errorlevel 1 exit /b 1
copy /y ..\..\NOTICE.md dist\ >nul
copy /y dist_readme.txt dist\README.txt >nul

echo.
echo Built %GOINGMOBILE_VER% into port\dist\ -- zip that folder.
dir /b dist
endlocal
