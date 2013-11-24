set terminal png
set output "corridasEvalEvolsSeparadas.png"

set title ""
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
#set auto x
set xrange [-1:2]
set yrange [0:600]
set xtic scale 0 font ",7"
set grid
set ylabel "mm"
set xlabel "conjunto de datos"

plot "./evalEvols.csv" u :2:3:xtic(1) w boxerrorbars lc rgb "blue" t ""
