@REM  This file is part of "Mind Compiler" is free software: you can redistribute 
@REM  it and/or modify it under the terms of the GNU Lesser General Public License 
@REM  as published by the Free Software Foundation, either version 3 of the 
@REM  License, or (at your option) any later version.
@REM 
@REM  This program is distributed in the hope that it will be useful, but WITHOUT 
@REM  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
@REM  FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
@REM  details.
@REM 
@REM  You should have received a copy of the GNU Lesser General Public License
@REM  along with this program.  If not, see <http://www.gnu.org/licenses/>.
@REM 
@REM  Contact: mind-members@lists.minalogic.net
@REM 
@REM  Authors: Edine Coly (edine.coly@mail.sogeti.com)
@REM  Contributors: 
@REM -----------------------------------------------------------------------------
@REM Mind Compiler batch script 0.1-alpha-4-SNAPSHOT
@REM
@REM Required ENV vars:
@REM ------------------
@REM   JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM -----------------
@REM   MINDPWS_ROOT - location of mind's installed home dir
@REM   MIND_OPTS - parameters passed to the Java VM running the mind compiler
@REM     e.g. to specify logging levels, use
@REM       set MIND_OPTS=-Ddefault.console.level=FINE -Ddefault.file.level=FINER
@REM   See documentation for more detail on logging system.

@echo off

@REM ==== CHECK DPWSCORE_ROOT ===
if not "%DPWSCORE_ROOT%" == "" goto OkDpwsRoot
echo.
echo ERROR: DPWSCORE_ROOT not found in your environment.
echo Please set the DPWSCORE_ROOT variable in your environment to match the
echo location of your DPWS installation
echo.
goto error

:OkDpwsRoot
@REM ==== CHECK JAVA_HOME ===
if not "%JAVA_HOME%" == "" goto OkJHome
echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHome
@REM ==== CHECK JAVA_HOME_EXE ===
if exist "%JAVA_HOME%\bin\java.exe" goto OkJHomeExe

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHomeExe
@REM ==== CHECK MINDPWS_ROOT===
if not "%MINDPWS_ROOT%" == "" goto endInit
echo.
echo ERROR: MINDPWS_ROOT not found in your environment.
echo Please set the MINDPWS_ROOT variable in your environment to match the
echo location of your MIND installation
echo.
goto error

:endInit

setlocal
set MIND_CMD_LINE_ARGS=%*
set MIND_RUNTIME=%MINDPWS_ROOT%/runtime
set MIND_LIB=%MINDPWS_ROOT%/lib
set LAUNCHER=org.ow2.mind.extensions.ext4dpws.Launcher
set MIND_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
if not "%MIND_CLASSPATH%" == "" set MIND_CLASSPATH=%MIND_CLASSPATH%;

for /r "%MIND_LIB%\" %%i in (*.jar) do (
	set VarTmp=%%~fnxi;& call :concat
	)

goto :runMind
:concat
set MIND_CLASSPATH=%MIND_CLASSPATH%%VarTmp%
goto :eof

:runMind
%MIND_JAVA_EXE% -classpath %MIND_CLASSPATH% %MIND_OPTS% -Dcecilia.launcher.name=mindpwsc %LAUNCHER% -src-path=%MIND_RUNTIME% %MIND_CMD_LINE_ARGS%


:error
@echo off
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
(set ERROR_CODE=1)
