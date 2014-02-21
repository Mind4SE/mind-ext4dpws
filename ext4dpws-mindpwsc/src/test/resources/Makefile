all: archA archB archC archD archE archF archG

# If the MINDPWS_ROOT variable is defined, look for 'mindpwsc' in this directory
# otherwise, expect that 'mindpwsc' is in the PATH.
ifdef MINDPWS_ROOT
  # If MINDPWS_ROOT contains '\', replace them by '/' (for Windows)
  MINDPWSC = $(subst \,/, $(MINDPWS_ROOT)/bin/mindpwsc)
else 
  MINDPWSC = mindpwsc
endif

GUMSTIX_IP = 10.226.174.44
GUMSTIX_REMOTE_DIR = /home/ecoly/MIND 
FOX_IP = 10.226.174.40
FOX_REMOTE_DIR = /mnt/flash/root


archA: 
	mkdir -p build/archA
	$(MINDPWSC) -e -src-path=sources/src -src-path=. -o=build/archA DeploymentArchitectureA
		
archB: 
	mkdir -p build/archB
	$(MINDPWSC) -src-path=sources/src -src-path=. -o=build/archB DeploymentArchitectureB	
	
archC: 
	mkdir -p build/archC
	$(MINDPWSC) -src-path=sources/src -src-path=. -o=build/archC DeploymentArchitectureC
	
archD: 
	mkdir -p build/archD
	$(MINDPWSC) -src-path=sources/src -src-path=. -o=build/archD DeploymentArchitectureD	
	
archE: 
	mkdir -p build/archE
	$(MINDPWSC) -src-path=sources/src -src-path=. -o=build/archE DeploymentArchitectureE

archF: 
	mkdir -p build/archF
	$(MINDPWSC) -src-path=sources/src -src-path=. -o=build/archF DeploymentArchitectureF
	
archG: 
	mkdir -p build/archG
	$(MINDPWSC) -src-path=sources/src -src-path=. -o=build/archG DeploymentArchitectureG
	

cleanArchA:
	rm -Rf build/archA/*
	
cleanArchB:
	rm -Rf build/archB/*
	
cleanArchC:
	rm -Rf build/archC/*
	
cleanArchD:
	rm -Rf build/archD/*
	
cleanArchE:
	rm -Rf build/archE/*
	
cleanArchF:
	rm -Rf build/archF/*
	
cleanArchG:
	rm -Rf build/archG/*

clean :
	rm -Rf build/

.PHONY: all 