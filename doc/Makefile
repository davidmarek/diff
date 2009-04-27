all: dokumentace.pdf programator.pdf

dokumentace.pdf: dokumentace.tex
	pdflatex dokumentace.tex

programator.pdf: programator.tex
	pdflatex programator.tex

clean:
	rm -f dokumentace.aux dokumentace.log dokumentace.pdf
	rm -f programator.aux programator.log programator.pdf
	rm -f *~
