SH = bash

PACKAGE := conreality
VERSION := $(shell cat VERSION)

SOURCES := $(wildcard src/*/*/*/*/*/*.kt)
OUTPUTS := build/libs/conreality.kt.jar

all: build

build/libs/conreality.kt.jar: $(SOURCES)
	@echo "not implemented" # TODO

build: $(OUTPUTS)

check:
	@echo "not implemented" # TODO

dist:
	@echo "not implemented" # TODO

install:
	@echo "not implemented" # TODO

clean:
	@rm -f *~ $(OUTPUTS)

distclean: clean

mostlyclean: clean

.PHONY: check dist install clean distclean mostlyclean
