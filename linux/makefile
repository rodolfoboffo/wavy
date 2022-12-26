C = g++
OUT_DIR = ./bin
LIBS = -lSDL2
RES = ../resources

EXE = wavy.exe

SOURCES = $(RES)/main.cpp

sources: $(SOURCES)

all: sources
		@mkdir -p $(OUT_DIR)
		$(C) -o $(OUT_DIR)/$(EXE) $(SOURCES) $(LIBS)
clean:
		rm -r $(OUT_DIR)

run: $(OUT_DIR)/$(EXE)
		$(OUT_DIR)/$(EXE)