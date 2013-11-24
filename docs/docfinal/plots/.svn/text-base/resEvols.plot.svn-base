#
set terminal jpeg
set output "resEvols.jpeg"
#set encoding utf8
set title "Cubrimiento Inútil"
set auto x
set yrange [0:30]
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
#set xtic rotate by -45 scale 0 font ",8"
set grid
set ylabel "%"

#set key autotitle columnheader
plot 'resEvols.dat' using 2 t "Odometría", '' u 3:xtic(1) t "SLAM"

#
