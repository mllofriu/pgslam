#
set terminal png
set output "cubriLargo.png"
set title ""
set yrange [0:100]
set key bottom right
set auto x
set style data line
#set xtic rotate by -45 scale 0 font ",8"
set grid
set xlabel "mins"
set ylabel "%"

#set key autotitle columnheader
plot 'cubriSlamSumaLargo.dat' using ($0 / 600.):1 t "SLAM", 'cubriOdomSumaLargo.dat' using ($0 / 600.):1 t "Odometría"

#pause -1
#
