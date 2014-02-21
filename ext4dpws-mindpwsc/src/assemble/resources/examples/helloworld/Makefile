# If the MINDPWS_ROOT variable is defined, look for 'mindpwsc' in this directory
# otherwise, expect that 'mindpwsc' is in the PATH.
ifdef MINDPWS_ROOT
  # If MINDPWS_ROOT contains '\', replace them by '/' (for Windows)
  MINDPWSC = $(subst \,/, $(MINDPWS_ROOT)/bin/mindpwsc)
else 
  MINDPWSC = mindpwsc
endif

all: helloworld

helloworld: build
	$(MINDPWSC) -src-path=src -o=build helloworld.DistributedHelloWorldApp

build:
	mkdir -p build

clean:
	rm -Rf build/*
	rm -Rf target

.PHONY: all helloworld clean
