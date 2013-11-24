#!/bin/bash

# Nombre del trabajo
PBS -N paramSLAM$inicio

# Requerimientos
# En este caso nuestro trabajo requiere: 1 procesador, 1 hora de ejecución.
PBS -l nodes=1,walltime=01:20:00

# Cola de ejecución
PBS -q publica

# Directorio de trabajo
PBS -d /home/pgslam/ejec1/

# Correo electronico
PBS -M mllofriu@fing.edu.uy

# Email
PBS -m n
# n: no mail will be sent.
# a: mail is sent when the job is aborted by the batch system.
# b: mail is sent when the job begins execution.
# e: mail is sent when the job terminates.

# Directorio donde se guardará la salida estándar y de error de nuestro trabajo
PBS -e /home/pgslam/ejec1/
PBS -o /home/pgslam/ejec1/

# Will make  all variables defined in the environment from which the job is submitted available to the job.
PBS -V

echo Job Name: $PBS_JOBNAME
echo Working directory: $PBS_O_WORKDIR
echo Queue: $PBS_QUEUE
echo Cantidad de tasks: $PBS_TASKNUM
echo Home: $PBS_O_HOME
echo Puerto del MOM: $PBS_MOMPORT
echo Nombre del usuario: $PBS_O_LOGNAME
echo Idioma: $PBS_O_LANG
echo Cookie: $PBS_JOBCOOKIE
echo Offset de numero de nodos: $PBS_NODENUM
echo Shell: $PBS_O_SHELL
echo JobID: $PBS_O_JOBID
echo Host: $PBS_O_HOST
echo Cola de ejecucion: $PBS_QUEUE
echo Archivo de nodos: $PBS_NODEFILE
echo Path: $PBS_O_PATH
echo
cd $PBS_O_WORKDIR
echo Current path:
pwd
echo
echo NODES
cat $PBS_NODEFILE

# Ejecuto la tarea
time java -jar ./evolparam.jar $inicio $fin
