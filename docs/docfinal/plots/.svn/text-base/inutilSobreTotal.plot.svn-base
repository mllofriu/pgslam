#
set terminal png
set output "inutilSobreTotal.png"
#set encoding utf8
set title ""
#set auto x
#set yrange [0:7]
#set yrange [0:110]
set xrange [-1:5]
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
#set xtic rotate by -45 scale 0 font ",8"
set grid
set xtics ("20 min." 0.5, "100 min." 3.5)

#set key autotitle columnheader
plot 'inutilSobreTotal.dat' u 1:2:3 w boxerrorbars t "Odometría", '' u 4:5:6 w boxerrorbars t "SLAM"
#
