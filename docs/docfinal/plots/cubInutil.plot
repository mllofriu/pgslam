#!/usr/bin/gnuplot
set terminal png
set output "cubInutil.png"

set title ""
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
set auto x
set xrange [-1:5]
#set yrange [0:30]
#set xtic rotate by -45 scale 0 font ",8"
set xtics ("20 min." 0.5, "100 min." 3.5)
set grid
set ylabel "%"

#set key autotitle columnheader
plot "./cubInutil.csv" u 1:2:3 w boxerrorbars t "Odometría", '' u 4:5:6 w boxerrorbars t "SLAM"
