Introduction
============

mindext4dpws-${project.version} is an extension of the mind-compiler-${mind-compiler.version}
that should be used to build distributed fractal application.It is based on the DPWS stack and 
offer to an architect designer the possibility to design fractal application based on the Service 
Oriented Architecture (SOA) implemented over Web Services.
Since DPWS is designed for embedded application, this extension of mind-compiler is also design to 
build and run embedded application. 

Installing dpwscore-${dpwscore.version}
=======================================
 1) Checkout dpwscore sources by typing the following command : 
 	svn checkout --username anonsvn https://forge.soa4d.org/svn/dpwscore/tags/dpwscore-${dpwscore.version} dpwscore-${dpwscore.version}
 	
 	Note: the svn password is anonsvn as well.
 	
 2) Set the DPWSCORE_ROOT environment variable pointing to the dpwscore-${dpwscore.version} folder.
 	
 3) Process the compilation of the dpws libraries for your specific platform. (see the "DC Build Notes.html" for this purpose).
 
 ->Ex  : linux i686
 
 	You have to execute a set a makefile in those following folder : 
 	 - $DPWSCORE_ROOT$/platform/gnu/linux/dpws/i686
 	 - $DPWSCORE_ROOT$/platform/gnu/linux/xmltools/i686
 	 - $DPWSCORE_ROOT$/platform/gnu/linux/gsoap-2.7.6/i686
 	 - $DPWSCORE_ROOT$/platform/gnu/linux/gsoap-soapcpp-2.7.6/i686
 	 - $DPWSCORE_ROOT$/platform/gnu/linux/gsoap-wsdl2h-2.7.6/i686
 	 
 	Add the $DPWSCORE_ROOT$/platform/gnu/linux/gsoap-soapcpp-2.7.6/i686 and the 
 	$DPWSCORE_ROOT$/platform/gnu/linux/gsoap-wsdl2h-2.7.6/i686 fodlers to your PATH.
    # export PATH=$DPWSCORE_ROOT$/platform/gnu/linux/gsoap-soapcpp-2.7.6/i686:$PATH
    # export PATH=$DPWSCORE_ROOT$/platform/gnu/linux/gsoap-wsdl2h-2.7.6/i686:$PATH	 
    
 ->Ex  : mingw i686 (To generate Win 32 binaries)
 
 	You have to execute a set a makefile (mingw32-make.exe) in those following folder : 
 	 - $DPWSCORE_ROOT$/platform/gnu/mingw/dpws/i686
 	 - $DPWSCORE_ROOT$/platform/gnu/mingw/xmltools/i686
 	 - $DPWSCORE_ROOT$/platform/gnu/mingw/gsoap-2.7.6/i686
 	 - $DPWSCORE_ROOT$/platform/gnu/mingw/gsoap-soapcpp-2.7.6/i686
 	 - $DPWSCORE_ROOT$/platform/gnu/mingw/gsoap-wsdl2h-2.7.6/i686
 	 
 	Add the $DPWSCORE_ROOT$/platform/gnu/mingw/gsoap-soapcpp-2.7.6/i686 and the 
 	$DPWSCORE_ROOT$/platform/gnu/linux/gsoap-wsdl2h-2.7.6/i686 fodlers to your PATH.
    # export PATH=$DPWSCORE_ROOT$/platform/gnu/mingw/gsoap-soapcpp-2.7.6/i686:$PATH
    # export PATH=$DPWSCORE_ROOT$/platform/gnu/mingw/gsoap-wsdl2h-2.7.6/i686:$PATH	 


Installing mindext4dpws-${project.version}
===================================

  The following instructions show how to install MindExt4DPWS:

  1) Unpack the archive where you would like to store the binaries, eg:

# tar zxvf mind-ext4dpws-${project.version}-bin.tar.gz

  2) A directory called "mind-ext4dpws-${project.version}" will be created.

  3) Add the bin sub-directory to your PATH, eg:
  
# export PATH=$HOME/mind-ext4dpws-${project.version}/bin:$PATH

  4) Make sure JAVA_HOME is set to the location of your JRE or JDK

  5) Run "mindpwsc" to verify that it is correctly installed.

Running the MindpwsC compiler
=============================

  The mindpwsc extension compiles a given deployment architecture definition file.
  It is used with the following command-line arguments:

# mindpwsc [OPTIONS] (<deployment-arch-definition>)+

  where <definition> is the name of the deployment architecture composite component to be compiled.

Available options are :
  -h, --help                      Print this help and exit
  -S=<path list>, --src-path      the search path of ADL,IDL and implementation 
                                  files (list of path separated by ';'). This 
                                  option may be specified several times.
  -o=<output path>, --out-path    the path where generated files will be put 
                                  (default is '.')
  -t=<name>, --target-descriptor  Specify the target descriptor
  --compiler-command=<path>       the command of the C compiler (default is 
                                  'gcc')
  -c=<flags>, --c-flags           the c-flags compiler directives. This option
                                  may be specified several times.
  -I=<path list>, --inc-path      the list of path to be added in compiler 
                                  include paths. This option may be specified 
                                  several times.
  --linker-command=<path>         the command of the linker (default is 'gcc')
  -l=<flags>, --ld-flags          the ld-flags compiler directives. This option 
                                  may be specified several times.
  -L=<path list>, --ld-path       the list of path to be added to linker library
                                  search path. This option may be specified 
                                  several times.
  -T=<path>, --linker-script      linker script to use (given path is resolved 
                                  in source path)
  -j=<number>, --jobs             The number of concurrent compilation jobs 
                                  (default is '1')
  -e                              Print error stack traces
  --check-adl                     Only check input ADL(s), do not compile

A set of executable files (one executable per DeploymentUnit) should be generated 
in the output folder.

Setting the verbosity level of the mindc compiler
====================================================

  Use the MIND_OPTS environment variable to specify verbosity level. 

  For instance 

# set MIND_OPTS=-Ddefault.file.level=FINEST

  specifies the FINE level for console messages and FINER for messages dumped in 
  log file.
 
