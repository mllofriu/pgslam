#!/usr/bin/gplot


set parametric

set xlabel "mins"
set ylabel "error (m)"
set xrange [0:100]

set terminal png
set output "evolErrorDist1.png"
set trange [0.16:0.36]
plot './slamlargo1.log.errorEst' u (s=$0/20):1 w lines title "Distancia", 70,t title ''

set terminal png
set output "evolErrorDist2.png"
set trange [0.1:0.4]
plot './slamlargo2.log.errorEst' u (s=$0/20):1 w lines title "Distancia", 40,t title ''

set terminal png
set output "evolErrorDist3.png"
set trange [0.1:0.6]
plot './slamlargo3.log.errorEst' u (s=$0/20):1 w lines title "Distancia", 22,t title ''

set terminal png
set output "evolErrorDist4.png"
plot './slamlargo4.log.errorEst' u (s=$0/20):1 w lines title "Distancia"

set terminal png
set output "evolErrorDist5.png"
plot './slamlargo5.log.errorEst' u (s=$0/20):1 w lines title "Distancia"


unset parametric
set ylabel "error (rads)"

set terminal png
set output "evolErrorRot1.png"
set yrange [-(2*3.14159):0]
plot './slamlargo1.log.errorEst' u  (s=$2 < 3.14159  / 2  ? $2 : $2 - 2 * 3.14159) w lines t "Rot. relativa", -2.7 t ''

set terminal png
set output "evolErrorRot2.png"
set yrange [-3.14159:3.14159]
plot './slamlargo2.log.errorEst' u (s=$0/20):2 w lines t "Rot. relativa", -1.5 t ''

set terminal png
set output "evolErrorRot3.png"
plot './slamlargo3.log.errorEst' u (s=$0/20):2 w lines t "Rot. relativa", -2 t ''

set terminal png
set output "evolErrorRot4.png"
#plot './slamlargo4.log.errorEst' u (s=$2 < 3.14159  / 2  ? $2 : $2 - 2 * 3.14159) w lines t "Rot. relativa"
plot './slamlargo4.log.errorEst' u (s=$0/20):2 w lines t "Rot. relativa"

set terminal png
set output "evolErrorRot5.png"
plot './slamlargo5.log.errorEst' u (s=$0/20):2 w lines t "Rot. relativa", -1.5 t ''
