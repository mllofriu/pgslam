set terminal png
set output "corridasEvolsSeparadas.png"

set title ""
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
#set auto x
set xrange [-1:10]
set yrange [0:600]
#set xtic rotate by -45 scale 0 font ",8"
set grid
set ylabel "mm"
set xlabel "conjunto de datos"

plot "./corridasEvolSeparadas.csv" u :1:2 w boxerrorbars lc rgb "blue" t ""
