#!/usr/bin/gnuplot

set terminal png
set output "paramsEvols.png"
set xtics ("Despl." 0, "Rotación" 1, "Sensado" 2, "Resamp." 3)
set ytics ("Despl." 0, "Rotación" 1, "Sensado" 2, "Resamp." 3)
set yrange [-1:4]
set xrange [-1:4]
set title ""


plot "./covMatrixEvols.csv" matrix with image t ""
